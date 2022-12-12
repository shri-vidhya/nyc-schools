package com.shri.nycschools.ui.home;

import android.util.Log
import androidx.lifecycle.*
import com.shri.nycschools.model.HighSchoolDTO;
import com.shri.nycschools.network.NycSchoolsAPI
import com.shri.nycschools.network.UIResource
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class HomeViewModel(private val repo: NycSchoolsAPI) : ViewModel() {
    private val highSchoolListPostStatus: MutableLiveData<UIResource<List<HighSchoolDTO?>?>> = MutableLiveData()

    fun getHighSchoolList(): MutableLiveData<UIResource<List<HighSchoolDTO?>?>> {
        highSchoolListPostStatus.postValue(UIResource.loading(null))
        viewModelScope.launch {
            try {
                val response = repo.getNycHighSchoolList()
                if (response.isSuccessful && response.body() != null)
                    highSchoolListPostStatus.postValue(UIResource.success(response.body()))
                else
                    highSchoolListPostStatus.postValue(UIResource.error(response.message(), null, null))
            } catch (e: java.lang.Exception) {
                // TODO: Handle specific errors
                Log.e(TAG, e.toString())
                highSchoolListPostStatus.postValue(
                    UIResource.error(
                        "Error Occurred while fetching High School list: $e",
                        e,
                        null
                    )
                )
            }
        }
        return highSchoolListPostStatus

    }

//    public fun isReady(): Boolean {
//        return isBasicListReady;
//    }

    companion object {
        const val TAG = "HomeViewModel"
    }

}

class HomeViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val nycSchoolsAPI = NycSchoolsAPI()
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(nycSchoolsAPI) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}