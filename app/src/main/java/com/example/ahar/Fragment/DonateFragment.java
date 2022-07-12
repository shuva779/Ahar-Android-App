package com.example.ahar.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahar.Activity.RestaurantHomeActivity;
import com.example.ahar.Adaptar.TimeAdapter;
import com.example.ahar.utils.ImageGetHelper;
import com.example.ahar.Model.TimeModel;
import com.example.ahar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


import static android.app.Activity.RESULT_OK;

/**
 * Created by Istiak Saif on 02/04/21.
 */

public class DonateFragment extends Fragment {
    private TextInputLayout restaurantAddress,foodprice,consumePeople,fooddescription;
    private TextView startTimeStore;
    private DatabaseReference databaseReference,database;
    private StorageReference storageReference;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private Button donateButton;
    private String DonateID;
    private Uri image;
    private ImageView foodItemImage;

    private RecyclerView timeRecyclerView;
    private TimeAdapter timeAdapter;
    private List<TimeModel> timeModelList;
    private ImageGetHelper getImageFunction;
    private ProgressDialog pro;
    private Toolbar toolbar;

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getImageFunction = new ImageGetHelper(this);

        restaurantAddress = view.findViewById(R.id.address);
        foodprice = view.findViewById(R.id.addfoodprice);
        consumePeople = view.findViewById(R.id.noOfPeople);
        fooddescription = view.findViewById(R.id.addfooddescription);
        donateButton = view.findViewById(R.id.productaddbutton);
        startTimeStore = view.findViewById(R.id.starttimestore);
        foodItemImage = view.findViewById(R.id.itemfoodimage);

        pro = new ProgressDialog(getContext());

        database = FirebaseDatabase.getInstance().getReference("users");
        Query query = database.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    String receiveaddress = ""+dataSnapshot.child("address").getValue();

                    restaurantAddress.getEditText().setText(receiveaddress);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Some Thing Wrong",Toast.LENGTH_SHORT).show();
            }
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("donateFoodList");
        storageReference = FirebaseStorage.getInstance().getReference();
        DonateID = databaseReference.child(uid).push().getKey();

        donateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadToFirebase(image);
                pro.show();
                pro.setContentView(R.layout.progress_dialog);
                pro.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

            }
        });

        timeRecyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        timeRecyclerView.setLayoutManager(layoutManager);

        timeModelList = new ArrayList<TimeModel>();

        timeModelList.add(new TimeModel("20:00"));
        timeModelList.add(new TimeModel("20:30"));
        timeModelList.add(new TimeModel("21:00"));
        timeModelList.add(new TimeModel("21:30"));
        timeModelList.add(new TimeModel("22:00"));
        timeModelList.add(new TimeModel("22:30"));
        timeModelList.add(new TimeModel("23:00"));
        timeModelList.add(new TimeModel("23:30"));
        timeModelList.add(new TimeModel("00:00"));
        timeModelList.add(new TimeModel("00:30"));
        timeModelList.add(new TimeModel("01:00"));
        timeModelList.add(new TimeModel("01:30"));
        timeModelList.add(new TimeModel("02:00"));
        timeModelList.add(new TimeModel("02:30"));
        timeModelList.add(new TimeModel("03:00"));
        timeModelList.add(new TimeModel("03:30"));
        timeModelList.add(new TimeModel("04:00"));
        timeModelList.add(new TimeModel("04:30"));
        timeModelList.add(new TimeModel("05:00"));
        timeModelList.add(new TimeModel("05:30"));
        timeModelList.add(new TimeModel("06:00"));
        timeModelList.add(new TimeModel("06:30"));
        timeModelList.add(new TimeModel("07:00"));
        timeModelList.add(new TimeModel("07:30"));
        timeModelList.add(new TimeModel("08:00"));
        timeModelList.add(new TimeModel("08:30"));
        timeModelList.add(new TimeModel("09:00"));
        timeModelList.add(new TimeModel("09:30"));
        timeModelList.add(new TimeModel("10:00"));
        timeModelList.add(new TimeModel("10:30"));
        timeModelList.add(new TimeModel("11:00"));
        timeModelList.add(new TimeModel("11:30"));
        timeModelList.add(new TimeModel("12:00"));
        timeModelList.add(new TimeModel("12:30"));
        timeModelList.add(new TimeModel("13:00"));
        timeModelList.add(new TimeModel("13:30"));
        timeModelList.add(new TimeModel("14:00"));
        timeModelList.add(new TimeModel("14:30"));
        timeModelList.add(new TimeModel("15:00"));
        timeModelList.add(new TimeModel("15:30"));
        timeModelList.add(new TimeModel("16:00"));
        timeModelList.add(new TimeModel("16:30"));
        timeModelList.add(new TimeModel("17:00"));
        timeModelList.add(new TimeModel("17:30"));
        timeModelList.add(new TimeModel("18:00"));
        timeModelList.add(new TimeModel("18:30"));
        timeModelList.add(new TimeModel("19:00"));
        timeModelList.add(new TimeModel("19:30"));

        timeAdapter = new TimeAdapter(getContext(),timeModelList);
        timeRecyclerView.setAdapter(timeAdapter);
        timeAdapter.notifyDataSetChanged();
        timeAdapter.setOnItemClickListner(new TimeAdapter.onItemClickListner() {
            @Override
            public void onClick(String str) {
                startTimeStore.setText(str);
            }
        });

        //addImage
        foodItemImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getImageFunction.pickFromGallery();
            }
        });
    }

    private void uploadToFirebase(Uri uri) {
        String FoodDescription = fooddescription.getEditText().getText().toString().trim();
        String FoodApproximatePrice = foodprice.getEditText().getText().toString().trim();
        String ConsumePeople = consumePeople.getEditText().getText().toString().trim();
        String RestaurantAddress = restaurantAddress.getEditText().getText().toString().trim();
        String EndTime = startTimeStore.getText().toString().trim();

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss ");
        String currentTime = sdf.format(new Date());
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime());

