package com.example.noteapp.ui.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.domain.model.Filter
import com.example.domain.model.NoteSortOrder
import com.example.domain.model.TodoSortOrder
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

data class FilterInfo(
    val sortOrder: NoteSortOrder,
    val todoSortOrder: TodoSortOrder,
    val filter: Filter
)

@Singleton
class PreferenceStorage @Inject constructor(@ApplicationContext private val applicationContext: Context) {
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

    // todo
//    private val preferenceFlow = applicationContext.dataStore.data
//        .map { preferences ->
//            val sortOrder =
//                SortOrder.valueOf(
//                    preferences[PreferenceKeys.SORT_ORDER]
//                        ?: SortOrder.DEFAULT.name
//                )
//            val filter =
//                Filter.valueOf(preferences[PreferenceKeys.FILTER] ?: Filter.BOTH.name)
//            FilterInfo(sortOrder, filter)
//        }
//
//    suspend fun updateSortOrder(sortOrder: SortOrder) {
//        applicationContext.dataStore.edit { mutablePreferences ->
//            mutablePreferences[PreferenceKeys.SORT_ORDER] = sortOrder.name
//        }
//    }
//
//    suspend fun updateFilter(filter: Filter) {
//        applicationContext.dataStore.edit { mutablePreferences ->
//            mutablePreferences[PreferenceKeys.FILTER] = filter.name
//        }
//    }
//
//    private object PreferenceKeys {
//        val SORT_ORDER = stringPreferencesKey("sort_order")
//        val FILTER = stringPreferencesKey("filter")
//    }
}
