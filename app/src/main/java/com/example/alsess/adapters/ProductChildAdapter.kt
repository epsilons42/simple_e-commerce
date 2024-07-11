package com.example.aaaaaa

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ToggleButton
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProductChildRowBinding
import com.example.alsess.model.ProductRVChildModel
import com.example.alsess.service.FavoritesSQLiteDao
import com.example.alsess.service.FavoritesSQLiteDataHelper
import com.example.alsess.view.fragment.ProductChildFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class ProductChildAdapter(val context: Context, val childList: List<ProductRVChildModel>) :
    RecyclerView.Adapter<ProductChildAdapter.ProductChildVH>() {
    private lateinit var firebaseAuth: FirebaseAuth

    class ProductChildVH(val dataBinding: FragmentProductChildRowBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductChildVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<FragmentProductChildRowBinding>(
            inflater,
            R.layout.fragment_product_child_row,
            parent,
            false
        )
        return ProductChildVH(view)
    }

    override fun getItemCount(): Int {
        return childList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductChildVH, position: Int) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        holder.dataBinding.product = childList[position]


        //Data cannot be added to the favorites list if no user is logged in
        if (currentUser != null) {
            addDataFavorites(holder.dataBinding.rowProductChildTgbAddFavorites, position)
        } else {
            holder.dataBinding.rowProductChildTgbAddFavorites.setBackgroundResource(R.drawable.asset_favorites_white)
           /*
            holder.dataBinding.rowProductChildTgbAddFavorites.setOnClickListener {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }

            */

        }


        //Gets the id of the clicked product and shows the product in the product detail
        holder.dataBinding.rowProductChildCardView.setOnClickListener {
            val navARg = ProductChildFragmentDirections.toProductDetail(
                childList.get(position).productId
            )
            Navigation.findNavController(it).navigate(navARg)
        }


    }

    //To add to favorites from child in nested recyclerview
    fun addDataFavorites(toggleButton: ToggleButton, position: Int) {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(context)
        val favoritesDAO = FavoritesSQLiteDao()

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (favoritesDAO.controlFavorites(
                        favoritesDataHelper,
                        childList.get(position).productId
                    ) == 0
                ) {
                    favoritesDAO.addFavorites(
                        favoritesDataHelper,
                        childList.get(position).productId,
                        childList.get(position).productName,
                        childList.get(position).productPrice,
                        childList.get(position).productImage
                    )
                }
            } else {
                favoritesDAO.deleteFavorites(favoritesDataHelper, childList.get(position).productId)
            }
        }
        //Toggle button is checked as true if added to favorites, false if not
        if (favoritesDAO.controlFavorites(
                favoritesDataHelper,
                childList.get(position).productId
            ) == 0
        ) {
            toggleButton.isChecked = false
        } else {
            toggleButton.isChecked = true
        }
    }
}