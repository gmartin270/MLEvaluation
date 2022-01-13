package com.gmartin.mlevaluation.data.api

import org.koin.core.component.KoinComponent

/**
 * Retrofit client for the Product API endpoint.
 *
 * @author Guillermo O. Martín
 */
class ProductApiClient : MercadoLibreBaseApiClient(), KoinComponent {
    /**
     * Builds and instances the endpoint service connector for products API in MercadoLibre.
     */
    fun provideProductApi(): IProductApiService {
        return mRetrofit.create(IProductApiService::class.java)
    }
}