package com.test.camprent;


import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private Context mContext;
    private List<Upload> mUploads;
    private RecyclerViewClickListener listener;

    public ImageAdapter (Context context,List<Upload> uploads,RecyclerViewClickListener listener)
    {
        this.mContext=context;
        this.mUploads=uploads;
        this.listener=listener;
    }

    @Override
    public ImageViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.image_item,parent,false);
        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {

        Upload uploadCurrent=mUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getName());
        holder.from_view.setText(uploadCurrent.getFrom());
        holder.to_view.setText(uploadCurrent.getTo());
        holder.textViewPrice.setText(uploadCurrent.getPrice());
        holder.text_view_phone.setText(uploadCurrent.getPhone());
        holder.adress.setText(uploadCurrent.getLocation1());
        holder.key_view.setText(uploadCurrent.getKey());
        holder.mail.setText(uploadCurrent.getmail());

        Glide.with(holder.imageView.getContext()).load(uploadCurrent.getImageUrl())
                .fitCenter()
                .centerCrop()
                .apply(new RequestOptions().override(200, 200))
                .into(holder.imageView);



    }

    @Override
    public int getItemCount() {
        return mUploads.size();
    }
    public void filteredlist(ArrayList<Upload> filteredlist){
        mUploads=filteredlist;
        notifyDataSetChanged();
    }
    public interface RecyclerViewClickListener{
        void onClick (View vi,int pos);
    }


    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public TextView textViewName;
        public ImageView imageView;
        public TextView textViewPrice;
        public TextView adress;
        public TextView text_view_phone;
        public TextView from_view;
        public TextView to_view;
        public TextView key_view;
        public TextView mail;



        public ImageViewHolder (View itemView){
            super(itemView);
            textViewName=itemView.findViewById(R.id.text_view_name);
            from_view=itemView.findViewById(R.id.from_retreive);
            to_view=itemView.findViewById(R.id.to_retreive);
            textViewPrice=itemView.findViewById(R.id.text_view_price);
            adress =itemView.findViewById(R.id.text_view_adress);
            text_view_phone=itemView.findViewById(R.id.text_view_phone);
            key_view=itemView.findViewById(R.id.key_view);
            mail=itemView.findViewById(R.id.mail_owner);

            imageView =  itemView.findViewById(R.id.image_view_retreive);



            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {
            listener.onClick(itemView,getAdapterPosition());

        }
    }




}

