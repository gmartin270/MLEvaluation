package com.gmartin.mlevaluation.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.gmartin.mlevaluation.data.repository.ProductRepository
import com.gmartin.mlevaluation.model.Product
import com.gmartin.mlevaluation.utils.ErrorType
import com.gmartin.mlevaluation.utils.ExceptionHelper
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
     * Method called when the search action is performed on the UI.
     *
     * @param searchPattern A [String] pattern to be requested.
     */
    fun onSearchProducts(searchPattern: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _statusDataProductsResult.value = StatusData.Loading()

            try {
                mProductsList = withContext(Dispatchers.IO) {
                    mProductRepository.getProductsList(searchPattern)
                } as ArrayList<Product>

                if (mProductsList.isNotEmpty()) {
                    setStatusDataSuccess()
                } else {
                    _statusDataProductsResult.value = StatusData.Error(
                        errorType = ErrorType.ERROR
                    )
                }
            } catch (exception: Exception) {
                _statusDataProductsResult.value = StatusData.Error(
                    errorType = ExceptionHelper.resolveError(exception),
                    message = exception.message ?: "Error Occurred!"
                )
            }
        }
    }

    /**
     * Method called when an element is selected in the UI.
     *
     * @param productId The product Id.
     */
    fun onProductItemSelected(productId: String) {
        viewModelScope.launch(Dispatchers.Main) {
            _statusDataProductsResult.value = StatusData.Loading()

            try {
                val product = withContext(Dispatchers.IO) {
                    mProductRepository.getProduct(productId)
                }
                _statusDataItemResult.value = StatusData.Success(data = product)
            } catch (exception: Exception) {
                _statusDataItemResult.value = StatusData.Error(
                    errorType = ExceptionHelper.resolveError(exception),
                    message = exception.message ?: "Error Occurred!"
                )
            }
        }
    }

    /**
     * Method called on back event on the UI.
     */
    fun onBack() {
        // We refresh the list of products on the UI.
        setStatusDataSuccess()
    }

    /**
     * Set the live data value for the success result.
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
