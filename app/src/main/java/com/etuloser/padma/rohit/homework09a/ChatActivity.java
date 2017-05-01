package com.etuloser.padma.rohit.homework09a;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity implements ChatAdapter.ItemClickCallBack {
    TextView username;
    ImageView logout;
    FirebaseUser user;
    String tripid;
    ImageView btnSend,bntGal;
    EditText chatmsg;
    private RecyclerView recyclerView;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    static final int REQUEST_IMAGE_GET = 199;
    private Uri fullPhotoUri;
    private ChatAdapter chatAdapter;
    StorageReference storageRef;
    DatabaseReference mRoot;DatabaseReference  mConditionRef ;
//static int imagflag=0;
    ArrayList<message> allChats = new ArrayList<message>();
    trip tripobj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String tripname="";
       // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if(getIntent().getExtras()!=null)
        {
            tripid=(String)getIntent().getExtras().getString("tripid");
            tripname=(String)getIntent().getExtras().getString("tripname");
        }

        mRoot =  FirebaseDatabase.getInstance().getReference("trips").child("trip").child(tripid);

        user= FirebaseAuth.getInstance().getCurrentUser();

        btnSend =  (ImageView)findViewById(R.id.imageViewsend);
        bntGal =  (ImageView)findViewById(R.id.imageViewGal);
        chatmsg = (EditText)findViewById(R.id.editTextchat);

        username = (TextView)findViewById(R.id.textViewUser);
        logout = (ImageView)findViewById(R.id.imageViewLogout);
        user = FirebaseAuth.getInstance().getCurrentUser();
        username.setText(tripname);
        recyclerView = (RecyclerView) findViewById(R.id.container);
        LinearLayoutManager llm=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        llm.setStackFromEnd(true);
        recyclerView.setLayoutManager(llm);

        mConditionRef=mRoot.child("msgs");
        //Log.d("path",mConditionRef.toString());

        /*
        mConditionRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                HashMap<String,Object> trip= (HashMap<String, Object>) dataSnapshot.getValue();

                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(trip);
                tripobj = gson.fromJson(jsonElement, trip.class);
//Log.d("trip object",tripobj.getTripname());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

*/

        mConditionRef.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                message toadd = new message();
                HashMap<String,Object> user= (HashMap<String, Object>) dataSnapshot.getValue();

                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(user);
                toadd = gson.fromJson(jsonElement,message.class);

                FirebaseUser usd= FirebaseAuth.getInstance().getCurrentUser();

                if(toadd.getDeletedusers()!=null) {
                    if (!toadd.getDeletedusers().contains(usd.getUid())) {
                        allChats.add(toadd);
                    }
                }
                else
                {
                    allChats.add(toadd);
                }
                if(allChats.size()!= 0)
                {
                }
setview();
             //   chatAdapter.notifyDataSetChanged();



            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        bntGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new  Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(intent, REQUEST_IMAGE_GET);
                }
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(chatmsg.getText().length()<1  )
                {

                 //   Toast.makeText(v.getContext(),"enter message",Toast.LENGTH_SHORT).show();
                }
                else {
                    message toSend = new message();
                    toSend.setMsg(chatmsg.getText().toString());
                    toSend.setName(user.getDisplayName());
                    toSend.setMimgurl("NA");
                    toSend.setWhen(String.valueOf(System.currentTimeMillis()));
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    String key1 = mRoot.child("msgs").push().getKey();
                    toSend.setMsgkey(key1);
                    toSend.setUserid(user.getUid());
                    mRoot.child("msgs").child(key1).setValue(toSend);

                    chatmsg.setText("");
                   // imagflag=0;

                }
            }
        });



    }


    public void setview()
    {

        ArrayList<message> msglist=new ArrayList<message>();
        msglist.addAll(allChats);
        if(msglist.size()>0) {
            chatAdapter = new ChatAdapter(allChats, this, R.layout.chatitem, user.getUid());
            chatAdapter.notifyDataSetChanged();
            recyclerView.setAdapter(chatAdapter);
        }
    }
    public void getmsg()
    {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
            fullPhotoUri = data.getData();


            final message toSend = new message();
            toSend.setMsg("");
            toSend.setName(user.getDisplayName());
            toSend.setWhen(String.valueOf(System.currentTimeMillis()));
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            final String key1 = mRoot.child("msgs").push().getKey();
            toSend.setMsgkey(key1);
            toSend.setUserid(user.getUid());
            toSend.setName(user.getDisplayName());

            storageRef = storage.getReference();
            StorageReference riversRef = storageRef.child("chats/images/" + key1 + ".png");


            UploadTask uploadTask = riversRef.putFile(fullPhotoUri);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    toSend.setMimgurl(downloadUrl.toString());
                    mRoot.child("msgs").child(key1).setValue(toSend);
                   // imagflag=1;
                    //  Toast.makeText(Home.this, downloadUrl.toString(), Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
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




    @Override
    public void OnMsgDeleteClick(final message mo) {

       final DatabaseReference msgref= mRoot.child("msgs").child(mo.getMsgkey());
        msgref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                message msgobj=new message();
                HashMap<String,Object> user= (HashMap<String, Object>) dataSnapshot.getValue();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(user);
                msgobj = gson.fromJson(jsonElement, message.class);

                ArrayList<String> deluserid=new ArrayList<String>();
                if(msgobj.getDeletedusers()!=null) {
                 deluserid=msgobj.getDeletedusers();
                }
                FirebaseUser fmsgu = FirebaseAuth.getInstance().getCurrentUser();
                deluserid.add(fmsgu.getUid());
                msgobj.setDeletedusers(deluserid);

                msgref.setValue(msgobj);

                allChats.remove(mo);
                setview();
chatAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }




}
