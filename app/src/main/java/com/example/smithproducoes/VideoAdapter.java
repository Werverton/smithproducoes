package com.example.smithproducoes;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import javax.annotation.Nonnull;

public class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder>{
    Context context;
    ArrayList<Video> arrayList;
    OnItemClickListener onItemClickListener;
    public VideoAdapter(Context context, ArrayList<Video> arrayList){
        this.arrayList = arrayList;
        this.context = context;
    }
    @Nonnull
    @Override
    public ViewHolder onCreateViewHolder(@Nonnull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(context).inflate(R.layout.activity_video_play, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        try{
            MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(arrayList.get(position).getPath()));

        } catch (Exception e){
            e.printStackTrace();
        }

    }



    private String getDuration(long duration){
        String videoDuration;
        int dur =(int)duration;
        int hrs =(dur / 3600000);
        int min =(dur / 60000) % 6000;
        int sec =(dur % 60000 / 1000);

        if(hrs > 0){
            videoDuration = String.format("%02d hrs, %02d min, %02d sec", hrs, min,sec);
        } else if (min > 0){
            videoDuration = String.format("%02d min, %02d sec", min,sec);
        } else {
            videoDuration = String.format("%02d sec", sec);
        } return videoDuration;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ViewHolder(@Nonnull View itemView){
            super(itemView);



        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }


    public interface OnItemClickListener{
        void onClick(View view, String path);
    }
}
