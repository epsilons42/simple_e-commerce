package com.example.alsess.view.fragment

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.alsess.adapters.SearchAdapter
import com.example.alsess.databinding.FragmentSearchBinding
import com.example.alsess.view.fragment.SearchFragmentDirections
import com.example.alsess.viewmodel.SearchViewModel
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var adapter: SearchAdapter
    private lateinit var viewBinding: FragmentSearchBinding
    private lateinit var searchViewModel: SearchViewModel
    val searchItemList = HashSet<String>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentSearchBinding.inflate(inflater, container, false)
        viewBinding.fragmentSearchToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        val sharedPreferences =
            context?.getSharedPreferences("radioButtonClick", Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences?.edit()
        sharedPreferencesEditor?.clear()
        sharedPreferencesEditor?.apply()

        searchViewModel = ViewModelProvider(this).get(SearchViewModel::class.java)
        searchViewModel.searchItemLoadData()
        viewModelObserve()
        searchProduct()

        recyclerView = viewBinding.fragmentSearchRecyclerView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = SearchAdapter(searchItemList.toList())
        recyclerView.adapter = adapter

        return viewBinding.root
    }

    fun searchProduct() {
        searchView = viewBinding.fragmentSearchSchv
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //The entered data is confirmed and sent to the product search page
                if (query != null) {
                    if (query.length >= 2) {
                        val dataDirections =
                            SearchFragmentDirections.actionSearchFragmentToProductSearchFragment(
                                query
                            )
                        view?.let {
                            Navigation.findNavController(it).navigate(dataDirections)
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })
    }

    private fun filterList(query: String?) {
        if (query != null) {
            if (query.isNotEmpty()) {
                recyclerView.visibility = View.VISIBLE
                val filteredList = ArrayList<String>()
                for (i in searchItemList) {
                    if (i.lowercase(Locale.ROOT).contains(query)) {
                        filteredList.add(i)
                    }
                }

                if (filteredList.isNotEmpty()) {
                    adapter.setFilteredList(filteredList)
                }
            } else {
                recyclerView.visibility = View.GONE
            }

        }
    }

    private fun viewModelObserve() {
        searchViewModel.searchItemMLD.observe(
            viewLifecycleOwner, androidx.lifecycle.Observer { product ->
                product?.let {
                    searchItemList.addAll(it)
                }
            })
    }
}