package com.example.pagination.data.photos.local

import android.arch.persistence.room.Room
import android.support.test.InstrumentationRegistry
import android.support.test.runner.AndroidJUnit4
import com.example.pagination.data.common.db.PaginationDatabase
import com.example.pagination.domain.photos.Photo
import dagger.Lazy
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import io.reactivex.subscribers.TestSubscriber
import org.assertj.core.api.Assertions.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PhotosStorageImplTest {
    private lateinit var photoDao: PhotosDao
    private lateinit var db: PaginationDatabase
    private lateinit var storage: PhotosStorage
    private val testData = listOf(
        Photo(
            albumId = 1,
            id = 2,
            title = "title2",
            url = "url2",
            thumbnailUrl = "thumbnail2"
        ),
        Photo(
            albumId = 1,
            id = 1,
            title = "title1",
            url = "url1",
            thumbnailUrl = "thumbnail1"
        )
    )

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getTargetContext()
        db = Room.inMemoryDatabaseBuilder(
            context, PaginationDatabase::class.java
        ).build()
        photoDao = db.photosDao()
        storage = PhotosStorageImpl(PAGE_SIZE, Lazy { db })
    }

    @Test
    fun shouldReturnEmptyList() {
        db.clearAllTables()
        val testObserver = TestObserver<List<Photo>>()
        storage.fetch(1)
            .subscribe(testObserver)
        testObserver.run {
            assertComplete()
            assertNoErrors()
            assertValueCount(1)
            assertThat(values()[0]).isEmpty()
        }
    }

    @Test
    fun saveBlockingLessThanPageSize() {
        db.clearAllTables()
        storage.saveBlocking(testData)
        val testObserver = TestObserver<List<Photo>>()
        storage.fetch(1)
            .subscribe(testObserver)
        testObserver.run {
            assertComplete()
            assertNoErrors()
            assertValueCount(1)
            assertThat(values()[0]).isNotEmpty
            assertThat(values()[0]).containsOnlyElementsOf(testData)
        }
    }

    @Test
    fun saveBlockingMoreThanPageSize() {
        db.clearAllTables()

        val testData = (1..2 * PAGE_SIZE).map {
            Photo(
                albumId = it.toLong(),
                id = it.toLong(),
                title = "title$it",
                url = "url$it",
                thumbnailUrl = "thumbnail$it"
            )
        }
        storage.saveBlocking(testData)
        val testObserver = TestSubscriber<List<Photo>>()
        Single.merge(storage.fetch(1), storage.fetch(2))
            .subscribe(testObserver)
        testObserver.run {
            assertComplete()
            assertNoErrors()
            assertValueCount(2)
            val values = values()[0] + values()[1]
            assertThat(values).isNotEmpty
            assertThat(values).containsOnlyElementsOf(testData)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    companion object {
        const val PAGE_SIZE = 100
    }
}