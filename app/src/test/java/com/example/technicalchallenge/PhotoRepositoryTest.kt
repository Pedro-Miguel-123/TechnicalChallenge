package com.example.technicalchallenge

import com.example.technicalchallenge.data.Photo
import com.example.technicalchallenge.data.PhotoRepositoryImpl
import com.example.technicalchallenge.data.api.APIService
import com.example.technicalchallenge.data.db.PhotoDao
import com.example.technicalchallenge.data.net.NetworkMonitor
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.argumentCaptor
import org.mockito.kotlin.capture
import org.mockito.kotlin.doNothing
import org.mockito.kotlin.eq
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

    @Before
    fun setup() {
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

    @Test
    fun `fetchAndStorePhotos should fetch data and store in DB`() = runTest {
        val mockPhotos = listOf(Photo(
            albumId = 1,
            id = 1,
            title = "accusamus beatae ad facilis cum similique qui sunt",
            url = "https://placehold.co/600x600/92c952/white/png",
            thumbnailUrl = "https://placehold.co/150x150/92c952/white/png"
        ))

        // Mock API response
        whenever(apiService.fetchPhotos()).thenAnswer { mockPhotos }

        // Call function directly
        repository.fetchAndStorePhotos()

        verify(photoDao, times(1)).insertPhotos(mockPhotos)
    }
}