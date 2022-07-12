package com.example.ahar.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ahar.Adaptar.TabViewPagerAdapter;
import com.example.ahar.Fragment.ChatFragment;
import com.example.ahar.Fragment.DonateFragment;
import com.example.ahar.Fragment.RestaurantHomeFragment;
import com.example.ahar.Fragment.ProfileFragment;
import com.example.ahar.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

/**
 * Created by Istiak Saif on 14/03/21.
 */
public class RestaurantHomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager tabviewPager;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private long backPressedTime;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_home);

        int displayWidth = getWindowManager().getDefaultDisplay().getHeight();

        tabLayout = (TabLayout)findViewById(R.id.tab);
        tabviewPager = (ViewPager)findViewById(R.id.tabviewpager);
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.AddFragment(new RestaurantHomeFragment(),null);
        tabViewPagerAdapter.AddFragment(new DonateFragment(),"Donation");
        tabViewPagerAdapter.AddFragment(new ChatFragment(),"Chat");
        tabViewPagerAdapter.AddFragment(new ProfileFragment(),"Profile");
        tabviewPager.setAdapter(tabViewPagerAdapter);
        tabLayout.setupWithViewPager(tabviewPager);
        LinearLayout layout = ((LinearLayout) ((LinearLayout) tabLayout.getChildAt(0)).getChildAt(0));
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
        layoutParams.weight = 0.5f;
        layout.setLayoutParams(layoutParams);
        tabLayout.getTabAt(0).setIcon(R.drawable.home);

    }
    @Override
    protected void onResume() {
        super.onResume();
//        checkStatus("online");
//        doOnline();
//        Toast.makeText(RestaurantHomeActivity.this, "resume", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onPause() {
        super.onPause();
//        checkStatus("offline");
//        doOffline();
//        Toast.makeText(RestaurantHomeActivity.this, "pause", Toast.LENGTH_SHORT).show();
    }

    public void setToolbar(Toolbar toolbar){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
        }return true;
    }
    public void onBackPressed(){
        if(backPressedTime + 2000>System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            Toast.makeText(getBaseContext(),"Press Back Again to Exit",Toast.LENGTH_SHORT).show();
        }
        backPressedTime = System.currentTimeMillis();
    }
    private void checkStatus(String status){
        HashMap<String, Object> result = new HashMap<>();
        result.put("status",status);

        databaseReference.child("users").child(firebaseUser.getUid()).updateChildren(result);
    }

    private void doOffline() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkStatus("offline");
            }
        }, 1000);
    }

    private void doOnline() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkStatus("online");
            }
        }, 1000);
    }

}