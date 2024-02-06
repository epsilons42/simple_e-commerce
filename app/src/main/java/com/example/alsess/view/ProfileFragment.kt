package com.example.alsess.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProfileBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private lateinit var viewBinding: FragmentProfileBinding
    private lateinit var fireBaseAuth: FirebaseAuth
    val firebaseFirestoreDB = FirebaseFirestore.getInstance()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBinding = FragmentProfileBinding.inflate(inflater, container, false)
        fireBaseAuth = FirebaseAuth.getInstance()
        buttonClickAction()

        if (fireBaseAuth.currentUser != null) {
            checkFirestoredocument()
        }

        //If the user is logged in, the profile is listed, if not, the register or login buttons appear
        if (fireBaseAuth.currentUser != null) {
            viewBinding.fragmentProfileCardViewParent.visibility = View.VISIBLE
        } else {
            viewBinding.fragmentProfileBtnLogin.visibility = View.VISIBLE
            viewBinding.fragmentProfileBtnSignUp.visibility = View.VISIBLE
        }
        return viewBinding.root

    }

    fun buttonClickAction() {
        //Transition from profile fragment to other detail fragments
        viewBinding.fragmentProfileBtnUserInfo.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileFragment_to_userInfoFragment)
        }
        viewBinding.fragmentProfileBtnAccountSettings.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_profileFragment_to_accountSettingsFragment)
        }


        //The user logs out, both for those who enter with google and for those who register and enter
        if (fireBaseAuth.currentUser != null) {
            viewBinding.fragmentProfileBtnLogOut.setOnClickListener {
                val googleSignInClient =
                    GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN)
                fireBaseAuth.signOut()
                googleSignInClient.revokeAccess()
                val intent = Intent(context, MainActivity::class.java)
                startActivity(intent)
                activity?.let(FragmentActivity::finish)
            }
        }

        //transition to sign up and login activity
        viewBinding.fragmentProfileBtnLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }
        viewBinding.fragmentProfileBtnSignUp.setOnClickListener {
            val intent = Intent(context, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    //The user who enters Google for the first time does not have a user information document,
    // this is checked
    fun checkFirestoredocument() {
        val currentUser = fireBaseAuth.currentUser!!
        firebaseFirestoreDB.collection("Users").document(currentUser.uid)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val document = task.result
                    if (document != null) {
                        if (document.exists() || document == null) {
                            firestoreUserData()
                        } else {
                            firebaseFirestoreAddData()
                            viewBinding.fragmentProfileTxvName.text =
                                currentUser.displayName
                            viewBinding.fragmentProfileTxvNameChar.text =
                                currentUser.displayName?.get(0)
                                    .toString()
                        }
                    }
                }
            }.addOnFailureListener { exception ->
                exception.localizedMessage
            }
    }

    //Information received when registering is withdrawn from the profile
    @SuppressLint("SetTextI18n")
    fun firestoreUserData() {
        val currentUser = fireBaseAuth.currentUser
        if (currentUser != null) {
            firebaseFirestoreDB.collection("Users").document(currentUser.uid)
                .addSnapshotListener { snapshot, exception ->
                    if (exception != null) {
                        Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT)
                            .show()
                    } else {
                        if (snapshot != null && snapshot.exists()) {
                            val name = snapshot.data?.get("name").toString()
                            val lastName = snapshot.data?.get("lastName").toString()
                            if (name != "" && lastName != "") {
                                viewBinding.fragmentProfileTxvName.text = "$name $lastName"
                                viewBinding.fragmentProfileTxvNameChar.text =
                                    name.get(0).toString() + lastName.get(0).toString()
                            } else {
                                if (currentUser.displayName != null && currentUser.displayName != "") {
                                    viewBinding.fragmentProfileTxvName.text =
                                        currentUser.displayName
                                    viewBinding.fragmentProfileTxvNameChar.text =
                                        currentUser.displayName?.first().toString()
                                } else {
                                    viewBinding.fragmentProfileTxvName.text = currentUser.email
                                    viewBinding.fragmentProfileTxvNameChar.text =
                                        currentUser.email?.get(0).toString().replaceFirstChar {
                                            it.uppercaseChar()
                                        }
                                }
                            }
                        }
                    }
                }
        }
    }

    //Create user information document if it does not exist
    fun firebaseFirestoreAddData() {
        val currentUserUid = fireBaseAuth.currentUser!!.uid
        val usersHashMap = HashMap<String, Any>()
        usersHashMap.put("name", "")
        usersHashMap.put("lastName", "")
        usersHashMap.put("phone", "")
        firebaseFirestoreDB.collection("Users").document(currentUserUid).set(usersHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                }
            }.addOnFailureListener { exception ->
                exception.localizedMessage
            }
    }
}