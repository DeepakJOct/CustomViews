package app.com.customviews.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.jsibbold.zoomage.ZoomageView;

import java.io.File;

import app.com.customviews.R;

public class GalleryPreview extends AppCompatActivity {
    ZoomageView GalleryPreviewImg;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gallery_preview);
        Intent intent = getIntent();
        path = intent.getStringExtra("path");
        GalleryPreviewImg = findViewById(R.id.GalleryPreviewImg);
        Glide.with(GalleryPreview.this)
                .load(new File(path))
                .into(GalleryPreviewImg);

    }
}
