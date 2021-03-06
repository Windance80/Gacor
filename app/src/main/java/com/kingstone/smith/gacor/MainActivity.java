package com.kingstone.smith.gacor;


import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.kingstone.smith.gacor.Gacor.GacorFragment;
import com.kingstone.smith.gacor.HeatSpot.HeatSpotFragment;

public class MainActivity extends AppCompatActivity implements
        HeatSpotFragment.OnFragmentInteractionListener,
        GacorFragment.OnFragmentInteractionListener,
        PlacesFragment.OnFragmentInteractionListener{

//    private TextView mTextMessage;
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private String BOTTOM_NAV_SELECTED_ID = "ID";
    BottomNavigationView mNavigation;

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_gacor:
                    GacorFragment gacorFragment = GacorFragment.newInstance("","");
                    mFragmentManager.beginTransaction().replace(R.id.fragment_container, gacorFragment).commit();
                    return true;
                case R.id.navigation_heatspot:
                    HeatSpotFragment heatSpotFragment = HeatSpotFragment.newInstance("","");
                    mFragmentManager.beginTransaction().replace(R.id.fragment_container, heatSpotFragment).commit();
                    return true;
                case R.id.navigation_crowd:
                    PlacesFragment placesFragment = PlacesFragment.newInstance("", "");
                    mFragmentManager.beginTransaction().replace(R.id.fragment_container, placesFragment).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigation= (BottomNavigationView) findViewById(R.id.navigation);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        mNavigation.setSelectedItemId(R.id.navigation_gacor);
    }
}
