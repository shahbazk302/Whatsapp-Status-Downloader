package com.shahbaz.myapplication.Adapters;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.shahbaz.myapplication.Models.StoryModel;
import com.shahbaz.myapplication.R;
import com.shahbaz.myapplication.Utils.Constant;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class StoriesRecyclerViewAdapter extends RecyclerView.Adapter<StoriesRecyclerViewAdapter.ViewHolder> {
    Context context;
    ArrayList<Object> filesList;

    public StoriesRecyclerViewAdapter(Context context, ArrayList<Object> filesList) {
        this.context = context;
        this.filesList = filesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.story_recycler_view_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final StoryModel model = (StoryModel) filesList.get(position);
        if (model.getUri().toString().endsWith(".mp4")) {
            holder.imgPlayStatus.setVisibility(View.VISIBLE);
        } else {
            holder.imgPlayStatus.setVisibility(View.GONE);
        }

        Glide.with(context).load(model.getUri()).into(holder.imgStatus);


        holder.imgDownloadStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkFolder();
                final String path = ((StoryModel) filesList.get(position)).getPath();
                final File file = new File(path);
                String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.SAVE_FOLDER_NAME;
                File destFile = new File(destinationPath);
                try {
                    FileUtils.copyFileToDirectory(file, destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                MediaScannerConnection.scanFile(
                        context,
                        new String[]{destinationPath + model.getFileName()},
                        new String[]{"*/*"},
                        new MediaScannerConnection.MediaScannerConnectionClient() {
                            @Override
                            public void onMediaScannerConnected() {

                            }

                            @Override
                            public void onScanCompleted(String s, Uri uri) {

                            }
                        }
                );
                Toast.makeText(context, "Saved to:" + destinationPath + model.getFileName(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void checkFolder() {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File dir = new File(path);
        boolean isDirectory = dir.exists();
        if (!isDirectory) {
            isDirectory = dir.mkdir();
        }

        if (isDirectory) {
            Log.d("Folder", "Already Created");
        }
    }

    @Override
    public int getItemCount() {
        return filesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPlayStatus;
        ImageView imgStatus;
        ImageView imgDownloadStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPlayStatus = itemView.findViewById(R.id.imgPlayStatus);
            imgStatus = itemView.findViewById(R.id.imgStatus);
            imgDownloadStatus = itemView.findViewById(R.id.imgDownloadStatus);
        }
    }
}
