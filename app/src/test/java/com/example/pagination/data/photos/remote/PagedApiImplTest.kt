package com.example.pagination.data.photos.remote

import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PagedApiImplTest {

    @Test
    fun `should return empty list when server return empty list`() {
        val pagedApi = PagedApiImpl(PAGE_SIZE, object : PhotosApi {
            override fun photos(): Single<List<PhotoResponse>> {
                return Single.just(emptyList())
            }
        })

        val testObserver = TestObserver<List<PhotoResponse>>()
        pagedApi.fetch(1)
            .subscribe(testObserver)
        testObserver.run {
            assertComplete()
            assertNoErrors()
            assertValueCount(1)
            assertThat(values()[0]).isEmpty()
        }
    }

    @Test
    fun `should return single element when server return only one element`() {
        val element = PhotoResponse(
            albumId = 1,
            id = 1,
            title = "title1",
            url = "url1",
            thumbnailUrl = "thumbnail1"
        )
        val pagedApi = PagedApiImpl(PAGE_SIZE, object : PhotosApi {
            override fun photos(): Single<List<PhotoResponse>> {
                return Single.just(listOf(element))
            }
        })

        val testObserver = TestObserver<List<PhotoResponse>>()
        pagedApi.fetch(1)
            .subscribe(testObserver)
        testObserver.run {
            assertComplete()
            assertNoErrors()
            assertValueCount(1)
            assertThat(values()[0]).containsOnly(element)
        }
    }

    @Test
    fun `should return list of PAGE_SIZE elements on first page even server return more`() {
        val testData = (1..2 * PAGE_SIZE).map {
            PhotoResponse(
                albumId = it.toLong(),
                id = it.toLong(),
                title = "title$it",
                url = "url$it",
                thumbnailUrl = "thumbnail$it"
            )
        }
        val pagedApi = PagedApiImpl(PAGE_SIZE, object : PhotosApi {
            override fun photos(): Single<List<PhotoResponse>> {
                return Single.just(testData)
            }
        })

        val testObserver = TestObserver<List<PhotoResponse>>()
        pagedApi.fetch(1)
            .subscribe(testObserver)

        testObserver.run {
            assertComplete()
            assertNoErrors()
            assertValueCount(1)
            assertThat(values()[0]).hasSize(PAGE_SIZE)
        }
    }

    @Test
    fun `two page request should return all data of two page list`() {
        val testData = (1..2 * PAGE_SIZE).map {
            PhotoResponse(
                albumId = it.toLong(),
                id = it.toLong(),
                title = "title$it",
                url = "url$it",
                thumbnailUrl = "thumbnail$it"
            )
        }
        val pagedApi = PagedApiImpl(PAGE_SIZE, object : PhotosApi {
            override fun photos(): Single<List<PhotoResponse>> {
                return Single.just(testData)
            }
        })

        val testSubscriber = TestSubscriber<List<PhotoResponse>>()
        Single.merge(pagedApi.fetch(1), pagedApi.fetch(2))
            .subscribe(testSubscriber)

        testSubscriber.run {
            assertComplete()
            assertNoErrors()
            assertValueCount(2)
            val values = values()[0] + values()[1]
            assertThat(values).hasSize(PAGE_SIZE * 2)
            assertThat(values).containsOnlyElementsOf(testData)

        }
    }

    companion object {
        const val PAGE_SIZE = 100
    }
}