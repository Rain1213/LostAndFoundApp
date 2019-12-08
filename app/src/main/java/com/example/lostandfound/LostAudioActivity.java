package com.example.lostandfound;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class LostAudioActivity extends Activity {

    private Spinner mLostAudioSpinner;
    private Button mLostAudioBackButton;
    private Button mLostAudioSubmitButton;
    private EditText mLostAudioCompanyName;
    private EditText mLostAudioColor;
    public EditText mLostAudioDate;
    private EditText mLostAudioPlace;
    private EditText mLostAudioRoomNo;

    public String LostAudioDate;

    private DatabaseReference lostFound;
    MainActivity obj1 = new MainActivity();
    private String Check;

    FirebaseAuth mAuth;

    DatabaseReference lostAudio;
    String LostAudioType;

    AudioActivityClass member = new AudioActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_audio_layout);

        mLostAudioSpinner=(Spinner)findViewById(R.id.LostAudioSpinner);
        mLostAudioBackButton=(Button)findViewById(R.id.LostAudioBackButton);
        mLostAudioSubmitButton=(Button)findViewById(R.id.LostAudioSubmitButton);
        mLostAudioCompanyName=(EditText) findViewById(R.id.LostAudioCompanyName);
        mLostAudioColor=(EditText)findViewById(R.id.LostAudioColor);
        mLostAudioDate=(EditText)findViewById(R.id.LostAudioDate);
        mLostAudioPlace=(EditText)findViewById(R.id.LostAudioPlace);
        mLostAudioRoomNo=(EditText)findViewById(R.id.LostAudioRoomNo);

        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        lostAudio= FirebaseDatabase.getInstance().getReference().child("LostAudioActivity");

        LostAudioSpinner();


        mLostAudioSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertData();

                Query queryLostAudio = FirebaseDatabase.getInstance().getReference("FoundAudioActivity")
                        .orderByChild("check")
                        .equalTo(Check);

                queryLostAudio.addListenerForSingleValueEvent(lostAudioListner);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();
                setContentView(R.layout.submitted_layout);
            }


            ValueEventListener lostAudioListner = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AudioActivityClass BAC = snapshot.getValue(AudioActivityClass.class);
                            String lf_Email = BAC.getEmail();
                            String lf_date = BAC.getDate();
                            lostFound = FirebaseDatabase.getInstance().getReference().child("Lost_Found");

                            LostFound lfobj = new LostFound();

                            lfobj.setLostEmail(obj1.obj.getEmail());
                            lfobj.setFoundEmail(lf_Email);
                            lfobj.setDetail(Check);
                            lfobj.setFoundDate(lf_date);
                            lostFound.push().setValue(lfobj);

                        }

                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            };

        }
    );

        mLostAudioBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostAudioActivity.this,LostPageActivity.class));
            }
        });
    }

    public void LostAudioSpinner() {
        //******Sort Items Alphabetically******//
        String[] LostAudioTypes=getResources().getStringArray(R.array.audio_devices);
        List<String> LostAudioTypesList= Arrays.asList(LostAudioTypes);
        Collections.sort(LostAudioTypesList);
        /////////////////////////////////////////

        ArrayAdapter AudioAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,LostAudioTypesList);
        AudioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLostAudioSpinner.setAdapter(AudioAdapter);
        mLostAudioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                 LostAudioType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),LostAudioType,Toast.LENGTH_SHORT).show();
                mLostAudioSpinner.setPrompt("Select Item");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void insertData(){
        String LostAudioCompanyName=mLostAudioCompanyName.getText().toString().toLowerCase();
        String LostAudioColor=mLostAudioColor.getText().toString().toLowerCase();
        String LostAudioPlace=mLostAudioPlace.getText().toString().toLowerCase();
        String LostAudioRoomNo=mLostAudioRoomNo.getText().toString().toLowerCase();
        Check = LostAudioType+"_"+LostAudioCompanyName + "_" + LostAudioColor + "_" + LostAudioPlace;


        //OnlyEmail eml = new OnlyEmail();



        member.setEmail(obj1.obj.getEmail());
        member.setType(LostAudioType);
        member.setCompanyName(LostAudioCompanyName);
        member.setColour(LostAudioColor);
        member.setDate(LostAudioDate);
        member.setPlace(LostAudioPlace);
        member.setLabNo(LostAudioRoomNo);
        member.setCheck(Check);

        lostAudio.push().setValue(member);
    }

    public void DatePicker(){
        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel() {
                String myFormat = "dd/MM/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

                LostAudioDate=sdf.format(myCalendar.getTime());
                mLostAudioDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostAudioDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostAudioActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }


}
