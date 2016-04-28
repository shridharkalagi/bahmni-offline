package org.bahmni.offline.dbServices.dao;

import android.content.ContentValues;
import android.database.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import org.bahmni.offline.Constants;
import org.json.JSONException;
import org.json.JSONObject;


public class VisitDbService {
    private DbHelper mDBHelper;

    public VisitDbService(DbHelper mDBHelper) {
        this.mDBHelper = mDBHelper;
    }

    public JSONObject insertVisitData(JSONObject visitData) throws JSONException {
        SQLiteDatabase db = mDBHelper.getWritableDatabase(Constants.KEY);
        ContentValues values = new ContentValues();
        values.put("uuid", visitData.getString("uuid"));
        values.put("patientUuid", visitData.getJSONObject("patient").getString("uuid"));
        values.put("visitJson", String.valueOf(visitData));
        db.insertWithOnConflict("visit", null, values, SQLiteDatabase.CONFLICT_REPLACE);
        return  visitData;
    }

    public JSONObject getVisitByUuid(String uuid) throws JSONException {
        SQLiteDatabase db = mDBHelper.getReadableDatabase(Constants.KEY);
        Cursor c = db.rawQuery("SELECT * from visit" +
                " WHERE uuid = '" + uuid + "'" , new String[]{});
        if (c.getCount() < 1) {
            c.close();
            return null;
        }
        c.moveToFirst();
        JSONObject visitJson = new JSONObject();
        visitJson.put("visitJson", new JSONObject(c.getString(c.getColumnIndex("visitJson"))));
        visitJson.put("uuid", c.getString(c.getColumnIndex("uuid")));
        visitJson.put("patientUuid", c.getString(c.getColumnIndex("patientUuid")));
        c.close();
        return visitJson;
    }
}