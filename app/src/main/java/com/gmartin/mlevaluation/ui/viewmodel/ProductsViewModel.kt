package com.gmartin.mlevaluation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.gmartin.mlevaluation.data.repository.ProductRepository
import com.gmartin.mlevaluation.utils.StatusData
import kotlinx.coroutines.Dispatchers
import org.koin.core.component.KoinComponent

/**
 * A product view model class with the responsibility to provide business models to the view and
 * abstract business logic for user interaction.
 *
 * @author Guillermo O. Martín
 */
class ProductsViewModel(private val mProductRepository: ProductRepository) : ViewModel() {

    fun onSearchProductsButtonClick(searchPattern: String) = liveData(Dispatchers.IO) {
        emit(StatusData.loading(data = null))
        try {
            emit(StatusData.success(data = mProductRepository.getProducts(searchPattern)))
        } catch (exception: Exception) {
            emit(StatusData.error(data = null, message = exception.message ?: "Error Occurred!"))
        }
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
