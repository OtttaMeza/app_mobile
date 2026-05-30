package com.example.primeraaplicacion.core.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    protected abstract fun setupUI()
    protected abstract fun observeViewModel()
}
