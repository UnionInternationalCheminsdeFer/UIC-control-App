package nl.ns.barcode_tester.activity.scanner;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import nl.ns.barcode_tester.R;

/**
 * Created by joelhaasnoot on 20/10/2016.
 */

public class ScannerFragmentPagerAdapter extends FragmentPagerAdapter {
    private final Context context;
    private Integer[] tabTitles = new Integer[] { R.string.tab_scanner,
            R.string.tab_tickets};
    private ArrayList<Fragment> fragments = new ArrayList<>();

    public ScannerFragmentPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.context = ctx;
        fragments.add(ScannerFragment.getInstance());
        fragments.add(TicketsFragment.getInstance());
    }

    public void refreshTickets() {
        if (fragments.get(1) instanceof TicketsFragment) {
            TicketsFragment tickets = (TicketsFragment) fragments.get(1);
            tickets.refreshTickets();
        }
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
        return context.getString(tabTitles[position]);
    }
}