package com.jetsada.firebasemvvmapplication.di

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.jetsada.firebasemvvmapplication.util.Constants.Companion.LOCAL_SHARE_PREF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPref(@ApplicationContext context: Context): SharedPreferences {
       return context.getSharedPreferences(LOCAL_SHARE_PREF, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

}