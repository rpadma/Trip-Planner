package com.etuloser.padma.rohit.homework09a;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener  {


    private EditText edxemail;
    private EditText edxpwd;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "LoginActivity";
    ProgressDialog mProgress;

    private static final int RC_SIGN_IN = 9001;

    private GoogleApiClient mGoogleApiClient;

    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        edxemail=(EditText)findViewById(R.id.editTextEmail);
        edxpwd=(EditText)findViewById(R.id.editTextPassword);

        SignInButton signInButton = (SignInButton) findViewById(R.id.gLogin);
        signInButton.setSize(SignInButton.SIZE_STANDARD);
        findViewById(R.id.gLogin).setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("300113930932-0pi71kfaoimsgf8d3poo80dginc55vfh.apps.googleusercontent.com")
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Intent Expense = new Intent(LoginActivity.this,MainActivity.class);
                    Expense.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(Expense);

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                updateUI(user);

            }
        };


    }


    private void updateUI(FirebaseUser user) {
        edxemail.setText("");
        edxpwd.setText("");
    }

    private void hideProgressDialog() {
        mProgress.hide();
    }

    private void showProgressDialog() {
        mProgress = new ProgressDialog(LoginActivity.this);
        mProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgress.setCancelable(false);
        mProgress.setMessage("Authenticating...");
        mProgress.show();
    }



    public void lSignup(View v)
{
    Intent i=new Intent(this,SignUp.class);
    startActivity(i);

}

    @Override
    public void onClick(View v) {


        signIn();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }


    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            final GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            final Intent i =new Intent(this,MainActivity.class);

            final GoogleSignInAccount acct = result.getSignInAccount();
            if(acct!=null) {

                AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
                mAuth.signInWithCredential(credential)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithCredential:success");
                                    SaveGDetails(result);
                                    i.putExtra("Stringdata", acct.getDisplayName());
                                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                                    startActivity(i);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.w(TAG, "signInWithCredential:failure", task.getException());


                                }

                                // ...
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Authentication failed."+e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
            else
            {
                Toast.makeText(this,"Cannot signin",Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void nsignIn(String email, String password) {

        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "Login failed-Invalid Email and Password Combination",
                                    Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Intent Expense = new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(Expense);
                        }
                        hideProgressDialog();
                    }
                });

    }

    public void nLogin(View v)
    {

        FirebaseAuth.getInstance().signOut();
        nsignIn(edxemail.getText().toString(), edxpwd.getText().toString());

    }


    private boolean validateForm() {
        boolean valid = true;

        String email = edxemail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            edxemail.setError("Mandatory.");
            valid = false;
        } else {
            edxemail.setError(null);
        }


        String password = edxpwd.getText().toString();
        if (TextUtils.isEmpty(password)) {
            edxpwd.setError("Mandatory.");
            valid = false;
        } else {
            edxemail.setError(null);
        }


        return valid;
    }



    public void SaveGDetails(GoogleSignInResult result)
    {
        User u=new User();
        u.setFirstname(result.getSignInAccount().getGivenName());
        u.setLastname(result.getSignInAccount().getFamilyName());
        u.setEmail(result.getSignInAccount().getEmail());

        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        if(result.getSignInAccount().getPhotoUrl()!=null) {
            u.setImgurl(result.getSignInAccount().getPhotoUrl().toString());
        }
            mDatabase = FirebaseDatabase.getInstance().getReference("users");
        u.setUid(user.getUid());
        mDatabase.child("user").child(user.getUid()).setValue(u);


    }

/*
    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }
*/
    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();


          //  mStatusTextView.setText(getString(R.string.signed_in_fmt, acct.getDisplayName()));
            updateUI(true);

        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) {
            findViewById(R.id.gLogin).setVisibility(View.GONE);
          //  findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
        } else {
           // mStatusTextView.setText(R.string.signed_out);

            findViewById(R.id.gLogin).setVisibility(View.VISIBLE);
           // findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
        }
    }

}

