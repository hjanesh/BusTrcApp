package com.bustrc101.ammu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LocationTrack extends AppCompatActivity {
EditText trackid ;
String id;
boolean doubleBackToExitPressedOnce = false;
private SharedPreferences mPref;
private SharedPreferences.Editor mEdit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_track);
        Button btnTrack= findViewById(R.id.btnTrack);

        trackid = findViewById(R.id.trackID);
        mPref = PreferenceManager.getDefaultSharedPreferences(this);
        mEdit = mPref.edit();
        trackid.setText(mPref.getString("TrackId",""));
        btnTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = trackid.getText().toString();
                if(TextUtils.isEmpty(id)){
                    Toast.makeText(getApplicationContext(),"Enter a Track ID",Toast.LENGTH_SHORT).show();
                    return;
                }else if(id.length()!= 28){
                    if (doubleBackToExitPressedOnce) {
                        mEdit.putString("TrackId",id);
                        mEdit.apply();
                        Intent intent = new Intent(getApplicationContext(),LiveLocation.class);
                        startActivity(intent);
                        return;
                    }
                    doubleBackToExitPressedOnce = true;
                    Toast.makeText(getApplicationContext(), "Track id is supposed to be 28 characters, Press Button to continue anyway", Toast.LENGTH_SHORT).show();
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            doubleBackToExitPressedOnce=false;
                        }
                    }, 2000);
                    return;

                }
                mEdit.putString("TrackId",id);
                mEdit.apply();
                Intent intent = new Intent(getApplicationContext(),LiveLocation.class);
                startActivity(intent);
            }
        });
    }
}
