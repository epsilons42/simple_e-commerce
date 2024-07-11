package com.example.alsess.viewmodel

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.alsess.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginViewModel : ViewModel() {
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    val loginMLD = MutableLiveData<Boolean>()
    val loginWitGoogleMLD = MutableLiveData<Boolean>()
    val errorMessageMLD = MutableLiveData<String>()
    val loginEmptyMLD = MutableLiveData<Boolean>()
    val resetEmptyMLD = MutableLiveData<Boolean>()
    val resetLinkMLD = MutableLiveData<Boolean>()
    val resetErrorMessageMLD = MutableLiveData<String>()
    val handleErrorMLD = MutableLiveData<String>()
    val updateUIErrorMessageMLD = MutableLiveData<String>()


    fun loginWithEmail(email: String, password: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        if (email != "" && password != "") {
            firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginMLD.value = true
                }
            }.addOnFailureListener { exception ->
                errorMessageMLD.value = exception.localizedMessage
            }
        } else {
            loginEmptyMLD.value = true
        }
    }

    fun resetPassword(email: String) {
        firebaseAuth = FirebaseAuth.getInstance()
        if (email != "") {
            resetEmptyMLD.value = false
            firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    resetLinkMLD.value = true
                }
            }.addOnFailureListener { exception ->
                resetErrorMessageMLD.value = exception.localizedMessage
            }
        } else {
            resetEmptyMLD.value = true
        }
    }
    fun request(context: Context) {
        val clientID = context.getString(R.string.google_json_client_id)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(clientID)
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }
    //login with Google
     fun signInGoogle(launcher: ActivityResultLauncher<Intent>) {
        val signInIntent = googleSignInClient.signInIntent
        launcher.launch(signInIntent)
    }
    fun handleResults(task: Task<GoogleSignInAccount>) {
        if (task.isSuccessful) {
            val account: GoogleSignInAccount? = task.result
            if (account != null) {
                updateUI(account)
            }
        } else {
            handleErrorMLD.value = task.exception?.localizedMessage
        }
    }
   private fun updateUI(account: GoogleSignInAccount) {
        firebaseAuth = FirebaseAuth.getInstance()
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener {task->
            if(task.isSuccessful){
                loginWitGoogleMLD.value = true
            }else{
               updateUIErrorMessageMLD.value = task.exception?.localizedMessage
            }
        }
    }
    
}