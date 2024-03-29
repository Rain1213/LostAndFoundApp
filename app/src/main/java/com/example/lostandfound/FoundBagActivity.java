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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class FoundBagActivity extends Activity {
    private Button mFoundBagBackButton;
    private Button mFoundBagSubmitButton;
    private EditText mFoundBagCompanyName;
    private EditText mFoundBagColor;
    private EditText mFoundBagDate;
    private EditText mFoundBagPlace;
    private EditText mFoundBagRoomNo;
    private EditText mFoundBagSpecialNotes;

    String FoundBagDate;

    FirebaseAuth mAuth;
    DatabaseReference FoundBag;
    BagActivityClass FoundBagMember = new BagActivityClass();

    private String Check;
    private DatabaseReference lostFound;
    MainActivity objfbag = new MainActivity();




    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.found_bag_layout);


        mFoundBagSubmitButton=(Button)findViewById(R.id.FoundBagSubmitButton);
        mFoundBagBackButton=(Button)findViewById(R.id.FoundBagBackButton);
        mFoundBagCompanyName = (EditText)findViewById(R.id.FoundBagCompanyName);
        mFoundBagColor = (EditText)findViewById(R.id.FoundBagColor);
        mFoundBagDate= (EditText)findViewById(R.id.FoundBagDate);
        mFoundBagPlace =(EditText)findViewById(R.id.FoundBagPlace);
        mFoundBagRoomNo = (EditText)findViewById(R.id.FoundBagRoomNo);
        mFoundBagSpecialNotes = (EditText)findViewById(R.id.FoundBagSpecialNotes);


        mFoundBagSubmitButton=(Button)findViewById(R.id.FoundBagSubmitButton);
        mFoundBagBackButton=(Button)findViewById(R.id.FoundBagBackButton);


        /*------------------------------------- Date Picker Section START-------------------------------------------------------*/
        DatePicker();
        /*--------------------------------- Date Picker Section END-------------------------------------------------------------*/


        FoundBag= FirebaseDatabase.getInstance().getReference().child("FoundBagActivity");

        mFoundBagBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(FoundBagActivity.this,FoundPageActivity.class));
            }
        });

        mFoundBagSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getInfo();

                FoundBag.push().setValue(FoundBagMember);

                Query query3 = FirebaseDatabase.getInstance().getReference("LostBagActivity")
                        .orderByChild("check")
                        .equalTo(Check);

                query3.addListenerForSingleValueEvent(FoundBagEventListner);

                Toast.makeText(getApplicationContext(), "data inserted",Toast.LENGTH_SHORT).show();


                setContentView(R.layout.submitted_layout);
            }
                                                     ValueEventListener FoundBagEventListner = new ValueEventListener() {
                                                         @Override
                                                         public void onDataChange(DataSnapshot dataSnapshot) {

                                                             if (dataSnapshot.exists()) {
                                                                 for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                                     BagActivityClass BAC = snapshot.getValue(BagActivityClass.class);
                                                                     String lf_Email = BAC.getEmail();
                                                                     String lf_date = BAC.getDate();

                                                                     lostFound = FirebaseDatabase.getInstance().getReference().child("Lost_Found");

                                                                     LostFound lfobj = new LostFound();

                                                                     lfobj.setLostEmail(lf_Email);
                                                                     lfobj.setFoundEmail(objfbag.obj.getEmail());
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

    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(FoundBagActivity.this,FoundPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private void getInfo(){

        String FoundBagCompanyName=mFoundBagCompanyName.getText().toString().toLowerCase();
        String FoundBagColor=mFoundBagColor.getText().toString().toLowerCase();
        String FoundBagDate=mFoundBagDate.getText().toString().toLowerCase();
        String FoundBagPlace=mFoundBagPlace.getText().toString().toLowerCase();
        String FoundBagRoomNo=mFoundBagRoomNo.getText().toString().toLowerCase();
        String FoundBagExtra = mFoundBagSpecialNotes.getText().toString().toLowerCase();
        Check = FoundBagCompanyName + "_" + FoundBagColor + "_" + FoundBagPlace;



        FoundBagMember.setType("bag");
        FoundBagMember.setEmail(objfbag.obj.getEmail());
        FoundBagMember.setCompanyName(FoundBagCompanyName);
        FoundBagMember.setColour(FoundBagColor);
        FoundBagMember.setDate(FoundBagDate);
        FoundBagMember.setPlace(FoundBagPlace);
        FoundBagMember.setLabNo(FoundBagRoomNo);
        FoundBagMember.setExtra(FoundBagExtra);
        FoundBagMember.setCheck(Check);

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

                FoundBagDate=sdf.format(myCalendar.getTime());
                mFoundBagDate.setText(sdf.format(myCalendar.getTime()));
            }

        };

        mFoundBagDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(FoundBagActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
}
