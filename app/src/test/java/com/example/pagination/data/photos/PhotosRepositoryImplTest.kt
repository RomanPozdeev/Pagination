package com.example.pagination.data.photos

import com.example.pagination.data.common.network.NoInternetException
import com.example.pagination.data.photos.local.PhotosStorage
import com.example.pagination.data.photos.remote.PagedApi
import com.example.pagination.data.photos.remote.PhotoResponse
import com.example.pagination.domain.photos.Photo
import com.example.pagination.domain.photos.PhotosRepository
import com.example.pagination.utils.page
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.assertj.core.api.Assertions
import org.junit.Before
import org.junit.Test

class PhotosRepositoryImplTest {
    //в таблице не должно быть повторяющихся данных
    private val table = linkedSetOf<Photo>()
    private var response: List<PhotoResponse>? = null

    private lateinit var repository: PhotosRepository

    @Before
    fun fetch() {
        val api = object : PagedApi {
            override fun fetch(page: Int): Single<List<PhotoResponse>> {
                return Single.fromCallable {
                    response ?: throw NoInternetException
                }
            }
        }

        val storage = object : PhotosStorage {
            override fun fetch(page: Int): Single<List<Photo>> {
                return Single.just(
                    table.toList().page(page, PAGE_SIZE)
                )
            }

            override fun saveBlocking(data: List<Photo>) {
                table.addAll(data)
            }

        }

        repository = PhotosRepositoryImpl(PAGE_SIZE, api, storage)
    }

    @Test
    fun `should fetch from api when db is empty`() {
        response = null
        val testObserver = TestObserver<List<Photo>>()
        repository.fetch(1)
            .subscribe(testObserver)

        testObserver.run {
            assertError(NoInternetException::class.java)
        }
    }

    @Test
    fun `should fetch from api when db is empty and save to db`() {
        table.clear()
        response = generateServerResponse(1)
        val testObserver = TestObserver<List<Photo>>()
        repository.fetch(1)
            .subscribe(testObserver)

        testObserver.run {
            assertComplete()
            assertNoErrors()
            assertValueCount(1)
            Assertions.assertThat(values()[0]).hasSize(PAGE_SIZE)
            Assertions.assertThat(table).hasSize(PAGE_SIZE)
        }
    }

    @Test
    fun `should fetch from db`() {
        response = null
        val photos = generatePhotos(1)
        table.addAll(photos)

        val testObserver = TestObserver<List<Photo>>()
        repository.fetch(1)
            .subscribe(testObserver)

        testObserver.run {
            assertComplete()
            assertNoErrors()
            assertValueCount(1)
            val values = values()[0]
            Assertions.assertThat(values).hasSize(PAGE_SIZE)
            Assertions.assertThat(values).containsOnlyElementsOf(photos)
        }
    }

    private fun generateServerResponse(page: Int): List<PhotoResponse> {
        return (1..PAGE_SIZE).map {
            val index = it * PAGE_SIZE
            PhotoResponse(
                albumId = index.toLong(),
                id = index.toLong(),
                title = "title$index",
                url = "url$index",
                thumbnailUrl = "thumbnail$index"
            )
        }
    }

    private fun generatePhotos(page: Int): List<Photo> {
        return (1..PAGE_SIZE).map {
            val index = it * PAGE_SIZE
            Photo(
                albumId = index.toLong(),
                id = index.toLong(),
                title = "title$index",
                url = "url$index",
                thumbnailUrl = "thumbnail$index"
            )
        }
    }

    companion object {
        const val PAGE_SIZE = 100
    }
}