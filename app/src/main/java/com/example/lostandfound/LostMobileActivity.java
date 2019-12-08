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

public class LostMobileActivity extends Activity {
    private Button mLostMobileBackButton;
    private Button mLostMobileSubmitButton;

    private EditText mLostMobileCompanyName;
    private EditText mLostMobileModelName;
    private EditText mLostMobileColor;
    private EditText mLostMobileDate;
    private EditText mLostMobilePlace;
    private EditText mLostMobileRoomNo;
    private EditText mLostMobileSpecialNotes;

    String LostMobileDate;

    private DatabaseReference lostFound;
    private MainActivity obj1 = new MainActivity();
    String Check;

    DatabaseReference lostMobile;

    MobileActivityClass lostMobileMember = new MobileActivityClass();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_mobile_layout);

        mLostMobileBackButton=(Button)findViewById(R.id.LostMobileBackButton);
        mLostMobileSubmitButton=(Button)findViewById(R.id.LostMobileSubmitButton);

        mLostMobileCompanyName=(EditText) findViewById(R.id.LostMobileCompanyName);
        mLostMobileModelName=(EditText)findViewById(R.id.LostMobileModel);
        mLostMobileColor=(EditText)findViewById(R.id.LostMobileColor);
        mLostMobileDate=(EditText)findViewById(R.id.LostMobileDate);
        mLostMobilePlace=(EditText)findViewById(R.id.LostMobilePlace);
        mLostMobileRoomNo=(EditText)findViewById(R.id.LostMobileRoomNo);
        mLostMobileSpecialNotes = (EditText)findViewById(R.id.LostMobileSpecialNotes);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        lostMobile= FirebaseDatabase.getInstance().getReference().child("LostMobileActivity");

        mLostMobileBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LostMobileActivity.this,LostPageActivity.class));
            }
        });

        mLostMobileSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                getInfo();
                lostMobile.push().setValue(lostMobileMember);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();

                Query queryLostMobile = FirebaseDatabase.getInstance().getReference("FoundMobileActivity")
                        .orderByChild("check")
                        .equalTo(Check);

                queryLostMobile.addListenerForSingleValueEvent(lostMobileListner);

                setContentView(R.layout.submitted_layout);
            }

            ValueEventListener lostMobileListner = new ValueEventListener() {
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
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(LostMobileActivity.this,LostPageActivity.class);
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

                LostMobileDate=sdf.format(myCalendar.getTime());
                mLostMobileDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mLostMobileDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(LostMobileActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private void getInfo(){
        String LostMobileCompanyName=mLostMobileCompanyName.getText().toString().toLowerCase();
        String LostMobileModelName = mLostMobileModelName.getText().toString().toLowerCase();
        String LostMobileColor=mLostMobileColor.getText().toString().toLowerCase();
        String LostMobileDate=mLostMobileDate.getText().toString().toLowerCase();
        String LostMobilePlace=mLostMobilePlace.getText().toString().toLowerCase();
        String LostMobileRoomNo=mLostMobileRoomNo.getText().toString().toLowerCase();
        String LostMobileSpecialNotes = mLostMobileSpecialNotes.getText().toString().toLowerCase();
        Check=LostMobileCompanyName+"_"+LostMobileModelName+"_"+LostMobilePlace;


        lostMobileMember.setEmail(obj1.obj.getEmail());
        lostMobileMember.setType("mobile");
        lostMobileMember.setCompanyName(LostMobileCompanyName);
        lostMobileMember.setModelName(LostMobileModelName);
        lostMobileMember.setColour(LostMobileColor);
        lostMobileMember.setDate(LostMobileDate);
        lostMobileMember.setPlace(LostMobilePlace);
        lostMobileMember.setLabNo(LostMobileRoomNo);
        lostMobileMember.setSpecialNotes(LostMobileSpecialNotes);
        lostMobileMember.setCheck(Check);
    }
}
