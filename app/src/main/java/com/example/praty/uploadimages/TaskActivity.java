package com.example.praty.uploadimages;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TaskActivity extends AppCompatActivity {
    private Button mUploadButton;
    private Button mDownloadButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        mUploadButton=(Button) findViewById(R.id.upload);
        mDownloadButton=(Button) findViewById(R.id.download);

        mUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent= new Intent(TaskActivity.this, UploadActivity.class);
                startActivity(mIntent);
            }
        });

        mDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(TaskActivity.this, DisplayActivity.class);
                startActivity(mIntent);
            }
        });

    }
}
