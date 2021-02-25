package com.hoc081098.datastoresample

import android.app.Application
import androidx.datastore.preferences.createDataStore
import com.hoc081098.datastoresample.data.TaskRepositoryImpl
import com.hoc081098.datastoresample.data.UserPreferencesRepositoryImpl
import com.hoc081098.datastoresample.domain.ChangeShowCompleted
import com.hoc081098.datastoresample.domain.ChangeTheme
import com.hoc081098.datastoresample.domain.EnableSortByDeadline
import com.hoc081098.datastoresample.domain.GetTheme
import com.hoc081098.datastoresample.domain.model.FilterSortTasks
import com.hoc081098.datastoresample.ui.MainViewModel

object Locator {
    private var application: Application? = null

    private inline val requireApplication
        get() = application ?: error("Missing call: initWith(application)")

    fun initWith(application: Application) {
        this.application = application
    }

    val mainViewModelFactory
        get() = MainViewModel.Factory(
            filterSortTasks = filterSortTasks,
            getTheme = getTheme,
            changeShowCompleted = changeShowCompleted,
            enableSortByDeadline = enableSortByDeadline,
            changeTheme = changeTheme,
        )

    private val filterSortTasks
        get() = FilterSortTasks(
            taskRepository = taskRepository,
            userPreferencesRepository = userPreferencesRepository
        )

    private val changeTheme get() = ChangeTheme(userPreferencesRepository)

    private val getTheme get() = GetTheme(userPreferencesRepository)

    private val changeShowCompleted get() = ChangeShowCompleted(userPreferencesRepository)

    private val enableSortByDeadline get() = EnableSortByDeadline(userPreferencesRepository)

    private val taskRepository by lazy { TaskRepositoryImpl() }
    private val userPreferencesRepository by lazy {
        val dataStore = requireApplication.createDataStore(name = "user_preferences")
        UserPreferencesRepositoryImpl(dataStore)
    }
}