package com.dicoding.storyapp.view.tambah_cerita

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.R
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.api.ApiStoryConfig
import com.dicoding.storyapp.data.response.UploadResponse
import com.dicoding.storyapp.reduceFileImage
import com.dicoding.storyapp.uriToFile
import com.dicoding.storyapp.view.main.MainActivity
import com.google.gson.Gson
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.HttpException

class UploadViewModel(private val repository: UserRepository, val context: Context) : ViewModel() {

    private var currentImageUri: Uri? = null
    private lateinit var apiService: ApiService

    init {
        viewModelScope.launch {
            val token = getTokenFromDataStore()
            apiService = ApiStoryConfig.getApiService(token)
        }
    }
    private suspend fun getTokenFromDataStore(): String {
        return repository.getSession().first().token
    }

    fun uploadImage(uril: Uri?, desc :String) {
        currentImageUri = uril
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, context).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = desc

            val requestBody = description.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
            val multipartBody = MultipartBody.Part.createFormData(
                "photo",
                imageFile.name,
                requestImageFile
            )
            viewModelScope.launch {
                try {
                    val successResponse = apiService.uploadImage(multipartBody, requestBody)
                    showToast(successResponse.message)
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    context.startActivity(intent)
                } catch (e: HttpException) {
                    val errorBody = e.response()?.errorBody()?.string()
                    val errorResponse = Gson().fromJson(errorBody, UploadResponse::class.java)
                    showToast(errorResponse.message)
                }
            }
        } ?: showToast(context.getString(R.string.empty_image_warning))
    }
    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}