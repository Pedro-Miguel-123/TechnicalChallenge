package com.example.technicalchallenge

import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.example.technicalchallenge.data.db.Photo
import com.example.technicalchallenge.data.PhotoRepositoryImpl
import com.example.technicalchallenge.data.api.APIService
import com.example.technicalchallenge.data.db.PhotoDao
import com.example.technicalchallenge.data.net.NetworkMonitor
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class PhotoRepositoryTest {
    private lateinit var photoDao: PhotoDao
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var apiService: APIService
    private lateinit var repository: PhotoRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        photoDao = mock()
        networkMonitor = mock()
        apiService = mock()

        repository = PhotoRepositoryImpl(
            photoDao = photoDao,
            networkMonitor = networkMonitor,
            apiService = apiService,
            coroutineScope = testScope
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `fetchAndStorePhotos should fetch data and store in DB`() = runTest {

        verify(networkMonitor).register()

        val mockPhotos = listOf(
            Photo(
            albumId = 1,
            id = 1,
            title = "accusamus beatae ad facilis cum similique qui sunt",
            url = "https://placehold.co/600x600/92c952/white/png",
            thumbnailUrl = "https://placehold.co/150x150/92c952/white/png"
        )
        )


        whenever(apiService.fetchPhotos()).thenAnswer { mockPhotos }


        repository.fetchAndStorePhotos()

        verify(photoDao, times(1)).insertPhotos(mockPhotos)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getPagedData should return data properly formatted`() = runTest {
        val mockPhoto = listOf(
            Photo(
            albumId = 1,
            id = 1,
            title = "Test Photo",
            url = "https://placehold.co/600x600.png",
            thumbnailUrl = "https://placehold.co/150x150.png"
        )
        )

        val pagingSourceFactory = mockPhoto.asPagingSourceFactory()
        val pagingSource = pagingSourceFactory()


        whenever(photoDao.getPagedPhotos()).thenReturn(pagingSource)


        val flow = repository.getPagedPhotos()
        println("FLOW -> ${flow.first()}")
        val differ = flow.asSnapshot()

        advanceUntilIdle()


        assertEquals(1, differ.size)
        assertEquals(mockPhoto, differ)
    }
}
