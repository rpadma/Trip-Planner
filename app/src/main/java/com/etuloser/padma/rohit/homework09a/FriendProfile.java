package com.etuloser.padma.rohit.homework09a;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class FriendProfile extends AppCompatActivity {

    TextView edxfrdgender;
    TextView edxfrdfullname;
    ImageView frdimg;
    friend fd;
    User ufrd;
    User cuser;
    FrdTripAdapter ta;
    DatabaseReference mDatabase;
    DatabaseReference tDatabase;
    DatabaseReference df;
    DatabaseReference tf;
    ArrayList<trip> frdtrips;
     ListView lv;
     FirebaseUser fuser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        edxfrdfullname=(TextView)findViewById(R.id.frd_profile_fullname);
        edxfrdgender=(TextView)findViewById(R.id.frd_profile_Gender);
        frdimg=(ImageView)findViewById(R.id.frd_profile_image);
        lv=(ListView)findViewById(R.id.lvdisplayrooms);
        if(getIntent().getExtras()!=null)
        {
            fd=(friend)getIntent().getExtras().getSerializable("frdd");
        }
         fuser = FirebaseAuth.getInstance().getCurrentUser();


        mDatabase= FirebaseDatabase.getInstance().getReference("users");
        df=mDatabase.child("user").child(fd.getFuid());


        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ufrd = dataSnapshot.getValue(User.class);
                if(ufrd!=null) {
                    Log.d("gender",ufrd.getGender());
                    edxfrdfullname.setText(ufrd.getFirstname().toString()+" "+ufrd.getLastname().toString());
                    if(ufrd.getGender().equals("0"))
                    {
                     edxfrdgender.setText("Gender : Male");
                    } if(ufrd.getGender().equals("1"))
                    {
                        edxfrdgender.setText("Gender : Female");
                    }
                    else
                    {
                        edxfrdgender.setText("Gender : Other");
                    }
                    Picasso.with(getApplicationContext()).load(ufrd.getImgurl()).placeholder(R.drawable.avatar_11_raster).into(frdimg);



                    ArrayList<String> ltrip=ufrd.getSubltrip();

                    tDatabase=FirebaseDatabase.getInstance().getReference("trips");
                    tf=tDatabase.child("trip");

                    tf.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            frdtrips=new ArrayList<trip>();
                            for(DataSnapshot d:dataSnapshot.getChildren())
                            {

                                trip t=d.getValue(trip.class);

                                if(t.getTowner().equals(ufrd.getUid()))
                                {
                                    frdtrips.add(t);
                                }
                            }


                            showTrips();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }


    public void showTrips(){


        ArrayList<trip> ttlist=new ArrayList<trip>();
        ttlist.addAll(frdtrips);
        if(ttlist.size()>0) {
            ta = new FrdTripAdapter(this, R.layout.childrowtrip, ttlist,fuser.getUid());
           // ta.setNotifyOnChange(true);
            lv.setAdapter(ta);
        }
    }


    public void jointrip(final trip tp,String cuid)
    {
        ArrayList<String> memberlist=new ArrayList<String>();

        if(tp.getMembers()!=null)
        {
            memberlist=tp.getMembers();
        }
        memberlist.add(fuser.getUid());
        tDatabase=FirebaseDatabase.getInstance().getReference("trips");
        tf=tDatabase.child("trip").child(tp.getTkey());
        tp.setMembers(memberlist);
        tf.setValue(tp);

        mDatabase=FirebaseDatabase.getInstance().getReference("users");
        df=mDatabase.child("user").child(cuid);

        df.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Object> user= (HashMap<String, Object>) dataSnapshot.getValue();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(user);
                cuser = gson.fromJson(jsonElement, User.class);

                ArrayList<String> subtriplist=new ArrayList<String>();
                if(cuser.getSubltrip()!=null)
                {
                    subtriplist=cuser.getSubltrip();
                }

                subtriplist.add(tp.getTkey());
                cuser.setSubltrip(subtriplist);

                mDatabase= FirebaseDatabase.getInstance().getReference("users");
                df=mDatabase.child("user").child(cuser.getUid());
                df.setValue(cuser);
//                ta.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });






    }

}
