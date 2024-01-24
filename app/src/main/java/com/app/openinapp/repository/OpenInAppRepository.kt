package com.app.openinapp.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.openinapp.db.OpenInAppApi
import com.app.openinapp.model.DataResponse
import com.app.openinapp.utils.Constants
import javax.inject.Inject

class OpenInAppRepository @Inject constructor(private val openInAppApi: OpenInAppApi) {

    private val dashBoardData = MutableLiveData<DataResponse>()

    val _dashBoardData: LiveData<DataResponse>
    get() = dashBoardData

    suspend fun getDashBoardData(){
        val result=openInAppApi.getDashboardData(Constants.TOKEN)
        if(result.isSuccessful && result.body()!=null){
            dashBoardData.postValue(result.body())
            Log.d("entries",result.body().toString())
        }
    }

}