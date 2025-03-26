package com.example.technicalchallenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingData
import com.example.technicalchallenge.data.AlbumRepository
import com.example.technicalchallenge.data.local.Photo
import com.example.technicalchallenge.ui.album.AlbumsViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever


class AlbumsViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var albumsViewModel: AlbumsViewModel
    private val repository: AlbumRepository = mock()
    private val testDispatcher = StandardTestDispatcher()
    private val photo = Photo(
        albumId = 1,
        id = 1,
        title = "Test Photo",
        url = "https://placehold.co/600x600.png",
        thumbnailUrl = "https://placehold.co/150x150.png"
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        val fakePhotos = listOf(
            photo,
            photo.copy(id = 2, title = "Test Photo 2")
        )
        val pagingData: PagingData<Photo> = PagingData.from(fakePhotos)
        whenever(repository.getPagedPhotos()).thenReturn(flowOf(pagingData))

        albumsViewModel = AlbumsViewModel(repository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchAndSaveAlbums should call repository fetchAndStoreAlbums and reach onSuccess`() = runTest {
        whenever(repository.fetchAndStoreAlbums()).thenReturn(Unit)

        albumsViewModel.fetchAndSaveAlbums()
        advanceUntilIdle()
        assertEquals(false, albumsViewModel.uiState.loading)
        assertEquals(false, albumsViewModel.uiState.showSnackBar)
        verify(repository, times(1)).fetchAndStoreAlbums()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `fetchAndSaveAlbums should call repository fetchAndStoreAlbums and reach onFailure`() = runTest {
        whenever(repository.fetchAndStoreAlbums()).thenThrow(RuntimeException("Network error"))

        albumsViewModel.fetchAndSaveAlbums()
        advanceUntilIdle()
        assertEquals(false, albumsViewModel.uiState.loading)
        assertEquals(true, albumsViewModel.uiState.showSnackBar)
        verify(repository, times(1)).fetchAndStoreAlbums()
    }
}
