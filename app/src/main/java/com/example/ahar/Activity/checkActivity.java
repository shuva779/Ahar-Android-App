package com.example.ahar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.ahar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class checkActivity extends AppCompatActivity {

    private Button submit;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;

    /**
     * Created by Istiak Saif on 14/03/21.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        popupDialogUserTypeDefine();

    }
    public void popupDialogUserTypeDefine(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View contactPopupView = getLayoutInflater().inflate(R.layout.usertype,null);

        LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Spinner sp = (Spinner) contactPopupView.findViewById(R.id.userinputlogin);

        dialogBuilder.setView(contactPopupView);
        dialogBuilder.setCancelable(false);
        dialog = dialogBuilder.create();
        dialog.show();

        String []option = {"Rider","Restaurant","Volunteer"};
        arrayAdapter = new ArrayAdapter<>(this,R.layout.usertype_item,option);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(arrayAdapter);
        submit = (Button) contactPopupView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String value = sp.getSelectedItem().toString();
                if(!TextUtils.isEmpty(value)){
                    HashMap<String, Object> result = new HashMap<>();
                    result.put("isUser", value);

                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    checkUserType();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"Error ",Toast.LENGTH_SHORT).show();
                        }
                    });
                }else {
                    Toast.makeText(getApplicationContext(),"Please Enter ",Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });

    }
    private void checkUserType() {
        FirebaseUser user = mAuth.getCurrentUser();
        Query query = databaseReference.child(user.getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("isUser").getValue(String.class).equals("Rider")) {
                    Intent intent = new Intent(checkActivity.this, RiderHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (snapshot.child("isUser").getValue(String.class).equals("Restaurant")) {
                    Intent intent = new Intent(checkActivity.this, RestaurantHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                if (snapshot.child("isUser").getValue(String.class).equals("Volunteer")) {
                    Intent intent = new Intent(checkActivity.this, RiderHomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}