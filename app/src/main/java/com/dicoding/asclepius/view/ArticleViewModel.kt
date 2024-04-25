package com.dicoding.asclepius.view

import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.data.CanionRepository

class ArticleViewModel(private val canionRepository: CanionRepository) : ViewModel() {

    fun getArticles() = canionRepository.getArticles()

}