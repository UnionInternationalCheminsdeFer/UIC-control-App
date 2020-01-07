package nl.ns.barcode_tester.activity.scanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.open918.lib.domain.Ticket;

import nl.ns.barcode_tester.R;
import nl.ns.barcode_tester.activity.detail.TicketDetailActivity;
import nl.ns.barcode_tester.database.TicketDatabase;


public class TicketsFragment extends Fragment implements ScannerActivity.OnScanEvent, TicketsAdapter.OnTicketClickListener {

    private static final String TAG = TicketsFragment.class.getSimpleName();

    private TicketDatabase db;
    private TicketsAdapter ticketsAdapter;

    private boolean ticketsHidden = false;
    private RecyclerView mRecyclerList;
    private TextView mMoFields;

    public static TicketsFragment getInstance() {
        return new TicketsFragment();
    }

    public TicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new TicketDatabase(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tickets, container, false);
        Cursor cTickets = db.getTickets();
        ticketsAdapter = new TicketsAdapter(getActivity(), cTickets);
        ticketsAdapter.setOnTicketClickListener(this);

        mRecyclerList = (RecyclerView) v.findViewById(R.id.list_tickets);
        mMoFields = (TextView) v.findViewById(R.id.label_tickets_none);
        checkTicketView(cTickets);

        return v;
    }

    private void checkTicketView(Cursor cTickets) {
        if (cTickets != null && cTickets.getCount() != 0) {
            mRecyclerList.setAdapter(ticketsAdapter);

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerList.setLayoutManager(mLayoutManager);
            mRecyclerList.setItemAnimator(new DefaultItemAnimator());
            mRecyclerList.setVisibility(View.VISIBLE);
            mMoFields.setVisibility(View.GONE);
            ticketsHidden = false;
        } else {
            mRecyclerList.setVisibility(View.GONE);
            mMoFields.setVisibility(View.VISIBLE);
            ticketsHidden = true;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() != null && getActivity() instanceof ScannerActivity) {
            ((ScannerActivity) getActivity()).registerForScanEvent(this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (db != null) {
            db.close();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (db != null) {
            db.open();
        }
        getLatestTickets();
    }

    @Override
    public void onScanEvent() {
        getLatestTickets();
    }

    private void getLatestTickets() {
        if (db == null) {
            db = new TicketDatabase(getContext());
        }
        Cursor c = db.getTickets();
        if (c != null) {
            ticketsAdapter.swapCursor(c);
            checkTicketView(c);
        }
    }

    @Override
    public void onTicketClick(int id, Ticket t) {
        Intent in = new Intent(getActivity(), TicketDetailActivity.class);
        in.putExtra("id", id);
        startActivity(in);
    }

    public void refreshTickets() {
        getLatestTickets();
    }
}
