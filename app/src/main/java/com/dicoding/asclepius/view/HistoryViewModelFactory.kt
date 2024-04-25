package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.data.CanionRepository

class HistoryViewModelFactory(private val canionRepository: CanionRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryViewModel::class.java)) {
            return HistoryViewModel(canionRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}