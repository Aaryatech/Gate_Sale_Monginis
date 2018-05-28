package com.ats.gate_sale_monginis.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.interfaces.ApprovedBillInterface;
import com.ats.gate_sale_monginis.interfaces.PendingBillInterface;
import com.ats.gate_sale_monginis.interfaces.RejectedBillInterface;

import static android.content.Context.MODE_PRIVATE;
import static com.ats.gate_sale_monginis.activity.HomeActivity.cartArray;
import static com.ats.gate_sale_monginis.activity.HomeActivity.llCart;
import static com.ats.gate_sale_monginis.activity.HomeActivity.tvTitle;


public class ViewBillsFragment extends Fragment {

    public TabLayout tabLayout;
    public ViewPager viewPager;

    FragmentPagerAdapter adapterViewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_bills, container, false);
        tvTitle.setText("Bills");


        tabLayout = (TabLayout) view.findViewById(R.id.tab);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        adapterViewPager = new ViewPagerAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapterViewPager);

        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

        try {
            int tabNo = getArguments().getInt("TabNo");
            //Log.e(" VIEW BILLS ", "-----------------------" + tabNo);
            viewPager.setCurrentItem(tabNo);
        } catch (Exception e) {
            //Log.e(" VIEW BILLS ", "------------ERROR-----------" + e.getMessage());
            e.printStackTrace();
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    PendingBillInterface fragmentPending = (PendingBillInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentPending != null) {
                        fragmentPending.fragmentBecameVisible();
                    }
                } else if (position == 1) {
                    ApprovedBillInterface fragmentApproved = (ApprovedBillInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentApproved != null) {
                        fragmentApproved.fragmentBecameVisible();
                    }
                } else if (position == 2) {
                    RejectedBillInterface fragmentRejected = (RejectedBillInterface) adapterViewPager.instantiateItem(viewPager, position);
                    if (fragmentRejected != null) {
                        fragmentRejected.fragmentBecameVisible();
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });


        return view;
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new PendingBillsFragment();
                case 1:
                    return new ApproveBillsFragment();
                case 2:
                    return new RejectedBillsFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Pending";
                case 1:
                    return "Approved";
                case 2:
                    return "Rejected";
                default:
                    return null;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        SharedPreferences pref = getContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        editor.putString("category", "");
        editor.putInt("categoryId", 0);
        editor.putInt("eId", 0);
        editor.putInt("monthlyLimit", 0);
        editor.putInt("yearlyLimit", 0);
        editor.putFloat("catDiscount", 0);
        editor.putFloat("monthlyConsumed", 0);
        editor.putFloat("yearlyConsumed", 0);
        editor.apply();

        llCart.setVisibility(View.INVISIBLE);
        cartArray.clear();
    }
}
