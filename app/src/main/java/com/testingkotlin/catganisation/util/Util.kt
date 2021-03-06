package com.testingkotlin.catganisation.util

import android.content.Context
import android.widget.ImageView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.testingkotlin.catganisation.R
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import java.lang.Exception


fun getProgressDrawable(context: Context) : CircularProgressDrawable {
    return CircularProgressDrawable(context).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

fun getShorterDescription(description: String) : String {
    var shorterSrring = description
    if (description.length > 140) {
        shorterSrring = description.substring(0,140) + "..."
    }
    return shorterSrring
}

fun ImageView.loadImage(uri : String?, progressDrawable: CircularProgressDrawable ) {

    Picasso.get()
        .load(uri)
        .resize(650, 600)
        .transform(CropCircleTransformation())
        .placeholder(progressDrawable)
        .error(R.drawable.ic_error_outline)
        .into(this, object : Callback {
            override fun onSuccess() {
            }

            override fun onError(e: Exception?) {
            }
        })
}