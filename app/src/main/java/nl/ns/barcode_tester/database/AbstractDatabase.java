package nl.ns.barcode_tester.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Created by joelhaasnoot on 21/10/2016.
 */

public abstract class AbstractDatabase {

    private static final String TAG = AbstractDatabase.class.getSimpleName();

    private Context mContext;
    private DatabaseHelper mHelper;
    protected SQLiteDatabase mDatabase;

    public AbstractDatabase(Context ctx) {
        this.mContext = ctx;
    }

    public AbstractDatabase open() {
        Log.i(TAG, "Opening database");
        mHelper = new DatabaseHelper(mContext);
        mDatabase = mHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDatabase != null) {
            Log.i(TAG, "Closing database");
            mDatabase.close();
            mDatabase = null;
        } else {
            Log.d(TAG, "No database to close");
        }
    }

    protected boolean isDatabaseOpen() {
        return mDatabase != null && mDatabase.isOpen();
    }
}
