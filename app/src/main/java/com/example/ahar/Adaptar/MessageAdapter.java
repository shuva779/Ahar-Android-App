package com.example.ahar.Adaptar;

import android.content.Context;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ahar.Activity.MessageActivity;
import com.example.ahar.Model.Chat;
import com.example.ahar.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.spec.SecretKeySpec;

import static com.example.ahar.Activity.MessageActivity.getSecretKeySpec;

/**
 * Created by Istiak Saif on 15/04/21.
 */
public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    public static final int Message_Sender = 1;
    public static final int Message_Receiver = 0;

    private List<Chat> chatlist;
    private Context context;
    private String imageUrl;

    private FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    public MessageAdapter(Context context, List<Chat> chatlist, String imageUrl) {
        this.chatlist = chatlist;
        this.context = context;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Message_Sender) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.chat_item_right, parent, false);
            return new ViewHolder(view);
        }else {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.chat_item_left, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String ss = null;
        try {
            ss = decrypt(chatlist.get(position).getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.messagetxt.setText(ss);
        if(imageUrl.equals("default")){
            holder.userimageView.setImageResource(R.drawable.ic_avater);
        }else {
            Glide.with(context).load(imageUrl).into(holder.userimageView);
        }
    }

    @Override
    public int getItemCount() {
        return chatlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView messagetxt;
        private ImageView userimageView;
        private RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = window.getDefaultDisplay();
            int displayWidth = display.getWidth();
            messagetxt = itemView.findViewById(R.id.messageshow);
            userimageView = itemView.findViewById(R.id.userimg);
            relativeLayout = itemView.findViewById(R.id.chatitemwidth);
            relativeLayout.getLayoutParams().width = ((displayWidth/10)*7);
        }
    }

    @Override
    public int getItemViewType(int position){
        if (chatlist.get(position).getSender().equals(firebaseUser.getUid())){
            return Message_Sender;
        }else {
            return Message_Receiver;
        }
    }

    private String decrypt(String output)throws Exception{
        SecretKeySpec keySpec = getSecretKeySpec();
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE,keySpec);
        byte[] decodedValue = Base64.decode(output,Base64.DEFAULT);
        byte[] decValue = c.doFinal(decodedValue);
        String dec = new String(decValue);
        return dec;
    }
}
