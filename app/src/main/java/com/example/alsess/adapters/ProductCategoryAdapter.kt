package com.example.alsess.adapters

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
import com.example.alsess.databinding.FragmentProductCategoryRowBinding
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.service.BasketSQLiteDao
import com.example.alsess.service.FavoritesSQLiteDao
import com.example.alsess.service.BasketSQLiteDataHelper
import com.example.alsess.service.FavoritesSQLiteDataHelper
import com.example.alsess.view.fragment.ProductCategoryFragmentDirections
import com.google.firebase.auth.FirebaseAuth

class ProductCategoryAdapter(
    val context: Context,
    val productMutableList: MutableList<ApiProductsModel>
) : RecyclerView.Adapter<ProductCategoryAdapter.ProductCategoryAllVH>() {
    private lateinit var firebaseAuth: FirebaseAuth

    class ProductCategoryAllVH(val dataBinding: FragmentProductCategoryRowBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductCategoryAllVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<FragmentProductCategoryRowBinding>(
            inflater,
            R.layout.fragment_product_category_row,
            parent,
            false
        )
        return ProductCategoryAllVH(view)
    }

    override fun getItemCount(): Int {
        return productMutableList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductCategoryAllVH, position: Int) {
        firebaseAuth = FirebaseAuth.getInstance()
        val currentUser = firebaseAuth.currentUser
        val mutablePosition = productMutableList.get(position)

        holder.dataBinding.product = productMutableList[position]

        //Data cannot be added to the favorites list if no user is logged in
        if (currentUser != null) {
            addDataFavorites(holder.dataBinding.rowProductCategoryAllTgbFavorites, position)
        } else {
            holder.dataBinding.rowProductCategoryAllTgbFavorites.setBackgroundResource(R.drawable.asset_favorites_white)
            /*
            holder.dataBinding.rowProductCategoryAllTgbFavorites.setOnClickListener {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
            }

             */

        }

        addDataBasket(holder.dataBinding.rowProductCategoryAllTgbAddBasket, position)

        holder.dataBinding.rowProductCategoryAllCardView.setOnClickListener {
            val dataDrections =
                ProductCategoryFragmentDirections.actionProductCategoryFragmentToProductDetailFragment(
                    mutablePosition.id
                )
            Navigation.findNavController(it).navigate(dataDrections)
        }
    }

    fun addDataFavorites(toggleButton: ToggleButton, position: Int) {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(context)
        val favoritesDAO = FavoritesSQLiteDao()
        val mutablePositionFav = productMutableList.get(position)

        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (favoritesDAO.controlFavorites(
                        favoritesDataHelper,
                        mutablePositionFav.id
                    ) == 0
                ) {
                    favoritesDAO.addFavorites(
                        favoritesDataHelper,
                        mutablePositionFav.id,
                        mutablePositionFav.title,
                        mutablePositionFav.price,
                        mutablePositionFav.image
                    )
                }
            } else {
                favoritesDAO.deleteFavorites(favoritesDataHelper, mutablePositionFav.id)
            }
        }
        //Toggle button is checked as true if added to favorites, false if not
        if (favoritesDAO.controlFavorites(favoritesDataHelper, mutablePositionFav.id) == 0) {
            toggleButton.isChecked = false
        } else {
            toggleButton.isChecked = true
        }
    }

    fun addDataBasket(toggleButton: ToggleButton, position: Int) {
        val basketDataHelper = BasketSQLiteDataHelper(context)
        val basketDAO = BasketSQLiteDao()
        val mutablePositionBasket = productMutableList.get(position)
        toggleButton.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                if (basketDAO.controlBasket(
                        basketDataHelper,
                        mutablePositionBasket.id
                    ) == 0
                ) {
                    basketDAO.addBasket(
                        basketDataHelper,
                        mutablePositionBasket.id,
                        mutablePositionBasket.title,
                        mutablePositionBasket.price,
                        mutablePositionBasket.image,
                        1
                    )
                }
            } else {
                basketDAO.deleteBasket(basketDataHelper, mutablePositionBasket.id)
            }
        }
        //Toggle button is checked as true if added to basket, false if not
        if (basketDAO.controlBasket(basketDataHelper, mutablePositionBasket.id) == 0) {
            toggleButton.isChecked = false
        } else {
            toggleButton.isChecked = true
        }

    }
}