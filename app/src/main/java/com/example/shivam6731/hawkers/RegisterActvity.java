package com.example.shivam6731.hawkers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActvity extends AppCompatActivity {
    private EditText mnamefield, memailfield, mpasswordfield;
    private Button mregisterbtn;
    private FirebaseAuth mauth;
    private ProgressDialog mprogress;
    private DatabaseReference mdatabase;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mauth = FirebaseAuth.getInstance();
        mnamefield = (EditText) findViewById(R.id.namefield);
        memailfield = (EditText) findViewById(R.id.emailfield);
        mpasswordfield = (EditText) findViewById(R.id.passwordfield);
        mregisterbtn = (Button) findViewById(R.id.registerbtn);
        mdatabase = FirebaseDatabase.getInstance().getReference().child("users");
        mprogress = new ProgressDialog(this);
        mregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RegisterActvity.this, "Emailllll", Toast.LENGTH_LONG);
                startRegister();
            }
        });
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void startRegister() {
        final String name = mnamefield.getText().toString().trim();
        String email = memailfield.getText().toString().trim();
        String password = mpasswordfield.getText().toString().trim();
        if (isValidEmailAddress(email)) {
            // Toast.makeText(RegisterActvity.this, "Email", Toast.LENGTH_LONG);
            if (password.length() >= 6) {

                if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                    mprogress.setMessage("Signing Up....");
                    mprogress.show();
                    mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                String user_id = mauth.getCurrentUser().getUid();
                                DatabaseReference current_user_db = mdatabase.child(user_id);
                                current_user_db.child("name").setValue(name);
                                current_user_db.child("image").setValue("default");
                                mprogress.dismiss();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                user.sendEmailVerification()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                   // Log.d(TAG, "Email sent.");
                                                    Toast.makeText(RegisterActvity.this,"Verification Mail send",Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });




                                Intent mainintent = new Intent(RegisterActvity.this, LoginActivity.class);
                                mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(mainintent);
                            } else {
                                mprogress.dismiss();
                                Toast.makeText(RegisterActvity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } else {
                    Toast.makeText(RegisterActvity.this, "Something Missed", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(RegisterActvity.this, "Password length is short atleast 6 letters", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(RegisterActvity.this, "Email Address not valid", Toast.LENGTH_LONG).show();
        }
    }

    public boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("RegisterActvity Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}

