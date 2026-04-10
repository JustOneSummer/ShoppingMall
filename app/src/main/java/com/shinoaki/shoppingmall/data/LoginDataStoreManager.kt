package com.shinoaki.shoppingmall.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "login_data")

class LoginDataStoreManager(private val context: Context) {
    // 定义存储键
    companion object {
        // 登录状态
        val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")

        // 用户信息
        val USER_ID = intPreferencesKey("user_id")
        val USER_NAME = stringPreferencesKey("user_name")
        val USER_EMAIL = stringPreferencesKey("user_email")
        val USER_AVATAR = stringPreferencesKey("user_avatar")

        // 登录时间
        val LOGIN_TIME = longPreferencesKey("login_time")

        // Token
        val ACCESS_TOKEN = stringPreferencesKey("access_token")
        val REFRESH_TOKEN = stringPreferencesKey("refresh_token")
    }

    //获取登录状态
    fun isLoggedIn(): Flow<Boolean> {
        return context.dataStore.data.map { preferences ->
            preferences[IS_LOGGED_IN] ?: false
        }
    }

    suspend fun getUserEmail(): String? {
        return context.dataStore.data.map { preferences ->
            preferences[USER_EMAIL]
        }.first()
    }

    // 退出登录
    suspend fun logout() {
        context.dataStore.edit { preferences ->
            preferences.clear()  // 清除所有数据
        }
    }

    suspend fun setLoginStatus(userEmail: String, isLoggedIn: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN] = isLoggedIn
            preferences[USER_EMAIL] = userEmail
        }
    }
    // 保存完整登录信息
//    suspend fun saveLoginData(user: User, token: Token) {
//        context.dataStore.edit { preferences ->
//            preferences[IS_LOGGED_IN] = true
//            preferences[USER_ID] = user.id
//            preferences[USER_NAME] = user.name
//            preferences[USER_EMAIL] = user.email
//            preferences[USER_AVATAR] = user.avatar
//            preferences[LOGIN_TIME] = System.currentTimeMillis()
//            preferences[ACCESS_TOKEN] = token.accessToken
//            preferences[REFRESH_TOKEN] = token.refreshToken
//        }
//    }

}