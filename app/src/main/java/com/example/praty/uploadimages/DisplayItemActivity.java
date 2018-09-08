package com.example.praty.uploadimages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class DisplayItemActivity extends AppCompatActivity {

    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);

        mImageView=(ImageView) findViewById(R.id.itemImage);
        setupImage();


    }

    private void setupImage() {
        Intent mIntent=getIntent();
        String url_link=mIntent.getStringExtra("url");

        GlideApp.with(getApplicationContext()).load(url_link)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(mImageView);
    }
}
