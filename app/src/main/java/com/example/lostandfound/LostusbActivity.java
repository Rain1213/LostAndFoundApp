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

public class LostusbActivity extends Activity {

    private Spinner mLostusbSpinner;
    private Button mLostusbBackButton;
    private Button mLostusbSubmitButton;

    private EditText mLostusbCompanyName;
    private EditText mLostusbColor;
    private EditText mLostusbDataCapacity;
    private EditText mLostusbDate;
    private EditText mLostusbPlace;
    private EditText mLostusbRoomNo;
    DatabaseReference lostusb;
    String LostusbType,LostusbDate;

    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();
    private String Check;

    UsbActivityClass lostusbmember = new UsbActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_usb_layout);

        mLostusbSpinner=(Spinner)findViewById(R.id.LostusbSpinner);
        mLostusbBackButton=(Button)findViewById(R.id.LostusbBackButton);
        mLostusbSubmitButton=(Button)findViewById(R.id.LostusbSubmitButton);

        mLostusbBackButton=(Button)findViewById(R.id.LostusbBackButton);
        mLostusbSubmitButton=(Button)findViewById(R.id.LostusbSubmitButton);
        mLostusbCompanyName=(EditText) findViewById(R.id.LostusbCompanyName);
        mLostusbColor=(EditText)findViewById(R.id.LostusbColor);
        mLostusbDataCapacity = (EditText)findViewById(R.id.LostusbDataCapacity);
        mLostusbDate=(EditText)findViewById(R.id.LostusbDate);
        mLostusbPlace=(EditText)findViewById(R.id.LostusbPlace);
        mLostusbRoomNo=(EditText)findViewById(R.id.LostusbRoomNo);

        DatePicker();

        lostusb= FirebaseDatabase.getInstance().getReference().child("LostusbActivity");

        LostusbSpinner();

        mLostusbBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(LostusbActivity.this,LostPageActivity.class));
            }
        });

        mLostusbSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInfo();

                lostusb.push().setValue(lostusbmember);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();

                Query queryLostAudio = FirebaseDatabase.getInstance().getReference("FoundusbActivity")
                        .orderByChild("check")
                        .equalTo(Check);

                queryLostAudio.addListenerForSingleValueEvent(lostusbListner);

                setContentView(R.layout.submitted_layout);
            }

            ValueEventListener lostusbListner = new ValueEventListener() {
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
        });

    }

    private void LostusbSpinner() {
        //******Sort Items Alphabetically******//
        String[] LostusbTypes=getResources().getStringArray(R.array.usb_devices);
        List<String> LostusbTypesList= Arrays.asList(LostusbTypes);
        Collections.sort(LostusbTypesList);
        /////////////////////////////////////////

        ArrayAdapter usbAdapter= new ArrayAdapter(getApplicationContext(),android.R.layout.simple_spinner_item,LostusbTypesList);
        usbAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLostusbSpinner.setAdapter(usbAdapter);
        mLostusbSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //Here storing value of selected item in LostWatchType Variable//
                //((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
                //((TextView) adapterView.getChildAt(1)).setTextColor(Color.WHITE);

                LostusbType=adapterView.getItemAtPosition(i).toString();
                Toast.makeText(adapterView.getContext(),LostusbType,Toast.LENGTH_SHORT).show();
                mLostusbSpinner.setPrompt("Select Item");
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

                LostusbDate=sdf.format(myCalendar.getTime());
                mLostusbDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostusbDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostusbActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void getInfo(){

        String LostusbCompanyName=mLostusbCompanyName.getText().toString().toLowerCase();
        String LostusbColor=mLostusbColor.getText().toString().toLowerCase();
        String LostusbCapacity = mLostusbDataCapacity.getText().toString().toLowerCase();
        String LostusbDate=mLostusbDate.getText().toString().toLowerCase();
        String LostusbPlace=mLostusbPlace.getText().toString().toLowerCase();
        String LostusbRoomNo=mLostusbRoomNo.getText().toString().toLowerCase();
        Check=LostusbCompanyName+"_"+LostusbColor+"_"+LostusbCapacity+"_"+LostusbPlace;

        lostusbmember.setType("usb");
        lostusbmember.setEmail(obj1.obj.getEmail());
        lostusbmember.setLostUsbType(LostusbType);
        lostusbmember.setCompanyName(LostusbCompanyName);
        lostusbmember.setColour(LostusbColor);
        lostusbmember.setDataCapcity(LostusbCapacity);
        lostusbmember.setDate(LostusbDate);
        lostusbmember.setPlace(LostusbPlace);
        lostusbmember.setLabNo(LostusbRoomNo);
        lostusbmember.setCheck(Check);
    }
}
