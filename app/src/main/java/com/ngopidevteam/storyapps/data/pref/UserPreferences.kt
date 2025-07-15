package com.ngopidevteam.storyapps.data.pref

import android.annotation.SuppressLint
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ngopidevteam.storyapps.data.model.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")

class UserPreferences private constructor(private val context: Context){

    companion object {

        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val IS_LOGIN_KEY = booleanPreferencesKey("isLogin")
        private val USER_ID_KEY = stringPreferencesKey("userId")
        private val NAME_KEY = stringPreferencesKey("name")

        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(context: Context): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(context)
                INSTANCE = instance
                instance
            }
        }
    }

    suspend fun saveSession(user: UserModel) {
        context.dataStore.edit { preferences ->
            preferences[EMAIL_KEY] = user.email
            preferences[TOKEN_KEY] = user.token
            preferences[IS_LOGIN_KEY] = true
            preferences[USER_ID_KEY] = user.userId
            preferences[NAME_KEY] = user.name
        }
    }

    fun getSession(): Flow<UserModel> {
        return context.dataStore.data.map { preferences ->
            UserModel(
                preferences[EMAIL_KEY] ?: "",
                preferences[TOKEN_KEY] ?: "",
                preferences[IS_LOGIN_KEY] == true,
                preferences[USER_ID_KEY] ?: "",
                preferences[NAME_KEY] ?: ""
            )
        }
    }

    suspend fun logout(){
        context.dataStore.edit { preferences ->
            preferences.clear()
        }
    }
}