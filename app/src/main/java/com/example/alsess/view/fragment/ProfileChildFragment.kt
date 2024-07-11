package com.example.alsess.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alsess.adapters.ProfileChildRvAdapter
import com.example.alsess.databinding.FragmentProfileChildBinding
import com.example.alsess.util.AppUtils
import com.example.alsess.view.activity.LoginSignUpActivity
import com.example.alsess.viewmodel.ProfileChildViewModel

class ProfileChildFragment : Fragment() {
    private lateinit var viewBinding: FragmentProfileChildBinding
    private lateinit var profileChildViewModel: ProfileChildViewModel
    private val appUtils = AppUtils()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProfileChildBinding.inflate(inflater, container, false)
        profileChildViewModel = ViewModelProvider(this).get(ProfileChildViewModel::class.java)
        profileChildViewModel.currentUser()
        profileChildViewModel.controlAddProfileList(requireContext())
        viewModelObserve()
        buttonClickAction()
        return viewBinding.root
    }

    fun viewModelObserve() {
        profileChildViewModel.profileItemMLD.observe(viewLifecycleOwner) { it ->
            it?.let {
                viewBinding.fragmentProfileChildRV.adapter = ProfileChildRvAdapter(requireContext(), it)
                viewBinding.fragmentProfileChildRV.layoutManager = LinearLayoutManager(context)
            }
        }

        profileChildViewModel.errorMessageMLD.observe(viewLifecycleOwner){ error ->
            error?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT)
                    .show()
            }

        }
        profileChildViewModel.loadMLD.observe(viewLifecycleOwner) { load ->
            load?.let {
                if (it) {
                    viewBinding.fragmentProfileChildCardViewParent.visibility = View.VISIBLE
                }
            }
        }

        profileChildViewModel.userMLD.observe(viewLifecycleOwner) { user ->
            user?.let {
                viewBinding.fragmentProfileChildTxvNameChar.text = it.get("nameChar")
                viewBinding.fragmentProfileChildTxvName.text = it.get("name")
                viewBinding.fragmentProfileChildCardViewParent.visibility = View.VISIBLE
            }
        }
        profileChildViewModel.currentUserMLD.observe(viewLifecycleOwner){ currentUser ->
            currentUser?.let {
                if (!it) {
                    viewBinding.fragmentProfileChildCardViewNoUser.visibility = View.VISIBLE
                }
            }
        }
    }

    fun buttonClickAction() {
        viewBinding.fragmentProfileChildBtnLogin.setOnClickListener {
            appUtils.intentActivityAndExtra(requireContext(),LoginSignUpActivity(),0)
        }
        viewBinding.fragmentProfileChildBtnSignUp.setOnClickListener {
            appUtils.intentActivityAndExtra(requireContext(),LoginSignUpActivity(),1)
        }

    }
}