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

public class LostChargerActivity extends Activity {
    private Spinner mLostChargerSpinner;
    private Button mLostChargerBackButton;
    private Button mLostChargerSubmitButton;

    private EditText mLostChargerCompanyName;
    private EditText mLostChargerColor;
    private EditText mLostChargerDate;
    private EditText mLostChargerPlace;
    private EditText mLostChargerRoomNo;
    DatabaseReference lostCharger;

    private DatabaseReference lostFound;

    String Check;

    String LostChargerType,LostChargerDate;
    MainActivity obj1 = new MainActivity();

    ChargerActivityClass lostChargerMember = new ChargerActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_charger_layout);

        mLostChargerSpinner=(Spinner)findViewById(R.id.LostChargerSpinner);
        mLostChargerBackButton=(Button)findViewById(R.id.LostChargerBackButton);
        mLostChargerSubmitButton=(Button)findViewById(R.id.LostChargerSubmitButton);



        mLostChargerCompanyName=(EditText) findViewById(R.id.LostChargerCompanyName);
        mLostChargerColor=(EditText)findViewById(R.id.LostChargerColor);
        mLostChargerDate=(EditText)findViewById(R.id.LostChargerDate);
        mLostChargerPlace=(EditText)findViewById(R.id.LostChargerPlace);
        mLostChargerRoomNo=(EditText)findViewById(R.id.LostChargerRoomNo);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        lostCharger= FirebaseDatabase.getInstance().getReference().child("LostChargerActivity");

        LostChargerSpinner();

        mLostChargerBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostChargerActivity.this,LostPageActivity.class));
            }
        });

        mLostChargerSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInfo();
                lostCharger.push().setValue(lostChargerMember);

                Query query3 = FirebaseDatabase.getInstance().getReference("FoundChargerActivity")
                        .orderByChild("check")
                        .equalTo(Check);

                query3.addListenerForSingleValueEvent(lostChargerListner);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();

                setContentView(R.layout.submitted_layout);
            }

            ValueEventListener lostChargerListner = new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            AudioActivityClass BAC = snapshot.getValue(AudioActivityClass.class);
                            String lf_Email = BAC.getEmail();
                            String lf_date = BAC.getDate();

                            lostFound = FirebaseDatabase.getInstance().getReference().child("Lost_Found");

                            LostFound lfobj = new LostFound();
                            lfobj.setFoundEmail(lf_Email);
                            lfobj.setLostEmail(obj1.obj.getEmail());
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
        Intent i=new Intent(LostChargerActivity.this,LostPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void LostChargerSpinner() {
        //******Sort Items Alphabetically******//
        String[] LostChargerTypes=getResources().getStringArray(R.array.charger_types);
        List<String> LostChargerTypesList= Arrays.asList(LostChargerTypes);
        Collections.sort(LostChargerTypesList);
        /////////////////////////////////////////

        ArrayAdapter ChargerAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,LostChargerTypesList);
        ChargerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLostChargerSpinner.setAdapter(ChargerAdapter);
        mLostChargerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                LostChargerType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),LostChargerType,Toast.LENGTH_SHORT).show();
                mLostChargerSpinner.setPrompt("Select Item");
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

                LostChargerDate=sdf.format(myCalendar.getTime());
                mLostChargerDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostChargerDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostChargerActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void getInfo(){

        String LostChargerCompanyName=mLostChargerCompanyName.getText().toString().toLowerCase();
        String LostChargerColor=mLostChargerColor.getText().toString().toLowerCase();
        String LostChargerDate=mLostChargerDate.getText().toString().toLowerCase();
        String LostChargerPlace=mLostChargerPlace.getText().toString().toLowerCase();
        String LostChargerRoomNo=mLostChargerRoomNo.getText().toString().toLowerCase();
        Check= LostChargerType+"_"+LostChargerCompanyName+"_"+LostChargerColor+"_"+LostChargerPlace;

        lostChargerMember.setEmail(obj1.obj.getEmail());
        lostChargerMember.setType(LostChargerType);
        lostChargerMember.setCompanyName(LostChargerCompanyName);
        lostChargerMember.setColour(LostChargerColor);
        lostChargerMember.setDate(LostChargerDate);
        lostChargerMember.setPlace(LostChargerPlace);
        lostChargerMember.setLabNo(LostChargerRoomNo);
        lostChargerMember.setCheck(Check);
    }
}
