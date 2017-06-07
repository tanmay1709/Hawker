package com.example.shivam6731.hawkers;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.List;

public class first extends Activity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
TextView cl,man;
    Spinner spinner;
    String st;
    String[] la1,lon1,t1;
    String jk,slon;
    private GoogleApiClient mGoogleApiClient;
    String slat="10.0",slng ="11.0",rad;
    private Location mLastLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        cl=(TextView)findViewById(R.id.cl);
        man=(TextView)findViewById(R.id.man);
        spinner=(Spinner)findViewById(R.id.spinner);
        rad=spinner.getSelectedItem().toString();
        la1=new String[100];
        lon1=new String[100];
        t1=new String[100];
        Intent i=getIntent();
        la1=i.getStringArrayExtra("arr");
        lon1=i.getStringArrayExtra("arr1");
        t1=i.getStringArrayExtra("arr2");
        jk=i.getStringExtra("co");
       // jk1=Integer.parseInt(jk);

        cl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i1=new Intent(first.this,search.class);
                i1.putExtra("lat11", slat);
               i1.putExtra("lon11", slng);
                i1.putExtra("arr",la1);
                i1.putExtra("arr1",lon1);
                i1.putExtra("arr2",t1);
                i1.putExtra("co",jk);
                i1.putExtra("radius",rad);
                startActivity(i1);

            }
        });
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(first.this);

                final EditText et = new EditText(first.this);

                // set prompts.xml to alertdialog builder
                alertDialogBuilder.setView(et);

                // set dialog message
                alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        st=et.getText().toString();


                        convertAddress();

                    }
                });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });




    }




   /* @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.man)
        {
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            final EditText et = new EditText(this);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(et);

            // set dialog message
            alertDialogBuilder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    st=et.getText().toString();


                    convertAddress();

                }
            });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            // show it
            alertDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }
*/

    public void convertAddress() {
        Geocoder geoCoder = new Geocoder(this);
        if (st != null && !st.isEmpty()) {
            try {
                List<Address> addressList = geoCoder.getFromLocationName(st, 1);
                if (addressList != null && addressList.size() > 0) {
                     Double llat = addressList.get(0).getLatitude();
                    Double llng = addressList.get(0).getLongitude();
                    String sslat=Double.toString(llat);

                   // Toast.makeText(first.this,sslat , Toast.LENGTH_LONG).show();
                    //flag=1;
                    //onStart();
                   // String jk1=Integer.toString(jk);
                    String ll=Double.toString(llat);
                    String lt=Double.toString(llng);
                    Intent i11=new Intent(first.this,search.class);
                    i11.putExtra("lat11", ll);
                    i11.putExtra("lon11", lt);
                    i11.putExtra("arr",la1);
                    i11.putExtra("arr1",lon1);
                    i11.putExtra("arr2",t1);
                    i11.putExtra("co",jk);
                    i11.putExtra("radius",rad);
                    startActivity(i11);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } // end catch
        } // end if
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            Double lat=mLastLocation.getLatitude();
            Double lon=mLastLocation.getLongitude();
            slng=lon.toString();
            slat=lat.toString();

            return;
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
}
