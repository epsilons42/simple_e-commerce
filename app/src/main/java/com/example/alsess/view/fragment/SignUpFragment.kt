package com.example.alsess.view.fragment

import android.os.Bundle
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alsess.R
import com.example.alsess.databinding.FragmentSignUpBinding
import com.example.alsess.viewmodel.SignUpViewModel

class SignUpFragment : Fragment() {
    private lateinit var viewBinding: FragmentSignUpBinding
    private lateinit var signUpViewModel: SignUpViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding =  FragmentSignUpBinding.inflate(inflater,container,false)
        signUpViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)
        signUpViewModel.userAgreement(requireContext())
        viewModelObserve()
        viewBinding.fragmentSignUpBtnSingUp.setOnClickListener {
            signUpButton()
        }
        return viewBinding.root
    }

    fun signUpButton() {

        val email = viewBinding.fragmentSignUpEdtEMail.text.toString()
        val password = viewBinding.fragmentSignUpEdtPassword.text.toString()
        val passwordAgain = viewBinding.fragmentSignUpEdtPasswordAgain.text.toString()
        val userAgreementBoolean = viewBinding.fragmentSignUpChbUserAgreement.isChecked

        if (!userAgreementBoolean) {
            Toast.makeText(
                requireContext(),
                getString(R.string.no_user_agreement),
                Toast.LENGTH_SHORT
            ).show()
        } else if (email != "" && password != "" && passwordAgain != "") {
            if (password == passwordAgain) {
                signUpViewModel.firebaseAuthentication(
                    requireContext(),
                    email,
                    password,
                )
            } else {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.password_not_same),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(requireContext(), getString(R.string.empty_field), Toast.LENGTH_SHORT)
                .show()
        }
    }

    fun viewModelObserve() {
        signUpViewModel.errorMessageMLD.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                    .show()
            }
        }
        signUpViewModel.charSequenceMLD.observe(viewLifecycleOwner) { charsSequence ->
            charsSequence?.let {
                viewBinding.fragmentSignUpChbUserAgreement.setText(it)
                viewBinding.fragmentSignUpChbUserAgreement.movementMethod =
                    LinkMovementMethod.getInstance()
            }
        }

    }
}