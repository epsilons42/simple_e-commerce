package com.example.alsess.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aaaaaa.ProductParentAdapter
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProductChildBinding
import com.example.alsess.model.ProductRVParentModel
import com.example.alsess.viewmodel.ProductChildViewModel

class ProductChildFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductChildBinding
    private lateinit var productChildViewModel: ProductChildViewModel
    private val productParentList = ArrayList<ProductRVParentModel>()

    /*Clicking the radiobutton in the product search and categories section is saved,
     when you come to the home page,
      this record is deleted and returns to the default values
     */
    override fun onStart() {
        super.onStart()
        val sharedPreferences =
            requireContext().getSharedPreferences("radioButtonClick", Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences.edit()
        sharedPreferencesEditor.clear()
        sharedPreferencesEditor.apply()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProductChildBinding.inflate(inflater, container, false)
        productChildViewModel = ViewModelProvider(this).get(ProductChildViewModel::class.java)
        productChildViewModel.productLoadData(requireContext())
        viewModelObserve()
        viewBinding.fragmentProductChildRecyclerView.adapter =
            ProductParentAdapter(requireContext(), productParentList)

        viewBinding.fragmentProductChildRecyclerView.layoutManager =
            LinearLayoutManager(requireContext())

        //Click on the search button and it switches to the search fragment
        viewBinding.fragmentProductChildSearchView.setOnQueryTextFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                Navigation.findNavController(v)
                    .navigate(R.id.action_productChildFragment_to_searchFragment)
            }
        }
        return viewBinding.root
    }

    fun viewModelObserve() {
        if (productParentList.size == 0) {
            productChildViewModel.producParenttMLD.observe(viewLifecycleOwner, Observer { product ->
                product?.let {
                    productParentList.addAll(it)
                    viewBinding.fragmentProductChildRecyclerView.adapter =
                        ProductParentAdapter(requireContext(), it)
                    viewBinding.fragmentProductChildRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext())
                }
            })
        }


        productChildViewModel.loadMLD.observe(viewLifecycleOwner, Observer { load ->
            load?.let {
                if (it) {
                    viewBinding.fragmentProductChildPgb.visibility = View.GONE

                }
            }
        })
    }
}