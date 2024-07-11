package com.example.alsess.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProductDetailBinding
import com.example.alsess.model.ApiProductsModel
import com.example.alsess.view.fragment.ProductDetailFragmentArgs
import com.example.alsess.viewmodel.ProductDetailViewModel
import com.google.firebase.auth.FirebaseAuth

class ProductDetailFragment : Fragment() {
    private lateinit var dataBinding: FragmentProductDetailBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private val bundle: ProductDetailFragmentArgs by navArgs()
    private val productArrayList = ArrayList<ApiProductsModel>()
    private lateinit var productDetailViewModel: ProductDetailViewModel

    @SuppressLint("SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_product_detail, container, false)
        firebaseAuth = FirebaseAuth.getInstance()
        productDetailViewModel = ViewModelProvider(this)[ProductDetailViewModel::class.java]
        productDetailViewModel.loadProductData(bundle.id)
        viewModelObserve()
        if (productArrayList.size != 0) {
            dataBinding.product = productArrayList.get(0)
        }

        dataBinding.fragmentProductDetailToolbar.setNavigationOnClickListener {
            Navigation.findNavController(it).popBackStack()
        }

        return dataBinding.root
    }

    private fun viewModelObserve() {
        val currentUser = firebaseAuth.currentUser
        productDetailViewModel.productMLD.observe(viewLifecycleOwner, Observer { product ->
            product?.let {
                productArrayList.addAll(it)
                dataBinding.product = it.get(0)
                productDetailViewModel.controlBasketData(requireContext(), bundle.id)
                productDetailViewModel.controlFavoritesData(requireContext(), bundle.id)
                addDataFavorites()
                addDataBasket()
            }
        })

        productDetailViewModel.loadMLD.observe(viewLifecycleOwner, Observer { product ->
            product?.let {
                if (it) {
                    dataBinding.fragmentProductDetailPgb.visibility = View.GONE
                    dataBinding.fragmentProductDetailCardView.visibility = View.VISIBLE
                    dataBinding.fragmentProductDetailCl.visibility = View.VISIBLE
                }
            }
        })

        productDetailViewModel.controlFavorites.observe(viewLifecycleOwner, Observer { control ->
            if (currentUser != null) {
                dataBinding.fragmentProductDetailTgbAddFavorites.isChecked = !control
            } else {
                dataBinding.fragmentProductDetailTgbAddFavorites.setBackgroundResource(R.drawable.asset_favorites_white)
                /*
                dataBinding.fragmentProductDetailTgbAddFavorites.setOnClickListener {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent)
                }

                 */
            }

        })

        productDetailViewModel.controlBasket.observe(viewLifecycleOwner, Observer { control ->
            dataBinding.fragmentProductDetailTgbAddBasket.isChecked = !control
        })
    }
    private fun addDataFavorites() {
        dataBinding.fragmentProductDetailTgbAddFavorites.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                productDetailViewModel.addFavoritesData(requireContext())
            } else {
                productDetailViewModel.deleteFavorites(requireContext(), bundle.id)
            }
        }

    }

    private fun addDataBasket() {
        dataBinding.fragmentProductDetailTgbAddBasket.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                productDetailViewModel.addBasketData(requireContext())
            } else {
                productDetailViewModel.deleteBasket(requireContext(), bundle.id)
            }
        }
    }
}
