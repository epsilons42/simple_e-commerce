package com.example.alsess.service

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.alsess.view.activity.MainActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AddFirebaseData {
    private lateinit var firebaseAuth: FirebaseAuth
    val firebaseFireStoreDB = FirebaseFirestore.getInstance()

    fun addFireStoreUserDataSignUp(
        context: Context,
        collectionName: String,
        setHashMap: HashMap<String, String>
    ) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUserUid = firebaseAuth.currentUser!!.uid
        firebaseFireStoreDB.collection(collectionName).document(currentUserUid).set(setHashMap)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(context, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    context.startActivity(intent)
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT)
                    .show()
            }
    }
    fun addFireStoreUserDataProfile() {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUserUid = firebaseAuth.currentUser!!.uid
        val usersHashMap = HashMap<String, Any>()
        usersHashMap.put("name", "")
        usersHashMap.put("lastName", "")
        usersHashMap.put("phone", "")
        firebaseFireStoreDB.collection("Users").document(currentUserUid).set(usersHashMap)
            .addOnCompleteListener { task ->
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }
    }
}