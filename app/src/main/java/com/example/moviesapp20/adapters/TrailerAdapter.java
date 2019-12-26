package com.example.moviesapp20.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviesapp20.OnTrailerClickListener;
import com.example.moviesapp20.R;
import com.example.moviesapp20.data.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {
    private ArrayList<Trailer> trailers;
    private OnTrailerClickListener onTrailerClickListener;


    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer,parent,false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.name_of_video_text_view.setText(trailer.getName());
//        Picasso.get().load()
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public void setOnTrailerClickListener(OnTrailerClickListener onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder{
        ImageView play_image_view;
        TextView name_of_video_text_view;
        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            play_image_view = itemView.findViewById(R.id.play_image_view);
            name_of_video_text_view = itemView.findViewById(R.id.name_of_video_text_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onTrailerClickListener !=null){
                        onTrailerClickListener.onTrailerClick(trailers.get(getAdapterPosition()).getKey());
                    }
                }
            });
        }
    }
}
