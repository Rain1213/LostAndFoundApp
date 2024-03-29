package com.example.lostandfound;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FoundPowerBankActivity extends Activity {
    private Button mFoundPowerBankBackButton;
    private Button mFoundPowerBankSubmitButton;

    private EditText mFoundPowerBankCapacity;
    private EditText mFoundPowerBankCompanyName;
    private EditText mFoundPowerBankColor;
    private EditText mFoundPowerBankDate;
    private EditText mFoundPowerBankPlace;
    private EditText mFoundPowerBankRoomNo;
    private EditText mFoundPowerBankSpecialNotes;

    String FoundPowerBankDate;

    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();
    private String Check;

    DatabaseReference foundPowerBank;

    PowerBankActivityClass foundPowerBankMember = new PowerBankActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_powerbank_layout);

        mFoundPowerBankBackButton=(Button)findViewById(R.id.FoundPowerBankBackButton);
        mFoundPowerBankSubmitButton=(Button)findViewById(R.id.FoundPowerBankSubmitButton);

        mFoundPowerBankCompanyName=(EditText) findViewById(R.id.FoundPowerBankCompanyName);
        mFoundPowerBankCapacity=(EditText)findViewById(R.id.FoundPowerBankCapacity);
        mFoundPowerBankColor=(EditText)findViewById(R.id.FoundPowerBankColor);
        mFoundPowerBankDate=(EditText)findViewById(R.id.FoundPowerBankDate);
        mFoundPowerBankPlace=(EditText)findViewById(R.id.FoundPowerBankPlace);
        mFoundPowerBankRoomNo=(EditText)findViewById(R.id.FoundPowerBankRoomNo);
        mFoundPowerBankSpecialNotes = (EditText)findViewById(R.id.FoundPowerBankSpecialNotes);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
         DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        foundPowerBank= FirebaseDatabase.getInstance().getReference().child("FoundPowerBankActivity");

        mFoundPowerBankSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getInfo();
                foundPowerBank.push().setValue(foundPowerBankMember);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();

                Query query3 = FirebaseDatabase.getInstance().getReference("LostPowerBankActivity")
                        .orderByChild("check")
                        .equalTo(Check);
                query3.addListenerForSingleValueEvent(foundPowerBankListner);

                setContentView(R.layout.submitted_layout);
            }

            ValueEventListener foundPowerBankListner = new ValueEventListener() {
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

        mFoundPowerBankBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoundPowerBankActivity.this,FoundPageActivity.class));
            }
        });

    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(FoundPowerBankActivity.this,FoundPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
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

                FoundPowerBankDate=sdf.format(myCalendar.getTime());
                mFoundPowerBankDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mFoundPowerBankDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FoundPowerBankActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void getInfo(){
        String FoundPowerBankCompanyName=mFoundPowerBankCompanyName.getText().toString();
        String FoundPowerBankCapacity = mFoundPowerBankCapacity.getText().toString();
        String FoundPowerBankColor=mFoundPowerBankColor.getText().toString();
        String FoundPowerBankDate=mFoundPowerBankDate.getText().toString();
        String FoundPowerBankPlace=mFoundPowerBankPlace.getText().toString();
        String FoundPowerBankRoomNo=mFoundPowerBankRoomNo.getText().toString();
        String FoundPowerBankSpecialNotes = mFoundPowerBankSpecialNotes.getText().toString();
        Check=FoundPowerBankCompanyName+"_"+FoundPowerBankCapacity+"_"+FoundPowerBankColor+"_"+FoundPowerBankPlace;


        foundPowerBankMember.setType("powerbank");
        foundPowerBankMember.setEmail(obj1.obj.getEmail());
        foundPowerBankMember.setCapacity(FoundPowerBankCapacity);
        foundPowerBankMember.setCompanyName(FoundPowerBankCompanyName);
        foundPowerBankMember.setColour(FoundPowerBankColor);
        foundPowerBankMember.setDate(FoundPowerBankDate);
        foundPowerBankMember.setPlace(FoundPowerBankPlace);
        foundPowerBankMember.setLabNo(FoundPowerBankRoomNo);
        foundPowerBankMember.setSpecialNotes(FoundPowerBankSpecialNotes);
        foundPowerBankMember.setCheck(Check);
    }
}
