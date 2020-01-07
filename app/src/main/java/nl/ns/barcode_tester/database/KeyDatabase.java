package nl.ns.barcode_tester.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import nl.ns.barcode_tester.domain.Key;

/**
 * Created by joelhaasnoot on 21/10/2016.
 */

public class KeyDatabase extends AbstractDatabase {

    private static final String TAG = KeyDatabase.class.getSimpleName();

    private static final String TABLE = "keys";
    public static final String COLUMN_ID = "id as _id";
    public static final String COLUMN_CARRIER = "carrier";
    public static final String COLUMN_KEY_ID = "key_id";
    public static final String COLUMN_KEY = "key";
    public static final String COLUMN_CREATED = "created";

    public KeyDatabase(Context ctx) {
        super(ctx);
    }

    public Cursor getKeys() {
        if (!isDatabaseOpen()) {
            return null;
        }
        return mDatabase.query(TABLE, new String[]{COLUMN_ID, COLUMN_CARRIER, COLUMN_KEY_ID, COLUMN_KEY, COLUMN_CREATED },
                null, null, null, null, COLUMN_CARRIER+ " DESC, "+COLUMN_CREATED+ " DESC");
    }

    public Key getKey(int carrier, String keyId) {
        Cursor result = mDatabase.query(TABLE, new String[]{COLUMN_ID, COLUMN_CARRIER, COLUMN_KEY_ID, COLUMN_KEY, COLUMN_CREATED },
                COLUMN_CARRIER +"= ? AND "+ COLUMN_KEY_ID +" = ?", new String[]{ String.valueOf(carrier), keyId },
                null, null, null);
        if (result.getCount() == 0) {
            return null;
        }
        result.moveToFirst();
        return keyFromCursor(result);
    }

    public void insertKey(String carrier, String key, String cert) {
        if (!isDatabaseOpen()) {
            return;
        }
        ContentValues newKey = new ContentValues();
        newKey.put(COLUMN_CARRIER, carrier);
        newKey.put(COLUMN_KEY_ID, key);
        newKey.put(COLUMN_KEY, cert);

        long id = mDatabase.insertOrThrow(TABLE, null, newKey);
        Log.i(TAG, "Inserted key to database succesfully, id = "+ id);
    }

    public void deleteKey(int id) {
        if (!isDatabaseOpen()) {
            return;
        }
        mDatabase.delete(TABLE, "id = ?", new String[]{ String.valueOf(id) });
    }

    public static Key keyFromCursor(Cursor row) {
        return new Key(row.getInt(row.getColumnIndex("_id")),
                row.getInt(row.getColumnIndex(KeyDatabase.COLUMN_CARRIER)),
                row.getString(row.getColumnIndex(KeyDatabase.COLUMN_KEY_ID)),
                row.getString(row.getColumnIndex(KeyDatabase.COLUMN_KEY)));
    }
}
