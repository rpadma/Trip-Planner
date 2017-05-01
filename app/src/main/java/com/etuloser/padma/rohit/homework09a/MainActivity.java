package com.etuloser.padma.rohit.homework09a;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.zzbmn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.ChildEventListener;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;
import com.txusballesteros.bubbles.BubbleLayout;
import com.txusballesteros.bubbles.BubblesManager;
import com.txusballesteros.bubbles.OnInitializedCallback;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private BubblesManager bubblesManager;
    private Uri fullPhotoUri;
    StorageReference storageRef;
    DatabaseReference mDatabase;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    TripAdapter ta;
    ImageView tiv;
    DatabaseReference tDatabase;
    DatabaseReference df;
    DatabaseReference tf;
    com.daimajia.numberprogressbar.NumberProgressBar pb;
LinearLayout proll;
    User us;
    TextView txtusername;
    ImageView proimage;
    FirebaseUser u;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private String TAG = "MainActivity";
    ListView lvtrip;
trip tripobj;
    ArrayList<trip> tlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initializeBubblesManager();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tripobj=new trip();
         mAuth = FirebaseAuth.getInstance();
        lvtrip=(ListView)findViewById(R.id.triplist);
        u = mAuth.getCurrentUser();
        mDatabase= FirebaseDatabase.getInstance().getReference("users");
        tDatabase= FirebaseDatabase.getInstance().getReference("trips");
        df=mDatabase.child("user").child(u.getUid());

        tf=tDatabase.child("trip");
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                 u = firebaseAuth.getCurrentUser();
                if (u != null) {


                    Log.d(TAG, "onAuthStateChanged:signed_in:" + u.getUid());
                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        final View header=navigationView.getHeaderView(0);

        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Object> user= (HashMap<String, Object>) dataSnapshot.getValue();

                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(user);
                 us = gson.fromJson(jsonElement, User.class);

            //  us = dataSnapshot.getValue(User.class);

                if(us!=null) {

                    txtusername=(TextView)header.findViewById(R.id.nav_header_username);
                    proimage=(ImageView)header.findViewById(R.id.nav_header_profile_image);
                    txtusername.setText(us.getFirstname().toString()+" "+ us.getLastname().toString());
                    Picasso.with(getApplicationContext()).load(us.getImgurl()).placeholder(R.drawable.avatar_11_raster).into(proimage);

                    Log.d("onlogin","debug point");
           //         Log.d("username:",us.getFirstname().toString());
       //             txtusername.setText(us.getFirstname().toString() + us.getLastname().toString());
         //           Picasso.with(getApplicationContext()).load(us.getImgurl()).into(proimage);
                }
                //System.out.println(post);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
*/
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(R.layout.addtrip);
                final AlertDialog dialog = builder.create();
                dialog.show();
                pb=(com.daimajia.numberprogressbar.NumberProgressBar)dialog.findViewById(R.id.tripdp_progress_bar);
                pb.setVisibility(View.VISIBLE);
                final EditText edxtripname=(EditText)dialog.findViewById(R.id.add_tripname);
                final EditText edxlocationname=(EditText)dialog.findViewById(R.id.add_locationname);
                Button btncancel=(Button)dialog.findViewById(R.id.atcancel);
                Button btnaddtrip=(Button)dialog.findViewById(R.id.ataddtrip);
                ImageView btnivtrip=(ImageView) dialog.findViewById(R.id.Add_tripimageView);
                tiv=(ImageView)dialog.findViewById(R.id.tripdp);
                proll=(LinearLayout)dialog.findViewById(R.id.proll);

 btnivtrip.setOnClickListener(new View.OnClickListener() {
     @Override
     public void onClick(View v) {

         Intent intent = new  Intent(
                 Intent.ACTION_PICK,
                 android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
         intent.setType("image/*");

         if (intent.resolveActivity(getPackageManager()) != null) {

              startActivityForResult(intent, 123);
         }
     }
 });

                btncancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        dialog.hide();
                    }
                });

                btnaddtrip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        tripobj.setTripname(edxtripname.getText().toString());
                        tripobj.setTripLocationname(edxlocationname.getText().toString());
                        tripobj.setTowner(u.getUid());
                        tripobj.setCreatedate(String.valueOf(System.currentTimeMillis()));
                        tripobj.setTownername(u.getDisplayName());
                        ArrayList<String> sublist=new ArrayList<String>();
                        if(us.getSubltrip()!=null) {
                        sublist = us.getSubltrip();
                          }
                        if(sublist==null)
                        {
                            sublist=new ArrayList<String>();
                        }

                         sublist.add(tripobj.getTkey());
                        us.setSubltrip(sublist);

                        ArrayList <String> memlist = tripobj.getMembers();
                        if(memlist==null)
                        {
                            memlist=new ArrayList<String>();
                        }
                        memlist.add(us.getUid());
                        tripobj.setMembers(memlist);
                        mDatabase.child("user").child(us.getUid()).setValue(us);
                        tDatabase.child("trip").child(tripobj.getTkey()).setValue(tripobj);
                        Toast.makeText(v.getContext(),"Trip is Added",Toast.LENGTH_SHORT).show();
                        dialog.hide();
                    }
                });


            }


        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        tf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                tlist=new ArrayList<trip>();
                for(DataSnapshot d : dataSnapshot.getChildren()) {

                    trip toadd = new trip();
                    toadd = d.getValue(trip.class);

                    if((toadd.getTowner().equals(u.getUid())) || (toadd.getMembers().contains(u.getUid())))
                    tlist.add(toadd);

                    Log.d("trip1",toadd.getTripname().toString());

                }
                if(tlist.size()!= 0)
                {
                }


                SetView();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        }

