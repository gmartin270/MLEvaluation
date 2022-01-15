package com.gmartin.mlevaluation.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.gmartin.mlevaluation.utils.StatusData
import org.koin.android.ext.android.inject
import java.text.DecimalFormat

/**
 * TODO
 */
class ProductFragment : Fragment() {
    private val mProductsViewModelFactory by inject<ProductsViewModelFactory>()
    private lateinit var mViewModel: ProductsViewModel
    private lateinit var mBinding: FragmentProductBinding
    private lateinit var mProductPicturesListAdapter: ProductPicturesListAdapter

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
        mBinding = FragmentProductBinding.inflate(layoutInflater)
        val view = mBinding.root

        mBinding.appToolbarView.toolbarCloseIcon.setOnClickListener {
            findNavController().navigate(R.id.action_productFragment_to_productsListFragment)
        }

        mBinding.picturesList.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        return view
    }


    /**
     * Init observers for [ProductsViewModel] live data.
     */
    private fun initObservers(){
        mViewModel.statusDataItemResult.observe(this) {
            it?.let { status ->
                when (status) {
                    is StatusData.Loading -> {
                        mBinding.loader.visibility = View.VISIBLE
                    }
                    is StatusData.Success -> {
                        status.data?.let { item ->
                            setViews(item)
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
     * TODO
     */
    private fun setViews(item: Product) {
        mBinding.productTitle.text = item.title
        mBinding.productPrice.text = DecimalFormat("$ #.##")
            .format(item.price)

        mProductPicturesListAdapter = ProductPicturesListAdapter(
            item.pictureList
        )
        mBinding.picturesList.adapter = mProductPicturesListAdapter
        LinearSnapHelper().attachToRecyclerView(mBinding.picturesList)
    }
}
