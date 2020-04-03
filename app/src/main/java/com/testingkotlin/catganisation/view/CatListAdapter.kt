package com.testingkotlin.catganisation.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.recyclerview.widget.RecyclerView
import com.testingkotlin.catganisation.R
import com.testingkotlin.catganisation.di.DaggerApiComponent
import com.testingkotlin.catganisation.model.Breed
import com.testingkotlin.catganisation.model.Cat
import com.testingkotlin.catganisation.model.CatsApi
import com.testingkotlin.catganisation.util.getProgressDrawable
import com.testingkotlin.catganisation.util.loadImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.cat_item.view.*
import javax.inject.Inject

class CatListAdapter(val itemClickListener : OnItemClickListener) : RecyclerView.Adapter<CatListAdapter.CatListHolder>() {
    var catList: ArrayList<Cat> = arrayListOf()
    private val compDisposable = CompositeDisposable()

    fun updateCatList(cats: List<Cat>) {
        catList.clear()
        catList.addAll(cats)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatListHolder {
        return CatListHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.cat_item,
                parent,
                false
            )
        , compDisposable)

    }


    override fun getItemCount(): Int {
        return catList.size
    }

    override fun onBindViewHolder(holder: CatListHolder, position: Int) {
        holder.bind(catList[position], itemClickListener)
    }

    class CatListHolder(view: View, val compDisposable: CompositeDisposable) : RecyclerView.ViewHolder(view) {
        val image = view.imageView
        private val name = view.name
        private val description = view.description
        private val progressDrawable = getProgressDrawable(view.context)
        @Inject
        lateinit var api: CatsApi



        init {
            DaggerApiComponent.create().inject(this)
        }


        fun bind(
            cat: Cat,
            itemClickListener: OnItemClickListener
        ) {
            compDisposable.add(api.getSpecificBreedImage()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<List<Breed>>() {
                override fun onSuccess(list: List<Breed>) {
                    image.loadImage(list[0].url, progressDrawable)

                }

                override fun onError(e: Throwable) {

                }
            }))
            name.text = cat.name
            description.text = cat.description
            itemView.setOnClickListener {
                itemClickListener.onItemClicked(cat)
            }
        }
    }
    interface OnItemClickListener{
        fun onItemClicked(cat: Cat)
    }
}