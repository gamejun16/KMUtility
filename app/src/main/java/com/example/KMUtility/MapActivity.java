package com.example.KMUtility;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.KMUtility.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import androidx.appcompat.app.AppCompatActivity;

public class MapActivity extends AppCompatActivity {
//public class MapActivity extends  SwipeBackActivity {
    ImageView map_imageView;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);


        PhotoView photoView = (PhotoView)findViewById(R.id.imageView);
        setContentView(R.layout.activity_map);

        //setDragEdge(SwipeBackLayout.DragEdge.LEFT);

        map_imageView = (ImageView)findViewById(R.id.imageView);
        mAttacher = new PhotoViewAttacher(map_imageView);



    }
}
