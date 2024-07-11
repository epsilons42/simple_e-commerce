package com.example.alsess.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.R
import com.example.alsess.model.ProfileRvModel
import com.example.alsess.util.FirebaseUtils
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileChildViewModel : ViewModel() {
    private lateinit var firebaseAuth: FirebaseAuth
    private val firebaseFireStoreDB = FirebaseFirestore.getInstance()
    private val profileItemArrayList = ArrayList<ProfileRvModel>()
    private val firebaseUtils = FirebaseUtils()
    val profileItemMLD = MutableLiveData<List<ProfileRvModel>>()
    val errorMessageMLD = MutableLiveData<String>()
    val loadMLD = MutableLiveData<Boolean>()
    val userMLD = MutableLiveData<HashMap<String, String>>()
    val currentUserMLD = MutableLiveData<Boolean>()

    fun controlAddProfileList(context: Context) {
        if (profileItemArrayList.size == 0) {
            addProfileItem(context)
        }
    }

    fun currentUser() {
        val currentUserBoolean = firebaseUtils.currentUser()
        if (currentUserBoolean) {
            checkFireStoreDocument()
        }
        currentUserMLD.value  = currentUserBoolean
    }


    private fun addProfileItem(context: Context) {
        profileItemArrayList.add(
            ProfileRvModel(
                R.id.action_profileChildFragment_to_userInfoFragment,
                context.getString(R.string.user_information),
                R.drawable.asset_profile
            )
        )
        profileItemArrayList.add(
            ProfileRvModel(
                R.id.action_profileChildFragment_to_accountSettingsFragment,
                context.getString(R.string.account_settings),
                R.drawable.asset_settings
            )
        )
        profileItemArrayList.add(
            ProfileRvModel(
                R.id.profileButtonRowBtn,
                context.getString(R.string.log_out),
                R.drawable.asset_exit
            )
        )
        profileItemMLD.value = profileItemArrayList
    }

    //The user who enters Google for the first time does not have a user information document,
    // this is checked
    private fun checkFireStoreDocument() {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUserUid = firebaseAuth.currentUser!!.uid
        firebaseFireStoreDB.collection("Users").document(currentUserUid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        if (document.exists()) {
                            fireStoreUserData()
                        } else {
                            firebaseUtils.addFireStoreUserDataProfile()
                            fireStoreUserData()
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                exception.localizedMessage
            }
    }

    @SuppressLint("SetTextI18n")
    private fun fireStoreUserData() {
        val userHashMap = HashMap<String, String>()
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser!!
        firebaseFireStoreDB.collection("Users").document(currentUser.uid)
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    errorMessageMLD.value = exception.localizedMessage
                } else {
                    if (snapshot != null && snapshot.exists()) {
                        loadMLD.value = true
                        val name = snapshot.data?.get("name").toString()
                        val lastName = snapshot.data?.get("lastName").toString()
                        if (name != "" && lastName != "") {
                            userHashMap.put("name", "$name $lastName")
                            userHashMap.put(
                                "nameChar",
                                name.get(0).toString() + lastName.get(0).toString()
                            )
                            userMLD.value = userHashMap

                        } else {
                            if (currentUser.displayName != null && currentUser.displayName != "") {
                                currentUser.displayName?.let { userHashMap.put("name", it) }
                                userHashMap.put(
                                    "nameChar", currentUser.displayName?.first().toString()
                                )
                                userMLD.value = userHashMap
                            } else {
                                currentUser.displayName?.let { userHashMap.put("name", it) }
                                userHashMap.put(
                                    "nameChar",
                                    currentUser.email?.get(0).toString().replaceFirstChar {
                                        it.uppercaseChar()
                                    }
                                )
                                userHashMap.put("name",currentUser.email)
                                userMLD.value = userHashMap
                            }
                        }
                    }
                }
            }
    }
}