package com.etuloser.padma.rohit.homework09a;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Rohit on 5/1/2017.
 */

public class FrdTripAdapter  extends ArrayAdapter<trip> {

    ArrayList<trip> triplist;
    Context mcontext;
    int mres;
    String cuid;

    public FrdTripAdapter(Context context, int resource, ArrayList<trip> objects,String cuid) {
        super(context, resource, objects);
        this.triplist=objects;
        this.mcontext=context;
        this.mres=resource;
        this.cuid=cuid;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)mcontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView =inflater.inflate(mres,parent,false);
        }
        final trip g=triplist.get(position);


        ImageView iv=(ImageView)convertView.findViewById(R.id.roompic);
        TextView txtroomname=(TextView)convertView.findViewById(R.id.temproomname);
        final Button btnjoin=(Button)convertView.findViewById(R.id.tempjoin);
        if(g.getMembers().contains(cuid))
        {
            btnjoin.setVisibility(View.INVISIBLE);
        }

        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((FriendProfile)mcontext).jointrip(g,cuid);
                btnjoin.setVisibility(View.INVISIBLE);

            }
        });

        txtroomname.setText(g.getTripname());
        Picasso.with(mcontext).load(g.getTimgurl()).placeholder(R.drawable.avatar_11_raster).into(iv);
        //notifyDataSetChanged();

        return convertView;


    }



}
