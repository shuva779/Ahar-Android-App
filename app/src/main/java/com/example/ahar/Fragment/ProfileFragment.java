package com.example.ahar.Fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.ahar.Activity.LogInActivity;
import com.example.ahar.Activity.RestaurantHomeActivity;
import com.example.ahar.Activity.RiderHomeActivity;
import com.example.ahar.utils.ImageGetHelper;
import com.example.ahar.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Istiak Saif on 02/04/21.
 */

public class ProfileFragment extends Fragment {

    private ImageGetHelper getImageFunction;
    private ImageView logoutButton,imageView;
    private TextView nickName,nid,fullName,email,phone,address,restaurantName;
    private DatabaseReference databaseReference;
    private StorageReference storageReference;
    private Uri imageUri;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private String uid = user.getUid();
    private ProgressDialog progressDialog,pro;

    private String profilePhoto;
    private GoogleSignInClient googleSignInClient;

    private Toolbar toolbar;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getImageFunction = new ImageGetHelper(this);

        nickName = view.findViewById(R.id.nickname);
        logoutButton = view.findViewById(R.id.logout);
        imageView = view.findViewById(R.id.profileimage);
        fullName = view.findViewById(R.id.profilefullname);
        email = view.findViewById(R.id.profileemail);
        phone = view.findViewById(R.id.profilephone);
        nid = view.findViewById(R.id.managernid);
        address = view.findViewById(R.id.profileaddress);
        restaurantName = view.findViewById(R.id.restaurantname);

        progressDialog = new ProgressDialog(getActivity());

        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        storageReference = FirebaseStorage.getInstance().getReference();

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren()) {
                    String name = ""+dataSnapshot.child("name").getValue();
                    String retriveEmail = ""+dataSnapshot.child("email").getValue();
                    String img = ""+dataSnapshot.child("imageUrl").getValue();
                    String receivephone = ""+dataSnapshot.child("phone").getValue();
                    String receiveaddress = ""+dataSnapshot.child("address").getValue();
                    String receivenid = ""+dataSnapshot.child("nid").getValue();
                    String receiverestaurantname = ""+dataSnapshot.child("Restaurant Name").getValue();

                    fullName.setText(name);
                    String[] splitStr = name.split("\\s+");
                    nickName.setText(splitStr[0]);
                    email.setText(retriveEmail);
                    phone.setText(receivephone);
                    address.setText(receiveaddress);
                    nid.setText(receivenid);
                    restaurantName.setText(receiverestaurantname);
                    try {
                        Picasso.get().load(img).resize(320,320).into(imageView);
                    }catch (Exception e){
                        Picasso.get().load(R.drawable.dropdown).into(imageView);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(),"Some Thing Wrong",Toast.LENGTH_SHORT).show();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Update Profile Image");
                profilePhoto = "imageUrl";
                getImageFunction.showImagePicDialog();
            }
        });

        fullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Update Manager Name");
                showMoreUpdating("name");
            }
        });
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Update Profile Phone");
                    showMoreUpdating("phone");
            }
        });
        nid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Update National Identity");
                showMoreUpdating("nid");
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Update Profile Address");
                    showMoreUpdating("address");
            }
        });
        restaurantName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Update Restaurant Name");
                showMoreUpdating("Restaurant Name");
            }
        });


        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(),googleSignInOptions);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.logout:
                        signOut();
                        break;
                }
            }
        });

    }

    private void showMoreUpdating(String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Update "+ key);
        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        TextInputEditText editText = new TextInputEditText(getActivity());
        editText.setHint("Enter "+key);
        linearLayout.addView(editText);

        builder.setView(linearLayout);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String value = editText.getText().toString().trim();
                if(!TextUtils.isEmpty(value)){
                    progressDialog.show();
                    HashMap<String, Object> result = new HashMap<>();
                    result.put(key, value);

                    databaseReference.child(uid).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    Toast.makeText(getActivity(),"Updating "+key,Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(),"Error ",Toast.LENGTH_SHORT).show();
                        }
                    });

                }else {
                    Toast.makeText(getActivity(),"Please Enter "+key,Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.dismiss();
            }
        });

        builder.create().show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == getImageFunction.IMAGE_PICK_GALLERY_CODE){
                imageUri = data.getData();
                uploadProfilePhoto(imageUri);
                imageView.setImageURI(imageUri);
            }
            if(requestCode == getImageFunction.IMAGE_PICK_CAMERA_CODE){
                try {
                    uploadProfilePhoto(imageUri);
                    imageView.setImageURI(imageUri);
                }catch (Exception e){
                   e.printStackTrace();
                }
            }
        }
    }

    private void uploadProfilePhoto(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] fileInBytes = baos.toByteArray();

        String filePathName = profilePhoto+"_"+uid;
        StorageReference storageReference1 = storageReference.child(filePathName);

        pro = new ProgressDialog(getContext());
        pro.show();
        pro.setContentView(R.layout.progress_dialog);
        pro.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        storageReference1.putBytes(fileInBytes).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful());
                Uri downloadUri = uriTask.getResult();
                if(uriTask.isSuccessful()){
                    HashMap<String, Object> results = new HashMap<>();
                    results.put(profilePhoto,downloadUri.toString());

                    databaseReference.child(uid).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressDialog.dismiss();
                                    pro.dismiss();
                                    Toast.makeText(getContext(),"Image Update",Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(),"Error Update",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else {
                    progressDialog.dismiss();
                    Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(getActivity(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void signOut(){
        FirebaseAuth.getInstance().signOut();
        googleSignInClient.signOut();
        Intent intent1 = new Intent(getActivity(), LogInActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_profile, container, false);
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