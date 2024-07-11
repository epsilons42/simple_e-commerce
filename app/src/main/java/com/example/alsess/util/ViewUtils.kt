package com.example.alsess.util

import android.graphics.drawable.Drawable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.navigation.Navigation
import com.example.alsess.R


fun ImageView.Glide(url : String){
    com.bumptech.glide.Glide.with(context).load(url).into(this)
}
@BindingAdapter("android:downloadUrl")
fun downloadImage(view: ImageView, url:String?) {
    url?.let { view.Glide(it) }
}

fun Toolbar.backIcon(){
    setNavigationIcon(R.drawable.asset_back_button)
}
fun Toolbar.navigationBackClick(){
    setNavigationOnClickListener {
        Navigation.findNavController(it).popBackStack()
    }
}

@BindingAdapter("android:drawableLeft")
fun Button.setDrawableLeft(resourceId: Int) {
    val drawable = ContextCompat.getDrawable(context, resourceId)
    setIntrinsicBounds(drawable)
    val drawables = compoundDrawables
    setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3])
}

private fun setIntrinsicBounds(drawable: Drawable?) {
    drawable?.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
}
fun EditText.emptyControlAndAddText(str : String?){
    if (str != "") {
        setText(str)
        setBackgroundResource(
            R.drawable.edittext_shape
        )
    } else {
        setBackgroundResource(
            R.drawable.edittext_empty_shape
        )
    }
}