package com.example.ahar.Adaptar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ahar.Activity.MessageActivity;
import com.example.ahar.Model.User;
import com.example.ahar.R;

import java.util.List;

public class UserContactAdapter extends RecyclerView.Adapter<UserContactAdapter.ViewHolder>{
    private List<User> userlist;
    private Context context;

    public UserContactAdapter(Context context,List<User> userlist){
        this.context = context;
        this.userlist = userlist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.contact_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.username.setText(userlist.get(position).getName());
        if(userlist.get(position).getImageUrl().equals("default")){
            holder.userimageView.setImageResource(R.drawable.ic_avater);
        }else {
            Glide.with(context).load(userlist.get(position).getImageUrl()).into(holder.userimageView);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userId",userlist.get(position).getUserId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView username;
        private ImageView userimageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.contactlistusername);
            userimageView = itemView.findViewById(R.id.userimg);
        }
    }
}
