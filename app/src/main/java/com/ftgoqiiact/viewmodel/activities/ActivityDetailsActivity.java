package com.ftgoqiiact.viewmodel.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ftgoqiiact.R;
import com.ftgoqiiact.viewmodel.fragments.ActivityDetailFragment;
import com.ftgoqiiact.viewmodel.utils.Utilities;

public class ActivityDetailsActivity extends AppCompatActivity implements Utilities.BookingSuccessfulListener{
    public static final String ACTIVITY_ID ="ACTIVITY_ID" ;
    ActivityDetailFragment activityDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_details);
        activityDetailFragment= ActivityDetailFragment.newInstance(getIntent().getStringExtra(ACTIVITY_ID));
        getSupportFragmentManager().beginTransaction().add(R.id.container,activityDetailFragment).commit();
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
}
