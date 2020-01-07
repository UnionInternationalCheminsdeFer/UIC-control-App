package nl.ns.barcode_tester.activity.table;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import org.open918.lib.domain.TicketStandard;
import org.open918.lib.domain.uic918_3.Ticket918Dash3;
import org.open918.lib.domain.uic918_3.TicketField;

import java.util.ArrayList;
import java.util.List;

import nl.ns.barcode_tester.activity.AbstractTicketActivity;
import nl.ns.barcode_tester.domain.ScanResult;
import nl.ns.barcode_tester.R;

public class FieldsTableActivity extends AbstractTicketActivity {

    private RecyclerView mRecyclerView;
    private TicketFieldsAdapter mAdapter;

    private ScanResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fields_table);

        result = getTicket(savedInstanceState);

        List<TicketField> fields = new ArrayList<>();
        if (result != null && result.isReadable() && result.getTicket().getStandard() == TicketStandard.TICKET918_3) {
            fields = ((Ticket918Dash3) result.getTicket()).getContents().getFields();
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.list_fields);
        if (fields.size() > 0) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mAdapter = new TicketFieldsAdapter(this, fields);
            mRecyclerView.setAdapter(mAdapter);
        } else {
            TextView noFields = (TextView) findViewById(R.id.label_fields_none);
            noFields.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }

    }

}
