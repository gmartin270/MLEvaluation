package com.gmartin.mlevaluation.ui.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import com.gmartin.mlevaluation.R
import com.gmartin.mlevaluation.databinding.FragmentProductBinding
import com.gmartin.mlevaluation.model.Product
import com.gmartin.mlevaluation.ui.adapter.ProductPicturesListAdapter
import com.gmartin.mlevaluation.ui.viewmodel.ProductsViewModel
import com.gmartin.mlevaluation.ui.viewmodel.ProductsViewModelFactory
import com.gmartin.mlevaluation.utils.ErrorType
import com.gmartin.mlevaluation.utils.StatusData
import java.text.DecimalFormat
import org.koin.android.ext.android.inject

/**
 * [Fragment] class implementation for a product item.
 */
class ProductFragment : Fragment() {
    private val mProductsViewModelFactory by inject<ProductsViewModelFactory>()
    private lateinit var mContext: Context
    private lateinit var mProductsViewModel: ProductsViewModel
    private lateinit var mBinding: FragmentProductBinding
    private lateinit var mProductPicturesListAdapter: ProductPicturesListAdapter
    private val mSnapHelper = LinearSnapHelper()

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
        mBinding = FragmentProductBinding.inflate(layoutInflater)
        val view = mBinding.root

        initViews()
        return view
    }

    /**
     * Initialize component views.
     */
    private fun initViews() {
        mBinding.appToolbarView.toolbarCloseIcon.setOnClickListener {
            findNavController().navigate(R.id.action_productFragment_to_productsListFragment)
            mProductsViewModel.onBack()
        }

        mBinding.picturesList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
    }

    /**
     * Init observers for [ProductsViewModel] live data.
     */
    private fun initObservers() {
        mProductsViewModel.statusDataItemResult.observe(this) {
            it?.let { status ->
                when (status) {
                    is StatusData.Loading -> {
                        mBinding.loader.visibility = View.VISIBLE
                    }
                    is StatusData.Success -> {
                        status.data?.let { item ->
                            setDataUI(item)
                        }
                    }
                    is StatusData.Error -> {
                        handleErrorResult(status.errorType, status.message)
                    }
                }
            }
        }
    }

    /**
     * Sets the data retrieved from the view model to the correspondent views in the fragment
     * and shown the product container and hides the progress and error layout panel if these
     * are visible.
     *
     * @param item A [Product] instance element.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun setDataUI(item: Product) {
        mBinding.productDetailContainer.visibility = View.VISIBLE
        mBinding.loader.visibility = View.GONE
        mBinding.errorPanelContainer.root.visibility = View.GONE

        mBinding.productTitle.text = item.title
        mBinding.productPrice.text = DecimalFormat("$ #.##")
            .format(item.price)

        mProductPicturesListAdapter = ProductPicturesListAdapter(
            item.pictureList
        )

        mBinding.picturesList.adapter = mProductPicturesListAdapter
        mProductPicturesListAdapter.notifyDataSetChanged()

        mSnapHelper.attachToRecyclerView(mBinding.picturesList)
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

        mBinding.loader.visibility = View.GONE
        mBinding.productDetailContainer.visibility = View.GONE
        mBinding.errorPanelContainer.root.visibility = View.VISIBLE
    }

    /**
     * @see [Fragment.onDestroyView]
     */
    override fun onDestroyView() {
        super.onDestroyView()

        mSnapHelper.attachToRecyclerView(null)
        mBinding.picturesList.recycledViewPool.clear()
    }
}
