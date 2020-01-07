package nl.ns.barcode_tester.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.open918.lib.UicTicketParser;
import org.open918.lib.domain.Ticket;

import java.nio.charset.Charset;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.domain.ScanResult;
import nl.ns.barcode_tester.util.Base64Util;
import nl.ns.barcode_tester.util.TicketSerializer;

public class TicketDatabase extends AbstractDatabase {

    private static final String TAG = TicketDatabase.class.getSimpleName();

    private static final Gson gson = new GsonBuilder().registerTypeHierarchyAdapter(Ticket.class, new TicketSerializer()).create();

    private static final String TABLE = "tickets";
    public static final String COLUMN_ID = "id as _id";
    public static final String COLUMN_RAW = "raw";
    public static final String COLUMN_JSON = "json";
    public static final String COLUMN_CREATED = "created";
    public static final String COLUMN_UPDATED = "updated";
    private final Context ctx;

    public TicketDatabase(Context ctx) {
        super(ctx);
        this.ctx = ctx;
    }

    public boolean insertTicket(byte[] raw, Ticket t) {
        if (!isDatabaseOpen()) {
            return false;
        }

        ContentValues newTicket = new ContentValues();
        newTicket.put(COLUMN_RAW, raw);

        try {
            long ret = mDatabase.insertOrThrow(TABLE, null, newTicket);
        } catch (SQLiteConstraintException constraintEx) {
            Log.i(TAG, "Updating ticket as last updated just now", constraintEx);
            try {
                mDatabase.execSQL(ctx.getString(R.string.sql_update_ticket_updated_now), new Object[]{raw});
            } catch (SQLiteException updateEx) {
                Log.e(TAG, "Error updating existing ticket", updateEx);
                return false;
            }
            return true;
        } catch (SQLiteException e) {
            Log.e(TAG, "Failed to write ticket to database", e);
            return false;
        }
        Log.i(TAG, "Wrote ticket to database succesfully");
        return true;
    }

    public Cursor getTickets() {
        if (!isDatabaseOpen()) {
            return null;
        }
        return mDatabase.query(TABLE, new String[]{COLUMN_ID, COLUMN_RAW, COLUMN_JSON, COLUMN_CREATED, COLUMN_UPDATED }, null, null, null, null, COLUMN_UPDATED+ " DESC");
    }

    public ScanResult getTicket(int ticketId) {
        if (!isDatabaseOpen()) {
            return null;
        }
        Cursor cur = mDatabase.query(TABLE, new String[]{COLUMN_ID, COLUMN_RAW, COLUMN_JSON, COLUMN_CREATED, COLUMN_UPDATED }, "id = ?", new String[] { String.valueOf(ticketId) }, null, null, null);
        if (cur.getCount() == 0) {
            return null;
        }
        cur.moveToFirst();
        ScanResult result = fromCursor(cur);
        cur.close();
        return result;
    }

    public static ScanResult fromCursor(Cursor cur) {
        byte[] raw = cur.getBlob(cur.getColumnIndex(COLUMN_RAW));
        Ticket ticket = null;
        try {
            ticket = UicTicketParser.decode(raw, false);
        } catch (Exception e) {
            Log.e(TAG, "Error reading data", e);
        }
        return new ScanResult(raw, new String(raw), ticket);
    }

    public void deleteTickets() {
        if (!isDatabaseOpen()) {
            return;
        }
        mDatabase.delete(TABLE, null, null);
    }
}
