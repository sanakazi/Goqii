package com.ftgoqiiact.viewmodel.activities;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.InputMethodManager;

import com.ftgoqiiact.R;
import com.ftgoqiiact.viewmodel.adapters.ActivitiesAdapterNew;
import com.ftgoqiiact.viewmodel.fragments.ActivitiesFragmentNew;
import com.ftgoqiiact.viewmodel.fragments.ActivityMapFragment;
import com.ftgoqiiact.viewmodel.utils.Utilities;

import java.text.DecimalFormat;

public class MapActivity extends AppCompatActivity implements Utilities.BookingSuccessfulListener,
        ActivityMapFragment.OnFragmentInteractionListener,
        ActivitiesAdapterNew.ActivitySelectedListener{

    public static final String LOCATION = "LOCATION";
    public static final String CAT_ID = "CAT_ID";
    private static final String ACT_MAP_FRAGMENT = "ACT_MAP_FRAGMENT";
    private static final String ACT_FRAGMENT ="ACT_FRAGMENT" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Location location = getIntent().getParcelableExtra(LOCATION);
        String catId=getIntent().getStringExtra(CAT_ID);
        DecimalFormat df = new DecimalFormat("#.##");
        String latitude="";
        String longitude="";
        if (location != null) {
            latitude = df.format(location.getLatitude());
            longitude = df.format(location.getLongitude());
        }
        ActivityMapFragment fragment = ActivityMapFragment.newInstance(latitude, longitude, catId);
        fragment.setMyLocation(location);
        getSupportFragmentManager().beginTransaction().add(R.id.container,fragment).commit();
    }

    private void launchActivitiesDetailActivity(String activityId) {
        Intent i= new Intent(this,ActivityDetailsActivity.class);
        i.putExtra(ActivityDetailsActivity.ACTIVITY_ID,activityId);
        startActivity(i);
        overridePendingTransition(R.anim.anim_up, R.anim.anim_down);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_activity_details, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id==android.R.id.home) {
            try
            {
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                try {
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (Exception e)
            {
                // Ignore exceptions if any
                Log.e("KeyBoardUtil", e.toString(), e);
            }

            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBookingSucessful() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.SHOW_UPCOMING,true);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0);
    }

    @Override
    public void onActivitySelected(String activityId) {
        launchActivitiesDetailActivity(activityId);
    }

    @Override
    public void onActivityClicked(String activityId) {
        launchActivitiesDetailActivity(activityId);
    }

    @Override
    public void onResultsClicked() {
        ActivitiesFragmentNew  activitiesFragment =  ActivitiesFragmentNew.newInstance(ActivitiesFragmentNew.FROM_MAP,"");
        replaceFragment(activitiesFragment,ACT_FRAGMENT,false);

    }
    private void replaceFragment(Fragment fragment, String tag, boolean addtoBackStack) {
        if(addtoBackStack){
            getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment, tag).addToBackStack(null).commit();
//            toolbarNAvigationListener();
        }else{
            getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment,tag).commit();
        }

    }
}
