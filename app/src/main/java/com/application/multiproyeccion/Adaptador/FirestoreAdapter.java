package com.application.multiproyeccion.Adaptador;

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

import com.application.multiproyeccion.DetailActivity;
import com.application.multiproyeccion.Modelo.ImageModel;
import com.application.multiproyeccion.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FirestoreAdapter extends RecyclerView.Adapter<FirestoreAdapter.ViewHolder> {

    private ArrayList<ImageModel> mDataList;
    private Context mContext;


    public FirestoreAdapter(ArrayList<ImageModel> dataList, Context context){
        mDataList = dataList;
        mContext = context;
    }

    @NonNull
    @Override
    public FirestoreAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FirestoreAdapter.ViewHolder holder, int position) {

        Glide.with(mContext)
                .load(mDataList.get(position).getDataImage())
                .into(holder.mImageView);
        holder.mDescriptionTextView.setText(mDataList.get(position).getDescripcion());
        holder.mStartDateTextView.setText(mDataList.get(position).getFechaInicial());
        holder.mEndDateTextView.setText(mDataList.get(position).getFechaFinal());

        holder.mrecCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("dataImage",mDataList.get(holder.getAdapterPosition()).getDataImage());
                intent.putExtra("descripcion",mDataList.get(holder.getAdapterPosition()).getDescripcion());
                intent.putExtra("fechaInicial",mDataList.get(holder.getAdapterPosition()).getFechaInicial());
                intent.putExtra("fechaFinal",mDataList.get(holder.getAdapterPosition()).getFechaFinal());
                intent.putExtra("key",mDataList.get(holder.getAdapterPosition()).getKey());
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public TextView mDescriptionTextView;
        public TextView mStartDateTextView;
        public TextView mEndDateTextView;

        public CardView mrecCard;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            mImageView = itemView.findViewById(R.id.recImage);
            mDescriptionTextView = itemView.findViewById(R.id.recDesc);
            mStartDateTextView = itemView.findViewById(R.id.recFechI);
            mEndDateTextView = itemView.findViewById(R.id.recFechF);
            mrecCard=itemView.findViewById(R.id.recCard);
        }

    }
}
