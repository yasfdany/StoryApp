package dev.studiocloud.storyapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import dev.studiocloud.storyapp.MainDispatcherRule
import dev.studiocloud.storyapp.data.ResultData
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.data.source.network.model.DefaultResponse
import dev.studiocloud.storyapp.data.source.network.model.LoginResponse
import dev.studiocloud.storyapp.getOrAwaitValue
import dev.studiocloud.storyapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
internal class AuthViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var mainRepository: MainRepository
    private lateinit var authViewModel: AuthViewModel

    private val dummyLoginSuccess = DataDummy.loginSuccessResponse
    private val dummyLoginError = DataDummy.failedLoginResponse
    private val dummyRegisterSuccess = DataDummy.registerSuccessResponse
    private val dummyEmailAlreadyRegistered = DataDummy.emailAlreadyRegisteredResponse

    @Before
    fun setup(){
        authViewModel = AuthViewModel(mainRepository)
    }

    @Test
    fun `when login response success and should not null`(){
        val expectedResponse = liveData<ResultData<LoginResponse?>> {
            emit(ResultData.Success(dummyLoginSuccess))
        }
        `when`(mainRepository.doLogin(
            "testcase@gmail.com",
            "qwer1234"
        )).thenReturn(expectedResponse)

        val actualResponse = authViewModel.doLogin(
            "testcase@gmail.com",
            "qwer1234",
        )?.getOrAwaitValue()

        Mockito.verify(mainRepository).doLogin(
            "testcase@gmail.com",
            "qwer1234",
        )
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ResultData.Success)
        Assert.assertEquals(dummyLoginSuccess.loginResult?.name, (actualResponse as ResultData.Success).data?.loginResult?.name)
    }

    @Test
    fun `when login failed`(){
        val expectedResponse = liveData<ResultData<LoginResponse?>> {
            emit(ResultData.Error(dummyLoginError.message.toString()))
        }
        `when`(mainRepository.doLogin(
            "testcase@gmail.com",
            "wrongpassword"
        )).thenReturn(expectedResponse)

        val actualResponse = authViewModel.doLogin(
            "testcase@gmail.com",
            "wrongpassword",
        )?.getOrAwaitValue()

        Mockito.verify(mainRepository).doLogin(
            "testcase@gmail.com",
            "wrongpassword",
        )
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ResultData.Error)
        Assert.assertEquals(dummyLoginError.message, (actualResponse as ResultData.Error).error)
    }

    @Test
    fun `when register response success and should not null`(){
        val expectedResponse = liveData<ResultData<DefaultResponse?>> {
            emit(ResultData.Success(dummyRegisterSuccess))
        }
        val randomEmail = "test_${System.currentTimeMillis()}@gmail.com"
        `when`(mainRepository.doRegister(
            "Dany",
            randomEmail,
            "qwer1234",
        )).thenReturn(expectedResponse)

        val actualResponse = authViewModel.doRegister(
            "Dany",
            randomEmail,
            "qwer1234",
        )?.getOrAwaitValue()

        Mockito.verify(mainRepository).doRegister(
            "Dany",
            randomEmail,
            "qwer1234",
        )
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ResultData.Success)
        Assert.assertEquals(dummyRegisterSuccess.message, (actualResponse as ResultData.Success).data?.message)
    }

    @Test
    fun `when email already registered`(){
        val expectedResponse = liveData<ResultData<DefaultResponse?>> {
            emit(ResultData.Error(dummyEmailAlreadyRegistered.message.toString()))
        }
        `when`(mainRepository.doRegister(
            "Dany",
            "testcase@gmail.com",
            "qwer1234",
        )).thenReturn(expectedResponse)

        val actualResponse = authViewModel.doRegister(
            "Dany",
            "testcase@gmail.com",
            "qwer1234",
        )?.getOrAwaitValue()

        Mockito.verify(mainRepository).doRegister(
            "Dany",
            "testcase@gmail.com",
            "qwer1234",
        )
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ResultData.Error)
        Assert.assertEquals(dummyEmailAlreadyRegistered.message, (actualResponse as ResultData.Error).error)
    }
}