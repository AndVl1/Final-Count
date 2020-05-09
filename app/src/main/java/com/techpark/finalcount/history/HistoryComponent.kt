package com.techpark.finalcount.history

import com.techpark.finalcount.history.views.fragment.HistoryFragment
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [
    HistoryModule::class
])
interface HistoryComponent {
    fun inject(fragment: HistoryFragment)
}