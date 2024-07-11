package com.example.alsess.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.alsess.databinding.FragmentProfileParentBinding

class ProfileParentFragment : Fragment() {
    private lateinit var viewBinding: FragmentProfileParentBinding
    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBinding = FragmentProfileParentBinding.inflate(inflater, container, false)
        return viewBinding.root
    }
}