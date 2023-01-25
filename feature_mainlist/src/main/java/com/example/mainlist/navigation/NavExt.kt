package com.example.mainlist.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptions

const val bookmarksRoute = "bookmarks_route"
fun NavController.navigateToBookmarks(navOptions: NavOptions? = null) {
    this.navigate(bookmarksRoute, navOptions)
}
//
//fun NavGraphBuilder.bookmarksScreen() {
//    composable(route = bookmarksRoute) {
//        BookmarksRoute()
//    }
//}
