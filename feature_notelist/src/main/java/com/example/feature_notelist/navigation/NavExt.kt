package com.example.feature_notelist.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.noteapp.ui.R

internal fun NavController.toDetailedNote(noteId: Long = -1) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(
            context.getString(R.string.deeplink_detailed_note)
                .replace("{noteId}", noteId.toString())
        )
    ).build()
    navigate(deeplink)
    // -1 -> create note todo make a constant
    // not null -> open existing note
}
