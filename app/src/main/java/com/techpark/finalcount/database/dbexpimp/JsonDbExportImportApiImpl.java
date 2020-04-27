package com.techpark.finalcount.database.dbexpimp;

//import android.content.Context;
import android.util.Log;
//import androidx.room.Room;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
//import java.util.List;
//import com.techpark.finalcount.database.model.Purchase;
import com.techpark.finalcount.database.room.PurchaseDao;
//import com.techpark.finalcount.database.room.PurchaseDatabase;

public class JsonDbExportImportApiImpl implements JsonDbExportImportApi {

    private static final String TAG = "JsonDbExportImportApi";

//    private PurchaseDatabase db;
//    private PurchaseDao dao;
//
//    void setUpDb(Context context) {
//        db = Room.inMemoryDatabaseBuilder(context, PurchaseDatabase.class).build();
//        dao = db.purchaseDao();
//    }
//
//    void tearDownDb() {
//        db.close();
//    }

    public static JSONArray exportPurchaseDbToJson(/*Context context, */PurchaseDao dao) {
        JSONArray resultSet = null;
//        setUpDb(context);
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create(); //new Gson();
//            String jsonString = gson.toJson(dao.loadAllAsync());
            String jsonString = gson.toJson(dao.loadAll(
                    //https://stackoverflow.com/questions/52869672/call-kotlin-suspend-function-in-java-class (:)
                    (new CoroutinesAdapter()).getContinuation(
                    (funcResult, throwable) -> {
                        Log.i(TAG, "Coroutines finished");
                        Log.i(TAG, "Result: " + funcResult);
                        Log.e(TAG, "Exception: " + throwable);
            })));
            resultSet = new JSONArray(jsonString);
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
//        } finally {
//            tearDownDb();
        }
        return resultSet;
    }
}
