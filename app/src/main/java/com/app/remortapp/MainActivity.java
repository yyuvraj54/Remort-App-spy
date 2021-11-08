package com.app.remortapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements dilogcode.ExampleDialogListener {


    ArrayList<OrderHelper> last;

    EditText start ,stop;
    Button Newschedule ,startrecordding ,stoprecording,restoreDefault ,unin;
    TextView ru,offt,ont,fc,recs,autorec,call;
    Switch swi;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference databaseReferenceAdmin;

    boolean isRecording=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start=findViewById(R.id.starttime);
        stop=findViewById(R.id.stoptime);
        Newschedule=findViewById(R.id.newschedule);

        startrecordding=findViewById(R.id.startRec);
        stoprecording=findViewById(R.id.stopRec);

        swi=findViewById(R.id.switch1);

        restoreDefault=findViewById(R.id.setup);

        unin=findViewById(R.id.uni);

        ru=findViewById(R.id.run);
        offt=findViewById(R.id.off);
        ont=findViewById(R.id.on);
        fc=findViewById(R.id.force);
        recs=findViewById(R.id.rec);

        autorec=findViewById(R.id.Auto);
        call=findViewById(R.id.call_state);



        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User");
        databaseReferenceAdmin=firebaseDatabase.getReference("Admin");



        swi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){ AutorecOption("1"); }
                else{AutorecOption("0");}

            }
        });


        Newschedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String starttime=start.getText().toString();
                String stoptime=stop.getText().toString();

                setdata(starttime,stoptime,"0","0","1");

            }
        });

        startrecordding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isRecording){
                    updateRecStstus("1");
                }
            }
        });

        stoprecording.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRecording){
                updateRecStstus("0");
                }
            }
        });

        restoreDefault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdata("12:00:00","12:10:00","0","0","1");
            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> items = new ArrayList<>();
                for (DataSnapshot snap:snapshot.getChildren()){
                    items.add(snap.getValue().toString());
                }

                fc.setText("Force Close:        "+items.get(0));
                offt.setText("Off Timing:           "+items.get(1));
                ont.setText("On Timing:           "+items.get(2));
                ru.setText("Running State:    "+items.get(3));

                if (items.get(4).equals("0")){ recs.setText("Rec Start:             no(code-"+items.get(4)+")");}
                else{recs.setText("Rec Start:             yes(code-"+items.get(4)+")");}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Faild to renew data", Toast.LENGTH_SHORT).show();

            }
        });

        unin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPasswordDialog();
            }
        });

        databaseReferenceAdmin.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> adminrr=new ArrayList<>();
                for (DataSnapshot snaps:snapshot.getChildren()) {
                    adminrr.add(snaps.getValue().toString());
                }
                if (adminrr.get(0).equals("1")){
                    swi.setChecked(true);
                autorec.setText("Auto Rec:          "+adminrr.get(0));}
                else{ swi.setChecked(false);autorec.setText("Auto Rec:          "+adminrr.get(0));}

                if (adminrr.get(1).contains("idle")){call.setTextColor(Color.BLACK); call.setText("Call State:           "+adminrr.get(1));}
                else if (adminrr.get(1).contains("talking")){call.setTextColor(Color.GREEN); call.setText("Call State:           "+adminrr.get(1));}
                else if (adminrr.get(1).contains("Ringing")){call.setTextColor(Color.BLUE); call.setText("Call State:           "+adminrr.get(1));}
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void setdata(String ontime,String offtime,String startRec,String forceClose,String running){
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                    OrderHelper DATA=new OrderHelper(ontime, offtime, startRec, forceClose,running);
                    databaseReference.setValue(DATA);
                Toast.makeText(MainActivity.this, "Upload Done", Toast.LENGTH_SHORT).show();
                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Failed to update", Toast.LENGTH_SHORT).show();

            }
        });

    }
    public void updateRecStstus(String state){
        HashMap h=new HashMap();
        h.put("startRec",state);
        databaseReference.updateChildren(h).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(MainActivity.this, "Updation Compeleted", Toast.LENGTH_SHORT).show();
                if (isRecording){isRecording=false;}
                else{isRecording=true;}
            }
        });

    }
    public void showPasswordDialog(){
        dilogcode dc=new dilogcode();
        dc.show(getSupportFragmentManager(),"Dialog");
    }

    @Override
    public void applyTexts(String password) {
         if (password.equals("killme")){
             HashMap h=new HashMap();
             h.put("force Close",1);
             databaseReference.updateChildren(h).addOnSuccessListener(new OnSuccessListener() {
                 @Override
                 public void onSuccess(Object o) {
                     Toast.makeText(MainActivity.this, "Emergency command executed", Toast.LENGTH_SHORT).show();
                 }
             });
         }
         else{
             Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
         }
    }

    public void  AutorecOption(String autorecState){
        HashMap h=new HashMap();
        h.put("auto_rec",autorecState);
        databaseReferenceAdmin.updateChildren(h);

    }


}