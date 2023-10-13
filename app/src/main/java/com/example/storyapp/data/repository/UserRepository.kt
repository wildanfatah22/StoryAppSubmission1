package com.example.storyapp.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.data.api.ApiConfig
import com.example.storyapp.data.api.ApiService
import com.example.storyapp.data.local.datastore.AppDb
import com.example.storyapp.data.local.entity.StoryDetailResponse
import com.example.storyapp.data.response.DetailResponse
import com.example.storyapp.data.response.LoginAccount
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.data.response.RegisterAccount
import com.example.storyapp.data.response.ResponseLocationStory
import com.example.storyapp.helper.wrapEspressoIdlingResource
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserRepository(
    private val appDb: AppDb,
    private val apiService: ApiService
){
    private var _stories = MutableLiveData<List<StoryDetailResponse>>()
    var stories: LiveData<List<StoryDetailResponse>> = _stories

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userLogin = MutableLiveData<LoginResponse>()
    var userLogin: LiveData<LoginResponse> = _userLogin

    fun getLoginResponse(loginAccount: LoginAccount) {
        wrapEspressoIdlingResource {
            _isLoading.value = true
            val api = ApiConfig.getApiService().loginUser(loginAccount)
            api.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    _isLoading.value = false
                    val responseBody = response.body()

                    if (response.isSuccessful) {
                        _userLogin.value = responseBody!!
                        _message.value = "Hello ${_userLogin.value!!.loginResult.name}!"
                    } else {
                        when (response.code()) {
                            401 -> _message.value =
                                "Email atau password yang anda masukan salah, silahkan coba lagi"
                            408 -> _message.value =
                                "Koneksi internet anda lambat, silahkan coba lagi"
                            else -> _message.value = "Pesan error: " + response.message()
                        }
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = "Pesan error: " + t.message.toString()
                }

            })
        }
    }

    fun getResponseRegister(registerUser: RegisterAccount) {
        wrapEspressoIdlingResource {
            _isLoading.value = true
            val api = ApiConfig.getApiService().registerUser(registerUser)
            api.enqueue(object : Callback<DetailResponse> {
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    _isLoading.value = false
                    if (response.isSuccessful) {
                        _message.value = "Yeay akun berhasil dibuat"
                    } else {
                        when (response.code()) {
                            400 -> _message.value =
                                "Email yang anda masukan sudah terdaftar, silahkan coba lagi"
                            408 -> _message.value =
                                "Koneksi internet anda lambat, silahkan coba lagi"
                            else -> _message.value = "Pesan error: " + response.message()
                        }
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    _isLoading.value = false
                    _message.value = "Pesan error: " + t.message.toString()
                }

            })
        }
    }

    fun upload(
        photo: MultipartBody.Part,
        des: RequestBody,
        lat: Double?,
        lng: Double?,
        token: String
    ) {
        _isLoading.value = true
        val service = ApiConfig.getApiService().addStory(
            photo, des, lat?.toFloat(), lng?.toFloat(), "Bearer $token"
        )
        service.enqueue(object : Callback<DetailResponse> {
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        _message.value = responseBody.message
                    }
                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message
            }
        })
    }

    fun getStories(token: String) {
        _isLoading.value = true
        val api = ApiConfig.getApiService().getLocationStory(32, 1, "Bearer $token")
        api.enqueue(object : Callback<ResponseLocationStory> {
            override fun onResponse(
                call: Call<ResponseLocationStory>,
                response: Response<ResponseLocationStory>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _stories.value = responseBody.listStory
                    }
                    _message.value = responseBody?.message.toString()

                } else {
                    _message.value = response.message()
                }
            }

            override fun onFailure(call: Call<ResponseLocationStory>, t: Throwable) {
                _isLoading.value = false
                _message.value = t.message.toString()
            }
        })
    }

    @ExperimentalPagingApi
    fun getPagingStories(token: String): LiveData<PagingData<StoryDetailResponse>> {
        val pager = Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(appDb, apiService, token),
            pagingSourceFactory = {
                appDb.storyDetailResponseDao().getAllStories()
            }
        )
        return pager.liveData
    }

}