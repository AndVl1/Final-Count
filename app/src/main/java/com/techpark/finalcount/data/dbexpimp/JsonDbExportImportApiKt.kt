package com.techpark.finalcount.data.dbexpimp

import android.Manifest
import android.content.Context
import android.util.Log
import com.google.gson.GsonBuilder
import com.opencsv.CSVWriter
import com.techpark.finalcount.data.room.PurchaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.FileWriter
import java.util.*

object JsonDbExportImportApiKt {
	suspend fun exportPurchaseDbToJsonArray(dao: PurchaseDao): JSONArray {
		var resultSet: JSONArray? = null
		val ioScope = CoroutineScope(Dispatchers.IO)
		try {
			val gson = GsonBuilder()
				.setPrettyPrinting()
				.create()
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

	fun saveCsv(root: String, jsonArray: JSONArray) {
		val date = Date(System.currentTimeMillis())
		val fileDir = "FinalCount"
		val dir = File(root, fileDir)
		if (!dir.exists())
			dir.mkdir()
		val file = File(dir, "${date.day}-${date.month}-${date.year}.csv")
		file.createNewFile()

		val fileWriter = FileWriter(file)

		for (i in 0 until jsonArray.length()) {
			val obj = jsonArray[i] as JSONObject
			val next =
				"\"${obj["id"]}\", \"${obj["name"]}\", \"${obj["cost"]}\", \"${Date(obj["date"] as Long)}\"\n"
			fileWriter.write(next)
		}
		fileWriter.flush()
		fileWriter.close()

	}


	private const val TAG = "JsonDbExportImportApi"
}