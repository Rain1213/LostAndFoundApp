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

public class FoundChargerActivity extends Activity {
    private Spinner mFoundChargerSpinner;
    private Button mFoundChargerBackButton;
    private Button mFoundChargerSubmitButton;

    private EditText mFoundChargerCompanyName;
    private EditText mFoundChargerColor;
    private EditText mFoundChargerDate;
    private EditText mFoundChargerPlace;
    private EditText mFoundChargerRoomNo;
    DatabaseReference foundCharger;
    String FoundChargerType,FoundChargerDate;

    private DatabaseReference lostFound;
    MainActivity obj1 = new MainActivity();
    String Check;

    ChargerActivityClass foundChargerMember = new ChargerActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_charger_layout);

        mFoundChargerSpinner=(Spinner)findViewById(R.id.FoundChargerSpinner);
        mFoundChargerBackButton=(Button)findViewById(R.id.FoundChargerBackButton);
        mFoundChargerSubmitButton=(Button)findViewById(R.id.FoundChargerSubmitButton);

        mFoundChargerCompanyName=(EditText) findViewById(R.id.FoundChargerCompanyName);
        mFoundChargerColor=(EditText)findViewById(R.id.FoundChargerColor);
        mFoundChargerDate=(EditText)findViewById(R.id.FoundChargerDate);
        mFoundChargerPlace=(EditText)findViewById(R.id.FoundChargerPlace);
        mFoundChargerRoomNo=(EditText)findViewById(R.id.FoundChargerRoomNo);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        foundCharger= FirebaseDatabase.getInstance().getReference().child("FoundChargerActivity");

        FoundChargerSpinner();

        mFoundChargerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoundChargerActivity.this,FoundPageActivity.class));
            }
        });

        mFoundChargerSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInfo();

                foundCharger.push().setValue(foundChargerMember);

                Query query3 = FirebaseDatabase.getInstance().getReference("LostChargerActivity")
                        .orderByChild("check")
                        .equalTo(Check);

                query3.addListenerForSingleValueEvent(foundChargerListner);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();

                setContentView(R.layout.submitted_layout);
            }

            ValueEventListener foundChargerListner = new ValueEventListener() {
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
        });
    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(FoundChargerActivity.this,FoundPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void FoundChargerSpinner() {
        //******Sort Items Alphabetically******//
        String[] FoundChargerTypes=getResources().getStringArray(R.array.charger_types);
        List<String> FoundChargerTypesList= Arrays.asList(FoundChargerTypes);
        Collections.sort(FoundChargerTypesList);
        /////////////////////////////////////////

        ArrayAdapter ChargerAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,FoundChargerTypesList);
        ChargerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mFoundChargerSpinner.setAdapter(ChargerAdapter);
        mFoundChargerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                FoundChargerType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),FoundChargerType,Toast.LENGTH_SHORT).show();
                mFoundChargerSpinner.setPrompt("Select Item");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
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

                FoundChargerDate=sdf.format(myCalendar.getTime());
                mFoundChargerDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mFoundChargerDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FoundChargerActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    private void getInfo(){
        String FoundChargerCompanyName=mFoundChargerCompanyName.getText().toString().toLowerCase();
        String FoundChargerColor=mFoundChargerColor.getText().toString().toLowerCase();
        String FoundChargerDate=mFoundChargerDate.getText().toString().toLowerCase();
        String FoundChargerPlace=mFoundChargerPlace.getText().toString().toLowerCase();
        String FoundChargerRoomNo=mFoundChargerRoomNo.getText().toString().toLowerCase();
        Check = FoundChargerType+"_"+FoundChargerCompanyName+"_"+FoundChargerColor+"_"+FoundChargerPlace;

        foundChargerMember.setEmail(obj1.obj.getEmail());
        foundChargerMember.setType(FoundChargerType);
        foundChargerMember.setCompanyName(FoundChargerCompanyName);
        foundChargerMember.setColour(FoundChargerColor);
        foundChargerMember.setDate(FoundChargerDate);
        foundChargerMember.setPlace(FoundChargerPlace);
        foundChargerMember.setLabNo(FoundChargerRoomNo);
        foundChargerMember.setCheck(Check);
    }
}
