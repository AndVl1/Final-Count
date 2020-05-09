package com.techpark.finalcount.di

import android.content.Context
import dagger.Module
import dagger.Provides


/**
 * App Module
 */
@Module
class AppModule (private val context: Context) {

    /**
     * Expose Application as an injectable context
     */
//    @Binds
//    internal abstract fun bindContext(app: App): Context

    @Provides
    fun providesAppContext() = context

}