package com.dicoding.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.ApiUserConfig
import com.dicoding.storyapp.data.pref.UserPreference
import com.dicoding.storyapp.data.pref.dataStore

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore("session")
object Injection {
    fun provideRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiUserConfig.apiService
        return UserRepository.getInstance(pref, apiService)
    }
}