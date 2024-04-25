package com.dicoding.asclepius.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.CanionRepository
import com.dicoding.asclepius.data.local.ResultDao
import com.dicoding.asclepius.data.local.ResultEntity
import com.dicoding.asclepius.data.remote.retrofit.ApiService

class HistoryViewModel(private val repository: CanionRepository) : ViewModel() {

    fun getAllResults(): LiveData<List<ResultEntity>> {
        return repository.getAllResults()
    }
}
