package dev.studiocloud.storyapp.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.studiocloud.storyapp.MainDispatcherRule
import dev.studiocloud.storyapp.data.FakeApiService
import dev.studiocloud.storyapp.data.FakeRemoteKeysDao
import dev.studiocloud.storyapp.data.FakeStoryDao
import dev.studiocloud.storyapp.data.FakeStoryDatabase
import dev.studiocloud.storyapp.data.source.local.StoryDao
import dev.studiocloud.storyapp.data.source.network.ApiService
import dev.studiocloud.storyapp.utils.DataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito

@ExperimentalCoroutinesApi
internal class MainRepositoryTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var storyDao: StoryDao
    private lateinit var remoteKeysDao: FakeRemoteKeysDao
    private lateinit var storyDatabase: FakeStoryDatabase
    private lateinit var remoteRepository: RemoteRepository
    private lateinit var mainRepository: MainRepository

    @Before
    fun setup(){
        apiService = FakeApiService()
        storyDao = FakeStoryDao()
        remoteKeysDao = FakeRemoteKeysDao()
        storyDatabase = FakeStoryDatabase()
        remoteRepository = RemoteRepository(storyDatabase, apiService)
        mainRepository = MainRepository(remoteRepository)
    }

    @Test
    fun `when doLogin Success`() = runTest {
        val expectedResponse = DataDummy.loginSuccessResponse
        val actualResponse = apiService.doLogin("emailsukses@gmail.com", "password")

        Assert.assertNotNull(expectedResponse)
        Assert.assertEquals(expectedResponse.loginResult?.name, actualResponse.body()?.loginResult?.name)
    }

    @Test
    fun `when doLogin Failed`() = runTest {
        val expectedResponse = DataDummy.failedLoginResponse
        val actualResponse = apiService.doLogin("emailgagal@gmail.com", "password")

        Assert.assertNotNull(expectedResponse)
        Assert.assertEquals(expectedResponse.message, actualResponse.body()?.message)
    }

    @Test
    fun `when doRegister Success`() = runTest {
        val expectedResponse = DataDummy.registerSuccessResponse
        val actualResponse = apiService.doRegister("nama","emailsukses@gmail.com", "password")

        Assert.assertNotNull(expectedResponse)
        Assert.assertEquals(expectedResponse.message, actualResponse.body()?.message)
    }

    @Test
    fun `when doRegister Failed`() = runTest {
        val expectedResponse = DataDummy.emailAlreadyRegisteredResponse
        val actualResponse = apiService.doRegister("nama","emailsama@gmail.com", "password")

        Assert.assertNotNull(expectedResponse)
        Assert.assertEquals(expectedResponse.message, actualResponse.body()?.message)
    }

    @Test
    fun `when postNewStory Success`() = runTest {
        val expectedResponse = DataDummy.createStorySuccessResponse

        val fakeRequestBody = Mockito.mock(RequestBody::class.java)
        val fakeMultipartRequestBody = Mockito.mock(MultipartBody.Part::class.java)

        val actualResponse = apiService.postNewStory(
            "token",
            fakeRequestBody,
            fakeRequestBody,
            fakeRequestBody,
            fakeMultipartRequestBody
        );

        Assert.assertNotNull(expectedResponse)
        Assert.assertEquals(expectedResponse.message, actualResponse.body()?.message)
    }

    @Test
    fun `when getStory Should Not Null`() = runTest {
        val expectedResponse = DataDummy.getStoryResponse
        val actualResponse = apiService.getAllStories("token", 1, 10);

        Assert.assertNotNull(expectedResponse)
        Assert.assertEquals(expectedResponse.listStory?.size, actualResponse.body()?.listStory?.size)
    }

    @Test
    fun `when getStory Return empty`() = runTest {
        val expectedResponse = DataDummy.getEmptyStoryResponse
        val actualResponse = apiService.getAllStories("apiKey", 1, 0);

        Assert.assertNotNull(expectedResponse)
        Assert.assertTrue(expectedResponse.listStory?.isEmpty() == true)
        Assert.assertEquals(expectedResponse.listStory?.size, actualResponse.body()?.listStory?.size)
    }

    @Test
    fun `when getStoryLocations Should Not Null`() = runTest {
        val expectedResponse = DataDummy.getStoryWithLocationResponse
        val actualResponse = apiService.getStoryLocations("token");

        Assert.assertNotNull(expectedResponse)
        Assert.assertEquals(expectedResponse.listStory?.size, actualResponse.body()?.listStory?.size)
    }

    @Test
    fun `when getStoryLocations Return empty`() = runTest {
        val expectedResponse = DataDummy.getEmptyStoryResponse
        val actualResponse = apiService.getStoryLocations("empty");

        Assert.assertNotNull(expectedResponse)
        Assert.assertTrue(expectedResponse.listStory?.isEmpty() == true)
        Assert.assertEquals(expectedResponse.listStory?.size, actualResponse.body()?.listStory?.size)
    }
}