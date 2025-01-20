package com.application.randomstringgenerator.ui

import android.content.ContentResolver
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.application.randomstringgenerator.data.model.RandomString
import com.application.randomstringgenerator.data.repository.RandomStringRepository
import com.application.randomstringgenerator.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val repository: RandomStringRepository) : ViewModel() {

    private val _randomStrings = MutableLiveData<Resource<List<RandomString>>>()
    val randomStrings: LiveData<Resource<List<RandomString>>> = _randomStrings

    private val stringList = mutableListOf<RandomString>()

    fun fetchRandomString(maxLength: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _randomStrings.postValue(Resource.Loading())
            try {
                val result = repository.fetchRandomString(maxLength)
                if (result != null) {
                    stringList.add(result)
                    _randomStrings.postValue(Resource.Success(stringList))
                } else {
                    _randomStrings.postValue(Resource.Error("No data received"))
                }
            } catch (e: Exception) {
                _randomStrings.postValue(Resource.Error("Error: ${e.message}"))
            }
        }
    }

    fun deleteString(item: RandomString) {
        stringList.remove(item)
        _randomStrings.postValue(Resource.Success(stringList))
    }

    fun clearAllStrings() {
        stringList.clear()
        _randomStrings.postValue(Resource.Success(stringList))
    }
}