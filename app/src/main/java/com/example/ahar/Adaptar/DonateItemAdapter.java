package com.example.ahar.Adaptar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ahar.Model.DonateFoodItemList;
import com.example.ahar.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class DonateItemAdapter extends RecyclerView.Adapter<DonateItemAdapter.ViewHolder> {
    private static final String Tag = "RecyclerView";
    private Context context;
    private ArrayList<DonateFoodItemList> mdata;

    public DonateItemAdapter(Context context, ArrayList<DonateFoodItemList> mdata) {
        this.context = context;
        this.mdata = mdata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.carddonateitem,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemDes.setText(mdata.get(position).getFoodDes());
        holder.itemPrice.setText("Approximate price "+mdata.get(position).getApproxPrice()+" tk");
        holder.resaddress.setText(mdata.get(position).getRestaurantAddress());
        holder.resname.setText(mdata.get(position).getRestaurantName());
        holder.date.setText("Date: "+mdata.get(position).getDate());
        holder.stattime.setText("StartTime: "+mdata.get(position).getStartTime());
        holder.endtime.setText("EndTime: "+mdata.get(position).getEndTime());
        holder.itemconpeople.setText("Approximate "+mdata.get(position).getConsumePeople()+" people eat foods");


        Glide.with(context).load(mdata.get(position).getImage()).placeholder(R.drawable.dropdown).into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView itemImage;
        TextView itemDes,itemPrice,resaddress,resname,date,stattime,endtime,itemconpeople;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemImage = (ImageView) itemView.findViewById(R.id.foodimg);
            itemDes = (TextView) itemView.findViewById(R.id.fooddescription);
            itemPrice = (TextView) itemView.findViewById(R.id.price);
            resaddress = (TextView) itemView.findViewById(R.id.resaddress);
            resname = (TextView) itemView.findViewById(R.id.restaurantname);
            date = (TextView) itemView.findViewById(R.id.date);
            stattime = (TextView) itemView.findViewById(R.id.starttime);
            endtime = (TextView) itemView.findViewById(R.id.endtime);
            itemconpeople = (TextView) itemView.findViewById(R.id.approxpeople);
        }
    }
}
