package com.testingkotlin.catganisation.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.testingkotlin.catganisation.R
import com.testingkotlin.catganisation.model.Cat
import com.testingkotlin.catganisation.viewModel.CatsListViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), CatListAdapter.OnItemClickListener {

    private val catFragmentClass: String? = "catDetailFragment"
    lateinit var listViewModel: CatsListViewModel
    private val recyclerAdapter = CatListAdapter(this)
    lateinit var spinner: Spinner
    var currentFilterCode = "No filter"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        spinner = recycler_filter
        listViewModel = ViewModelProviders.of(this).get(CatsListViewModel::class.java)
        initialiseRecyclerView()
        initialiseSpinner()
        observeViewModel()

    }

    private fun initialiseRecyclerView() {
        cat_recycler_list.apply {
            cat_recycler_list.adapter = recyclerAdapter
            cat_recycler_list.layoutManager = LinearLayoutManager(context)
        }
    }

    private fun initialiseSpinner() {
        val countryCode = resources.getStringArray(R.array.Countries)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, countryCode)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                currentFilterCode = countryCode[position]
                listViewModel.refreshList()
            }

        }
    }

    private fun observeViewModel() {
        listViewModel.cats.observe(this, Observer { cats ->
            cats?.filter {
                if (currentFilterCode.equals("All")) true else it.countryCode.equals(
                    currentFilterCode,
                    true
                )
            }.let {
                it?.let { list ->
                    recyclerAdapter.updateCatList(list)
                    loading_view.visibility = View.GONE
                    list_error.visibility = View.GONE
                }
            }

        })

        listViewModel.catsLoadingError.observe(this, Observer { error ->
            error?.let {
                if (it) cat_recycler_list.visibility = View.GONE else cat_recycler_list.visibility =
                    View.VISIBLE
            }
        })

        listViewModel.catsLoading.observe(this, Observer { loading ->
            loading?.let {
                if (it) cat_recycler_list.visibility = View.GONE else cat_recycler_list.visibility =
                    View.VISIBLE
            }
        })
    }

    override fun onItemClicked(cat: Cat) {
        if (supportFragmentManager.findFragmentByTag(catFragmentClass) == null) {
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.add(
                R.id.root_layout,
                CatDetailFragment.newInstance(cat),
                catFragmentClass
            )
                .addToBackStack(null)
                .commit()
        }
    }
}
