package com.application.randomstringgenerator.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.application.randomstringgenerator.R
import com.application.randomstringgenerator.data.model.RandomString
import com.application.randomstringgenerator.data.repository.RandomStringRepository
import com.application.randomstringgenerator.data.source.ContentProviderDataSource
import com.application.randomstringgenerator.databinding.ActivityMainBinding
import com.application.randomstringgenerator.utils.Resource

private lateinit var viewBinding: ActivityMainBinding
private lateinit var viewModel: MainViewModel
private val adapter = RandomStringAdapter { itemToDelete ->
    viewModel.deleteString(itemToDelete) // Call a method to handle deletion
}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(RandomStringRepository(ContentProviderDataSource(contentResolver)))
        )[MainViewModel::class.java]
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        initButtons()
        initViewModels()
    }

    private fun initViews() {
        viewBinding.listString.layoutManager = LinearLayoutManager(this)
        viewBinding.listString.adapter = adapter
    }

    private fun initButtons() {
        viewBinding.generateButton.setOnClickListener {
            val maxLength = viewBinding.stringLength.text.toString().toIntOrNull() ?: return@setOnClickListener
            viewModel.fetchRandomString(maxLength)
        }

        viewBinding.deleteAll.setOnClickListener {
            viewModel.clearAllStrings()
        }
    }

    private fun initViewModels() {
        viewModel.randomStrings.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> viewBinding.loader.visibility = View.VISIBLE
                is Resource.Success -> {
                    viewBinding.loader.visibility = View.GONE
                    adapter.submitList(resource.data as List<RandomString>)
                }
                is Resource.Error -> {
                    viewBinding.loader.visibility = View.GONE
                    Toast.makeText(this, resource.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}