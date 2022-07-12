package com.example.ahar.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ahar.Adaptar.MessageAdapter;
import com.example.ahar.Model.Chat;
import com.example.ahar.Model.User;
import com.example.ahar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Istiak Saif on 15/04/21.
 */
public class MessageActivity extends AppCompatActivity {

    private ImageView userImage, sendButton;
    private TextView userName;
    private Toolbar toolBar;
    private EditText editText;

    private RecyclerView recyclerView;
    private MessageAdapter messageAdapter;
    private List<Chat> chatList;

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private Intent intent;

    private Cipher cipher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        intent = getIntent();
        String userId = intent.getStringExtra("userId");

        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.leftarrow);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MessageActivity.this,RestaurantHomeActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//                checkStatus("offline");
                finish();
            }
        });
        userImage = findViewById(R.id.userimg);
        userName = findViewById(R.id.username);

        editText = findViewById(R.id.sendmessage);
        sendButton = findViewById(R.id.sendicon);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = null;
                try {
                    msg = encrypt(editText.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!msg.equals("")) {
                    sendMessage(firebaseUser.getUid(), userId, msg);//changes
                } else {
                    Toast.makeText(MessageActivity.this, "empty message", Toast.LENGTH_SHORT).show();
                }
                editText.setText("");
            }
        });

        recyclerView = findViewById(R.id.messagerecyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        databaseReference.child("users").child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                userName.setText(user.getName());
                if (user.getImageUrl().equals("default")) {
                    userImage.setImageResource(R.drawable.ic_avater);
                } else {
                    Glide.with(MessageActivity.this).load(user.getImageUrl()).into(userImage);
                }
                readMessage(firebaseUser.getUid(),userId,user.getImageUrl());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String currentTime = sdf.format(new Date());
        String currentDate = DateFormat.getDateInstance(DateFormat.SHORT).format(Calendar.getInstance().getTime());

        HashMap<String, Object> result = new HashMap<>();
        result.put("sender", sender);
        result.put("receiver", receiver);
        result.put("message", message);
        result.put("time", currentTime);
        result.put("date", currentDate);
        databaseReference.child("Chats").push().setValue(result);
        chatListNode(sender,receiver,currentTime);
    }

    private void readMessage(String senderid, String receiverid, String imageurl) {
        chatList = new ArrayList<>();
        databaseReference.child("Chats").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Chat chat = dataSnapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(senderid) && chat.getSender().equals(receiverid) ||
                    chat.getReceiver().equals(receiverid) && chat.getSender().equals(senderid)){
                        chatList.add(chat);
                    }else {

                    }
                    messageAdapter = new MessageAdapter(MessageActivity.this, chatList, imageurl);
                    recyclerView.setAdapter(messageAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void chatListNode(String sender, String receiver,String updateTime){
        final  DatabaseReference db1 = databaseReference.child("ChatsList").child(sender).child(receiver);
                db1.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(!snapshot.exists()){
                           db1.child("id").setValue(receiver);
                           db1.child("updateTime").setValue(updateTime);
                        }else {
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("updateTime", updateTime);
                            db1.updateChildren(result);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        DatabaseReference db2 = databaseReference.child("ChatsList").child(receiver).child(sender);
                db2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Chat chat = snapshot.getValue(Chat.class);
                        if(!snapshot.exists()){
                            db2.child("id").setValue(sender);
                            db2.child("updateTime").setValue(updateTime);
                        }else {
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("updateTime", updateTime);
                            db2.updateChildren(result);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    public static String decrypt(String output)throws Exception{
        SecretKeySpec keySpec = getSecretKeySpec();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE,keySpec);
        byte[] decodedValue = Base64.decode(output,Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String dec = new String(decValue);
        return dec;
    }

    private String encrypt(String Data) throws Exception{
        SecretKeySpec keySpec = getSecretKeySpec();
        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE,keySpec);
        byte[] bytes = cipher.doFinal(Data.getBytes());
        String encryptedValue = Base64.encodeToString(bytes,Base64.DEFAULT);
        return encryptedValue;
    }
    public static SecretKeySpec getSecretKeySpec() throws Exception{
        final MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[]key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }

//    private void checkStatus(String status){
//        HashMap<String, Object> result = new HashMap<>();
//        result.put("status",status);
//
//        databaseReference.child("users").child(firebaseUser.getUid()).updateChildren(result);
//    }

}