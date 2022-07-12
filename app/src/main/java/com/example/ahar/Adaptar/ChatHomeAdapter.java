package com.example.ahar.Adaptar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ahar.Activity.MessageActivity;
import com.example.ahar.Model.Chat;
import com.example.ahar.Model.User;
import com.example.ahar.R;

import java.util.HashMap;
import java.util.List;
/**
 * Created by Istiak Saif on 17/04/21.
 */
public class ChatHomeAdapter extends RecyclerView.Adapter<ChatHomeAdapter.ViewHolder>{
    private List<User> userlist;
    private Context context;
    private HashMap<String,String> lastmsg;
    private HashMap<String,String> timemap;
    private boolean isStatus;

    public ChatHomeAdapter(Context context,List<User> userlist,boolean isStatus) {
        this.userlist = userlist;
        this.context = context;
        lastmsg = new HashMap<>();
        timemap = new HashMap<>();
        this.isStatus = isStatus;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardchat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String id = userlist.get(position).getUserId();
        String lastMessage = lastmsg.get(id);
        String timetxt = timemap.get(id);
        holder.username.setText(userlist.get(position).getName());
        if(lastMessage == null || lastMessage.equals("default")){
            holder.lastMassageText.setVisibility(View.GONE);
            holder.time.setVisibility(View.GONE);
        }else {
            holder.lastMassageText.setVisibility(View.VISIBLE);
            holder.lastMassageText.setText(lastMessage);
            holder.time.setVisibility(View.VISIBLE);
            holder.time.setText(timetxt);
        }
        if(userlist.get(position).getImageUrl().equals("default")){
            holder.userimageView.setImageResource(R.drawable.ic_avater);
        }else {
            Glide.with(context).load(userlist.get(position).getImageUrl()).into(holder.userimageView);
        }

        if(isStatus){
            if(userlist.get(position).getStatus().equals("online")){
                holder.on.setVisibility(View.VISIBLE);
                holder.off.setVisibility(View.GONE);
            }else {
                holder.on.setVisibility(View.GONE);
                holder.off.setVisibility(View.VISIBLE);
            }
        }else {
            holder.on.setVisibility(View.GONE);
            holder.off.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userId",id);
                context.startActivity(intent);
            }
        });
    }

    public void setLastMessageMap(String userId, String lastMessage){
        lastmsg.put(userId,lastMessage);
    }
    public void setLastMessageTimeMap(String userId,String time){
        timemap.put(userId,time);
    }
    @Override
    public int getItemCount() {
        return userlist.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView username,lastMassageText,time;
        private ImageView userimageView,on,off;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.user_name);
            userimageView = itemView.findViewById(R.id.img);
            lastMassageText = itemView.findViewById(R.id.messageshorttxt);
            time = itemView.findViewById(R.id.senttime);
            on = itemView.findViewById(R.id.on);
            off = itemView.findViewById(R.id.off);
        }
    }
}
