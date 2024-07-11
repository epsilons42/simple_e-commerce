package com.example.alsess.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.alsess.R
import com.example.alsess.databinding.ProfileButtonRowBinding
import com.example.alsess.model.ProfileRvModel
import com.example.alsess.util.AppUtils

class ProfileChildRvAdapter(val context: Context, val profileList: List<ProfileRvModel>) :
    RecyclerView.Adapter<ProfileChildRvAdapter.ProfileChildVH>() {
    class ProfileChildVH(val dataBinding: ProfileButtonRowBinding) :
        RecyclerView.ViewHolder(dataBinding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileChildVH {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ProfileButtonRowBinding>(
            inflater,
            R.layout.profile_button_row, parent,
            false
        )
        return ProfileChildVH(view)
    }

    override fun getItemCount(): Int {
        return profileList.size
    }

    override fun onBindViewHolder(holder: ProfileChildVH, position: Int) {
        holder.dataBinding.button = profileList.get(position)
        holder.dataBinding.profileButtonRowBtn.setOnClickListener {
            if (profileList.get(position).id ==  R.id.profileButtonRowBtn ) {
                val appUtils = AppUtils()
                appUtils.logOut(context)
            } else {
                Navigation.findNavController(it).navigate(profileList.get(position).id)
            }
        }
    }
}