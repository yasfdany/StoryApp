package dev.studiocloud.storyapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.liveData
import dev.studiocloud.storyapp.MainDispatcherRule
import dev.studiocloud.storyapp.data.ResultData
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
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
internal class StoryViewModelTest{
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var mainRepository: MainRepository
    private lateinit var storyViewModel: StoryViewModel

    private val dummyStoryWithLocation = DataDummy.getStoryWithLocationResponse
    private val dummyEmptyStoryWithLocation = DataDummy.getEmptyStoryResponse

    @Before
    fun setup(){
        storyViewModel = StoryViewModel(mainRepository)
    }

    @Test
    fun `when get Story with location Should Not Null and Return Success`(){
        val expectedStory = liveData<ResultData<List<StoryItem>?>> {
            emit(ResultData.Success(dummyStoryWithLocation.listStory))
        }

        `when`(
            mainRepository.getStoryLocations()
        ).thenReturn(expectedStory)

        val actualResponse = storyViewModel.getStoryLocation()?.getOrAwaitValue()

        Mockito.verify(mainRepository).getStoryLocations()
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ResultData.Success)
        Assert.assertEquals(dummyStoryWithLocation.listStory?.size, (actualResponse as ResultData.Success).data?.size)
    }

    @Test
    fun `when Story with location Empty`(){
        val expectedStory = liveData<ResultData<List<StoryItem>?>> {
            emit(ResultData.Success(dummyEmptyStoryWithLocation.listStory))
        }

        `when`(
            mainRepository.getStoryLocations()
        ).thenReturn(expectedStory)

        val actualResponse = storyViewModel.getStoryLocation()?.getOrAwaitValue()

        Mockito.verify(mainRepository).getStoryLocations()
        Assert.assertNotNull(actualResponse)
        Assert.assertTrue(actualResponse is ResultData.Success)
        Assert.assertTrue((actualResponse as ResultData.Success).data?.isEmpty() == true)
        Assert.assertEquals(dummyEmptyStoryWithLocation.listStory?.size, actualResponse.data?.size)
    }
}