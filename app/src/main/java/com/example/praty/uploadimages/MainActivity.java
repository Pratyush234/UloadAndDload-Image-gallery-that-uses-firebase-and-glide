package com.example.praty.uploadimages;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
    private static final int RC_SIGN_IN = 1;
    private SignInButton mSignInButton;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mAuth;
    private String idToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSignInButton=(SignInButton) findViewById(R.id.SignInButton);
        mSignInButton.setColorScheme(SignInButton.COLOR_DARK);
        mSignInButton.setSize(SignInButton.SIZE_WIDE);
        setupGoogleSignIntext(mSignInButton);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        FirebaseApp.initializeApp(this);
        mAuth=FirebaseAuth.getInstance();
        ConfigureSignIn();


    }
    private void setupGoogleSignIntext(SignInButton mSignInButton) {
        for(int i=0;i<mSignInButton.getChildCount();i++)
        {
            View v= mSignInButton.getChildAt(i);

            if(v instanceof TextView){
                TextView tv=(TextView) v;
                tv.setText("Sign in with Google");
                return;
            }
        }
    }

    private void ConfigureSignIn(){
        GoogleSignInOptions options=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(MainActivity.this.getResources().getString(R.string.web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,options)
                .build();
        mGoogleApiClient.connect();

    }


    private void signInWithGoogle() {
        Intent mIntent= Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(mIntent,RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RC_SIGN_IN){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if(result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                idToken=account.getIdToken();

                AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
                firebaseAuthWithGoogle(credential);

            }

        }

    }

    private void firebaseAuthWithGoogle(AuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(!task.isSuccessful()){
                            Log.d("Storage","firebase authentication failed: "+task.getException().getMessage());
                            task.getException().printStackTrace();
                            Toast.makeText(MainActivity.this,"Authentication failed",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Intent mIntent=new Intent(MainActivity.this, TaskActivity.class);
                            startActivity(mIntent);

                            Toast.makeText(MainActivity.this,"Sign-in successful",Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }


    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
