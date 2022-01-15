package com.gmartin.mlevaluation.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gmartin.mlevaluation.data.repository.ProductRepository
import com.gmartin.mlevaluation.model.ApiException
import com.gmartin.mlevaluation.model.NetworkException
import com.gmartin.mlevaluation.model.Product
import com.gmartin.mlevaluation.utils.StatusData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

/**
 * A product view model class with the responsibility to provide business models to the view and
 * abstract business logic for user interaction.
 *
 * @author Guillermo O. Martín
 */
class ProductsViewModel(private val mProductRepository: ProductRepository) : ViewModel() {
    private var mProductsList = ArrayList<Product>()

    // LiveData attributes
    private var _statusDataProductsResult = MutableLiveData<StatusData<List<Product>>>()
    val statusDataProductsResult: LiveData<StatusData<List<Product>>>
        get() = _statusDataProductsResult

    private var _statusDataItemResult = MutableLiveData<StatusData<Product>>()
    val statusDataItemResult: LiveData<StatusData<Product>>
        get() = _statusDataItemResult

    /**
     * Method called when is performed the search action on the UI.
     *
     * @param searchPattern A [String] pattern to be requested.
     */
    fun onSearchProducts(searchPattern: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _statusDataProductsResult.value = StatusData.Loading()

            try {
                mProductsList = withContext(Dispatchers.IO) {
                    mProductRepository.getProducts(searchPattern)
                } as ArrayList<Product>

                setStatusDataSuccess()
            } catch (apiException: ApiException) {
                Log.d("Guille", "${apiException.code} - ${apiException.message}")
            } catch (networkException: NetworkException) {
                Log.d("Guille", "${networkException.message}")
            } catch (exception: Exception) {
                Log.d("Guille", exception.message)
                _statusDataProductsResult.value = StatusData.Error(
                    message = exception.message ?: "Error Occurred!"
                )
            }
        }
    }

    /**
     * TODO
     */
    fun onProductItemSelected(productId: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _statusDataProductsResult.value = StatusData.Loading()

            try {
                val product = withContext(Dispatchers.IO) {
                    mProductRepository.getProduct(productId)
                }
                _statusDataItemResult.value = StatusData.Success(data = product)
                Log.d("GUILLE", "product: ${product.title}")
            } catch (apiException: ApiException) {
                Log.d("Guille", "${apiException.code} - ${apiException.message}")
            } catch (networkException: NetworkException) {
                Log.d("Guille", "${networkException.message}")
            } catch (exception: Exception) {
                Log.d("Guille", exception.message)
                _statusDataProductsResult.value = StatusData.Error(
                    message = exception.message ?: "Error Occurred!"
                )
            }
        }
    }

    /**
     * TODO
     */
    fun onFragmentProductListStart() {
        setStatusDataSuccess()
    }

    /**
     * TODO
     */
    private fun setStatusDataSuccess() {
        _statusDataProductsResult.value = StatusData.Success(data = mProductsList)
    }
}

/**
 * Factory class for [ProductsViewModel].
 *
 * @param mProductsRepository A [ProductRepository] instance.
 */
class ProductsViewModelFactory(private val mProductsRepository: ProductRepository) :
    ViewModelProvider.Factory,
    KoinComponent {

    /**
     * @see [ViewModelProvider.Factory.create]
     */
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ProductRepository::class.java)
            .newInstance(mProductsRepository)
    }
}
