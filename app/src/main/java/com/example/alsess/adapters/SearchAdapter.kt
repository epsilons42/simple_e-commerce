package com.example.alsess.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.alsess.R
import com.example.alsess.databinding.FragmentSearchRowBinding
import com.example.alsess.view.fragment.SearchFragmentDirections

class SearchAdapter(var searchItemList: List<String>) :
    RecyclerView.Adapter<SearchAdapter.SearchVH>() {
    class SearchVH(val dataBinding: FragmentSearchRowBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<FragmentSearchRowBinding>(inflater, R.layout.fragment_search_row,parent,false)
        return SearchVH(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setFilteredList(searchItemList: List<String>) {
        this.searchItemList = searchItemList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (searchItemList.size > 8) {
            return 7
        } else {
            return searchItemList.size
        }

    }

    override fun onBindViewHolder(holder: SearchVH, position: Int) {
        holder.dataBinding.searchItem = searchItemList[position]
        holder.dataBinding.rowSearchCardView.setOnClickListener {
            val dataDirections =
                SearchFragmentDirections.actionSearchFragmentToProductSearchFragment(searchItemList.get(position))
            Navigation.findNavController(it)
                .navigate(dataDirections)
        }
    }
}