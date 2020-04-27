package com.techpark.finalcount.database.dbexpimp;

import android.content.Context;

import com.techpark.finalcount.database.model.Purchase;
import com.techpark.finalcount.database.room.PurchaseDao;

import org.json.JSONArray;

import java.util.List;

public interface JsonDbExportImportApi {
    static JSONArray exportPurchaseDbToJson(/*Context context, */PurchaseDao dao) {
        return null;
    }
}
