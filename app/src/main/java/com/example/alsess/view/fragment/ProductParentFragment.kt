package com.example.alsess.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.example.alsess.R
import com.example.alsess.databinding.FragmentProductParentBinding
import com.example.alsess.service.BasketSQLiteDao
import com.example.alsess.service.BasketSQLiteDataHelper
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductParentFragment : Fragment() {
    private lateinit var viewBinding: FragmentProductParentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        viewBinding = FragmentProductParentBinding.inflate(inflater, container, false)

        val activityMainBottomNavigation =
            activity!!.findViewById(R.id.activityMainBottomNavigationView) as BottomNavigationView

        val navHostFragment =
            childFragmentManager.findFragmentById(R.id.FragmentProductParentNavHostFragment) as NavHostFragment

        /*Since separate navhostfragment is used for products,
         remove the bottom navigation bar like this when you go into detail.
         */
        navHostFragment.navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            basketBedge()
            if (nd.id == R.id.productDetailFragment) {
                activityMainBottomNavigation.visibility = View.GONE
            } else {
                activityMainBottomNavigation.visibility = View.VISIBLE
            }
        }

        return viewBinding.root
    }

    //The number of products added to the cart is displayed as a bottom notification.
    fun basketBedge() {
        val activityMainBottomNavigation =
            activity!!.findViewById(R.id.activityMainBottomNavigationView) as BottomNavigationView
        val basketSQLiteDataHelper = BasketSQLiteDataHelper(context!!)
        val basketSqliteList = BasketSQLiteDao().getAllBaskets(basketSQLiteDataHelper)
        val basketBadge =
            activityMainBottomNavigation.getOrCreateBadge(R.id.basketFragment)
        basketBadge.number = basketSqliteList.size
        basketBadge.isVisible = true
        if (basketSqliteList.size == 0) {
            basketBadge.isVisible = false
        }
    }
}



