package com.example.shivam6731.hawkers;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText mloginemailfield,mloginpasswordfield;
    private Button mloginbtn,button3;
    private FirebaseAuth mauth;
    private DatabaseReference mdatabaseusers;
    private ProgressDialog mprogress;
    TextView fp;
    //private SignInButton mgooglebtn;
    private static final int RC_SIGN_IN=1;
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mauthlistner;
    private long backpress=0;
    String email;

    private static final String TAG ="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
fp=(TextView)findViewById(R.id.fp);

        mloginemailfield=(EditText)findViewById(R.id.loginemailfield);
        mloginpasswordfield=(EditText)findViewById(R.id.loginpasswordfield);
        button3=(Button)findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,RegisterActvity.class);
                startActivity(i);
            }
        });
        mloginbtn=(Button)findViewById(R.id.loginbtn);
        mdatabaseusers= FirebaseDatabase.getInstance().getReference().child("users");
        mdatabaseusers.keepSynced(true);
        mauth=FirebaseAuth.getInstance();

        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = mloginemailfield.getText().toString().trim();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                //String emailAddress = "user@example.com";

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(LoginActivity.this,"Mail Send",Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });
        //  mgooglebtn=(SignInButton)findViewById(R.id.googlebtn);
        mprogress=new ProgressDialog(this);
        if(mauth.getCurrentUser()!=null)
        {
            Intent i111=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(i111);
        }
        mloginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checklogin();
            }
        });
    }

    private void checklogin() {
        if(mauth.getCurrentUser()==null) {
            email = mloginemailfield.getText().toString().trim();
            String password = mloginpasswordfield.getText().toString().trim();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)) {
                mprogress.setMessage("Checking Login......");
                mprogress.show();
                mauth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            checkuserexist();
                        } else {


                            Toast.makeText(LoginActivity.this, "wrong password", Toast.LENGTH_LONG).show();



                            mprogress.dismiss();
                        }
                    }
                });

            }
            else
            {
                Toast.makeText(LoginActivity.this, "Login ", Toast.LENGTH_LONG);
            }
        }
        else {
            Toast.makeText(LoginActivity.this, "Null ", Toast.LENGTH_LONG);
        }
    }
    private void checkuserexist() {
        final String user_id=mauth.getCurrentUser().getUid();
        mdatabaseusers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id))
                {mprogress.dismiss();
                    Intent mainintent=new Intent(LoginActivity.this,MainActivity.class);
                    mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainintent);
                }
                else
                {mprogress.dismiss();
                    Intent setupintent=new Intent(LoginActivity.this,RegisterActvity.class);
                    setupintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(setupintent);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {        // to prevent irritating accidental logouts
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


}
