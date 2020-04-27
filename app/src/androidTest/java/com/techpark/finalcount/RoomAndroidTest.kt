package com.techpark.finalcount

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.techpark.finalcount.database.dbexpimp.JsonDbExportImportApiKt
import com.techpark.finalcount.database.model.Purchase
import com.techpark.finalcount.database.room.PurchaseDao
import com.techpark.finalcount.database.room.PurchaseDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

//@ExperimentalCoroutinesApi
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
        const val TAG = "TEST ROOM"
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

    @Test
    fun testExportDbToJson(): Unit = runBlocking {
        Truth.assertThat(db.purchaseDao().loadAll()).isEmpty()
        dao.insert(p1)
        Truth.assertThat(db.purchaseDao().loadAll()).isNotEmpty()
        dao.insert(p2)
        Truth.assertThat(db.purchaseDao().loadAll()).isEqualTo(listOf(p1, p2).sortedBy { it.date })

        //TODO:27.04.20_09:30: isEqualTo json string(:)
//        Truth.assertThat(JsonDbExportImportApiImpl.exportPurchaseDbToJson(dao).toString().isEqualTo();
        val json = JsonDbExportImportApiKt.exportPurchaseDbToJson(dao)
        Log.d(TAG, json.toString())
        Truth.assertThat(json.toString())
            .isEqualTo("[{\"cost\":${p1.cost},\"currency\":\"${p1.currency}\",\"date\":${p1.date},\"id\":${p1.id},\"name\":\"${p1.name}\"}," +
                "{\"cost\":${p2.cost},\"currency\":\"${p2.currency}\",\"date\":${p2.date},\"id\":${p2.id},\"name\":\"${p2.name}\"}]")
        dao.clear()
        Truth.assertThat(db.purchaseDao().loadAll()).isEmpty()
    }
}