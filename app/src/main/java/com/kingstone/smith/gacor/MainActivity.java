package com.kingstone.smith.gacor;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements
        HeatSpot.OnFragmentInteractionListener, GacorFragment.OnFragmentInteractionListener{

    private TextView mTextMessage;
    private FragmentManager mFragmentManager = getSupportFragmentManager();

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
                    HeatSpot heatSpot = HeatSpot.newInstance("","");
                    mFragmentManager.beginTransaction().replace(R.id.fragment_container, heatSpot).commit();

                    return true;
//                case R.id.navigation_notifications:
//                    mTextMessage.setText(R.string.title_notifications);
//                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mTextMessage = (TextView) findViewById(R.id.message);
//        findViewById(R.id.co)
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
