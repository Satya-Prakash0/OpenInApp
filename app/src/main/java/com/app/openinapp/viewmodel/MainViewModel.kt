package com.app.openinapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.openinapp.model.DataResponse
import com.app.openinapp.repository.OpenInAppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Dispatcher
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val openInAppRepository: OpenInAppRepository): ViewModel() {

    val dashBoardData:LiveData<DataResponse>
    get() = openInAppRepository._dashBoardData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            openInAppRepository.getDashBoardData()

        }
    }
}