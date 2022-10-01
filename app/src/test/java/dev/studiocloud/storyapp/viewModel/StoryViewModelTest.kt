package dev.studiocloud.storyapp.viewModel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import androidx.recyclerview.widget.ListUpdateCallback
import dev.studiocloud.storyapp.MainDispatcherRule
import dev.studiocloud.storyapp.data.ResultData
import dev.studiocloud.storyapp.data.repository.MainRepository
import dev.studiocloud.storyapp.data.source.network.model.StoryItem
import dev.studiocloud.storyapp.getOrAwaitValue
import dev.studiocloud.storyapp.ui.activities.home.adapters.StoryListAdapter
import dev.studiocloud.storyapp.utils.DataDummy
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
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

    private val dummyStory = DataDummy.getStoryResponse
    private val dummyStoryWithLocation = DataDummy.getStoryWithLocationResponse
    private val dummyEmptyStoryWithLocation = DataDummy.getEmptyStoryResponse

    @Before
    fun setup(){
        storyViewModel = StoryViewModel(mainRepository)
    }

    @Test
    fun `when get Story Should Not Null and Return Success`() = runTest {
        val data: PagingData<StoryItem> = StoryPagingSource.snapshot(dummyStory.listStory!!)
        val expectedStory = liveData { emit(data) }

        `when`(mainRepository.getStory()).thenReturn(expectedStory)

        val actualResponse: PagingData<StoryItem>? = storyViewModel.stories?.getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryListAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main
        )

        if (actualResponse != null){
            differ.submitData(actualResponse)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(dummyStory.listStory, differ.snapshot())
            Assert.assertEquals(dummyStory.listStory?.size, differ.snapshot().size)
            Assert.assertEquals(dummyStory.listStory?.get(0)?.id, differ.snapshot()[0]?.id)
        }
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

    class StoryPagingSource : PagingSource<Int, LiveData<List<StoryItem>>>() {
        companion object {
            fun snapshot(items: List<StoryItem>): PagingData<StoryItem> {
                return PagingData.from(items)
            }
        }

        override fun getRefreshKey(state: PagingState<Int, LiveData<List<StoryItem>>>): Int {
            return 0
        }

        override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<StoryItem>>> {
            return LoadResult.Page(emptyList(), 0, 1)
        }
    }


    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}