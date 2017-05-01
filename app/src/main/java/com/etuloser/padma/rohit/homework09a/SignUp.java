package com.etuloser.padma.rohit.homework09a;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class SignUp extends AppCompatActivity {

    private EditText mEmailField;
    private EditText mPasswordField,cPasswordField;
    private EditText mfirstName,mLastName;
    private FirebaseAuth mAuth;
    private String TAG = "signActivity";
    private FirebaseAuth.AuthStateListener mAuthListener;
    ProgressDialog progress;
    Button signUp,cancel;
    User u;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef;
LinearLayout llsignup;
    com.daimajia.numberprogressbar.NumberProgressBar pb;

    FirebaseUser fu;
    String profilepicurl=null;
    private Uri fullPhotoUri;
Spinner gspinner;

    DatabaseReference mDatabase;

ImageView piv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mEmailField = (EditText) findViewById(R.id.sign_up_email_et);
        mPasswordField = (EditText) findViewById(R.id.sign_up_password_et);
        cPasswordField=(EditText)findViewById(R.id.sign_up_repassword_et);
        mfirstName = (EditText) findViewById(R.id.sign_up_firstname_et);
        mLastName = (EditText) findViewById(R.id.sign_up_lastname_et);
        piv=(ImageView)findViewById(R.id.signup_image);
        gspinner=(Spinner)findViewById(R.id.sspinnergender);
        llsignup=(LinearLayout)findViewById(R.id.llsignup);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gspinner.setAdapter(adapter);

        signUp = (Button) findViewById(R.id.sign_up_register_btn);
        pb=(com.daimajia.numberprogressbar.NumberProgressBar)findViewById(R.id.signupdp_progress_bar);
        cancel = (Button) findViewById(R.id.sCancel);


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {


                } else {

                }


            }
        };


    }



    private void hideProgressDialog() {
        progress.hide();
    }

    private void showProgressDialog() {
        progress = new ProgressDialog(SignUp.this);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setCancelable(false);
        progress.setMessage("Please Wait...");
        progress.show();
    }

    public void changeprofileImage(View v)
    {

        Intent intent = new  Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, 1345);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1345 && resultCode == RESULT_OK) {
            fullPhotoUri = data.getData();
            mDatabase = FirebaseDatabase.getInstance().getReference("users");

            final String key1 = mDatabase.child("user").push().getKey();
            //   user.setKey(key1);
            //  user.setUserID(user.getUid());

            llsignup.setVisibility(View.VISIBLE);
            storageRef = storage.getReference();
            StorageReference riversRef = storageRef.child("Users/images/"+key1+".png");


            UploadTask uploadTask = riversRef.putFile(fullPhotoUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    profilepicurl= downloadUrl.toString();
                    //  mRoot.child("user").child(fuser.getUid()).setValue(user);
                    //  Toast.makeText(Home.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                    Picasso.with(getApplicationContext()).load(downloadUrl).into(piv);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int)Math.round ((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());
if(progress>0)
{
    pb.setProgress(progress);
    if(progress==100)
    {
        llsignup.setVisibility(View.INVISIBLE);
    }
}
                    System.out.println("Upload is " + progress + "% done");
                }
            }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                    System.out.println("Upload is paused");
                }
            });

        }
    }




    public void register(View v)
    {
        FirebaseAuth.getInstance().signOut();
        createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());



    }


    private void createAccount(String email, String password) {
        Log.d(TAG, "createAccount:" + email);
        if (!validateForm()) {
            return;
        }


        showProgressDialog();


        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUp.this, task.getException().getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(mfirstName.getText().toString() + " " + mLastName.getText().toString())
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                                u=new User();
                                                u.setEmail(mEmailField.getText().toString());
                                                u.setLastname(mLastName.getText().toString());
                                                u.setFirstname(mfirstName.getText().toString());
                                                u.setGender(String.valueOf(gspinner.getSelectedItemId()));
                                                if(profilepicurl!=null)
                                                u.setImgurl(profilepicurl);
                                                fu = FirebaseAuth.getInstance().getCurrentUser();

                                                u.setUid(fu.getUid());
                                                //FirebaseAuth.getInstance().signOut();
                                                mDatabase = FirebaseDatabase.getInstance().getReference("users");
                                                String id=mDatabase.push().getKey();
                                                u.setKey(id);
                                                mDatabase.child("user").child(fu.getUid()).setValue(u);
                                                Intent toSend = new Intent(SignUp.this, MainActivity.class);
                                                startActivity(toSend);
                                                updateUI();
                                            }
                                        }
                                    });


                        }
                        hideProgressDialog();

                    }
                });
    }

    public void sCancel(View v)
    {
        Intent login = new Intent(SignUp.this,LoginActivity.class);
        startActivity(login);
        finish();
    }


    private boolean validateForm() {
        boolean valid = true;

        String email = mEmailField.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mEmailField.setError("Mandatory.");
            valid = false;
        } else {
            mEmailField.setError(null);
        }

        String password = mPasswordField.getText().toString();
        String cpassword=cPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Mandatory.");
            valid = false;
        } else {

            mPasswordField.setError(null);

        }

        if (TextUtils.isEmpty(cpassword)) {
            cPasswordField.setError("Mandatory.");
            valid = false;
        } else {

            cPasswordField.setError(null);

        }

        if(!cpassword.equals(password))
        {

            mPasswordField.setError("enter same password");
            cPasswordField.setError("enter same password");
            valid=false;
        }
        else
        {
            mPasswordField.setError(null);
            cPasswordField.setError(null);
        }



        String firstName = mfirstName.getText().toString();
        if (TextUtils.isEmpty(firstName)) {
            mfirstName.setError("Mandatory.");
            valid = false;
        } else {
            mfirstName.setError(null);
        }


        String lastName = mLastName.getText().toString();
        if (TextUtils.isEmpty(lastName)) {
            mLastName.setError("Mandatory.");
            valid = false;
        } else {
            mLastName.setError(null);
        }



        return valid;
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

    private void updateUI() {
        mEmailField.setText("");
        mPasswordField.setText("");
        mfirstName.setText("");
        mLastName.setText("");
        cPasswordField.setText("");

    }


}
