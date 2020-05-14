package com.techpark.finalcount.database.dbexpimp

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import java.util.function.BiConsumer
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext

class CoroutinesAdapter {
    //https://stackoverflow.com/questions/52869672/call-kotlin-suspend-function-in-java-class (:)
    @JvmOverloads
    fun <R> getContinuation(onFinished: BiConsumer<R?, Throwable?>,
                            dispatcher: CoroutineDispatcher = Dispatchers.Default): Continuation<R> {
        return object : Continuation<R> {
            override val context: CoroutineContext
                get() = dispatcher

            @RequiresApi(Build.VERSION_CODES.N)
            override fun resumeWith(result: Result<R>) {
                onFinished.accept(result.getOrNull(), result.exceptionOrNull())
            }
        }
    }
}