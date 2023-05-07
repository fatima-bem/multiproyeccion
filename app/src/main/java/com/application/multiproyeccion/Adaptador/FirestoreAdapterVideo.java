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
import com.application.multiproyeccion.DetailActivityVideo;
import com.application.multiproyeccion.Modelo.ImageModel;
import com.application.multiproyeccion.Modelo.VideoModel;
import com.application.multiproyeccion.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class FirestoreAdapterVideo extends RecyclerView.Adapter<FirestoreAdapterVideo.MyViewHolder> {

    private ArrayList<VideoModel> vDataList;
    private Context vContext;

    public FirestoreAdapterVideo(ArrayList<VideoModel> vDataList, Context vContext) {
        this.vDataList = vDataList;
        this.vContext = vContext;
    }

    public FirestoreAdapterVideo(){}

    @NonNull
    @Override
    public FirestoreAdapterVideo.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_video, parent, false);
        return new FirestoreAdapterVideo.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FirestoreAdapterVideo.MyViewHolder holder, int position) {

        Glide.with(vContext).load(vDataList.get(position).getDataVideo()).into(holder.vImageView);
        holder.vDescriptionTextView.setText(vDataList.get(position).getVideoDescripcion());
        holder.vStartDateTextView.setText(vDataList.get(position).getVideoFechaInicial());
        holder.vEndDateTextView.setText(vDataList.get(position).getVideoFechaFinal());

        holder.recCardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(vContext, DetailActivityVideo.class);
                intent.putExtra("dataVideo",vDataList.get(holder.getAdapterPosition()).getDataVideo());
                intent.putExtra("descripcion",vDataList.get(holder.getAdapterPosition()).getVideoDescripcion());
                intent.putExtra("fechaInicial",vDataList.get(holder.getAdapterPosition()).getVideoFechaInicial());
                intent.putExtra("fechaFinal",vDataList.get(holder.getAdapterPosition()).getVideoFechaFinal());
                intent.putExtra("key",vDataList.get(holder.getAdapterPosition()).getKey());
                vContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return vDataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView vImageView;
        public TextView vDescriptionTextView;
        public TextView vStartDateTextView;
        public TextView vEndDateTextView;

        public CardView recCardVideo;

        public MyViewHolder(@NonNull View view){
            super(view);
            vImageView = itemView.findViewById(R.id.recVideo);
            vDescriptionTextView = itemView.findViewById(R.id.recDescVideo);
            vStartDateTextView = itemView.findViewById(R.id.recFechIVideo);
            vEndDateTextView = itemView.findViewById(R.id.recFechFVideo);
            recCardVideo=itemView.findViewById(R.id.recCardVideo);
        }
    }
}
