package com.example.alsess.util

import android.annotation.SuppressLint
import android.app.ActionBar.LayoutParams
import android.content.Context
import android.view.LayoutInflater
import android.widget.Toast
import androidx.core.widget.NestedScrollView
import com.example.alsess.R
import com.example.alsess.view.activity.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth

class DialogUtils {
    private lateinit var firebaseAuth: FirebaseAuth
    private val appUtils = AppUtils()
    private val firebaseUtils = FirebaseUtils()
    fun deleteAccoundAlerdDialog(context : Context,str : String) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currendUser = firebaseAuth.currentUser!!
        val userEmail = str

        val alertDialog = android.app.AlertDialog.Builder(context)

        alertDialog.setTitle(context.getString(R.string.delete_account))
        alertDialog.setMessage(context.getString(R.string.delete_account_alert_message))
        alertDialog.setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, i ->
            alertDialog.create().dismiss()
        }
        alertDialog.setNeutralButton(context.getString(R.string.yes)) { dialogInterface, i ->
            currendUser.delete().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    /*After deleting someone who enters Google,
                     so that the selection screen appears again on the login screen
                     */
                    val googleSignInClient = GoogleSignIn.getClient(
                        context,
                        GoogleSignInOptions.DEFAULT_SIGN_IN
                    )
                    googleSignInClient.revokeAccess()

                    appUtils.intentAndClear(context, MainActivity())
                    firebaseUtils.deleteFireStoreCollection()
                }

            }.addOnFailureListener { exception ->
                Toast.makeText(context, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
        if (currendUser.email == userEmail) {
            alertDialog.show()
        } else {
            Toast.makeText(
                context,
                context.getString(R.string.enter_info_correctly),
                Toast.LENGTH_SHORT
            )
                .show()
        }
    }
    @SuppressLint("InflateParams")
    fun userAgreementBottomSheet(context: Context){
        val bottomSheetDialog = BottomSheetDialog(context,R.style.BottomSheetDialogTheme)
        val bottomSheetView = LayoutInflater.from(context).inflate(R.layout.user_agreement_bottom_sheet,null)
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.create()

        val userAgreementScv = bottomSheetView.findViewById(R.id.userAgreementScv) as NestedScrollView
        userAgreementScv.layoutParams.height = LayoutParams.MATCH_PARENT
        bottomSheetDialog.show()

    }
}