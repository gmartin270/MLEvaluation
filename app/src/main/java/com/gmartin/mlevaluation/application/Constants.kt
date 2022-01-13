package com.gmartin.mlevaluation.application

import com.gmartin.mlevaluation.BuildConfig

/**
 * Constants definitions.
 *
 * @author Guillermo O. Martín
 */
object Constants {
    // App prefix for logging
    const val MAIN_TAG = "MLEval_"
    const val FORCE_VERBOSITY = BuildConfig.FORCE_DEBUG

    // MercadoLibre base URL
    const val URL_BASE = "https://api.mercadolibre.com/sites/MLA/"
}
