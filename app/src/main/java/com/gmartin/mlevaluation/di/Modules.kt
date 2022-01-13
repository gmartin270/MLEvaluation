package com.gmartin.mlevaluation.di

import com.gmartin.mlevaluation.data.api.ProductApiAdapter
import com.gmartin.mlevaluation.data.api.ProductApiClient
import com.gmartin.mlevaluation.data.repository.ProductRepository
import org.koin.dsl.module

/**
 * Koin [module] to collect the definition of injectable classes.
 *
 * @author Guillermo O. Martín
 */
val applicationModule = module {
    single { ProductApiClient() }
    single { ProductApiAdapter(ProductApiClient()) }
    single { ProductRepository(get()) }
}
