package com.techpark.finalcount.database.dbexpimp

import android.util.Log
import com.google.gson.GsonBuilder
import com.techpark.finalcount.database.room.PurchaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray

interface JsonDbExportImportApiKt {
    companion object{
        suspend fun exportPurchaseDbToJson(dao: PurchaseDao): JSONArray {
            var resultSet: JSONArray? = null
            val ioScope = CoroutineScope(Dispatchers.IO)
//        setUpDb(context);
            //        setUpDb(context);
            try {
                val gson = GsonBuilder().setPrettyPrinting().create()
                ioScope.launch {
                    val jsonString = gson.toJson(
                        dao.loadAll()
                    )
                    resultSet = JSONArray(jsonString)
                }.join()
            } catch (ex: Exception) {
                Log.e(TAG, ex.toString())
            }
            return resultSet!!
        }

        private const val TAG = "JsonDbExportImportApi"
    }
}