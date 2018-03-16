package com.etuloser.padma.rohit.homework09a;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidviewhover.BlurLayout;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.*;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShowTripActivity extends AppCompatActivity {

    int PLACE_PICKER_REQUEST = 1;
    private BlurLayout mSampleLayout;
    trip t;
    DatabaseReference mDatabase;
    DatabaseReference df;

    DatabaseReference uDatabase;
    DatabaseReference uf;

    DatabaseReference tDatabase;
    DatabaseReference tf;
     trip dtrip;
    FirebaseUser fuser;
    PlaceAdapter ta;
    MemberAdapter ma;
    ListView plv;
    ListView frdlv;
    ArrayList<User> memlist;
    ArrayList<String> memberlist;


    ArrayList<Place> placelist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_trip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        plv=(ListView)findViewById(R.id.lvshowplace);
        frdlv=(ListView)findViewById(R.id.lvshowmembers);

        fuser= FirebaseAuth.getInstance().getCurrentUser();
        if(getIntent().getExtras()!=null)
        {
            t=(trip)getIntent().getExtras().getSerializable("tripobj");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    showplacepicker();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                }

                //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //         .setAction("Action", null).show();
            }
        });

        mSampleLayout = (BlurLayout)findViewById(R.id.show_blur_trip_layout);
        View hover3 = LayoutInflater.from(this).inflate(R.layout.hoverlayout, null);
        mSampleLayout.setHoverView(hover3);
        mSampleLayout.addChildAppearAnimator(hover3, R.id.eye, Techniques.Landing);
        mSampleLayout.addChildDisappearAnimator(hover3, R.id.eye, Techniques.TakingOff);
        mSampleLayout.enableZoomBackground(true);
        mSampleLayout.setBlurDuration(1200);

        mDatabase= FirebaseDatabase.getInstance().getReference("trips");
        df=mDatabase.child("trip").child(t.getTkey());



        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                placelist =new ArrayList<Place>();
                trip toadd = new trip();
                 HashMap<String,Object> user= (HashMap<String, Object>) dataSnapshot.getValue();

                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(user);
                toadd = gson.fromJson(jsonElement,trip.class);
                //toadd=dataSnapshot.getValue(trip.class);
                FirebaseUser usd= FirebaseAuth.getInstance().getCurrentUser();

              //  placelist=new ArrayList<Place>();
                placelist=toadd.getPlaces();



                if(toadd.getMembers()!=null)
                {
                    memberlist=toadd.getMembers();
                }

                 uDatabase= FirebaseDatabase.getInstance().getReference("Users");
                         uf=uDatabase.child("User");

                uf.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        memlist=new ArrayList<User>();

                        for(DataSnapshot d : dataSnapshot.getChildren()) {
                            User user = new User();
                            HashMap<String,Object> map= (HashMap<String, Object>)d.getValue();
                            Gson gson = new Gson();
                            JsonElement jsonElement = gson.toJsonTree(map);
                            user = gson.fromJson(jsonElement, User.class);

                                if ((memberlist.contains(user.getUid()))) {


                                      memlist.add(user);
                                }
                        }







                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





                setplace( placelist);
                setmember(memlist);





            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void showplacepicker() throws GooglePlayServicesNotAvailableException, GooglePlayServicesRepairableException {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        startActivityForResult(builder.build(ShowTripActivity.this), PLACE_PICKER_REQUEST);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlacePicker.getPlace(data, this);
                final Place pl=new Place();
                pl.setPlacename(place.getName().toString());
                pl.setLatitude(String.valueOf(place.getLatLng().latitude));
                pl.setLongitude(String.valueOf(place.getLatLng().longitude));
                // pl.setLatlng(place.getLatLng());
                pl.setAddedbyUid(fuser.getUid());
                pl.setAddbyname(fuser.getDisplayName());
                mDatabase= FirebaseDatabase.getInstance().getReference("trips");
                df=mDatabase.child("trip").child(t.getTkey());

                df.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        HashMap<String,Object> user= (HashMap<String, Object>) dataSnapshot.getValue();
                        Gson gson = new Gson();
                        JsonElement jsonElement = gson.toJsonTree(user);
                        dtrip = gson.fromJson(jsonElement, trip.class);

                        if(dtrip.getPlaces()!=null)
                        {
                            placelist=dtrip.getPlaces();
                        }
                        else
                        {
                            placelist=new ArrayList<Place>();
                        }

                        placelist.add(pl);
                        dtrip.setPlaces(placelist);

                        tDatabase=FirebaseDatabase.getInstance().getReference("trips");
                        tf=tDatabase.child("trip").child(dtrip.getTkey());
                        tf.setValue(dtrip);


                        setplace(placelist);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


                String toastMsg = String.format("Place: %s", place.getName());
                Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void setplace(ArrayList<Place> pa)
    {

        if(pa!=null) {

            ArrayList<Place> ttlist = new ArrayList<Place>();
            ttlist.addAll(pa);
            if (ttlist.size() > 0) {
                ta = new PlaceAdapter(this, R.layout.placechilditem, ttlist, t.getTkey());
                ta.setNotifyOnChange(true);
                plv.setAdapter(ta);
            }
        }

    }

public void setmember(ArrayList<User> ulist)
{


    if(ulist!=null) {

        ArrayList<User> ttlist = new ArrayList<User>();
        ttlist.addAll(ulist);
        if (ttlist.size() > 0) {
            ma = new MemberAdapter(this, R.layout.frdrowitem, ttlist);
            ma.setNotifyOnChange(true);
            frdlv.setAdapter(ta);
        }
    }
}
    public void deleteplace(final Place p,String tuid)
    {


        mDatabase=FirebaseDatabase.getInstance().getReference("trips");
        df=mDatabase.child("trip").child(tuid);


        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                placelist =new ArrayList<Place>();
                trip toadd = new trip();
                HashMap<String,Object> user= (HashMap<String, Object>) dataSnapshot.getValue();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(user);
                toadd = gson.fromJson(jsonElement,trip.class);
                //toadd=dataSnapshot.getValue(trip.class);
                FirebaseUser usd= FirebaseAuth.getInstance().getCurrentUser();

                if(toadd.getPlaces()!=null)
                {
                    placelist=toadd.getPlaces();
placelist.remove(p);
                }

                toadd.setPlaces(placelist);


                tDatabase=FirebaseDatabase.getInstance().getReference("trips");
                tf=tDatabase.child("trip").child(toadd.getTkey());
                tf.setValue(toadd);

                //  placelist=new ArrayList<Place>();



                setplace( placelist);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void ShowNavigation()
    {

        Log.d("placelist size",String.valueOf(placelist.size()));
        Intent i=new Intent(this,MapRoundTripActivity.class);
                i.putExtra("Placeobj",placelist);
        startActivity(i);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mapmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.round_settings) {

            ShowNavigation();
            return true;
        }
else
    if(id==R.id.open_settings)
    {

    }
        return super.onOptionsItemSelected(item);
    }

}
