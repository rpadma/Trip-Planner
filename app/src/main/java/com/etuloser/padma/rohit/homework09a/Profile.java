package com.etuloser.padma.rohit.homework09a;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;

public class Profile extends AppCompatActivity {


    Spinner gspinner;
    static final int REQUEST_IMAGE_GET = 145;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private Uri fullPhotoUri;
     User edituser;
    ImageView piv;
    EditText fname;
    EditText lname;
    com.daimajia.numberprogressbar.NumberProgressBar pb;
    //Spinner spgender;

    LinearLayout llro;
    StorageReference storageRef;
    FirebaseUser u;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mDatabase;
    DatabaseReference df;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        piv=(ImageView)findViewById(R.id.profile_image);
        gspinner=(Spinner)findViewById(R.id.spinnergender);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.
                createFromResource(this, R.array.gender, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gspinner.setAdapter(adapter);
        fname=(EditText)findViewById(R.id.profile_firstname);
        lname=(EditText)findViewById(R.id.profile_lastname);
        llro=(LinearLayout)findViewById(R.id.llpro);
        pb=(com.daimajia.numberprogressbar.NumberProgressBar)findViewById(R.id.profiledp_progress_bar);
        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("uid",fuser.getUid());
        mDatabase= FirebaseDatabase.getInstance().getReference("users");
        df=mDatabase.child("user").child(fuser.getUid());
        edituser=new User();


        df.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                edituser = dataSnapshot.getValue(User.class);
                if(edituser!=null) {
                    Log.d("gender",edituser.getGender());
                    fname.setText(edituser.getFirstname().toString());
                    lname.setText(edituser.getLastname().toString());
                    gspinner.setSelection( Integer.valueOf(edituser.getGender()));
                    Picasso.with(getApplicationContext()).load(edituser.getImgurl()).placeholder(R.drawable.avatar_11_raster).into(piv);


                    Log.d("Uid",edituser.getFirstname());
                    Log.d("onlogin","debug point");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });




    }


    public void changeprofileImage(View v)
    {
        Intent intent = new  Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            fullPhotoUri = data.getData();
            final String key1 = mDatabase.child("user").push().getKey();
         //   user.setKey(key1);
          //  user.setUserID(user.getUid());
             llro.setVisibility(View.VISIBLE);
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
                    edituser.setImgurl(downloadUrl.toString());
                  //  mRoot.child("user").child(fuser.getUid()).setValue(user);
                    //  Toast.makeText(Home.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                    Picasso.with(getApplicationContext()).load(downloadUrl).into(piv);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int)Math.round((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());

                    if(progress>0)
                    {
                        pb.setProgress(progress);

                        if(progress==100)
                        {
                            llro.setVisibility(View.INVISIBLE);
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


    public void saveclick(View v)
    {

        edituser.setFirstname(fname.getText().toString());
        edituser.setLastname(lname.getText().toString());
        edituser.setGender(String.valueOf(gspinner.getSelectedItemId()));
        mDatabase.child("user").child(edituser.getUid()).setValue(edituser);
        Toast.makeText(this,"Updated Profile",Toast.LENGTH_SHORT).show();

        finish();

    }

}
