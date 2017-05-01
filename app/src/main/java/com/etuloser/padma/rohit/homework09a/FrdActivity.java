package com.etuloser.padma.rohit.homework09a;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

public class FrdActivity extends AppCompatActivity {

    DatabaseReference mDatabase;
    DatabaseReference df;
    DatabaseReference dfuser;
    ListView flv;
    private String TAG = "FrdActivity";

    private FirebaseAuth.AuthStateListener mAuthListener;
    //ArrayList<User> frdlist=new ArrayList<User>();
   // ArrayList<User> fullfrdlist=new ArrayList<User>();
    ArrayList<friend>  friendlist=new ArrayList<friend>();
    FirebaseUser fu;
    FrdAdapter fa;
    FrdRPAdapter fra;
    TextView ftitle;
    User currentuser;
    User cuserdata;
    User suserdata;
    ArrayList<User> AllUserlist=new ArrayList<User>();
    ArrayList<User> sAllUserlist=new ArrayList<User>();
    ArrayList<friend> tfrdlist = new ArrayList<friend>();

  //  ArrayList<String> fuid=new ArrayList<String>();
  //  ArrayList<friend> userfrdlist=new ArrayList<friend>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frd2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Friends");
        flv = (ListView) findViewById(R.id.flistview);
        ftitle = (TextView) findViewById(R.id.Frdtitle);
        if (getIntent().getExtras() != null) {
            cuserdata = (User) getIntent().getExtras().getSerializable("currentuser");
        }

        final FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();

        fu = FirebaseAuth.getInstance().getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                fu = firebaseAuth.getCurrentUser();
                if (fu != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + fu.getUid());
                } else {

                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };



        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        df = mDatabase.child("user");

        DatabaseReference udf = df.child(fuser.getUid());

        udf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, Object> user = (HashMap<String, Object>) dataSnapshot.getValue();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(user);
                currentuser = gson.fromJson(jsonElement, User.class);
              //  Log.d("current user", currentuser.getFirstname());
                //Log.d("current user", currentuser.getUid());
                friendlist = currentuser.getFlist();
                //Log.d("friendslistsize",String.valueOf(friendlist.size()));
                if (friendlist != null)
                    SetView(friendlist);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getallfrds();

    }




