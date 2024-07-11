package com.example.alsess.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.example.alsess.databinding.FragmentChangeEmailBinding
import com.example.alsess.service.UpdateFirebaseData
import com.google.firebase.auth.FirebaseAuth

class ChangeEmailFragment : Fragment() {
    private lateinit var viewBinding: FragmentChangeEmailBinding
    private lateinit var firebaaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentChangeEmailBinding.inflate(inflater, container, false)
        val changefirebaseEmail = UpdateFirebaseData(requireContext())
        firebaaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaaseAuth.currentUser!!
        viewBinding.fragmentChangeEmailTxvCurrentEmail.text = currentUser.email
        if (currentUser.isEmailVerified) {
            viewBinding.fragmentChangeEmailCardView.visibility = View.VISIBLE
        } else {
            viewBinding.fragmentChangeEmailTxvVerifyYourEmail.visibility = View.VISIBLE
            viewBinding.fragmentChangeEmailTxvGo.visibility = View.VISIBLE
        }
        viewBinding.fragmentChangeEmailBtnChange.setOnClickListener {
            val newEmail = viewBinding.fragmentChangeEmailEdtNewEmail.text.toString()
            val newEmailAgain = viewBinding.fragmentChangeEmailEdtNewEmailAgain.text.toString()
            changefirebaseEmail.changefirebaseEmail(newEmail,newEmailAgain)
        }
        viewBinding.fragmentChangeEmailToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }
        return viewBinding.root
    }
}