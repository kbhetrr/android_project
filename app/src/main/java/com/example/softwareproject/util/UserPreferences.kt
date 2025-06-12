package com.example.softwareproject.util // 적절한 패키지로 변경

import android.content.Context
import android.content.SharedPreferences

object UserPreferences {

    private const val PREFS_NAME = "user_app_prefs"
    private const val KEY_GITHUB_ID = "github_id"
    private const val KEY_SOLVEDAC_HANDLE = "solvedac_handle"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveGithubId(context: Context, githubId: String) {
        getPreferences(context).edit().putString(KEY_GITHUB_ID, githubId).apply()
    }

    fun getGithubId(context: Context): String? {
        return getPreferences(context).getString(KEY_GITHUB_ID, null)
    }

    fun saveSolvedAcHandle(context: Context, solvedAcHandle: String) {
        getPreferences(context).edit().putString(KEY_SOLVEDAC_HANDLE, solvedAcHandle).apply()
    }

    fun getSolvedAcHandle(context: Context): String? {
        return getPreferences(context).getString(KEY_SOLVEDAC_HANDLE, null)
    }

    // 모든 ID 삭제 (선택 사항: 로그아웃 또는 초기화 기능)
    fun clearUserIds(context: Context) {
        getPreferences(context).edit()
            .remove(KEY_GITHUB_ID)
            .remove(KEY_SOLVEDAC_HANDLE)
            .apply()
    }
}