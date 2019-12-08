package com.example.lostandfound;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
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

public class FoundAudioActivity extends Activity {

    private Spinner mFoundAudioSpinner;
    private Button mFoundAudioBackButton;
    private Button mFoundAudioSubmitButton;

    private EditText mFoundAudioCompanyName;
    private EditText mFoundAudioColor;
    private EditText mFoundAudioDate;
    private EditText mFoundAudioPlace;
    private EditText mFoundAudioRoomNo;

    String FoundAudioDate;

    DatabaseReference foundAudio;
    FirebaseAuth mAuth;
    AudioActivityClass fmember = new AudioActivityClass();
    MainActivity obj1 = new MainActivity();
    String Check;
    private DatabaseReference lostFound;

    String FoundAudioType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_audio_layout);

        mFoundAudioSpinner=(Spinner)findViewById(R.id.FoundAudioSpinner);
        mFoundAudioBackButton=(Button)findViewById(R.id.FoundAudioBackButton);
        mFoundAudioSubmitButton=(Button)findViewById(R.id.FoundAudioSubmitButton);
        mFoundAudioCompanyName=(EditText) findViewById(R.id.FoundAudioCompanyName);
        mFoundAudioColor=(EditText)findViewById(R.id.FoundAudioColor);
        mFoundAudioDate=(EditText)findViewById(R.id.FoundAudioDate);
        mFoundAudioPlace=(EditText)findViewById(R.id.FoundAudioPlace);
        mFoundAudioRoomNo=(EditText)findViewById(R.id.FoundAudioRoomNo);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        foundAudio= FirebaseDatabase.getInstance().getReference().child("FoundAudioActivity");

        FoundAudioSpinner();

        mFoundAudioSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                insertData();
                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();

                Query query3 = FirebaseDatabase.getInstance().getReference("LostAudioActivity")
                        .orderByChild("check")
                        .equalTo(Check);

                query3.addListenerForSingleValueEvent(foundAudioListner);

                setContentView(R.layout.submitted_layout);
            }

             ValueEventListener foundAudioListner = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                   if (dataSnapshot.exists()) {
                       for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                          AudioActivityClass BAC = snapshot.getValue(AudioActivityClass.class);
                             String lf_Email = BAC.getEmail();
                           String lf_date = BAC.getDate();

                             lostFound = FirebaseDatabase.getInstance().getReference().child("Lost_Found");

                             LostFound lfobj = new LostFound();
                             lfobj.setFoundEmail(obj1.obj.getEmail());
                             lfobj.setLostEmail(lf_Email);
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

        mFoundAudioBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoundAudioActivity.this,FoundPageActivity.class));
            }
        });
    }

    private void FoundAudioSpinner() {
        //******Sort Items Alphabetically******//
        String[] FoundAudioTypes=getResources().getStringArray(R.array.audio_devices);
        List<String> FoundAudioTypesList= Arrays.asList(FoundAudioTypes);
        Collections.sort(FoundAudioTypesList);
        /////////////////////////////////////////

        ArrayAdapter AudioAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,FoundAudioTypesList);
        AudioAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFoundAudioSpinner.setAdapter(AudioAdapter);
        mFoundAudioSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                FoundAudioType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),FoundAudioType,Toast.LENGTH_SHORT).show();
                mFoundAudioSpinner.setPrompt("Select Item");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void insertData(){
        String FoundAudioCompanyName=mFoundAudioCompanyName.getText().toString().toLowerCase();
        String FoundAudioColor=mFoundAudioColor.getText().toString().toLowerCase();
        String FoundAudioDate=mFoundAudioDate.getText().toString().toLowerCase();
        String FoundAudioPlace=mFoundAudioPlace.getText().toString().toLowerCase();
        String FoundAudioRoomNo=mFoundAudioRoomNo.getText().toString().toLowerCase();
        Check = FoundAudioType+"_"+FoundAudioCompanyName + "_" + FoundAudioColor + "_" + FoundAudioPlace;

        fmember.setEmail(obj1.obj.getEmail());

        fmember.setType(FoundAudioType);
        fmember.setCompanyName(FoundAudioCompanyName);
        fmember.setColour(FoundAudioColor);
        fmember.setDate(FoundAudioDate);
        fmember.setPlace(FoundAudioPlace);
        fmember.setLabNo(FoundAudioRoomNo);
        fmember.setCheck(Check);

        foundAudio.push().setValue(fmember);
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

                FoundAudioDate=sdf.format(myCalendar.getTime());
                mFoundAudioDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mFoundAudioDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FoundAudioActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
