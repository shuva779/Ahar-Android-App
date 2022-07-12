package com.example.ahar.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahar.Adaptar.DonateItemAdapter;
import com.example.ahar.Adaptar.DonateItemAdminpannelAdapter;
import com.example.ahar.Model.DonateFoodItemList;
import com.example.ahar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
/**
 * Created by Istiak Saif on 27/04/21.
 */
public class AdminHomeFragment extends Fragment {

    private RecyclerView productRecyclerView;
    private DonateItemAdminpannelAdapter donateItemAdapter;
    private ArrayList<DonateFoodItemList> donateFoodItemLists;
    private DatabaseReference donateItemDatabaseRef;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productRecyclerView = view.findViewById(R.id.productitemsellerrecycler);
        productRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),1));
        productRecyclerView.setHasFixedSize(true);

        donateItemDatabaseRef = FirebaseDatabase.getInstance().getReference();
        donateFoodItemLists = new ArrayList<>();

        GetDataFromFirebase();
        ClearAll();
    }

    private void GetDataFromFirebase() {
        Query query = donateItemDatabaseRef.child("donateFoodList");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearAll();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DonateFoodItemList donateItem = new DonateFoodItemList();
                    try {
                        donateItem.setRestaurantAddress(snapshot.child("RestaurantAddress").getValue().toString());
                        donateItem.setFoodDes(snapshot.child("FoodDescription").getValue().toString());
                        donateItem.setApproxPrice(snapshot.child("FoodApproximatePrice").getValue().toString());
                        donateItem.setConsumePeople(snapshot.child("ConsumePeople").getValue().toString());
                        donateItem.setDate(snapshot.child("Date").getValue().toString());
                        donateItem.setEndTime(snapshot.child("EndTime").getValue().toString());
                        donateItem.setStartTime(snapshot.child("startTime").getValue().toString());
                        donateItem.setImage(snapshot.child("image").getValue().toString());
                        donateItem.setStatus(snapshot.child("status").getValue().toString());
                        String donateid = snapshot.child("donateId").getValue().toString();
                        donateItem.setDonateid(donateid);
                        String id = snapshot.child("userId").getValue().toString();
                        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("userId").equalTo(id);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    try {
                                        String name = dataSnapshot.child("Restaurant Name").getValue().toString();
                                        donateItem.setRestaurantName(name);
                                    } catch (Exception e) {

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(getActivity(), "Some Thing Wrong", Toast.LENGTH_SHORT).show();
                            }
                        });
                        donateFoodItemLists.add(donateItem);
                    } catch (Exception e) {

                    }
                }
                donateItemAdapter = new DonateItemAdminpannelAdapter(getContext(), donateFoodItemLists);
                productRecyclerView.setAdapter(donateItemAdapter);
                donateItemAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
        View view = inflater.inflate(R.layout.fragment_admin_home, container, false);
        return view;
    }

}