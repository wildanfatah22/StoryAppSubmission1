package com.example.storyapp.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.data.response.LoginResponse
import com.example.storyapp.utils.DataDummy.generateDummyRequestLogin
import com.example.storyapp.utils.DataDummy.generateDummyRequestRegister
import com.example.storyapp.utils.DataDummy.generateDummyResponseLogin
import com.example.storyapp.utils.MainDispatcherRule
import com.example.storyapp.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AuthViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    private lateinit var authViewModel: AuthViewModel

    @Before
    fun setUp() {
        authViewModel = Mockito.mock(AuthViewModel::class.java)
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