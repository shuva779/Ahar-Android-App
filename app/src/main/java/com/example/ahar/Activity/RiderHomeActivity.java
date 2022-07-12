package com.example.ahar.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ahar.Adaptar.TabViewPagerAdapter;
import com.example.ahar.Fragment.ChatFragment;
import com.example.ahar.Fragment.DonateFragment;
import com.example.ahar.Fragment.MapFragment;
import com.example.ahar.Fragment.ProfileFragment;
import com.example.ahar.Fragment.RestaurantHomeFragment;
import com.example.ahar.Fragment.RiderHomeFragment;
import com.example.ahar.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RiderHomeActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager tabviewPager;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private long backPressedTime;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common_home);

        tabLayout = (TabLayout)findViewById(R.id.tab);
        tabviewPager = (ViewPager)findViewById(R.id.tabviewpager);
        TabViewPagerAdapter tabViewPagerAdapter = new TabViewPagerAdapter(getSupportFragmentManager());
        tabViewPagerAdapter.AddFragment(new RiderHomeFragment(),null);
        tabViewPagerAdapter.AddFragment(new MapFragment(),"Map");//update this line after fix our features
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
}