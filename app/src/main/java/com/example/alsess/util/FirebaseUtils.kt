package com.example.alsess.util

import android.content.Context
import android.widget.TextView
import android.widget.Toast
import com.example.alsess.R
import com.example.alsess.view.activity.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUtils() {
    private lateinit var firebaseAuth: FirebaseAuth
    private val appUtils = AppUtils()
    private val firebaseFireStoreDB = FirebaseFirestore.getInstance()
    fun currentUser(): Boolean {
        firebaseAuth = FirebaseAuth.getInstance()
        return firebaseAuth.currentUser != null
    }

    fun firebaseIsEmailVerified(): Boolean {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser!!
        return currentUser.isEmailVerified

    }

    fun firebaseCurrentUserEmail(): String? {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        return currentUser?.email
    }
    fun googleLastSignedInAccount(context: Context): Boolean {
        val getLastSigned = GoogleSignIn.getLastSignedInAccount(context)
        return getLastSigned == null
    }

    fun changeFirebaseEmail(context: Context, newEmail: String, newEmailAgain: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser!!

        if (newEmail != "" && newEmailAgain != "") {
            if (newEmail == newEmailAgain) {
                currentUser.updateEmail(newEmail).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(
                            context,
                            context.getString(R.string.change_email),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.email_not_same),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        } else {
            Toast.makeText(context, context.getString(R.string.empty_field), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun changeFirebasePassword(context: Context, newPassword: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser!!
        currentUser.updatePassword(newPassword).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(
                    context,
                    context.getString(R.string.password_updated),
                    Toast.LENGTH_SHORT
                )
                    .show()
            }

        }.addOnFailureListener { exception ->
            exception.printStackTrace()
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

    fun firebaseEmailVerification(context: Context) {
        val currentUser = firebaseAuth.currentUser!!
        if (currentUser.isEmailVerified) {
            Toast.makeText(
                context,
                context.getString(R.string.email_already_verified),
                Toast.LENGTH_SHORT
            )
                .show()
        } else {
            currentUser.sendEmailVerification()
            Toast.makeText(
                context,
                context.getString(R.string.verification_link_sent),
                Toast.LENGTH_SHORT
            ).show()

        }
    }

    fun refreshFirebaseEmailVerification(textView: TextView) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser!!
        currentUser.reload().addOnSuccessListener { void ->
            if (currentUser.isEmailVerified) {
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.asset_tic, 0, 0, 0)
            }
        }
    }

    fun deleteFireStoreCollection() {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUserUid = firebaseAuth.currentUser!!.uid
        firebaseFireStoreDB.collection("Users").document(currentUserUid).delete()
    }

    private fun updateFirestoreUserInfo(
        context: Context,
        currentUserUid: String,
        field: String,
        value: String
    ) {
        firebaseFireStoreDB.collection("Users").document(currentUserUid)
            .update(field, value).addOnSuccessListener {
            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }

    fun updateFireStoreUserData(
        context: Context,
        userName: String,
        userLastName: String,
        userPhone: String
    ) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUserUid = firebaseAuth.currentUser!!.uid
        updateFirestoreUserInfo(context, currentUserUid, "name", userName.replaceFirstChar {
            it.uppercase()
        })

        updateFirestoreUserInfo(context, currentUserUid, "lastName", userLastName.replaceFirstChar {
            it.uppercase()
        })
        updateFirestoreUserInfo(context, currentUserUid, "phone", userPhone)
    }
}