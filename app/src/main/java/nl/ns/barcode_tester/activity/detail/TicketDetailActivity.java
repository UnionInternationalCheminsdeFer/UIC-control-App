package nl.ns.barcode_tester.activity.detail;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.open918.lib.domain.Ticket;
import org.open918.lib.domain.TicketStandard;

import nl.ns.barcode_tester.activity.ShareClickListener;
import nl.ns.barcode_tester.activity.AbstractTicketActivity;
import nl.ns.barcode_tester.activity.settings.SettingsActivity;
import nl.ns.barcode_tester.activity.table.FieldsTableActivity;
import nl.ns.barcode_tester.domain.ScanResult;
import nl.ns.barcode_tester.R;

import java.util.ArrayList;
import java.util.List;


public class TicketDetailActivity extends AbstractTicketActivity {

    private static final String TAG = TicketDetailActivity.class.getSimpleName();

    private ScanResult result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.header_ticket_detail);
        setContentView(R.layout.activity_ticket_detail);

        result = getTicket(savedInstanceState);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        if (result != null && result.isReadable()) {
            viewPager.setAdapter(new DetailsFragmentPagerAdapter(getSupportFragmentManager(), result.getTicket()));
            tabLayout.setupWithViewPager(viewPager);
        } else {
            viewPager.setVisibility(View.GONE);
            tabLayout.setVisibility(View.GONE);

            // TODO: Determine if this is ever triggered
            CardView card = (CardView) findViewById(R.id.card_unknown);
            card.setVisibility(View.VISIBLE);

            Button shareButton = (Button) findViewById(R.id.button_share);
            ShareClickListener share = new ShareClickListener(this, result);
            shareButton.setOnClickListener(share);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_share:
                new ShareClickListener(getApplicationContext(), result).onClick(null);
                return true;
            case R.id.action_table:
                Intent start = new Intent(getApplicationContext(), FieldsTableActivity.class);
                start.putExtra("id", ticketId);
                startActivity(start);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ScanResult getScanResult() {
        return result;
    }

    public class DetailsFragmentPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();

        public DetailsFragmentPagerAdapter(FragmentManager fm, Ticket t) {
            super(fm);
            if (t.getStandard() == TicketStandard.TICKET918_3) {
                fragments.add(Properties9183HomeprintTicketFragment.getInstance());
                fragments.add(ContentsFragment.getInstance());
            } else {
                fragments.add(Properties9182CreditCardTicketFragment.getInstance());
            }
            //fragments.add(BarcodeFragment.getInstance());
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Fragment frg = fragments.get(position);
            if (frg instanceof TabFragment) {
                return getString(((TabFragment) frg).getTitleResource());
            }
            return "?";
        }
    }

}
