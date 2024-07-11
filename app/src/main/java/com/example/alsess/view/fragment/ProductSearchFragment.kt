package com.example.alsess.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.alsess.R
import com.example.alsess.adapters.ProductSearchAdapter
import com.example.alsess.databinding.FragmentProductSearchBinding
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.view.fragment.ProductSearchFragmentArgs
import com.example.alsess.viewmodel.ProductSearchViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.util.*
import kotlin.collections.ArrayList


class ProductSearchFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductSearchBinding
    val productArrayList = ArrayList<ApiProductsModel>()
    val bundle: ProductSearchFragmentArgs by navArgs()
    private lateinit var productSearchViewModel: ProductSearchViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = FragmentProductSearchBinding.inflate(inflater, container, false)
        productSearchViewModel = ViewModelProvider(this).get(ProductSearchViewModel::class.java)
        productSearchViewModel.productLoadData(bundle.category)

        viewModelObserve()

        recyclerViewActions()
        viewBinding.fragmentProductSearchToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        viewBinding.fragmentProductSearchTxv.setOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        viewBinding.fragmentProductSearchTxv.text = bundle.category

        viewBinding.fragmentProductSearchBtnSort.setOnClickListener {
            bottomSheetDialogAction()
        }


        return viewBinding.root
    }

    fun viewModelObserve() {
        if (productArrayList.size == 0) {
            productSearchViewModel.productMLD.observe(
                viewLifecycleOwner,
                androidx.lifecycle.Observer { product ->
                    product?.let {
                        productArrayList.addAll(it)

                    }
                })
        }


        productSearchViewModel.productLoadMLD.observe(
            viewLifecycleOwner,
            androidx.lifecycle.Observer { productLoad ->
                productLoad?.let {
                    if (it) {
                        viewBinding.fragmentProductSearchBtnSort.visibility = View.VISIBLE
                        viewBinding.fragmentProductSearchPgb.visibility = View.GONE

                    } else {
                        viewBinding.fragmentProductSearchPgb.visibility = View.VISIBLE
                    }
                }
            })
    }


    @SuppressLint("InflateParams")
    fun bottomSheetDialogAction() {
        val bottomSheetDialog = context?.let { BottomSheetDialog(it) }
        val bottomSheetView = layoutInflater.inflate(R.layout.fragment_category_bottom_sheet, null)
        val sharedPreferences =
            context?.getSharedPreferences("radioButtonClick", Context.MODE_PRIVATE)
        val sharedPreferencesEditor = sharedPreferences?.edit()

        val imbCancel =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetImbCancel) as ImageButton

        val radioGroup =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRg) as RadioGroup
        bottomSheetDialog?.setContentView(bottomSheetView)

        val radioButton1 =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRbDefaultSorting) as RadioButton
        val radioButton2 =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRbLowestPrice) as RadioButton
        val radioButton3 =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRbHighestPrice) as RadioButton
        val radioButton4 =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRbLowestRated) as RadioButton
        val radioButton5 =
            bottomSheetView.findViewById(R.id.fragmentCategoryBottomSheetRbHighestRated) as RadioButton

        //Radio button remembers the saved click id
        when (sharedPreferences?.getInt("rbClickId", 0)) {
            1 -> radioButton1.isChecked = true
            2 -> radioButton2.isChecked = true
            3 -> radioButton3.isChecked = true
            4 -> radioButton4.isChecked = true
            5 -> radioButton5.isChecked = true
        }

        //Select the category via Radio Button and save the click id in sharedPreferences
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.fragmentCategoryBottomSheetRbDefaultSorting -> {
                    sharedPreferencesEditor?.putInt("rbClickId", 1)
                    sharedPreferencesEditor?.apply()
                    if (productArrayList.size != 0) {
                        recyclerViewActions()
                        bottomSheetDialog?.dismiss()
                    }
                }
                R.id.fragmentCategoryBottomSheetRbLowestPrice -> {
                    sharedPreferencesEditor?.putInt("rbClickId", 2)
                    sharedPreferencesEditor?.apply()
                    if (productArrayList.size != 0) {
                        recyclerViewActions()
                        productArrayList.sortBy {
                            it.price
                        }
                        bottomSheetDialog?.dismiss()
                    }
                }
                R.id.fragmentCategoryBottomSheetRbHighestPrice -> {
                    sharedPreferencesEditor?.putInt("rbClickId", 3)
                    sharedPreferencesEditor?.apply()
                    if (productArrayList.size != 0) {
                        recyclerViewActions()
                        productArrayList.sortBy {
                            it.price
                        }
                        productArrayList.reverse()
                        bottomSheetDialog?.dismiss()
                    }
                }
                R.id.fragmentCategoryBottomSheetRbLowestRated -> {
                    sharedPreferencesEditor?.putInt("rbClickId", 4)
                    sharedPreferencesEditor?.apply()
                    if (productArrayList.size != 0) {
                        recyclerViewActions()
                        productArrayList.sortBy {
                            it.rating.rate
                        }
                        bottomSheetDialog?.dismiss()
                    }
                }
                R.id.fragmentCategoryBottomSheetRbHighestRated -> {
                    sharedPreferencesEditor?.putInt("rbClickId", 5)
                    sharedPreferencesEditor?.apply()
                    if (productArrayList.size != 0) {
                        recyclerViewActions()
                        productArrayList.sortBy {
                            it.rating.rate
                        }
                        productArrayList.reverse()
                        bottomSheetDialog?.dismiss()
                    }

                }
            }
        }

        imbCancel.setOnClickListener {
            bottomSheetDialog?.dismiss()
        }

        bottomSheetDialog?.show()

    }

    fun recyclerViewActions() {
        viewBinding.fragmentProductSearchRecyclerView.adapter =
            context?.let {
                ProductSearchAdapter(
                    it,
                    productArrayList
                )
            }
        viewBinding.fragmentProductSearchRecyclerView.layoutManager =
            GridLayoutManager(context, 2)
    }

}