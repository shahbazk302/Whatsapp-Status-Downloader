package com.shahbaz.myapplication.Activities;

import android.Manifest;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.shahbaz.myapplication.Adapters.StoriesRecyclerViewAdapter;
import com.shahbaz.myapplication.Models.StoryModel;
import com.shahbaz.myapplication.R;
import com.shahbaz.myapplication.Utils.Constant;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {
    RecyclerView rv_status;
    Context context;
    SwipeRefreshLayout swipRefreshLayout;
    StoriesRecyclerViewAdapter storiesRecyclerViewAdapter;
    File[] files;
    ArrayList<Object> filesList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);
        context = this;
        rv_status = findViewById(R.id.rv_status);
        swipRefreshLayout = findViewById(R.id.swipRefreshLayout);


        Dexter.withContext(context).withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        setUpRefreshLayout();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();

        swipRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipRefreshLayout.setRefreshing(true);
                setUpRefreshLayout();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipRefreshLayout.setRefreshing(false);

                    }
                }, 1500);

            }
        });

    }

    private void setUpRefreshLayout() {
        filesList.clear();
        rv_status.setHasFixedSize(true);
        rv_status.setLayoutManager(new GridLayoutManager(context,2));
        storiesRecyclerViewAdapter = new StoriesRecyclerViewAdapter(context, getData());
        rv_status.setAdapter(storiesRecyclerViewAdapter);
        storiesRecyclerViewAdapter.notifyDataSetChanged();
    }

    private ArrayList<Object> getData() {
        StoryModel model;
        String targetPath = Environment.getExternalStorageDirectory().getAbsolutePath() + Constant.FOLDER_NAME + "Media/.Statuses";
        File targetDirectory = new File(targetPath);
        files = targetDirectory.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            model = new StoryModel();
            model.setUri(Uri.fromFile(file));
            model.setPath(files[i].getAbsolutePath());
            model.setFileName(file.getName());
            if (!model.getUri().toString().endsWith(".nomedia")) {
                filesList.add(model);
            }
        }
        return filesList;
    }
}