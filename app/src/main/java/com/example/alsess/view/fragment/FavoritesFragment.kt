package com.example.alsess.view.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alsess.adapters.FavoritesAdapter
import com.example.alsess.databinding.FragmentFavoritesBinding
import com.google.firebase.auth.FirebaseAuth

class FavoritesFragment : Fragment() {
    private lateinit var viewBinding: FragmentFavoritesBinding
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentFavoritesBinding.inflate(inflater, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            viewBinding.fragmentFavoritesRecyclerView.adapter =
                context?.let { FavoritesAdapter(it) }
            viewBinding.fragmentFavoritesRecyclerView.layoutManager =
                LinearLayoutManager(requireContext())
        } else {
            viewBinding.fragmentFavoritesBtnLogin.visibility = View.VISIBLE
            viewBinding.fragmentFavoritesImvBasket.visibility = View.VISIBLE
        }

        /*
        viewBinding.fragmentFavoritesBtnLogin.setOnClickListener {
            val intent = Intent(context, LoginActivity::class.java)
            startActivity(intent)
        }

         */


        return viewBinding.root
    }
}