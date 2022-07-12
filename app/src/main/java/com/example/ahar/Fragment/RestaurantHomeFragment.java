package com.example.ahar.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahar.Activity.RestaurantHomeActivity;
import com.example.ahar.Activity.RiderHomeActivity;
import com.example.ahar.Adaptar.DonateItemAdapter;
import com.example.ahar.Model.DonateFoodItemList;
import com.example.ahar.R;
import com.google.android.material.appbar.AppBarLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RestaurantHomeFragment extends Fragment {

    private RecyclerView productRecyclerView;
    private DonateItemAdapter donateItemAdapter;
    private ArrayList<DonateFoodItemList> donateFoodItemLists;
    private DatabaseReference productDatabaseRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private Toolbar toolbar;

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productRecyclerView = view.findViewById(R.id.productitemsellerrecycler);
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        productRecyclerView.setHasFixedSize(true);

        productDatabaseRef = FirebaseDatabase.getInstance().getReference();
        donateFoodItemLists = new ArrayList<>();

        GetDataFromFirebase(null);
        ClearAll();
    }

    private void GetDataFromFirebase(String resname) {

        Query query = productDatabaseRef.child("donateFoodList").orderByChild("userId").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for(DataSnapshot  snapshot: dataSnapshot.getChildren()){
                    DonateFoodItemList donateItem = new DonateFoodItemList();
                    if(resname==null){
                        String id = snapshot.child("userId").getValue().toString();
                        getresname(id);
                    }else{
                    try {
                        donateItem.setRestaurantAddress(snapshot.child("RestaurantAddress").getValue().toString());
                        donateItem.setFoodDes(snapshot.child("FoodDescription").getValue().toString());
                        donateItem.setApproxPrice(snapshot.child("FoodApproximatePrice").getValue().toString());
                        donateItem.setConsumePeople(snapshot.child("ConsumePeople").getValue().toString());
                        donateItem.setDate(snapshot.child("Date").getValue().toString());
                        donateItem.setEndTime(snapshot.child("EndTime").getValue().toString());
                        donateItem.setStartTime(snapshot.child("startTime").getValue().toString());
                        donateItem.setImage(snapshot.child("image").getValue().toString());
                        donateItem.setRestaurantName(resname);
                        donateFoodItemLists.add(donateItem);
                    }catch (Exception e){

                    }
                    }
                }
                donateItemAdapter = new DonateItemAdapter(getContext(), donateFoodItemLists);
                productRecyclerView.setAdapter(donateItemAdapter);
                donateItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getresname(String id){
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("userId").equalTo(id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    try {
                        String name = dataSnapshot.child("Restaurant Name").getValue().toString();
                        GetDataFromFirebase(name);
                    }catch (Exception e){

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Some Thing Wrong",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void ClearAll(){
        if (donateFoodItemLists !=null){
            donateFoodItemLists.clear();
            if (donateItemAdapter!=null){
                donateItemAdapter.notifyDataSetChanged();
            }
        }
        donateFoodItemLists = new ArrayList<>();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_restaurant_home, container, false);
        return view;
    }

   @Override
    public void onResume() {
        super.onResume();
        ((RestaurantHomeActivity)getActivity()).setToolbar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();
    }
}