package com.debdas.vishwajeet.rishav.ffs.foodforsurvival;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.debdas.vishwajeet.rishav.ffs.foodforsurvival.foodforsurvival.R;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class GoogleSignIn extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient googleApiClient;
    static  final  int REQ_CODE=9001;
    static  final String TAG="GoogleActivity";
    private TextView name,email,phone;
    private FirebaseAuth mAuth;
    private  GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_sign_in);
        findViewById(R.id.sign_in_button).setOnClickListener(this);

        findViewById(R.id.li_layout).setVisibility(View.GONE);

        name=findViewById(R.id.etname);
        email=findViewById(R.id.email);
        phone=findViewById(R.id.phone);
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                requestEmail()
                .requestIdToken("359419203679-mih6pi7514gsi7j7g8ekchktqfl1qgci.apps.googleusercontent.com").build();

         mGoogleApiClient =new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,gso).build();

         mAuth= FirebaseAuth.getInstance();

         mAuthListener = new FirebaseAuth.AuthStateListener() {
             @Override
             public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 FirebaseUser user= firebaseAuth.getCurrentUser();
                 if(user!=null){
                     Log.d(TAG,"onAuthStateChanged:SignIn"+user.getUid());
                 }else{
                     Log.d(TAG,"onAuthStateChanged:SignOut");

                 }
                 updateUI(user);
             }
         };
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthListener!=null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount account=result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            }else {
                updateUI(null);
            }
        }

    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {

        Log.d(TAG,"FirebaseAuthwithGoogle:"+account.getId());

        AuthCredential credential= GoogleAuthProvider.getCredential(account.getIdToken(),null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Log.d(TAG,"SignInwithCredential:onComplete:"+task.isSuccessful());
                if(!task.isSuccessful()){
                    Log.v(TAG,"SigninwithCredential",task.getException());
                    Toast.makeText(getApplicationContext(),"Authentication Failed",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    private void signIn() {
        Intent  intent=Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(intent,REQ_CODE);

    }

    private void updateUI(FirebaseUser user) {

        if(user!=null){
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            findViewById(R.id.li_layout).setVisibility(View.VISIBLE);
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            phone.setText(user.getPhoneNumber());
        }
        else {
            Toast.makeText(getApplicationContext(),"Signed Out",Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.sign_in_button:
                signIn();
                break;
        }

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed:"+connectionResult);
        Toast.makeText(getApplicationContext(),"Google Play Service Error",Toast.LENGTH_LONG).show();

    }

}
