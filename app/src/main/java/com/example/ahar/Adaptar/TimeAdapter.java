package com.example.ahar.Adaptar;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ahar.Fragment.DonateFragment;
import com.example.ahar.Model.TimeModel;
import com.example.ahar.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder> {

    private List<TimeModel> timeModelList;
    private Context context;

    private static int lastClickPosition = -1;
    private  int selectedItem;
    private onItemClickListner onItemClickListner;

    public TimeAdapter(Context context, List<TimeModel> timeModelList) {
        this.context = context;
        this.timeModelList = timeModelList;
        selectedItem = -1;
    }

    @NonNull
    @Override
    public TimeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.timecard,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeAdapter.ViewHolder holder, int position) {
        holder.timeList.setText(timeModelList.get(position).getTime());
        holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.white));
        holder.timeList.setTextColor(context.getResources().getColor(R.color.dark));

        if (selectedItem == position) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.pink));
            holder.timeList.setTextColor(context.getResources().getColor(R.color.white));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int previousItem = selectedItem;
                selectedItem = position;

                notifyItemChanged(previousItem);
                notifyItemChanged(position);
                String text = holder.timeList.getText().toString();
                onItemClickListner.onClick(text);
            }
        });
    }
    public void setOnItemClickListner(TimeAdapter.onItemClickListner onItemClickListner) {
        this.onItemClickListner = onItemClickListner;
    }

    public interface onItemClickListner{
        void onClick(String str);
    }

    @Override
    public int getItemCount() {
        return timeModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView timeList;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            timeList = itemView.findViewById(R.id.timetext);
            cardView = itemView.findViewById(R.id.timeCardView);
        }
    }
}
