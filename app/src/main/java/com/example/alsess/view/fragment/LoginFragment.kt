package com.example.alsess.view.fragment

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.alsess.R
import com.example.alsess.databinding.FragmentLoginBinding
import com.example.alsess.util.AppUtils
import com.example.alsess.view.activity.MainActivity
import com.example.alsess.viewmodel.LoginViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn

class LoginFragment : Fragment() {
    private lateinit var viewBinding: FragmentLoginBinding
    private lateinit var loginViewModel: LoginViewModel
    private val appUtils = AppUtils()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLoginBinding.inflate(inflater,container,false)
        loginViewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        loginViewModel.request(requireContext())
        viewModelObserve()
        viewBinding.fragmentLoginBtnLogin.setOnClickListener {
            loginWithEmail()
        }
        viewBinding.fragmentLoginBtnResetPassword.setOnClickListener {
            resetPassword()
        }
        viewBinding.fragmentLoginImbGoogle.setOnClickListener {
            loginWithGoogle()
        }
        return viewBinding.root
    }

    //activityLoginButtonLogin onClick
    //Login with e-mail and password using Firebase
    fun loginWithEmail() {
        val email = viewBinding.fragmentLoginTxvEmail.text.toString().trim()
        val password = viewBinding.fragmentLoginTxvPassword.text.toString().trim()
        loginViewModel.loginWithEmail(email, password)
    }

    //activityLoginButtonResetPassword onClick
    //Firebase password reset link is sent to your e-mail
    fun resetPassword() {
        val email = viewBinding.fragmentLoginTxvEmail.text.toString().trim()
        loginViewModel.resetPassword(email)
    }

    //activityLoginImageButtonGoogle onClick
    //Login with Google using Firebase
    fun loginWithGoogle() {
        loginViewModel.signInGoogle(launcher)
    }

    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
                loginViewModel.handleResults(task)
            }
        }

    private fun viewModelObserve() {
        loginViewModel.loginMLD.observe(viewLifecycleOwner){ login ->
            login?.let {
                if (it) {
                   appUtils.intentAndClear(requireContext(), MainActivity())
                }
            }
        }
        loginViewModel.errorMessageMLD.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                    .show()

            }
        }
        loginViewModel.loginEmptyMLD.observe(viewLifecycleOwner) { loginEmpty ->
            loginEmpty?.let {
                if (it) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.email_password_empty),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        loginViewModel.resetLinkMLD.observe(viewLifecycleOwner){ reset ->
            reset?.let {
                if (it) {
                    viewBinding.fragmentLoginTxvPassword.setText("")
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.reset_password),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        loginViewModel.resetErrorMessageMLD.observe(viewLifecycleOwner){ resetError ->
            resetError?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT)
                    .show()
            }
        }

        loginViewModel.resetEmptyMLD.observe(viewLifecycleOwner){ resetEmpty ->
            resetEmpty?.let {
                if (it) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.enter_email),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        loginViewModel.handleErrorMLD.observe(viewLifecycleOwner) { handleError ->
            handleError?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        loginViewModel.loginWitGoogleMLD.observe(viewLifecycleOwner){ handleError ->
            handleError?.let {
                if (it) {
                    viewBinding.fragmentLoginPgb.visibility = View.VISIBLE
                    appUtils.intentAndClear(requireContext(), MainActivity())
                }
            }
        }
        loginViewModel.updateUIErrorMessageMLD.observe(viewLifecycleOwner){ updateUIError ->
            updateUIError?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

    }
}