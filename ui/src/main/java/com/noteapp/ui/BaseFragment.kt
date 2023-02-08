package com.noteapp.ui

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected abstract fun initUI()
    protected abstract fun setupListeners()
    protected abstract fun observeState()
}
