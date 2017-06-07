package com.example.shivam6731.hawkers;

import android.content.Intent;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class search extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String slat,slon;
    double lat,lon;
    private DatabaseReference mDatabase;
    String[] la1,lon1,t1;
    String jk;
    Double x,y;
    int jk1;
    Blog model;
    Location loc1,loc2;
String rad;
    int radius;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        la1=new String[100];
        lon1=new String[100];
        t1=new String[100];


/*
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Blog bl=dataSnapshot.getValue(Blog.class);
                Toast.makeText(search.this,bl.getTitle()+"",Toast.LENGTH_SHORT);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(search.this,"aa",Toast.LENGTH_SHORT);
            }
        });*/
        Intent i=getIntent();
        slat=i.getStringExtra("lat11");
        slon=i.getStringExtra("lon11");
        la1=i.getStringArrayExtra("arr");
        lon1=i.getStringArrayExtra("arr1");
        t1=i.getStringArrayExtra("arr2");
        jk=i.getStringExtra("co");
        rad=i.getStringExtra("radius");
        radius=Integer.parseInt(rad);
        if(jk==null)
        {
            jk="8";
        }
        jk1=Integer.parseInt(jk);


        lat=Double.parseDouble(slat);
        lon=Double.parseDouble(slon);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(lat, lon);
      //  mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        loc1 = new Location("");
        loc1.setLatitude(lat);
        loc1.setLongitude(lon);
        Double k=0.000001;
        for(int i1=0;i1<jk1&&i1<la1.length;++i1)
        {

            x=Double.parseDouble(la1[i1]);
            y=Double.parseDouble(lon1[i1]);//x=x+k;k=k+k;
            loc2 = new Location("");
            loc2.setLatitude(x);
            loc2.setLongitude(y);

            if(loc1.distanceTo(loc2)<=radius) {
               // Toast.makeText(search.this, jk1+"", Toast.LENGTH_LONG).show();
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(x, y))
                        .title(t1[i1]));
            }
        }

        // Add a marker in Sydney, Australia, and move the camera.
        /*;*/

    }
}
