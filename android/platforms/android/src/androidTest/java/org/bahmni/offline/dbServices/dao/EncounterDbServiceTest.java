package org.bahmni.offline.dbServices.dao;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;
import net.sqlcipher.database.SQLiteDatabase;
import org.bahmni.offline.Constants;
import org.bahmni.offline.MainActivity;
import org.bahmni.offline.Utils.TestUtils;
import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

public class EncounterDbServiceTest extends ActivityInstrumentationTestCase2<MainActivity>{

    public EncounterDbServiceTest() throws KeyManagementException, NoSuchAlgorithmException, IOException {
        super(MainActivity.class);
    }

    @Test
    public void testShouldInsertEncountersAndGetEncounterByPatientUuid() throws Exception {

        Context context = getInstrumentation().getTargetContext();
        SQLiteDatabase.loadLibs(context);

        String uuid = "1c5c237a-dc6e-4f4f-bcff-c761c1ae5972";
        String patientUuid = "fc6ede09-f16f-4877-d2f5-ed8b2182ec11";
        DateTime encounterDateTime = new DateTime("2016-04-22T11:06:20.000+0530");
        DbHelper mDBHelper = new DbHelper(context, context.getFilesDir() + "/Bahmni.db");
        mDBHelper.createTable(Constants.CREATE_ENCOUNTER_TABLE);

        EncounterDbService encounterDbService = new EncounterDbService(mDBHelper);
        String encounterJson = TestUtils.readFileFromAssets("encounter.json", getInstrumentation().getContext());

        encounterDbService.insertEncounterData(new JSONObject(encounterJson));

        JSONArray encounters = encounterDbService.getEncountersByPatientUuid(patientUuid);

        assertEquals(uuid, encounters.getJSONObject(0).getJSONObject("encounter").getString("encounterUuid"));
        assertEquals(patientUuid, encounters.getJSONObject(0).getJSONObject("encounter").getString("patientUuid"));
        assertEquals(encounterDateTime, new DateTime(encounters.getJSONObject(0).getJSONObject("encounter").getString("encounterDateTime")));

    }
}
