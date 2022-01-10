package com.gmartin.mlevaluation.application

import android.app.Application
import android.content.pm.ApplicationInfo
import android.util.Log
import com.gmartin.mlevaluation.BuildConfig.APPLICATION_ID
import com.gmartin.mlevaluation.BuildConfig.VERSION_CODE
import com.gmartin.mlevaluation.BuildConfig.VERSION_NAME
import com.gmartin.mlevaluation.injection.applicationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import kotlin.properties.Delegates

class MLEvaluationApplication : Application() {

    private val tag: String = "${Constants.MAIN_TAG}${MLEvaluationApplication::class.simpleName}"

    companion object {
        var isDebug by Delegates.notNull<Boolean>()
    }

    /**
     * TODO
     */
    override fun onCreate() {
        super.onCreate()

        isDebug = (
                applicationContext.applicationInfo.flags and
                        ApplicationInfo.FLAG_DEBUGGABLE != 0
                ) || Constants.FORCE_VERBOSITY

        if (isDebug) Log.d(tag, "$APPLICATION_ID $VERSION_NAME [$VERSION_CODE]")

        // Koin injection initialization.
        startKoin {
            androidContext(this@MLEvaluationApplication)
            modules(applicationModule)
        }
    }
}
