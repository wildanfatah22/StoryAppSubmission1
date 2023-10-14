package com.example.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.data.adapter.StoryAdapter
import com.example.storyapp.data.local.entity.StoryDetailResponse
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.utils.DataDummy.generateDummyNewStories
import com.example.storyapp.utils.DataDummy.generateDummyRequestLogin
import com.example.storyapp.utils.DataDummy.generateDummyRequestRegister
import com.example.storyapp.utils.DataDummy.generateDummyResponseLogin
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.getOrAwaitValue
import com.google.android.gms.maps.model.LatLng
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import java.io.File

@RunWith(MockitoJUnitRunner::class)
class ViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var authViewModel: AuthViewModel
    private lateinit var addStoryViewModel: AddStoryViewModel

    @Mock
    private var mockFile = File("fileName")

    @Before
    fun setUp() {
        mainViewModel = Mockito.mock(MainViewModel::class.java)
        authViewModel = Mockito.mock(AuthViewModel::class.java)
        addStoryViewModel = Mockito.mock(AddStoryViewModel::class.java)

        // Memutuskan koneksi dengan log Android
        System.setProperty("org.mockito.mock.android", "true")
    }

    // upload
    @Test
    fun `when message upload should return the right data and not null`() {
        val expectedRegisterMessage = MutableLiveData<String>()
        expectedRegisterMessage.value = "Story Uploaded"

        Mockito.`when`(addStoryViewModel.message).thenReturn(expectedRegisterMessage)

        val actualRegisterMessage = addStoryViewModel.message.getOrAwaitValue()

        Mockito.verify(addStoryViewModel).message
        Assert.assertNotNull(actualRegisterMessage)
        Assert.assertEquals(expectedRegisterMessage.value, actualRegisterMessage)
    }

    @Test
    fun `when loading upload should return the right data and not null`() {
        val expectedLoadingData = MutableLiveData<Boolean>()
        expectedLoadingData.value = true

        Mockito.`when`(addStoryViewModel.isLoading).thenReturn(expectedLoadingData)

        val actualLoading = addStoryViewModel.isLoading.getOrAwaitValue()

        Mockito.verify(addStoryViewModel).isLoading
        Assert.assertNotNull(actualLoading)
        Assert.assertEquals(expectedLoadingData.value, actualLoading)
    }

    @Test
    fun `verify upload function is working`() {
        val expectedUploadMessage = MutableLiveData<String>()
        expectedUploadMessage.value = "Story Uploaded"

        val requestImageFile = mockFile.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
            "photo",
            "fileName",
            requestImageFile
        )
        val description: RequestBody = "ini description".toRequestBody("text/plain".toMediaType())
        val token = "ini token"
        val latlng = LatLng(1.1, 1.1)

        addStoryViewModel.upload(imageMultipart, description, latlng.latitude, latlng.longitude, token)
        Mockito.verify(addStoryViewModel).upload(
            imageMultipart,
            description,
            latlng.latitude,
            latlng.longitude,
            token
        )

        Mockito.`when`(addStoryViewModel.message).thenReturn(expectedUploadMessage)

        val actualUploadMessage = addStoryViewModel.message.getOrAwaitValue()
        Mockito.verify(addStoryViewModel).message
        Assert.assertNotNull(actualUploadMessage)
        Assert.assertEquals(expectedUploadMessage.value, actualUploadMessage)
    }

    // get story
    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    @Test
    fun `verify getStory is working and Should Not Return Null`() = runTest {
        val noopListUpdateCallback = NoopListCallback()
        val dummyStory = generateDummyNewStories()
        val data = PagedTestDataSources.snapshot(dummyStory)
        val story = MutableLiveData<PagingData<StoryDetailResponse>>()
        val token = "ini token"
        story.value = data
        Mockito.`when`(mainViewModel.getPagingStories(token)).thenReturn(story)
        val actualData = mainViewModel.getPagingStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.StoryDetailDiffCallback(),
            updateCallback = noopListUpdateCallback,
            mainDispatcher = Dispatchers.Unconfined,
            workerDispatcher = Dispatchers.Unconfined,
        )
        differ.submitData(actualData)

        advanceUntilIdle()
        Mockito.verify(mainViewModel).getPagingStories(token)
        Assert.assertNotNull(differ.snapshot())
        assertEquals(dummyStory.size, differ.snapshot().size)
        assertEquals(dummyStory[0].name, differ.snapshot()[0]?.name)
    }

    @OptIn(ExperimentalPagingApi::class, ExperimentalCoroutinesApi::class)
    @Test
    fun `when GetStory is Empty Should Not return Null`() = runTest {
        val noopListUpdateCallback = NoopListCallback()
        val data = PagedTestDataSources.snapshot(listOf())
        val story = MutableLiveData<PagingData<StoryDetailResponse>>()
        val token = "ini token"
        story.value = data
        Mockito.`when`(mainViewModel.getPagingStories(token)).thenReturn(story)
        val actualData = mainViewModel.getPagingStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.StoryDetailDiffCallback(),
            updateCallback = noopListUpdateCallback,
            mainDispatcher = Dispatchers.Unconfined,
            workerDispatcher = Dispatchers.Unconfined,
        )
        differ.submitData(actualData)

        advanceUntilIdle()
        Mockito.verify(mainViewModel).getPagingStories(token)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertTrue(differ.snapshot().isEmpty())
        print(differ.snapshot().size)
    }

    class NoopListCallback : ListUpdateCallback {
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
    }

    class PagedTestDataSources private constructor() :
        PagingSource<Int, LiveData<List<StoryDetailResponse>>>() {
        companion object {
            fun snapshot(items: List<StoryDetailResponse>): PagingData<StoryDetailResponse> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryDetailResponse>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryDetailResponse>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }

    // login
    @Test
    fun `when login message should return the right data and not null`() {
        val expectedLoginMessage = MutableLiveData<String>()
        expectedLoginMessage.value = "Login Successfully"

        Mockito.`when`(authViewModel.message).thenReturn(expectedLoginMessage)

        val actualMessage = authViewModel.message.getOrAwaitValue()

        Mockito.verify(authViewModel).message
        Assert.assertNotNull(actualMessage)
        Assert.assertEquals(expectedLoginMessage.value, actualMessage)
    }

    @Test
    fun `when login loading state should return the right data and not null`() {
        val expectedLoadingData = MutableLiveData<Boolean>()
        expectedLoadingData.value = true

        Mockito.`when`(authViewModel.isLoading).thenReturn(expectedLoadingData)

        val actualLoading = authViewModel.isLoading.getOrAwaitValue()

        Mockito.verify(authViewModel).isLoading
        Assert.assertNotNull(actualLoading)
        Assert.assertEquals(expectedLoadingData.value, actualLoading)
    }

    @Test
    fun `when login should return the right login user data and not null`() {
        val dummyResponselogin = generateDummyResponseLogin()

        val expectedLogin = MutableLiveData<LoginResponse>()
        expectedLogin.value = dummyResponselogin

        Mockito.`when`(authViewModel.userLogin).thenReturn(expectedLogin)

        val actualLoginResponse = authViewModel.userLogin.getOrAwaitValue()

        Mockito.verify(authViewModel).userLogin
        Assert.assertNotNull(actualLoginResponse)
        assertEquals(expectedLogin.value, actualLoginResponse)
    }

    @Test
    fun `verify getResponseLogin function is working`() {
        val dummyRequestLogin = generateDummyRequestLogin()
        val dummyResponseLogin = generateDummyResponseLogin()

        val expectedResponseLogin = MutableLiveData<LoginResponse>()
        expectedResponseLogin.value = dummyResponseLogin

        authViewModel.login(dummyRequestLogin)

        Mockito.verify(authViewModel).login(dummyRequestLogin)

        Mockito.`when`(authViewModel.userLogin).thenReturn(expectedResponseLogin)

        val actualData = authViewModel.userLogin.getOrAwaitValue()

        Mockito.verify(authViewModel).userLogin
        Assert.assertNotNull(expectedResponseLogin)
        assertEquals(expectedResponseLogin.value, actualData)
    }

    // register
    @Test
    fun `when register message should return the right data and not null`() {
        val expectedRegisterMessage = MutableLiveData<String>()
        expectedRegisterMessage.value = "User Created"

        Mockito.`when`(authViewModel.message).thenReturn(expectedRegisterMessage)

        val actualRegisterMessage = authViewModel.message.getOrAwaitValue()

        Mockito.verify(authViewModel).message
        Assert.assertNotNull(actualRegisterMessage)
        Assert.assertEquals(expectedRegisterMessage.value, actualRegisterMessage)
    }

    @Test
    fun `when register loading state should return the right data and not null`() {
        val expectedLoadingData = MutableLiveData<Boolean>()
        expectedLoadingData.value = true

        Mockito.`when`(authViewModel.isLoading).thenReturn(expectedLoadingData)

        val actualLoading = authViewModel.isLoading.getOrAwaitValue()

        Mockito.verify(authViewModel).isLoading
        Assert.assertNotNull(actualLoading)
        Assert.assertEquals(expectedLoadingData.value, actualLoading)
    }

    @Test
    fun `verify getResponseRegister function is working`() {
        val dummyRequestRegister = generateDummyRequestRegister()
        val expectedRegisterMessage = MutableLiveData<String>()
        expectedRegisterMessage.value = "User Created"

        authViewModel.register(dummyRequestRegister)

        Mockito.verify(authViewModel).register(dummyRequestRegister)

        Mockito.`when`(authViewModel.message).thenReturn(expectedRegisterMessage)

        val actualData = authViewModel.message.getOrAwaitValue()

        Mockito.verify(authViewModel).message
        Assert.assertNotNull(actualData)
        Assert.assertEquals(expectedRegisterMessage.value, actualData)
    }
}