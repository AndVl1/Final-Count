package com.techpark.finalcount

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.techpark.finalcount.database.model.Purchase
import com.techpark.finalcount.database.room.PurchaseDao
import com.techpark.finalcount.database.room.PurchaseDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class RoomAndroidTest {
//    private val testDispatcher = TestCoroutineDispatcher()
//    private val testScope = TestCoroutineScope(testDispatcher)
    private lateinit var db : PurchaseDatabase
    private lateinit var dao: PurchaseDao

    companion object {
        var p1 = Purchase(1,
            "Test",
            1000,
            "EUR",
            System.currentTimeMillis()
        )
        var p2 = Purchase(2,
            "Test2",
            3000,
            "RUB",
            System.currentTimeMillis()
        )
    }

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PurchaseDatabase::class.java).build()
        dao = db.purchaseDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun create(): Unit = runBlocking {
        Truth.assertThat(db.purchaseDao().loadAll()).isEmpty()
        dao.insert(p1)
        Truth.assertThat(db.purchaseDao().loadAll()).isNotEmpty()
        dao.insert(p2)
        Truth.assertThat(db.purchaseDao().loadAll()).isEqualTo(listOf(p1, p2).sortedBy { it.date })
        p1 = Purchase(1, "change", 200, "EUR", System.currentTimeMillis())
        dao.update(p1)
        Truth.assertThat(db.purchaseDao().loadAll()).isEqualTo(listOf(p1, p2).sortedBy { it.date })
        dao.delete(p2)
        Truth.assertThat(db.purchaseDao().loadAll()).hasSize(1)
        dao.clear()
        Truth.assertThat(db.purchaseDao().loadAll()).isEmpty()
    }
}