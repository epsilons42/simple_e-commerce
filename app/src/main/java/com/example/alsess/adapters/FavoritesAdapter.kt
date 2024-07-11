package com.example.alsess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.alsess.R
import com.example.alsess.databinding.FragmentFavoritesRowBinding
import com.example.alsess.service.FavoritesSQLiteDao
import com.example.alsess.service.FavoritesSQLiteDataHelper
import com.example.alsess.view.fragment.FavoritesFragmentDirections
import java.util.*

class FavoritesAdapter(val context: Context) : RecyclerView.Adapter<FavoritesAdapter.FavoritesVH>() {
    class FavoritesVH(val dataBinding : FragmentFavoritesRowBinding) : RecyclerView.ViewHolder(dataBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<FragmentFavoritesRowBinding>(
            inflater,
            R.layout.fragment_favorites_row,
            parent,
            false
        )
        return FavoritesVH(view)
    }

    override fun getItemCount(): Int {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(context)
        val titlelist = FavoritesSQLiteDao().readFavorites(favoritesDataHelper)
        return titlelist.size
    }

    override fun onBindViewHolder(holder: FavoritesVH, position: Int) {
        val favoritesDataHelper = FavoritesSQLiteDataHelper(context)
        val favoritesList = FavoritesSQLiteDao().readFavorites(favoritesDataHelper)
        Collections.reverse(favoritesList)
        holder.dataBinding.product = favoritesList[position]

        //Products in favorites are displayed on the detail page
        holder.dataBinding.recyclerRowFavoritesCardViewProduct.setOnClickListener {
            val navArgs = FavoritesFragmentDirections.actionFavoritesFragmentToProductDetailFragment(favoritesList.get(position).id)
            Navigation.findNavController(it).navigate(navArgs)
        }

        holder.dataBinding.recyclerRowFavoritesBtnFavorites.setOnClickListener {
            FavoritesSQLiteDao().deleteFavorites(favoritesDataHelper,favoritesList.get(position).id)
            notifyDataSetChanged()
        }
    }
}