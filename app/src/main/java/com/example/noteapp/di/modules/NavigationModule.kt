package com.example.noteapp.di.modules

import com.example.noteapp.di.MainApplication
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn

@Module
@InstallIn(MainApplication::class)
object NavigationModule {

    @Provides
    fun providesCicerone() = Cicerone.create()

    @Provides
    fun providesRouter(cicerone: Cicerone<Router>): Router = cicerone.router

    @Provides
    fun providesNavigationHolder(cicerone: Cicerone<Router>): NavigatorHolder =
        cicerone.getNavigatorHolder()

}
