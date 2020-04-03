package com.testingkotlin.catganisation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.testingkotlin.catganisation.R
import com.testingkotlin.catganisation.di.DaggerApiComponent
import com.testingkotlin.catganisation.model.Breed
import com.testingkotlin.catganisation.model.Cat
import com.testingkotlin.catganisation.model.CatsApi
import com.testingkotlin.catganisation.util.getProgressDrawable
import com.testingkotlin.catganisation.util.loadImage
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.cat_fragment.*
import javax.inject.Inject

class CatDetailFragment : Fragment() {
    @Inject
    lateinit var api: CatsApi
    lateinit var imageDisposable : DisposableSingleObserver<List<Breed>>
    init {
        DaggerApiComponent.create().inject(this)
    }
    companion object {
        fun newInstance(cat: Cat): CatDetailFragment {
            val currFragment = CatDetailFragment()
            val args = Bundle()
            args.putParcelable("cat", cat)
            currFragment.setArguments(args)
            return currFragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.cat_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cat: Cat? = arguments?.getParcelable("cat")
        val imageView = fragment_img
        imageDisposable = api.getSpecificBreedImage()
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableSingleObserver<List<Breed>>() {
                override fun onSuccess(list: List<Breed>) {
                    imageView.loadImage(list[0].url, getProgressDrawable(requireContext()))
                }
                override fun onError(e: Throwable) {

                }
            })

        fragment_name.text = resources.getString(R.string.breed_name, cat?.name)
        fragment_description.text = cat?.description ?: "Description not found"
        fragment_country_code.text = resources.getString(R.string.country_code, cat?.countryCode)
        fragment_temperament.text = resources.getString(R.string.temperament, cat?.temperament)
        fragment_wikipedia_link.text = resources.getString(R.string.wikipedia_link, cat?.wikipediaLink)
    }

    override fun onDetach() {
        super.onDetach()
        imageDisposable.dispose()
    }
}