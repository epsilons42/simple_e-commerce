package com.example.alsess.util

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.example.alsess.view.activity.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class AppUtils() {
    private lateinit var firebaseAuth: FirebaseAuth
    fun logOut(context: Context) {
        firebaseAuth = FirebaseAuth.getInstance()
        val googleSignInClient =

                GoogleSignIn.getClient(
                    context,
                    GoogleSignInOptions.DEFAULT_SIGN_IN
                )

        firebaseAuth.signOut()
        googleSignInClient?.revokeAccess()
        intentAndClear(context, MainActivity())
    }

    fun intentAndClear(context: Context, appCompatActivity: AppCompatActivity) {
        val intent = Intent(context, appCompatActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        context.startActivity(intent)
    }

    fun intentActivity(context: Context, appCompatActivity: AppCompatActivity) {
        val intent = Intent(context, appCompatActivity::class.java)
        context.startActivity(intent)
    }
    fun intentActivityAndExtra(context: Context, appCompatActivity: AppCompatActivity,value : Int) {
        val intent = Intent(context, appCompatActivity::class.java)
        intent.putExtra("item",value)
        context.startActivity(intent)
    }
    fun sortRadioButtonRefresh(context: Context){
        val sharedPreferences =
            context.getSharedPreferences("radioButtonClick", Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences?.edit()
        sharedPreferencesEditor?.clear()
        sharedPreferencesEditor?.apply()
    }
}