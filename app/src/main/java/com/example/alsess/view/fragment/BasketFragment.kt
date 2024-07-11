package com.example.alsess.view.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.alsess.adapters.OnChangeAmount
import com.example.alsess.adapters.BasketAdapter
import com.example.alsess.databinding.FragmentBasketBinding

class BasketFragment : Fragment() {
    private lateinit var viewBinding: FragmentBasketBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBinding = FragmentBasketBinding.inflate(inflater, container, false)

        val sharedPreferences = context?.getSharedPreferences("totalPrice", Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()

        //It is used to get price totals instantly and give the changes instantly
        val onChangeAmount = object : OnChangeAmount {
            override fun onChange(totalPrice: String) {
                viewBinding.fragmentBasketTxvTotalPrice.text = totalPrice
                editor?.putFloat("total", totalPrice.toFloat())
                editor?.apply()
                if (totalPrice.toFloat() == 0F) {
                    viewBinding.fragmentBasketCardViewPrice.visibility = View.GONE

                } else {
                    viewBinding.fragmentBasketCardViewPrice.visibility = View.VISIBLE
                }
            }
        }


        if (sharedPreferences!!.getFloat(
                "total",
                0.0F
            ) == 0.0F
        ) {
            viewBinding.fragmentBasketCardViewPrice.visibility = View.GONE
        }
        viewBinding.fragmentBasketTxvTotalPrice.text =
            sharedPreferences.getFloat("total", 0F).toString()

        viewBinding.fragmentBasketRecyclerView.adapter =
            context?.let { BasketAdapter(it, onChangeAmount) }
        viewBinding.fragmentBasketRecyclerView.layoutManager = LinearLayoutManager(context)
        return viewBinding.root

    }

}
