package com.example.shivam6731.hawkers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

//import static com.google.android.gms.analytics.internal.zzy.v;

public class MainActivity extends AppCompatActivity {
    private Button msenddata;
    private Firebase mref;
    private RecyclerView mbloglist;
    private DatabaseReference mdatabase, mdatabaseusers, mdatabaselike;
    private FirebaseAuth mauth;
    private FirebaseAuth.AuthStateListener mauthlistner;
    private boolean mprocesslike = false;
    String lat, lon;
    private ProgressDialog mprogress;
    FloatingActionButton catsearch;
    double slat, slon;
    Geocoder geocoder;
    Location loc1,loc2;
    List<Address> addresses=null;
    Context context;
    String st;
    double llng;
    double llat;
    int flag=0;
    float distanceInMeters=-1;
    int fl,jk=0;long jki;
    String[] la1,lon1,t1;
    FirebaseUser user;
    // TextView rating;//long n;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mprogress=new ProgressDialog(this);
        mprogress.setMessage("Loading......");
        mprogress.show();
        catsearch=(FloatingActionButton)findViewById(R.id.catsearch);
        catsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String jk1 = Long.toString(jki);
                Intent iq=new Intent(MainActivity.this,first.class);
                iq.putExtra("arr", la1);
                iq.putExtra("arr1", lon1);
                iq.putExtra("arr2", t1);
                iq.putExtra("co", jk1);
                startActivity(iq);
            }
        });
        la1=new String[100];
        lon1=new String[100];
        t1=new String[100];
        //rating=(TextView)findViewById(R.id.rating);
        mauth = FirebaseAuth.getInstance();

        geocoder = new Geocoder(this, Locale.getDefault());
        mauthlistner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {


                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginintent = new Intent(MainActivity.this, LoginActivity.class);
                    loginintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginintent);
                }
            }
        };


        mbloglist = (RecyclerView) findViewById(R.id.blog_list);
        mbloglist.setHasFixedSize(true);
        mbloglist.setLayoutManager(new LinearLayoutManager(this));

        mdatabase = FirebaseDatabase.getInstance().getReference().child("Blog");
        mdatabaseusers = FirebaseDatabase.getInstance().getReference().child("users");
        mdatabaselike = FirebaseDatabase.getInstance().getReference().child("likes");
        mdatabaselike.keepSynced(true);
        mdatabaseusers.keepSynced(true);
        mdatabase.keepSynced(true);
        mdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                jki=dataSnapshot.getChildrenCount();
                user=mauth.getCurrentUser();
                if(user.isEmailVerified())
                {

                }
                else
                {
                    Toast.makeText(MainActivity.this, "Your Email is Not Verified", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }


    @Override
    protected void onStart() {
        super.onStart();// ATTENTION: This was auto-generated to implement the App Indexing API.
// See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        //checkuserexist();
        mauth.addAuthStateListener(mauthlistner);
        FirebaseRecyclerAdapter<Blog, BlogViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Blog, BlogViewHolder>(
                Blog.class,
                R.layout.blog_row,
                BlogViewHolder.class,
                mdatabase
        ) {
            @Override
            protected void populateViewHolder(BlogViewHolder viewHolder, Blog model, int position) {
                final String post_key = getRef(position).getKey().toString();
                if(jk==0)
                {
                    mprogress.dismiss();
                }
                lat = model.getLatitude();

                lon = model.getLongitude();
                la1[jk]=lat;
                lon1[jk]=lon;
                slat = Double.parseDouble(lat);
                slon = Double.parseDouble(lon);





                //else
                {
                    fl=1;

                    viewHolder.setTitle(model.getTitle()+jki);
                    t1[jk]=model.getTitle();jk++;
                    viewHolder.setDesc(model.getDesc());
                    viewHolder.setImage(getApplicationContext(), model.getImage());
                    viewHolder.setusername(model.getUsername());


                    try {
                        addresses = geocoder.getFromLocation(slat, slon, 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (addresses.size() > 0) {

                        String add = addresses.get(0).getAddressLine(0);
                        // Here 1 represent max location result to returned, by documents it recommended 1 to 5


                        viewHolder.setLatitude(model.getCat()+"               "+add);
                    }

                    //viewHolder.setLongitude(model.getLongitude());
                    viewHolder.setLikebtn(post_key);
                    //viewHolder.

                    viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i1 = new Intent(MainActivity.this, map.class);
                            i1.putExtra("lat1", lat);
                            i1.putExtra("lon1", lon);

                            startActivity(i1);
                        }
                    });
                    viewHolder.mlikebtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mprocesslike = true;

                            mdatabaselike.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (mprocesslike) {
                                        //Toast.makeText(MainActivity.this,post_key,Toast.LENGTH_SHORT).show();
                                        if (dataSnapshot.child(post_key).hasChild(mauth.getCurrentUser().getUid())) {
                                            mdatabaselike.child(post_key).child(mauth.getCurrentUser().getUid()).removeValue();
                                            mprocesslike = false;
                                        } else {
                                            mdatabaselike.child(post_key).child(mauth.getCurrentUser().getUid()).setValue("radomvlue");
                                            mprocesslike = false;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    });
                }
            }

        };
        //if(fl==1 || flag==0)
        {
        mbloglist.setAdapter(firebaseRecyclerAdapter);
           }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public static class BlogViewHolder extends RecyclerView.ViewHolder {
        View mview;
        ImageButton mlikebtn;
        DatabaseReference mdatabaselike;
        FirebaseAuth mauth;
        TextView rating;

        public BlogViewHolder(View itemView) {
            super(itemView);
            mview = itemView;
            rating=(TextView)mview.findViewById(R.id.rating);
            mlikebtn = (ImageButton) mview.findViewById(R.id.likebtn);
            mdatabaselike = FirebaseDatabase.getInstance().getReference().child("likes");
            mauth = FirebaseAuth.getInstance();
            mdatabaselike.keepSynced(true);
        }

        public void setLikebtn(final String post_key) {
            mdatabaselike.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   long n=dataSnapshot.child(post_key).getChildrenCount();
                    rating.setText(n+"");
                    //Toast.makeText(MainActivity.this,dataSnapshot.child(post_key).getChildren() , Toast.LENGTH_LONG).show();
                       // Toast.makeText(MainActivity.,dataSnapshot.child(post_key).getChildren(),Toast.LENGTH_SHORT).show();
                    if (dataSnapshot.child(post_key).hasChild(mauth.getCurrentUser().getUid())) {
                        mlikebtn.setImageResource(R.mipmap.lp);
                    } else {
                        mlikebtn.setImageResource(R.mipmap.ulp);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mview.findViewById(R.id.post_title);
            post_title.setText(title);
        }

        public void setLatitude(String latitude) {
            TextView location = (TextView) mview.findViewById(R.id.location);
            location.setText(latitude);
        }

        public void setLongitude(String longitude) {

        }

        public void setDesc(String desc) {
            TextView post_desc = (TextView) mview.findViewById(R.id.post_desc);
            post_desc.setText(desc);
        }

        public void setusername(String username) {
            TextView post_userame = (TextView) mview.findViewById(R.id.username);
            post_userame.setText(username.toUpperCase());
        }

        private void setImage(final Context ctx, final String image) {
            final ImageView post_image = (ImageView) mview.findViewById(R.id.post_image);
            // Picasso.with(ctx).load(image).into(post_image);


            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(post_image);
                }
            });
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            startActivity(new Intent(MainActivity.this, PostActivity.class));
        }
        if (item.getItemId() == R.id.action_logout) {
            logout();
        }

           /* AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

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

        }*/
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        mauth.signOut();
    }

   // accepted
   public void convertAddress() {
       Geocoder geoCoder = new Geocoder(this);
       if (st != null && !st.isEmpty()) {
           try {
               List<Address> addressList = geoCoder.getFromLocationName(st, 1);
               if (addressList != null && addressList.size() > 0) {
                    llat = addressList.get(0).getLatitude();
                   llng = addressList.get(0).getLongitude();
                   String sslat=Double.toString(llat);

                   Toast.makeText(MainActivity.this,sslat , Toast.LENGTH_LONG).show();
                   flag=1;
                   //onStart();
                   String jk1=Integer.toString(jk);
                   Intent i11=new Intent(MainActivity.this,search.class);
                   i11.putExtra("lat11", lat);
                   i11.putExtra("lon11", lon);
                   i11.putExtra("arr",la1);
                   i11.putExtra("arr1",lon1);
                   i11.putExtra("arr2",t1);
                   i11.putExtra("co",jk1);
                   startActivity(i11);
               }
           } catch (Exception e) {
               e.printStackTrace();
           } // end catch
       } // end if
   }
}
