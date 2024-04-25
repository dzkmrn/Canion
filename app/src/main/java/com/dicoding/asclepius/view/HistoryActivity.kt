package com.dicoding.asclepius.view

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.data.CanionRepository
import com.dicoding.asclepius.data.local.History
import com.dicoding.asclepius.data.remote.retrofit.RetrofitInstance
import com.dicoding.asclepius.databinding.ActivityHistoryBinding


class HistoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var historyAdapter: HistoryAdapter
    private lateinit var historyViewModel: HistoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        historyAdapter = HistoryAdapter()
        binding.rvHistory.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = historyAdapter
        }

        val resultDao = History.getInstance(application).resultDao()
        val canionRepository = CanionRepository(RetrofitInstance.apiService, resultDao)
        val viewModelFactory = HistoryViewModelFactory(canionRepository)
        historyViewModel = ViewModelProvider(this,viewModelFactory).get(HistoryViewModel::class.java)

        historyViewModel.getAllResults().observe(this) { results ->
            results?.let {
                historyAdapter.submitList(it)
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}