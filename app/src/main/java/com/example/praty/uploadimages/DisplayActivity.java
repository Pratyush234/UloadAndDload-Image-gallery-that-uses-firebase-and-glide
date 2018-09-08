package com.example.praty.uploadimages;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DisplayActivity extends AppCompatActivity {

    private RecyclerView mRecycler;
    private DatabaseReference mRef;
    private MyAdapter mAdapter;
    private List<UrlModel> mList=new ArrayList<>();
    private ChildEventListener mListener= new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            UrlModel mObj=dataSnapshot.getValue(UrlModel.class);
            mList.add(mObj);
            mAdapter.notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        mRef= FirebaseDatabase.getInstance().getReference();
        mRef=mRef.child("URL_LINKS");
        mRef.addChildEventListener(mListener);
        mRecycler=(RecyclerView) findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager gridLayoutManager=new GridLayoutManager(getApplicationContext(),2);
        mRecycler.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter=new MyAdapter(mList, getApplicationContext(), new ItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                //Toast.makeText(DisplayActivity.this, "Item "+position+" is clicked",Toast.LENGTH_SHORT).show();
                Intent mIntent=new Intent(DisplayActivity.this, DisplayItemActivity.class);
                mIntent.putExtra("url",mList.get(position).getmUrl());
                startActivity(mIntent);


            }
        });
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mRef.removeEventListener(mListener);
    }
}
