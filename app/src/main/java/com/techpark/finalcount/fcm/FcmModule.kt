package com.techpark.finalcount.fcm

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FcmModule {

	@ContributesAndroidInjector
	abstract fun contributeFcmService(): MessagingService

}