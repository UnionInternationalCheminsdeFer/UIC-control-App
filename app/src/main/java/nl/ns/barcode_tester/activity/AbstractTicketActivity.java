package nl.ns.barcode_tester.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import nl.ns.barcode_tester.database.TicketDatabase;
import nl.ns.barcode_tester.domain.ScanResult;

/**
 * Created by joelhaasnoot on 21/10/2016.
 */

public abstract class AbstractTicketActivity extends AppCompatActivity {

    private TicketDatabase db;
    protected int ticketId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new TicketDatabase(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("id", ticketId);
    }

    public int getTicketId() {
        return ticketId;
    }

    public ScanResult getTicket(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            ticketId = savedInstanceState.getInt("id");
        } else {
            ticketId = getIntent().getIntExtra("id", -1);
        }
        if (ticketId > -1) {
            db.open();
            ScanResult result = db.getTicket(ticketId);
            db.close();
            return result;
        }
        return null;
    }

}
