package com.gmartin.mlevaluation.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmartin.mlevaluation.R
import com.gmartin.mlevaluation.databinding.FragmentProductListBinding
import com.gmartin.mlevaluation.model.Product
import com.gmartin.mlevaluation.ui.adapter.ProductsListAdapter
import com.gmartin.mlevaluation.ui.viewmodel.ProductsViewModel
import com.gmartin.mlevaluation.ui.viewmodel.ProductsViewModelFactory
import com.gmartin.mlevaluation.utils.ErrorType
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
    private lateinit var mProductsViewModel: ProductsViewModel
    private lateinit var mProductsListAdapter: ProductsListAdapter
    private lateinit var mContext: Context

    /**
     * @see [Fragment.onCreate].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.let { activity ->
            mContext = activity.applicationContext
            mProductsViewModel = ViewModelProvider(
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
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        mBinding = FragmentProductListBinding.inflate(layoutInflater)
        val view = mBinding.root

        initViews()

        return view
    }

    /**
     * Init observers for [ProductsViewModel] live data.
     */
    private fun initObservers() {
        mProductsViewModel.statusDataProductsResult.observe(this) {
            it?.let { status ->
                when (status) {
                    is StatusData.Loading -> {
                        mBinding.loader.visibility = View.VISIBLE
                    }
                    is StatusData.Success -> {
                        status.data?.let { productList -> handleSuccessResult(productList) }
                    }
                    is StatusData.Error -> {
                        handleErrorResult(status.errorType, status.message)
                    }
                }
            }
        }
    }

    /**
     * Initialize component views.
     */
    private fun initViews() {
        mBinding.productList.layoutManager = LinearLayoutManager(context)

        mBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                activity?.let {
                    mProductsViewModel.onSearchProducts(query)
                }
                return false
            }
        })
    }

    /**
     * Init the recycler view adapter with the products list retrieved from the view model.
     *
     * @param productList A [List] of [Product] elements.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun initProductsAdapter(productList: List<Product>) {
        mProductsListAdapter = ProductsListAdapter(
            productList
        ) { productSelected ->
            mProductsViewModel.onProductItemSelected(productList[productSelected].id)
            findNavController().navigate(R.id.action_productsListFragment_to_productFragment)
        }

        mBinding.productList.adapter = mProductsListAdapter
        mProductsListAdapter.notifyDataSetChanged()
    }

    /**
     * Sets the data retrieved from the view model to the correspondent views in the fragment
     * and shown the product container and hides the progress and error layout panel if these
     * are visible.
     *
     * @param productList A [List] of [Product] elements.
     */
    private fun handleSuccessResult(productList: List<Product>) {
        initProductsAdapter(productList)
        mBinding.loader.visibility = View.INVISIBLE
        mBinding.errorPanelContainer.root.visibility = View.GONE
        mBinding.productListContainer.visibility = View.VISIBLE
    }

    /**
     * Handles the error panel view with the correspondent message depending on the error type and
     * hides the progress and product layout container if these are visible.
     */
    private fun handleErrorResult(errorType: ErrorType?, message: String?) {
        when (errorType) {
            ErrorType.ERROR -> {
                mBinding.errorPanelContainer.errorImage.setImageResource(R.drawable.ic_search)
                mBinding.errorPanelContainer.errorTitle.text = mContext.getText(
                    R.string.error_product_list_empty_title
                )
                mBinding.errorPanelContainer.errorSubtitle.text = mContext.getText(
                    R.string.error_product_list_empty_subtitle
                )
            }
            ErrorType.API_EXCEPTION -> {
                mBinding.errorPanelContainer.errorImage.setImageResource(R.drawable.ic_error_cloud)
                mBinding.errorPanelContainer.errorTitle.text = message
                mBinding.errorPanelContainer.errorSubtitle.text = ""
            }
            ErrorType.NETWORK_EXCEPTION -> {
                mBinding.errorPanelContainer.errorImage.setImageResource(R.drawable.ic_satellite)
                mBinding.errorPanelContainer.errorTitle.text = mContext.getText(
                    R.string.error_network_title
                )
                mBinding.errorPanelContainer.errorSubtitle.text = mContext.getText(
                    R.string.error_network_subtitle
                )
            }
            ErrorType.GENERIC_EXCEPTION -> {
            }
        }

        mBinding.loader.visibility = View.INVISIBLE
        mBinding.productListContainer.visibility = View.GONE
        mBinding.errorPanelContainer.root.visibility = View.VISIBLE
    }
}
