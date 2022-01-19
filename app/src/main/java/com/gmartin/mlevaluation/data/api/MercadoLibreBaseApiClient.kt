package com.gmartin.mlevaluation.data.api

import com.gmartin.mlevaluation.application.Constants
import com.gmartin.mlevaluation.model.ApiError
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import org.koin.core.component.KoinComponent
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val HEADER_NAME = "Authorization"
private const val HEADER_VALUE = "Bearer \$ACCESS_TOKEN"

/**
 * This class represents the Retrofit base client for the API endpoints in MercadoLibre.
 */
open class MercadoLibreBaseApiClient : KoinComponent {

    private val mHeaderInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader(HEADER_NAME, HEADER_VALUE)
            .build()

        // Interceptor response.
        chain.proceed(request)
    }

    private val mOkHttp = OkHttpClient.Builder().addInterceptor(mHeaderInterceptor)

    protected val mRetrofit: Retrofit = Retrofit.Builder()
        .baseUrl(Constants.URL_BASE)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .client(mOkHttp.build())
        .build()

    /**
     * Parses the JSON object in the body when the endpoint returns an error status.
     *
     * @param errorBody A [ResponseBody] with the error status in JSON format.
     * @return An [ApiError] instance for the error status.
     */
    fun parseApiError(errorBody: ResponseBody): ApiError? {
        val errorConverter: Converter<ResponseBody, ApiError> =
            mRetrofit.responseBodyConverter(
                ApiError::class.java,
                arrayOfNulls<Annotation>(0)
            )

        return errorConverter.convert(errorBody)
    }
}
