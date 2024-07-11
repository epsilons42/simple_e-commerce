package com.example.alsess.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.alsess.R
import com.example.alsess.databinding.FragmentBasketRowBinding
import com.example.alsess.model.BasketSQLiteModel
import com.example.alsess.service.BasketSQLiteDao
import com.example.alsess.service.BasketSQLiteDataHelper
import com.example.alsess.view.fragment.BasketFragmentDirections

class BasketAdapter(val context: Context, val onChangeAmount: OnChangeAmount) :
    RecyclerView.Adapter<BasketAdapter.BasketVH>() {
    class BasketVH(val dataBinding: FragmentBasketRowBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<FragmentBasketRowBinding>(
            inflater,
            R.layout.fragment_basket_row,
            parent,
            false
        )
        return BasketVH(view)
    }

    override fun getItemCount(): Int {
        val basketDataHelper = BasketSQLiteDataHelper(context)
        val titlelist = BasketSQLiteDao().getAllBaskets(basketDataHelper)
        return titlelist.size
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: BasketVH, position: Int) {
        val basketDataHelper = BasketSQLiteDataHelper(context)
        val basketList = BasketSQLiteDao().getAllBaskets(basketDataHelper)
        basketList.reverse()
        val basket: BasketSQLiteModel = basketList.get(position)
        holder.dataBinding.product = basket

        //An id is sent to the detail page and the product clicked in the detail is displayed thanks to this id
        holder.dataBinding.recyclerRowBasketCardView.setOnClickListener {
            val navArgs =
                BasketFragmentDirections.actionBasketFragmentToProductDetailFragment(basket.id)
            Navigation.findNavController(it).navigate(navArgs)
        }

        var count = basket.count
        total()
        //Used to increase the items in the basket and find the total price
        holder.dataBinding.recyclerRowBasketBtnIncrease.setOnClickListener {
            if (count < 20) {
                count++
                BasketSQLiteDao().updateBasket(
                    basketDataHelper,
                    basket.id,
                    basket.title,
                    basket.price,
                    basket.imageUrl,
                    count
                )

                total()
                holder.dataBinding.recyclerRowBasketTxvCount.text = count.toString()
                holder.dataBinding.recyclerRowBasketTxvPrice.text =
                    "${basket.price.toFloat() * count} $"
            }
        }

        //Used to reduce the items in the cart and find the total price
        holder.dataBinding.recyclerRowBasketBtnDecrease.setOnClickListener {
            if (count > 1) {
                count--
                BasketSQLiteDao().updateBasket(
                    basketDataHelper,
                    basket.id,
                    basket.title,
                    basket.price,
                    basket.imageUrl,
                    count
                )

                total()
            }

            holder.dataBinding.recyclerRowBasketTxvPrice.text =
                "${basket.price.toFloat() * count} $"
        }

       holder.dataBinding.recyclerRowBasketBtnDelete.setOnClickListener {
           if (basketList.size != 0) {
               onChangeAmount.onChange("0.0")
           }
           BasketSQLiteDao().deleteBasket(basketDataHelper, basket.id)
           total()
           notifyDataSetChanged()
       }
    }

    fun total() {
        val basketDataHelper = BasketSQLiteDataHelper(context)
        val basketList = BasketSQLiteDao().getAllBaskets(basketDataHelper)
        var total = 0.0F
        for (i in 0..(basketList.size - 1)) {
            total += basketList.get(i).price.toFloat() * basketList.get(i).count
            onChangeAmount.onChange(total.toString())
        }
    }
}