package com.example.alsess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.alsess.FavoritesFragmentDirections
import com.example.alsess.databinding.FavoritesFragmentRowBinding
import com.example.alsess.sqlitedaos.FavoritesSqliteDao
import com.example.alsess.sqlitedatahelpers.FavoritesSqliteDataHelper



class FavoritesReyclerViewAdapter(val context: Context) : RecyclerView.Adapter<FavoritesReyclerViewAdapter.FavoritesVH>() {
    class FavoritesVH(val viewBinding : FavoritesFragmentRowBinding) : RecyclerView.ViewHolder(viewBinding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoritesVH {
        val view = FavoritesFragmentRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return FavoritesVH(view)
    }

    override fun getItemCount(): Int {
        val favoritesDataHelper = FavoritesSqliteDataHelper(context)
        val titlelist = FavoritesSqliteDao().readFavorites(favoritesDataHelper)
        return titlelist.size
    }

    override fun onBindViewHolder(holder: FavoritesVH, position: Int) {
        //Ürünler listesinden veritabanına eklenen veri bilgileri çekilir ve görünümlere aktarılır
        val favoritesDataHelper = FavoritesSqliteDataHelper(context)
        val favoritesList = FavoritesSqliteDao().readFavorites(favoritesDataHelper)
        holder.viewBinding.titleText.text = favoritesList.get(position).title.replace("'"," ")
        holder.viewBinding.priceText.text = favoritesList.get(position).price.toString()
        Glide.with(context!!).load(favoritesList.get(position).image_url).into(holder.viewBinding.imageView)

        //Favorilerdeki ürünlerin detay sayfasında görüntülenmesi sağlanır
        //Bilgiler id üzerinden aktarılıp çekilir
        holder.viewBinding.cardView.setOnClickListener {
            val navArgs = FavoritesFragmentDirections.toProductsDetail(favoritesList.get(position).id.toInt())
            Navigation.findNavController(it).navigate(navArgs)
        }
        //Favoriler butonuna tıklanınca veri tabanına eklenen veriler id yardımıyla silinir
        holder.viewBinding.favoritesButton.setOnClickListener {
            FavoritesSqliteDao().deleteFavorites(favoritesDataHelper,favoritesList.get(position).id)
            notifyDataSetChanged()
        }
    }
}