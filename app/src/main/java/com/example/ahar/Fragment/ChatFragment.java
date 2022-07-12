package com.example.ahar.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.ahar.Activity.RestaurantHomeActivity;
import com.example.ahar.Activity.RiderHomeActivity;
import com.example.ahar.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ChatFragment extends Fragment {

    private Fragment selectedFragment = null;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomNavigationView = view.findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.icchat);
        bottomNavigationView.setItemIconTintList(null);

        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new ChatFragmentHome()).commit();

    }
    private BottomNavigationView.OnNavigationItemSelectedListener navItemSelectedListener=new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()){
                case R.id.icchat:
                    selectedFragment = new ChatFragmentHome();
                    break;
                case R.id.iccontact:
                    selectedFragment = new ContactList();
                    break;
            }
            if(selectedFragment!=null){
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                return true;
            }
            return false;
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_chat, container, false);
        return view;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        ((RestaurantHomeActivity)getActivity()).setToolbar(toolbar);
//        ((RiderHomeActivity)getActivity()).setToolbar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//    }

}
