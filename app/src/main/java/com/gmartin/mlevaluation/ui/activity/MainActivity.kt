package com.gmartin.mlevaluation.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.gmartin.mlevaluation.databinding.ActivityMainBinding
import com.gmartin.mlevaluation.ui.viewmodel.ProductsViewModel
import com.gmartin.mlevaluation.ui.viewmodel.ProductsViewModelFactory
import org.koin.android.ext.android.inject

/**
 * Main activity class.
 *
 * @author Guillermo O. Martín
 */
class MainActivity : AppCompatActivity() {
    private val mProductsViewModelFactory by inject<ProductsViewModelFactory>()
    private lateinit var mProductsViewModel: ProductsViewModel
    private lateinit var mBinding: ActivityMainBinding

    /**
     * @see [AppCompatActivity.onCreate].
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mProductsViewModel = ViewModelProvider(this, mProductsViewModelFactory).get()
    }

    /**
     * @see [AppCompatActivity.onBackPressed].
     */
    override fun onBackPressed() {
        mProductsViewModel.onBack()
        super.onBackPressed()
    }
}
