package com.example.lostandfound;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.LauncherActivity;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import Adapter.RVAdapter;
import Model.RVListItem;

public class MyAccountActivity extends AppCompatActivity {

    private MainActivity obj1 = new MainActivity();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private List<LauncherActivity.ListItem> listItems;

    public String LostEmail;
    public String FoundEmail;


    private  String actLost[]={"LostAudioActivity","LostBagActivity","LostBookActivity","LostChargerActivity","LostMobileActivity",
                           "LostOthersActivity","LostPowerBankActivity","LostusbActivity","LostWatchActivity"};

    private String actFound[]={"FoundAudioActivity","FoundBagActivity","FoundBookActivity","FoundChargerActivity","FoundMobileActivity",
            "FoundOthersActivity","FoundPowerBankActivity","FoundusbActivity","FoundWatchActivity"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        recyclerView=(RecyclerView)findViewById(R.id.MyAccountRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        listItems=new ArrayList<>();

        for(String lostActivity:actLost) {
            DatabaseReference mainReference = FirebaseDatabase.getInstance().getReference(lostActivity);
            final DatabaseReference checkLReference=FirebaseDatabase.getInstance().getReference("Lost_Found");

            mainReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot mainReferenceSnapshot) {

                    if (mainReferenceSnapshot.exists()) {
                        for (final DataSnapshot mainItemSnapshot : mainReferenceSnapshot.getChildren()) {
                            final String lEmail=String.valueOf(mainItemSnapshot.child("email").getValue());
                            final String ItemType=String.valueOf(mainItemSnapshot.child("type").getValue());
                            final String CompanyName=String.valueOf(mainItemSnapshot.child("companyName").getValue());

                            //----------------checkReference Section------------------------------------------------------//
                            checkLReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot checkReferenceSnapshot) {
                                    if(checkReferenceSnapshot.exists()) {
                                        for(DataSnapshot checkItemSnapshot:checkReferenceSnapshot.getChildren()){
                                            String checkInMain=String.valueOf(mainItemSnapshot.child("check").getValue());
                                            String detailInLostFound=String.valueOf(checkItemSnapshot.child("detail").getValue());
                                            String lostEmailInLostFound=String.valueOf(checkItemSnapshot.child("lostEmail").getValue());

                                            if(checkInMain.equals(detailInLostFound) && lEmail.equals(lostEmailInLostFound)){
                                                FoundEmail=String.valueOf(checkItemSnapshot.child("foundEmail").getValue());
                                                RVListItem i=new RVListItem("LOST: "+ItemType,"COMPANY: "+CompanyName,"Found By: "+FoundEmail);
                                                if(lEmail.equals(obj1.obj.getEmail())){
                                                    listItems.add(i);
                                                }
                                                adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                                recyclerView.setAdapter(adapter);
                                            }
                                            else{
                                                RVListItem i=new RVListItem("LOST: "+ItemType,"COMPANY: "+CompanyName,"Found By: ");
                                                if(lEmail.equals(obj1.obj.getEmail())){
                                                    listItems.add(i);
                                                }
                                                adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                                recyclerView.setAdapter(adapter);
                                            }
                                        }
                                    }
                                    else{
                                        RVListItem i=new RVListItem("LOST: "+ItemType,"COMPANY: "+CompanyName,"Found By: ");
                                        if(lEmail.equals(obj1.obj.getEmail())){
                                            listItems.add(i);
                                        }
                                        adapter = new RVAdapter(MyAccountActivity.this, listItems);
                                        recyclerView.setAdapter(adapter);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        for(String foundActivity:actFound) {
            DatabaseReference mainReference= FirebaseDatabase.getInstance().getReference(foundActivity);
            final DatabaseReference checkFReference=FirebaseDatabase.getInstance().getReference("Lost_Found");

            mainReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot mainReferenceSnapshot) {

                    if (mainReferenceSnapshot.exists()) {
                        for (final DataSnapshot mainItemSnapshot : mainReferenceSnapshot.getChildren()) {
                            final String fEmail=String.valueOf(mainItemSnapshot.child("email").getValue());
                            final String ItemType=String.valueOf(mainItemSnapshot.child("type").getValue());
                            final String CompanyName=String.valueOf(mainItemSnapshot.child("companyName").getValue());

                            //----------------checkReference Section------------------------------------------------------//
                            checkFReference.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot checkReferenceSnapshot) {
                                    if(checkReferenceSnapshot.exists()) {
                                        for(DataSnapshot checkItemSnapshot:checkReferenceSnapshot.getChildren()){

                                            String checkInMain=String.valueOf(mainItemSnapshot.child("check").getValue());
                                            String detailInLostFound=String.valueOf(checkItemSnapshot.child("detail").getValue());
                                            String foundEmailInLostFound=String.valueOf(checkItemSnapshot.child("foundEmail").getValue());

                                            if (checkInMain.equals(detailInLostFound) && fEmail.equals(foundEmailInLostFound) && fEmail.equals(obj1.obj.getEmail())) {
                                                    LostEmail = String.valueOf(checkItemSnapshot.child("lostEmail").getValue());
                                                    RVListItem i=new RVListItem("FOUND: "+ItemType,"COMPANY: "+CompanyName,"Lost By: "+LostEmail);
                                                    listItems.add(i);
                                            }
                                            else{
                                                RVListItem i=new RVListItem("FOUND: "+ItemType,"COMPANY: "+CompanyName,"Lost By: ");
                                                if(fEmail.equals(obj1.obj.getEmail())){
                                                    listItems.add(i);
                                                }
                                            }

                                        }
                                    }
                                    else{
                                        RVListItem i=new RVListItem("FOUND: "+ItemType,"COMPANY: "+CompanyName,"Lost By: ");
                                        if(fEmail.equals(obj1.obj.getEmail())){
                                            listItems.add(i);
                                        }

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            adapter = new RVAdapter(MyAccountActivity.this, listItems);
                            recyclerView.setAdapter(adapter);

                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        listItems.clear();
        adapter.notifyDataSetChanged();
    }
}
