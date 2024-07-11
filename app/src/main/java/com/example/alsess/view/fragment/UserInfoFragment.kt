package com.example.alsess.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.alsess.R
import com.example.alsess.databinding.FragmentUserInfoBinding
import com.example.alsess.viewmodel.UserInfoViewModel

class UserInfoFragment : Fragment() {
    private lateinit var viewBinding: FragmentUserInfoBinding
    private lateinit var userInfoViewModel: UserInfoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentUserInfoBinding.inflate(inflater, container, false)
        userInfoViewModel = ViewModelProvider(this).get(UserInfoViewModel::class.java)
        userInfoViewModel.readFireStoreUserData()
        viewModelObserve()
        viewBinding.fragmentUserInfoBtnUpdateInfo.setOnClickListener {
            val userName = viewBinding.fragmentUserInfoEdtName.text.toString()
            val userLastName = viewBinding.fragmentUserInfoEdtLastName.text.toString()
            val userPhone = viewBinding.fragmentUserInfoEdtPhone.text.toString()
            userInfoViewModel.updateFireStoreUserData(
                requireContext(),
                userName,
                userLastName,
                userPhone
            )
        }
        viewBinding.fragmentUserInfoToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        return viewBinding.root
    }

    private fun viewModelObserve() {
        val emptyEditTextShape = R.drawable.edittext_empty_shape
        val editTextShape = R.drawable.edittext_shape
        userInfoViewModel.userInfoMLD.observe(viewLifecycleOwner, Observer { user ->
            user?.let {
                val name = it.get("name")
                val lastName = it.get("lastName")
                val phone = it.get("phone")
                val email = it.get("email")
                viewBinding.fragmentUserInfoTxvEmail.text = email
                if (name != "") {
                    viewBinding.fragmentUserInfoEdtName.setText(name)
                    viewBinding.fragmentUserInfoEdtName.setBackgroundResource(
                        editTextShape
                    )
                } else {
                    viewBinding.fragmentUserInfoEdtName.setBackgroundResource(
                        emptyEditTextShape
                    )
                }
                if (lastName != "") {
                    viewBinding.fragmentUserInfoEdtLastName.setText(lastName)
                    viewBinding.fragmentUserInfoEdtLastName.setBackgroundResource(
                        editTextShape
                    )
                } else {
                    viewBinding.fragmentUserInfoEdtLastName.setBackgroundResource(
                        emptyEditTextShape
                    )
                }
                if (phone != "") {
                    viewBinding.fragmentUserInfoEdtPhone.setText(phone)
                    viewBinding.fragmentUserInfoEdtPhone.setBackgroundResource(
                        editTextShape
                    )
                } else {
                    viewBinding.fragmentUserInfoEdtPhone.setBackgroundResource(
                        emptyEditTextShape
                    )
                }
            }
        })
    }
}