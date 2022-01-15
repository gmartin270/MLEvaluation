package com.gmartin.mlevaluation.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmartin.mlevaluation.R
import com.gmartin.mlevaluation.databinding.FragmentProductListBinding
import com.gmartin.mlevaluation.model.Product
import com.gmartin.mlevaluation.ui.adapter.ProductsListAdapter
import com.gmartin.mlevaluation.ui.viewmodel.ProductsViewModel
import com.gmartin.mlevaluation.ui.viewmodel.ProductsViewModelFactory
import com.gmartin.mlevaluation.utils.StatusData
import org.koin.android.ext.android.inject

/**
 * TODO
 *
 * @author Guillermo O. Martín
 */
class ProductsListFragment : Fragment() {
    private val mProductsViewModelFactory by inject<ProductsViewModelFactory>()
    private lateinit var mBinding: FragmentProductListBinding
    private lateinit var mViewModel: ProductsViewModel
    private lateinit var mProductsListAdapter: ProductsListAdapter

    /**
     * @see [Fragment.onCreate].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let { activity ->
            mViewModel = ViewModelProvider(
                activity,
                mProductsViewModelFactory
            ).get(ProductsViewModel::class.java)
            initObservers()
        }
    }

    /**
     * @see [Fragment.onCreateView]
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentProductListBinding.inflate(layoutInflater)
        val view = mBinding.root

        initViews()
        mViewModel.onFragmentProductListStart()
        return view
    }

    /**
     * Init observers for [ProductsViewModel] live data.
     */
    private fun initObservers() {
        mViewModel.statusDataProductsResult.observe(this) {
            it?.let { status ->
                when (status) {
                    is StatusData.Loading -> {
                        mBinding.loader.visibility = View.VISIBLE
                    }
                    is StatusData.Success -> {
                        status.data?.let { productList ->
                            initProductsAdapter(productList)
                        }
                        mBinding.loader.visibility = View.INVISIBLE
                    }
                    is StatusData.Error -> {
                        mBinding.loader.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }

    /**
     * Initialize components views.
     */
    private fun initViews() {
        mBinding.productList.layoutManager = LinearLayoutManager(context)

        mBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                activity?.let {
                    mViewModel.onSearchProducts(query)
                }
                return false
            }
        })
    }

    /**
     * TODO
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun initProductsAdapter(productList: List<Product>) {
        mProductsListAdapter = ProductsListAdapter(
            productList
        ) { productSelected ->
            Log.d("GUILLE", "Product selected: ${productList[productSelected].title}")
            mViewModel.onProductItemSelected(productList[productSelected].id)
            findNavController().navigate(R.id.action_productsListFragment_to_productFragment)
        }

        mBinding.productList.adapter = mProductsListAdapter
        mProductsListAdapter.notifyDataSetChanged()
    }
}