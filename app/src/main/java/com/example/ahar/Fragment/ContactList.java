package com.example.ahar.Fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ahar.Adaptar.UserContactAdapter;
import com.example.ahar.Model.User;
import com.example.ahar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ContactList extends Fragment {
    private UserContactAdapter userContactAdapter;
    private List<User> userList;

    private RecyclerView recyclerView;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.contactrecycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        userList = new ArrayList<>();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    assert user!=null;
                    assert firebaseUser!=null;
                    if(!user.getUserId().equals(firebaseUser.getUid())){
                        userList.add(user);
                    }else {
//                        Toast.makeText(getActivity(),"You hove No friends",Toast.LENGTH_SHORT).show();
                    }
                }
                userContactAdapter = new UserContactAdapter(getContext(),userList);
                recyclerView.setAdapter(userContactAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        return view;
    }
}