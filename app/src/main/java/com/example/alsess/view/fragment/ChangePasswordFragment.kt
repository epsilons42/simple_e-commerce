package com.example.alsess.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.alsess.R
import com.example.alsess.databinding.FragmentChangePasswordBinding
import com.example.alsess.service.UpdateFirebaseData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ChangePasswordFragment : Fragment() {
    private lateinit var viewBinding: FragmentChangePasswordBinding
    private lateinit var auth: FirebaseAuth
    val firebaseFirestoreDB = FirebaseFirestore.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentChangePasswordBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()

        viewBinding.fragmentChangePasswordBtnChange.setOnClickListener {
            changePassword()
        }

        /*İf the e-mail has been verified, the password change screen opens,
         if not, the action to switch to the verification screen opens */
        //Does not need to change password when logged in with google
        if (auth.currentUser!!.isEmailVerified) {
            viewBinding.fragmentChangePasswordCardView.visibility = View.VISIBLE
        } else {
            if (GoogleSignIn.getLastSignedInAccount(requireContext()) != null) {
                viewBinding.fragmentChangePasswordTxvWithGoogle.visibility = View.VISIBLE
            } else {
                viewBinding.fragmentChangePasswordTxvVerifyYourEmail.visibility = View.VISIBLE
                viewBinding.fragmentChangePasswordTxvGo.visibility = View.VISIBLE
            }

        }
        viewBinding.fragmentChangePasswordTxvGo.setOnClickListener {
            Navigation.findNavController(it)
                .navigate(R.id.action_changePasswordFragment_to_emailVerificationFragment)
        }

        viewBinding.fragmentChangePasswordToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        return viewBinding.root
    }

    fun changePassword() {
        val updatePassword = UpdateFirebaseData(requireContext())
        val newPassword = viewBinding.fragmentChangePasswordEdtNewPassword.text.toString()
        val newPasswordAgain = viewBinding.fragmentChangePasswordEdtNewPasswordAgain.text.toString()
        if (newPassword != "" && newPasswordAgain != "") {
            if (newPassword == newPasswordAgain) {
              updatePassword.changefirebasePassword(newPassword)

            } else {
                Toast.makeText(context, getString(R.string.password_not_same), Toast.LENGTH_SHORT)
                    .show()
            }
        } else {
            Toast.makeText(context, getString(R.string.empty_field), Toast.LENGTH_SHORT)
                .show()
        }
    }

}
