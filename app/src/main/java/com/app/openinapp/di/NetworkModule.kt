package com.app.openinapp.di

import com.app.openinapp.db.OpenInAppApi
import com.app.openinapp.fragment.RecentLinksFragment
import com.app.openinapp.fragment.TopLinksFragment
import com.app.openinapp.utils.Constants
import com.app.openinapp.adapter.RecentLinkAdapter
import com.app.openinapp.adapter.TopLinkAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideFakerApi(retrofit: Retrofit):OpenInAppApi{
        return retrofit.create(OpenInAppApi::class.java)
    }

    @Singleton
    @Provides
    fun provideTopLinkAdapter(): TopLinkAdapter {
        return TopLinkAdapter()
    }

    @Singleton
    @Provides
    fun provideRecentLinkAdapter(): RecentLinkAdapter {
        return RecentLinkAdapter()
    }

}
