package com.ats.gate_sale_monginis.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ats.gate_sale_monginis.R;
import com.ats.gate_sale_monginis.bean.CartListData;
import com.ats.gate_sale_monginis.bean.LoginData;
import com.ats.gate_sale_monginis.constants.Constants;
import com.ats.gate_sale_monginis.fragment.AddDiscountFragment;
import com.ats.gate_sale_monginis.fragment.AddOtherItemFragment;
import com.ats.gate_sale_monginis.fragment.AddSupplierFragment;
import com.ats.gate_sale_monginis.fragment.AddUserFragment;
import com.ats.gate_sale_monginis.fragment.CartFragment;
import com.ats.gate_sale_monginis.fragment.CashCollectionFragment;
import com.ats.gate_sale_monginis.fragment.DiscountMasterFragment;
import com.ats.gate_sale_monginis.fragment.EmployeeListFragment;
import com.ats.gate_sale_monginis.fragment.EmployeeProductsFragment;
import com.ats.gate_sale_monginis.fragment.HomeFragment;
import com.ats.gate_sale_monginis.fragment.MonthEndFragment;
import com.ats.gate_sale_monginis.fragment.OtherBillsFragment;
import com.ats.gate_sale_monginis.fragment.OtherItemMasterFragment;
import com.ats.gate_sale_monginis.fragment.PrinterIPFragment;
import com.ats.gate_sale_monginis.fragment.ProductSellFragmentFragment;
import com.ats.gate_sale_monginis.fragment.SupplierMasterFragment;
import com.ats.gate_sale_monginis.fragment.UserMasterFragment;
import com.ats.gate_sale_monginis.fragment.ViewBillsFragment;
import com.ats.gate_sale_monginis.interfaces.InterfaceApi;
import com.ats.gate_sale_monginis.util.PermissionUtil;
import com.google.gson.Gson;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    public static TextView tvTitle, tvCartCount;
    public static LinearLayout llCart;
    private LinearLayout llMenuHome, llApproveBills, llMenuOtherBills, llUserMaster, llDiscount, llLogout, llCollectAmount, llProductSell, llSupplierMaster, llOtherItemMaster, llMonthEnd, llPrinterIP;

    int userId, userType;
    String userName, mob, pwd;

    public static ArrayList<CartListData> cartArray = new ArrayList<>();

    int fcmType = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        if (PermissionUtil.checkAndRequestPermissions(this)) {

        }

        SortedMap<String, Charset> map = Charset.availableCharsets();
        Set<String> keys = map.keySet();
        if (keys != null) {
            Iterator<String> iterator = keys.iterator();
            while (iterator.hasNext()) {
                Object key = iterator.next();
                Log.i("AppStart", "key " + key);
            }
        }

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        switch (wifiManager.getWifiState()) {
            case WifiManager.WIFI_STATE_DISABLED:
                wifiManager.setWifiEnabled(true);
                break;
            default:
                break;
        }

        if (!isGpsEnable(this))
            enableGps(this);
        getLocation();


        tvTitle = toolbar.findViewById(R.id.tvTitle);
        llCart = toolbar.findViewById(R.id.llCartLayout);
        tvCartCount = toolbar.findViewById(R.id.tvCartCount);

        llCart.setOnClickListener(this);

        llMenuHome = findViewById(R.id.llMenuHome);
        llApproveBills = findViewById(R.id.llMenuApproveBills);
        llMenuOtherBills = findViewById(R.id.llMenuOtherBills);
        llUserMaster = findViewById(R.id.llUserMaster);
        llDiscount = findViewById(R.id.llMenuDiscountMaster);
        llLogout = findViewById(R.id.llMenuLogout);
        llCollectAmount = findViewById(R.id.llMenuCollectAmount);
        llProductSell = findViewById(R.id.llMenuProductSell);
        llSupplierMaster = findViewById(R.id.llMenuSupplierMaster);
        llOtherItemMaster = findViewById(R.id.llMenuOtherItemMaster);
        llMonthEnd = findViewById(R.id.llMenuMonthEnd);
        llPrinterIP = findViewById(R.id.llMenuPrinterIP);

        llCollectAmount.setOnClickListener(this);
        llMenuHome.setOnClickListener(this);
        llApproveBills.setOnClickListener(this);
        llMenuOtherBills.setOnClickListener(this);
        llUserMaster.setOnClickListener(this);
        llDiscount.setOnClickListener(this);
        llLogout.setOnClickListener(this);
        llProductSell.setOnClickListener(this);
        llSupplierMaster.setOnClickListener(this);
        llOtherItemMaster.setOnClickListener(this);
        llMonthEnd.setOnClickListener(this);
        llPrinterIP.setOnClickListener(this);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        Gson gson = new Gson();
        String json2 = pref.getString("loginData", "");
        LoginData userBean = gson.fromJson(json2, LoginData.class);
        //Log.e("User Bean : ", "---------------" + userBean);
        try {

            if (userBean != null) {
                userId = userBean.getUserId();
                userName = userBean.getUserName();
                userType = userBean.getUserType();
                mob = userBean.getMobileNumber();
                pwd = userBean.getPassword();

            } else {
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        } catch (Exception e) {
            //Log.e("HomeActivity : ", " Exception : " + e.getMessage());
        }

        try {
            //Log.e("FCM", "-----------------------------------------" + getIntent().getIntExtra("FcmTitle", 0));
            fcmType = getIntent().getIntExtra("FcmTag", 0);
        } catch (Exception e) {
            //Log.e("HomeActivity : ", " FCM Exception : " + e.getMessage());
            e.printStackTrace();
            fcmType = 0;
        }


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView tvName = navigationView.findViewById(R.id.tvHeader_Name);
        TextView tvType = navigationView.findViewById(R.id.tvHeader_Type);
        tvName.setText("" + userName);
        if (userType == 1) {
            tvType.setText("( Initiator )");
            llProductSell.setVisibility(View.GONE);
            llCollectAmount.setVisibility(View.GONE);
            llUserMaster.setVisibility(View.GONE);
            llDiscount.setVisibility(View.GONE);
            llSupplierMaster.setVisibility(View.GONE);
            llOtherItemMaster.setVisibility(View.GONE);
            llMonthEnd.setVisibility(View.GONE);
        } else if (userType == 2) {
            tvType.setText("( Approver )");
            llApproveBills.setVisibility(View.GONE);
            //llCollectAmount.setVisibility(View.GONE);
            llSupplierMaster.setVisibility(View.GONE);
            llOtherItemMaster.setVisibility(View.GONE);
            llUserMaster.setVisibility(View.GONE);
            llMonthEnd.setVisibility(View.GONE);
        } else if (userType == 3) {
            tvType.setText("( Collector )");
            llCollectAmount.setVisibility(View.GONE);
        }

        if (fcmType == 0) {
            if (savedInstanceState == null) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new HomeFragment(), "Home");
                ft.commit();
            }
        } else if (fcmType == 1) {
            Fragment adf = new ViewBillsFragment();
            Bundle args = new Bundle();
            args.putInt("TabNo", 0);
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();
        } else if (fcmType == 2) {
            Fragment adf = new ViewBillsFragment();
            Bundle args = new Bundle();
            args.putInt("TabNo", 1);
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();
        } else if (fcmType == 3) {
            Fragment adf = new ViewBillsFragment();
            Bundle args = new Bundle();
            args.putInt("TabNo", 2);
            adf.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, adf, "HomeFragment").commit();
        }


    }

    @Override
    public void onBackPressed() {

        Fragment home = getSupportFragmentManager().findFragmentByTag("Home");
        Fragment homeFragment = getSupportFragmentManager().findFragmentByTag("HomeFragment");
        Fragment employeeListFragment = getSupportFragmentManager().findFragmentByTag("EmployeeListFragment");
        Fragment userMasterFragment = getSupportFragmentManager().findFragmentByTag("UserMasterFragment");
        Fragment discountMasterFragment = getSupportFragmentManager().findFragmentByTag("DiscountMasterFragment");
        Fragment productFragment = getSupportFragmentManager().findFragmentByTag("ProductFragment");
        Fragment supplierMasterFragment = getSupportFragmentManager().findFragmentByTag("SupplierMasterFragment");
        Fragment otherItemMasterFragment = getSupportFragmentManager().findFragmentByTag("OtherItemMasterFragment");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (home instanceof HomeFragment && home.isVisible()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //builder.setTitle("Confirm Action");
            builder.setMessage("Exit Application ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (homeFragment instanceof CartFragment && homeFragment.isVisible() ||
                homeFragment instanceof ViewBillsFragment && homeFragment.isVisible() ||
                homeFragment instanceof OtherBillsFragment && homeFragment.isVisible() ||
                homeFragment instanceof SupplierMasterFragment && homeFragment.isVisible() ||
                homeFragment instanceof OtherItemMasterFragment && homeFragment.isVisible() ||
                homeFragment instanceof UserMasterFragment && homeFragment.isVisible() ||
                homeFragment instanceof DiscountMasterFragment && homeFragment.isVisible() ||
                homeFragment instanceof CashCollectionFragment && homeFragment.isVisible() ||
                homeFragment instanceof MonthEndFragment && homeFragment.isVisible() ||
                homeFragment instanceof ProductSellFragmentFragment && homeFragment.isVisible() ||
                homeFragment instanceof PrinterIPFragment && homeFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new HomeFragment(), "Home");
            ft.commit();
        } else if (userMasterFragment instanceof AddUserFragment && userMasterFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new UserMasterFragment(), "HomeFragment");
            ft.commit();
        } else if (supplierMasterFragment instanceof AddSupplierFragment && supplierMasterFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new SupplierMasterFragment(), "HomeFragment");
            ft.commit();
        } else if (otherItemMasterFragment instanceof AddOtherItemFragment && otherItemMasterFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new OtherItemMasterFragment(), "HomeFragment");
            ft.commit();
        } else if (discountMasterFragment instanceof AddDiscountFragment && discountMasterFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DiscountMasterFragment(), "HomeFragment");
            ft.commit();
        } else if (productFragment instanceof CartFragment && productFragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new EmployeeProductsFragment(), "HomeFragment");
            ft.commit();
        } else if (homeFragment instanceof EmployeeProductsFragment && homeFragment.isVisible() ||
                homeFragment instanceof EmployeeListFragment && homeFragment.isVisible()) {
            if (userType == 1) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new HomeFragment(), "Home");
                ft.commit();
            } else {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, new ProductSellFragmentFragment(), "HomeFragment");
                ft.commit();
            }

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new CartFragment(), "HomeFragment");
            ft.commit();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.llMenuHome) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new HomeFragment(), "Home");
            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llMenuApproveBills) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ViewBillsFragment(), "HomeFragment");
            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llMenuOtherBills) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new OtherBillsFragment(), "HomeFragment");
            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llMenuSupplierMaster) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new SupplierMasterFragment(), "HomeFragment");
            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llMenuOtherItemMaster) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new OtherItemMasterFragment(), "HomeFragment");
            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llUserMaster) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new UserMasterFragment(), "HomeFragment");
            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llMenuDiscountMaster) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new DiscountMasterFragment(), "HomeFragment");
            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llMenuCollectAmount) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new CashCollectionFragment(), "HomeFragment");
            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llMenuMonthEnd) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new MonthEndFragment(), "HomeFragment");
            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llMenuProductSell) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new ProductSellFragmentFragment(), "HomeFragment");
            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llMenuPrinterIP) {

            startActivity(new Intent(this, PrinterSettingsActivity.class));
//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.replace(R.id.content_frame, new PrinterIPFragment(), "HomeFragment");
//            ft.commit();

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);

        } else if (view.getId() == R.id.llCartLayout) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, new CartFragment(), "ProductFragment");
            ft.commit();

        } else if (view.getId() == R.id.llMenuLogout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this, R.style.AlertDialogTheme);
            builder.setTitle("Logout");
            builder.setMessage("Are you sure you want to logout?");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.MY_PREF, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.commit();

                    Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            });
            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


    private boolean isGpsEnable(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
    }

    private void enableGps(final Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 0);
    }

    private Location getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            return locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            return locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        } else {
            return null;
        }
    }

}