/*

    public void getmyfrds()
    {

        dfuser=df.child(fu.getUid());

        dfuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                fuid.clear();
                HashMap<String,Object> user= (HashMap<String, Object>) dataSnapshot.getValue();

                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(user);
                currentuser = gson.fromJson(jsonElement, User.class);

                if(currentuser.getFlist()!=null) {
                    for (int i = 0; i < currentuser.getFlist().size(); i++) {
                        fuid.add(currentuser.getFlist().get(i).getFuid());

                        Log.d("currenuser",String.valueOf(fuid.size()));
                    }

                    userfrdlist=currentuser.getFlist();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });


        Showmyfrd();
//        Toast.makeText(this,currentuser.getUid(),Toast.LENGTH_SHORT).show();

    }

    public void Showmyfrd()
    {

        Log.d("Show frd size",userfrdlist.toString());
        ArrayList<friend> myfrd=new ArrayList<friend>();
      if(userfrdlist!=null) {
    for (friend f : userfrdlist) {

        if (f.getStatus().equals("0")) {
            myfrd.add(f);
        }

    }
}
         if(myfrd.size()>0)
         {
             Log.d("size",myfrd.get(0).getFrdname());
             FrdRPAdapter  fpaa = new FrdRPAdapter(this, R.layout.frdrowitem, myfrd,0);
             fpaa.setNotifyOnChange(true);
             flv.setAdapter(fpaa);

         }
         else
         {
           Toast.makeText(this,"You have no frds",Toast.LENGTH_SHORT).show();

         }

    }

    public void getpendingfrd()
    {

        Log.d("demopending","demo");
        ArrayList<friend> tfrdlist = new ArrayList<friend>();
        tfrdlist.addAll(userfrdlist);
        if(userfrdlist!=null)
        Log.d("pending frd size",String.valueOf(userfrdlist.size()));
        if (tfrdlist.size() > 0) {
            flv=(ListView)findViewById(R.id.flistview);
          FrdRPAdapter  fpaa = new FrdRPAdapter(this, R.layout.frdrowitem, tfrdlist,0);
            fpaa.setNotifyOnChange(true);
            flv.setAdapter(fpaa);
        }

    }

*/



    public void getallfrds()
    {

        final ArrayList<String> fulist=new ArrayList<String>();
        df.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                AllUserlist.clear();
                sAllUserlist=new ArrayList<User>();
                if(currentuser.getFlist()!=null) {
                    for (int i = 0; i < currentuser.getFlist().size(); i++)
                        fulist.add(currentuser.getFlist().get(i).getFuid());
                }
                for(DataSnapshot d : dataSnapshot.getChildren()) {
                    User user = new User();
                    HashMap<String,Object> map= (HashMap<String, Object>)d.getValue();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(map);
                    user = gson.fromJson(jsonElement, User.class);

                    if((user.getUid()!=null) &&(fu.getUid()!=null)) {
                        if (!(user.getUid().equals(fu.getUid()))) {

                            if (!fulist.contains(user.getUid())) {
                                sAllUserlist.add(user);
                                AllUserlist.add(user);
                            }


                        }
                    }


                     }
                SetAllFrdView(sAllUserlist);


                if(sAllUserlist.size()!= 0)
                {
                }



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    public void SetAllFrdView(ArrayList<User> users)
    {
        ArrayList<User> templist=users;

        if(templist!=null) {

            if (templist.size() > 0) {
                fa = new FrdAdapter(this, R.layout.frdrowitem,templist);
                flv.setAdapter(fa);
                fa.setNotifyOnChange(true);


            }
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.frdmenu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_myfrds) {
            //getmyfrds();
            SetView(friendlist);
            ftitle.setText("My Friends");
        }
        else if(id==R.id.action_myfreq)
        {

            SetReqView(friendlist);
            ftitle.setText("Friends Requests");
        }
        else if(id==R.id.action_myfpend)
        {

            SetPendingView(friendlist);
            ftitle.setText(" Pending Friends");
           // getpendingfrd();
        }
        else if(id==R.id.action_findfrd)
        {

            ftitle.setText("Find Friends");
            getallfrds();
            //getallfrds();
        }


        return super.onOptionsItemSelected(item);
    }


    public void SetView(ArrayList<friend> friendlist) {

        if(friendlist!=null) {

            tfrdlist.clear();

            for (friend u : friendlist) {
                if (u.getStatus().equals("0"))
                    tfrdlist.add(u);
            }
            if (tfrdlist.size() > 0) {

                fra = new FrdRPAdapter(this, R.layout.frdrowitem, tfrdlist);
                flv.setAdapter(fra);
                fra.setNotifyOnChange(true);

            }
        }
        else
        {

            if(fa!=null)
                fa.clear();
            if(fra!=null)
                fra.clear();
            flv.setAdapter(null);
        }
    }


    public void SetPendingView(ArrayList<friend> friendlist) {

        if(friendlist!=null) {
            tfrdlist.clear();
            for (friend u : friendlist) {
                if (u.getStatus().equals("1"))
                    tfrdlist.add(u);
            }
            if (tfrdlist.size() > 0) {
                fra = new FrdRPAdapter(this, R.layout.frdrowitem, tfrdlist);
                flv.setAdapter(fra);
                fra.setNotifyOnChange(true);

            }
        }
        else
        {

            if(fa!=null)
                fa.clear();
            if(fra!=null)
                fra.clear();
            flv.setAdapter(null);
        }
    }



    public void SetReqView(ArrayList<friend> friendlist) {

        if(friendlist!=null) {
            tfrdlist.clear();
            for (friend u : friendlist) {
                if (u.getStatus().equals("2"))
                    tfrdlist.add(u);
            }
            if (tfrdlist.size() > 0) {
                fra = new FrdRPAdapter(this, R.layout.frdrowitem, tfrdlist);
                flv.setAdapter(fra);
                fra.setNotifyOnChange(true);

            }
        }
        else
        {
            if(fa!=null)
            fa.clear();
            if(fra!=null)
            fra.clear();
            flv.setAdapter(null);
        }
    }





    public void sendfrdrequest(User uo)
    {
        currentuser=cuserdata;
        ArrayList <friend> flist=new ArrayList<friend>();
        if(currentuser.getFlist()!=null) {
            flist = currentuser.getFlist();
        }

        friend fo=new friend();
        fo.setFuid(uo.getUid());
        fo.setPimagurl(uo.getImgurl());
        fo.setFrdname(uo.getFirstname()+" "+uo.getLastname());
        fo.setStatus("2");

        flist.add(fo);
        currentuser.setFlist(flist);
        mDatabase= FirebaseDatabase.getInstance().getReference("users");
        df=mDatabase.child("user").child(currentuser.getUid());
        df.setValue(currentuser);


       // String id=mDatabase.child("user").child(uo.getUid()).child("flist").push().getKey();
        DatabaseReference rdf=mDatabase.child("user").child(uo.getUid()); //.child("flist");
           // rdf.child(ro.getFuid()).setValue(ro);
       rdf.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d("demo", dataSnapshot.getValue().toString());
                    HashMap<String,Object> user= (HashMap<String, Object>) dataSnapshot.getValue();
                    Gson gson = new Gson();
                    JsonElement jsonElement = gson.toJsonTree(user);
                    suserdata = gson.fromJson(jsonElement, User.class);

                    friend ro=new friend();
                    ro.setStatus("1");
                    ro.setFuid(currentuser.getUid());
                    ro.setFrdname(currentuser.getFirstname()+" "+currentuser.getLastname());
                    ro.setPimagurl(currentuser.getImgurl());

                    ArrayList <friend> slist=new ArrayList<friend>();
                    if(suserdata.getFlist()!=null) {
                        slist = suserdata.getFlist();
                    }
                    slist.add(ro);
                    suserdata.setFlist(slist);
                    mDatabase= FirebaseDatabase.getInstance().getReference("users");
                    df=mDatabase.child("user").child(suserdata.getUid());
                    df.setValue(suserdata);
               //     fra.notifyDataSetChanged();

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        Log.d("Add Object",uo.getFirstname());


    }



    public void Acceptfrdrequest(friend uo) {
        currentuser = cuserdata;
        ArrayList<friend> flist = new ArrayList<friend>();
        if (currentuser.getFlist() != null) {
            flist = currentuser.getFlist();
        }

        friend fp=new friend();
        for(friend fo1:flist)
        {
            if(fo1.getFuid().equals(uo.getFuid()))
            {
                fp=fo1;
                break;
            }
        }

       // friend fp=new friend();
        //fp.setPimagurl(uo.getPimagurl());
        //fp.setStatus(uo.getStatus());
        //fp.setFuid(uo.getFuid());
        //fp.setFrdname(uo.getFrdname());

        int position = flist.indexOf(fp);
        fp.setStatus("0");
        if (position >= 0)
        {       flist.set(position,fp);
    }
        currentuser.setFlist(flist);
        mDatabase= FirebaseDatabase.getInstance().getReference("users");
        df=mDatabase.child("user").child(currentuser.getUid());
        df.setValue(currentuser);


        // String id=mDatabase.child("user").child(uo.getUid()).child("flist").push().getKey();
        DatabaseReference rdf=mDatabase.child("user").child(uo.getFuid()); //.child("flist");
        // rdf.child(ro.getFuid()).setValue(ro);
        rdf.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("demo", dataSnapshot.getValue().toString());
                HashMap<String,Object> user= (HashMap<String, Object>) dataSnapshot.getValue();
                Gson gson = new Gson();
                JsonElement jsonElement = gson.toJsonTree(user);
                suserdata = gson.fromJson(jsonElement, User.class);

                friend ro=new friend();
               // ro.setStatus("1");
               // ro.setFuid(currentuser.getUid());
               // ro.setFrdname(currentuser.getFirstname()+" "+currentuser.getLastname());
                //ro.setPimagurl(currentuser.getImgurl());

                ArrayList <friend> slist=new ArrayList<friend>();
                if(suserdata.getFlist()!=null) {
                    slist = suserdata.getFlist();
                }

                for(friend fo:slist)
                {
                    if(fo.getFuid().equals(currentuser.getUid()))
                    {
                        ro=fo;
                        break;
                    }
                }
                int fposition =slist.indexOf(ro);
                ro.setStatus("0");
                slist.set(fposition,ro);
                suserdata.setFlist(slist);
                mDatabase= FirebaseDatabase.getInstance().getReference("users");
                df=mDatabase.child("user").child(suserdata.getUid());
                df.setValue(suserdata);
                fa.notifyDataSetChanged();



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Log.d("Add Object",uo.getFrdname());

//        fa.notifyDataSetChanged();

    }


    public void showfrdprofile(friend frd)
    {
        Intent i=new Intent(this,FriendProfile.class);
        i.putExtra("frdd",frd);
        startActivity(i);

    }

}
