package nl.ns.barcode_tester.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import nl.ns.barcode_tester.R;

/**
 * Created by joelhaasnoot on 21/10/2016.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String TAG = DatabaseHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "tickets.db";
    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Creating database");
        execMultiline(db, context.getString(R.string.sql_db_create));
    }

    private void execMultiline(SQLiteDatabase db, String statements) {
        for (String statement : statements.split(";")) {
            db.execSQL(statement);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "Upgrade from "+oldVersion+" to "+ newVersion);
        if (newVersion == 2 || newVersion == 3 || newVersion == 4) {
            execMultiline(db, context.getString(R.string.sql_db_wipe));
            execMultiline(db, context.getString(R.string.sql_db_create));
        }
    }

    public static SQLiteDatabase open(Context ctx) {
        Log.i(TAG, "Open writeable database");
        return new DatabaseHelper(ctx).getWritableDatabase();
    }
}
