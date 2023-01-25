package com.example.feature_notelist.internal.navigation

import android.net.Uri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest
import com.noteapp.core.constants.Constants
import com.noteapp.ui.R

internal fun NavController.toDetailedNote(noteId: Long = Constants.CREATE_NOTE_LONG) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(
            context.getString(R.string.deeplink_detailed_note)
                .replace("{noteId}", noteId.toString())
        )
    ).build()
    navigate(deeplink)
    // -1 -> create note
    // not null -> open existing note
}
