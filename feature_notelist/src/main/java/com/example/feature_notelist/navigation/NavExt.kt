package com.example.feature_notelist.navigation

import android.net.Uri
import androidx.fragment.app.Fragment
import androidx.navigation.NavDeepLinkRequest
import androidx.navigation.fragment.findNavController
import com.noteapp.core.constants.Constants
import com.noteapp.ui.R

internal fun Fragment.navigateToDetailedNote(noteId: Long = Constants.CREATE_NOTE_LONG) {
    val deeplink: NavDeepLinkRequest = NavDeepLinkRequest.Builder.fromUri(
        Uri.parse(getString(R.string.deeplink_detailed_note).replace("{noteId}", noteId.toString()))
    ).build()
    findNavController().navigate(deeplink)
}