/*
    private void initializeBubblesManager() {
        bubblesManager = new BubblesManager.Builder(this)
                .setTrashLayout(R.layout.bubbledestroy)
                .setInitializationCallback(new OnInitializedCallback() {
                    @Override
                    public void onInitialized() {
                        addNewBubble();
                    }
                })
                .build();
        bubblesManager.initialize();
    }

    private void addNewBubble() {
        BubbleLayout bubbleView = (BubbleLayout) LayoutInflater.from(MainActivity.this).inflate(R.layout.bubblelview, null);
        bubbleView.setOnBubbleRemoveListener(new BubbleLayout.OnBubbleRemoveListener() {
            @Override
            public void onBubbleRemoved(BubbleLayout bubble) { }
        });
        bubbleView.setOnBubbleClickListener(new BubbleLayout.OnBubbleClickListener() {

            @Override
            public void onBubbleClick(BubbleLayout bubble) {
                Toast.makeText(getApplicationContext(), "Clicked !",
                        Toast.LENGTH_SHORT).show();
            }
        });
        bubbleView.setShouldStickToWall(true);
        bubblesManager.addBubble(bubbleView, 60, 20);
    }
*/


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 123 && resultCode == RESULT_OK) {
            fullPhotoUri = data.getData();
            final String key1 = tDatabase.child("trip").push().getKey();
            //   user.setKey(key1);
            //  user.setUserID(user.getUid());

            storageRef = storage.getReference();
            StorageReference riversRef = storageRef.child("trips/images/"+key1+".png");

            proll.setVisibility(View.VISIBLE);
            UploadTask uploadTask = riversRef.putFile(fullPhotoUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    tripobj.setTimgurl(downloadUrl.toString());
                    tripobj.setTkey(key1);
                    //  mRoot.child("user").child(fuser.getUid()).setValue(user);
                    //  Toast.makeText(Home.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();
                   Picasso.with(getApplicationContext()).load(downloadUrl).into(tiv);

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    int progress = (int)Math.round((100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount());

                    if(progress>0) {

                        pb.setProgress(progress);

                        if(progress==100)
                        {
                            proll.setVisibility(View.INVISIBLE);
                        }
                        Log.d("progress",String.valueOf(progress));
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





    public void SetView()
    {
        ArrayList<trip> ttlist=new ArrayList<trip>();
        ttlist.addAll(tlist);
if(ttlist.size()>0) {
    ta = new TripAdapter(this, R.layout.childrowtrip, ttlist,u.getUid());
    ta.setNotifyOnChange(true);
    lvtrip.setAdapter(ta);
}

Log.d("InSetup","Inview ");
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_frd) {

            Intent i=new Intent(MainActivity.this,FrdActivity.class);
            i.putExtra("currentuser",us);
            startActivity(i);

            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_logout) {

            FirebaseAuth.getInstance().signOut();
            Intent loggOut = new Intent(MainActivity.this,LoginActivity.class);
            loggOut.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loggOut);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);


        return true;
    }


    public void editprofile(View v)
    {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
drawer.closeDrawer(GravityCompat.START);
        Intent i=new Intent(this,Profile.class);
        startActivity(i);
    }

    public void openchat(String id,String name)
    {
        Intent i=new Intent(this,ChatActivity.class);
        i.putExtra("tripname",name);
        i.putExtra("tripid",id);
        startActivity(i);

    }


    @Override
    protected void onPause() {
        super.onPause();
     //   addNewBubble();
        Log.d("Onpause:","on pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // bubblesManager.recycle();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d("Onresume","Onresume");
    }
}
