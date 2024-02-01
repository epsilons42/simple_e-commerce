package com.example.alsess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.example.alsess.databinding.FragmentEmailVerificationBinding
import com.google.firebase.auth.FirebaseAuth

class EmailVerificationFragment : Fragment() {
    private lateinit var viewBinding: FragmentEmailVerificationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentEmailVerificationBinding.inflate(inflater, container, false)

        return viewBinding.root

    }
}