//        String donateid = uid+"_"+DonateID;

        HashMap<String, Object> result = new HashMap<>();
        result.put("FoodDescription", FoodDescription);
        result.put("FoodApproximatePrice", FoodApproximatePrice);
        result.put("ConsumePeople", ConsumePeople);
        result.put("RestaurantAddress", RestaurantAddress);
        result.put("startTime" , currentTime);
        result.put("Date" , currentDate);
        result.put("EndTime",EndTime);
        result.put("donateId", DonateID);
        result.put("userId", uid);
        result.put("status", "");
        final StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getActivity().getContentResolver());
        databaseReference.child(DonateID).updateChildren(result).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                if(uri == null){
                    Uri imguri = Uri.parse("https://firebasestorage.googleapis.com/v0/b/ahar-project-299.appspot.com/o/1620468386134.android.app.ContextImpl%24ApplicationContentResolver%401e3dbf7f?alt=media&token=5eeb261a-2cfd-4215-a929-332e497ad1bb");
                    HashMap<String, Object> resultimg = new HashMap<>();
                    resultimg.put("image", imguri.toString());
                    databaseReference.child(DonateID).updateChildren(resultimg).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            intent();
                            Toast.makeText(getActivity(), "Donate Successful", Toast.LENGTH_SHORT).show();
                            pro.dismiss();
                        }
                    });
                }else
                    if(uri != null){
                    fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    HashMap<String, Object> resultimg = new HashMap<>();
                                        resultimg.put("image", uri.toString());
                                    databaseReference.child(DonateID).updateChildren(resultimg).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            intent();
                                            Toast.makeText(getActivity(), "Donate Successful", Toast.LENGTH_SHORT).show();
                                            pro.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getActivity(), "Uploading Failed !!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getActivity(), "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void intent() {
        Intent intent = new Intent(getContext(), RestaurantHomeActivity.class);
        getContext().startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == getImageFunction.IMAGE_PICK_GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            image = data.getData();
            foodItemImage.setImageURI(image);
            foodItemImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_donate, container, false);
        return view;
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        ((RestaurantHomeActivity)getActivity()).setToolbar(toolbar);
//        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
//    }
}
