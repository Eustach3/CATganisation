package com.testingkotlin.catganisation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.testingkotlin.catganisation.R
import com.testingkotlin.catganisation.di.DaggerApiComponent
import com.testingkotlin.catganisation.model.Cat
import com.testingkotlin.catganisation.model.CatsApi
import com.testingkotlin.catganisation.util.getProgressDrawable
import com.testingkotlin.catganisation.util.loadImage
import kotlinx.android.synthetic.main.cat_item.view.*

class CatListAdapter() : RecyclerView.Adapter<CatListAdapter.CatListHolder>() {
    lateinit var api: CatsApi
    var catList: ArrayList<Cat> = arrayListOf()

    init {
        DaggerApiComponent.create().inject(this)
    }

    fun updateCatList (cats : List<Cat>) {
        catList.clear()
        catList.addAll(cats)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatListHolder {
        return CatListHolder(LayoutInflater.from(parent.context).inflate(R.layout.cat_item, parent, false), api)
    }

    override fun getItemCount(): Int {
        return catList.size
    }

    override fun onBindViewHolder(holder: CatListHolder, position: Int) {
        holder.bind(catList[position])
    }
    class CatListHolder(view: View, val api: CatsApi) : RecyclerView.ViewHolder(view){
        private val image = view.imageView
        private val name = view.name
        private val description = view.description
        private val progressDrawable = getProgressDrawable(view.context)

        fun bind(cat: Cat) {
            image.loadImage(api.getSpecificBreedImage(cat.id),progressDrawable)
            name.text = cat.name
            description.text = cat.description
        }

    }
}