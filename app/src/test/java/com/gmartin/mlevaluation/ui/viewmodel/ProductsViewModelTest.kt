package com.gmartin.mlevaluation.ui.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gmartin.mlevaluation.data.repository.ProductRepository
import com.gmartin.mlevaluation.model.Picture
import com.gmartin.mlevaluation.model.Product
import com.gmartin.mlevaluation.utils.MainCoroutineRule
import com.gmartin.mlevaluation.utils.StatusData
import com.gmartin.mlevaluation.utils.getOrAwaitValue
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.just
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductsViewModelTest : TestCase() {

    @get:Rule
    val mInstantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mMainCoroutineRule = MainCoroutineRule()

    @MockK
    private lateinit var mProductRepositoryMock: ProductRepository

    private lateinit var mProductsViewModel: ProductsViewModel

    private val mExpectedProductResult = Product(
        "1",
        "Product XYZ",
        "http://http2.mlstatic.com/D_811351-MLA48463373361_122021-I.jpg",
        100.0,
        1,
        ArrayList<Picture>()
    )

    private val mExpectedProductsListResult = arrayListOf(
        mExpectedProductResult
    )

    private val mExpectedStatusDataProductsResult = StatusData.Success(mExpectedProductsListResult)
    private val mExpectedStatusDataItemsResult = StatusData.Success(mExpectedProductResult)

    @Before
    public override fun setUp() {
        super.setUp()

        mProductRepositoryMock = mockk()
        mProductsViewModel = ProductsViewModel(mProductRepositoryMock)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test on search Products event`() = mMainCoroutineRule.dispatcher.runBlockingTest {
        // Arrange
        val searchPattern = "test"
        val observer = mockk<Observer<StatusData<List<Product>>>> {
            every { onChanged(any()) } just Runs
        }

        mProductsViewModel.statusDataProductsResult.observeForever(observer)

        coEvery {
            mProductRepositoryMock.getProductsList(searchPattern)
        } returns mExpectedProductsListResult

        // Act
        mProductsViewModel.onSearchProducts(searchPattern)

        val result = mProductsViewModel.statusDataProductsResult.getOrAwaitValue()

        // Assert
        assertEquals(mExpectedStatusDataProductsResult.data, result.data)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test Product element is selected event`() = mMainCoroutineRule.dispatcher.runBlockingTest {
        // Arrange
        val productId = "test"
        val observer = mockk<Observer<StatusData<Product>>> {
            every { onChanged(any()) } just Runs
        }

        mProductsViewModel.statusDataItemResult.observeForever(observer)

        coEvery {
            mProductRepositoryMock.getProduct(productId)
        } returns mExpectedProductResult

        // Act
        mProductsViewModel.onProductItemSelected(productId)

        val result = mProductsViewModel.statusDataItemResult.getOrAwaitValue()

        // Assert
        assertEquals(mExpectedStatusDataItemsResult.data, result.data)
    }
}
