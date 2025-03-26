package com.example.technicalchallenge

import androidx.paging.testing.asPagingSourceFactory
import androidx.paging.testing.asSnapshot
import com.example.technicalchallenge.data.local.Photo
import com.example.technicalchallenge.data.AlbumRepositoryImpl
import com.example.technicalchallenge.data.api.LebonCoinAPIService
import com.example.technicalchallenge.data.local.PhotoDao
import com.example.technicalchallenge.util.network.NetworkMonitor
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

class AlbumRepositoryTest {
    private lateinit var photoDao: PhotoDao
    private lateinit var networkMonitor: NetworkMonitor
    private lateinit var lebonCoinApiService: LebonCoinAPIService
    private lateinit var repository: AlbumRepositoryImpl

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val mockPhotos = listOf(
        Photo(
            albumId = 1,
            id = 1,
            title = "Test Photo",
            url = "https://placehold.co/600x600.png",
            thumbnailUrl = "https://placehold.co/150x150.png"
        )
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        photoDao = mock()
        networkMonitor = mock()
        lebonCoinApiService = mock()

        repository = AlbumRepositoryImpl(
            photoDao = photoDao,
            networkMonitor = networkMonitor,
            lebonCoinApiService = lebonCoinApiService,
            coroutineScope = testScope
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init should register a connection callback on network monitor`() = runTest {
        verify(networkMonitor).register()
    }

    @Test
    fun `fetchAndStoreAlbums should fetch data and store in DB`() = runTest {

        whenever(lebonCoinApiService.fetchPhotos()).thenAnswer { mockPhotos }


        repository.fetchAndStoreAlbums()

        verify(photoDao, times(1)).insertPhotos(mockPhotos)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `getPagedData should return data properly formatted`() = runTest {

        val pagingSourceFactory = mockPhotos.asPagingSourceFactory()
        val pagingSource = pagingSourceFactory()


        whenever(photoDao.getPagedPhotos()).thenReturn(pagingSource)


        val flow = repository.getPagedPhotos()
        val differ = flow.asSnapshot()

        advanceUntilIdle()


        assertEquals(1, differ.size)
        assertEquals(mockPhotos, differ)
    }
}
