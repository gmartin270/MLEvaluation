package com.gmartin.mlevaluation.data.api

import com.gmartin.mlevaluation.model.ApiError
import com.gmartin.mlevaluation.model.Picture
import com.gmartin.mlevaluation.model.Product
import com.gmartin.mlevaluation.model.ProductResponseData
import com.gmartin.mlevaluation.model.exception.ApiException
import io.mockk.coEvery
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.ResponseBody
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class ProductApiAdapterTest : TestCase() {

    @MockK
    private lateinit var mProductApiClientMock: ProductApiClient

    @MockK
    private lateinit var mResponseBodyMock: ResponseBody

    @InjectMockKs
    private lateinit var mProductApiAdapter: ProductApiAdapter

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

    private val mProductsListResponseTest = ProductResponseData(
        "1",
        mExpectedProductsListResult
    )

    private val mApiErrorTest = ApiError(
        "api error",
        "error",
        404
    )

    @Before
    public override fun setUp() {
        super.setUp()

        mProductApiClientMock = mockk()
        mResponseBodyMock = mockk()

        mProductApiAdapter = ProductApiAdapter(mProductApiClientMock)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test get products when response is successful`() {
        runBlockingTest {
            // Arrange
            coEvery {
                mProductApiClientMock.provideProductApi().getProducts("test")
                    .isSuccessful
            } returns true

            coEvery {
                mProductApiClientMock.provideProductApi().getProducts("test").body()!!
            } returns mProductsListResponseTest

            // Act
            val result = mProductApiAdapter.getProductsList("test")

            // Assert
            assertEquals(mExpectedProductsListResult, result)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test get products when response is not successful`() {
        runBlockingTest {
            // Arrange
            coEvery {
                mProductApiClientMock.provideProductApi().getProducts("test")
                    .isSuccessful
            } returns false

            coEvery {
                mProductApiClientMock.provideProductApi().getProducts("test")
                    .errorBody()
            } returns mResponseBodyMock

            coEvery {
                mProductApiClientMock.parseApiError(mResponseBodyMock)
            } returns mApiErrorTest

            // Act
            val exceptionThrown = try {
                mProductApiAdapter.getProductsList("test")
                false
            } catch (apiException: ApiException) {
                true
            }

            // Assert
            assertTrue(exceptionThrown)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test get product when response is successful`() {
        runBlockingTest {
            // Arrange
            coEvery {
                mProductApiClientMock.provideProductApi().getProduct("test").isSuccessful
            } returns true

            coEvery {
                mProductApiClientMock.provideProductApi().getProduct("test").body()!!
            } returns mExpectedProductResult

            // Act
            val result = mProductApiAdapter.getProduct("test")

            // Assert
            assertEquals(mExpectedProductResult, result)
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `test get product when response is not successful`() {
        runBlockingTest {
            // Arrange
            coEvery {
                mProductApiClientMock.provideProductApi().getProduct("test").isSuccessful
            } returns false

            coEvery {
                mProductApiClientMock.provideProductApi().getProduct("test").errorBody()
            } returns mResponseBodyMock

            coEvery {
                mProductApiClientMock.parseApiError(mResponseBodyMock)
            } returns mApiErrorTest

            // Act
            val exceptionThrown = try {
                mProductApiAdapter.getProduct("test")
                false
            } catch (apiException: ApiException) {
                true
            }

            // Assert
            assertTrue(exceptionThrown)
        }
    }
}
