package com.vcco.hiltoncodechallenge.di.module

import android.content.Context
import androidx.room.Room
import com.vcco.hiltoncodechallenge.BuildConfig
import com.vcco.hiltoncodechallenge.database.AppDatabase
import com.vcco.hiltoncodechallenge.network.SearchIPService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {


    @Provides
    @Singleton
    fun provideOKHttpClient(): OkHttpClient {
        val loggerInterceptor = HttpLoggingInterceptor()
        loggerInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .addInterceptor(loggerInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .build()

    @Singleton
    @Provides
    fun provideSearchIpService(retrofit: Retrofit) =
        retrofit.create(SearchIPService::class.java)


    @Singleton
    @Provides
    fun provideDataBase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            BuildConfig.DATABASE_NAME
        ).build()

    @Provides
    fun provideIpInfoDao(database: AppDatabase) = database.ipInfoDao()
}