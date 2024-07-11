package com.example.aaaaaa

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alsess.databinding.FragmentProductParentRowBinding
import com.example.alsess.model.ProductRVParentModel
import com.example.alsess.view.fragment.ProductChildFragmentDirections


class ProductParentAdapter(val context: Context, val parentList: List<ProductRVParentModel>) :
    RecyclerView.Adapter<ProductParentAdapter.ProductParentVH>() {
    class ProductParentVH(val viewBinding: FragmentProductParentRowBinding) :
        RecyclerView.ViewHolder(viewBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductParentVH {
        val view = FragmentProductParentRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ProductParentVH(view)
    }

    override fun getItemCount(): Int {
        return parentList.size
    }

    override fun onBindViewHolder(holder: ProductParentVH, position: Int) {

        holder.viewBinding.rowProductParentRecyclerView.adapter =
            ProductChildAdapter(context, parentList.get(position).childList)

        holder.viewBinding.rowProductParentRecyclerView.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL, false
        )
        holder.viewBinding.rowProductParentTxvCategory.text = parentList.get(position).categoryTitle

        //Sends Category and is listed in ProductCategoryAll by category
        holder.viewBinding.rowProductParentBtnAll.setOnClickListener {
            val category = parentList.get(position).category
            val categoryTitle = parentList.get(position).categoryTitle
            val navDirections =
                ProductChildFragmentDirections.actionProductChildFragmentToProductCategoryFragment(
                    category,
                    categoryTitle
                )
            Navigation.findNavController(it).navigate(navDirections)
        }
    }
}