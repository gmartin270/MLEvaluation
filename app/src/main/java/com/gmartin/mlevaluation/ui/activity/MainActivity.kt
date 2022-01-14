package com.gmartin.mlevaluation.ui.activity

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.gmartin.mlevaluation.databinding.ActivityMainBinding
import com.gmartin.mlevaluation.ui.viewmodel.ProductsViewModel
import com.gmartin.mlevaluation.ui.viewmodel.ProductsViewModelFactory
import com.gmartin.mlevaluation.utils.Status
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val mProductsViewModelFactory by inject<ProductsViewModelFactory>()
    private lateinit var mProductsViewModel: ProductsViewModel
    private lateinit var mBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mProductsViewModel = ViewModelProvider(this, mProductsViewModelFactory).get()

        mBinding.buttonTest.setOnClickListener {
            mProductsViewModel.onSearchProductsButtonClick("asus")
                .observe(this) {
                    it?.let { statusData ->
                        when (statusData.status) {
                            Status.LOADING -> {
                            }
                            Status.SUCCESS -> {
                                statusData.data?.let { productList ->
                                    Log.d("GUILLE", "Product[0]: ${productList[0].title}")
                                }
                            }
                            Status.ERROR -> {
                            }
                        }
                    }
                }
        }
    }
}
