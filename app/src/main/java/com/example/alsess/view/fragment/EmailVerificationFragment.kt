package com.example.alsess.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.alsess.R
import com.example.alsess.databinding.FragmentEmailVerificationBinding
import com.google.firebase.auth.FirebaseAuth

class EmailVerificationFragment : Fragment() {
    private lateinit var viewBinding: FragmentEmailVerificationBinding
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentEmailVerificationBinding.inflate(inflater, container, false)

        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser!!

        if (currentUser.isEmailVerified) {
            viewBinding.fragmentEmailVerificationTxvCurrentEmail
                .setCompoundDrawablesWithIntrinsicBounds(R.drawable.asset_tic, 0, 0, 0)
        }

        viewBinding.fragmentEmailVerificationTxvCurrentEmail.text = firebaseAuth.currentUser!!.email

        viewBinding.fragmentEmailVerificationToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        viewBinding.fragmentEmailVerificationBtnEmailVerification.setOnClickListener {
            firebaseEmailVerification()
        }
        return viewBinding.root

    }

    //The user stops the app to look at the email and the data is refreshed when they come back
    override fun onResume() {
        super.onResume()
        refreshFirebaseEmailVerification()
    }

    //For live refresh of data
    fun refreshFirebaseEmailVerification() {
        val currentUser = firebaseAuth.currentUser!!
        currentUser.reload().addOnSuccessListener { void ->
            if (currentUser.isEmailVerified) {
                viewBinding.fragmentEmailVerificationTxvCurrentEmail
                    .setCompoundDrawablesWithIntrinsicBounds(R.drawable.asset_tic, 0, 0, 0)
            }
        }
    }

    fun firebaseEmailVerification() {
        val currentUser = firebaseAuth.currentUser!!
        if (currentUser.isEmailVerified) {
            Toast.makeText(context, getString(R.string.email_already_verified), Toast.LENGTH_SHORT)
                .show()
        } else {
            currentUser.sendEmailVerification()
            Toast.makeText(
                context,
                getString(R.string.verification_link_sent),
                Toast.LENGTH_SHORT
            ).show()

        }

    }
}