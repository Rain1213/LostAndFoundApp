package com.example.lostandfound;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

public class LostPageActivity extends Activity {
    private GridLayout mLostGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lost_grid_layout);


        mLostGrid=(GridLayout)findViewById(R.id.LostGridLayoutID);

        setSingleEvent(mLostGrid);
    }

    private void setSingleEvent(GridLayout mLostGrid) {
        final CardView LostWatchCard=(CardView)mLostGrid.getChildAt(0);
        final CardView LostAudioCard=(CardView)mLostGrid.getChildAt(1);
        final CardView LostMobileCard=(CardView)mLostGrid.getChildAt(2);
        final CardView LostusbCard=(CardView)mLostGrid.getChildAt(3);
        final CardView LostBookCard=(CardView)mLostGrid.getChildAt(4);
        final CardView LostBagCard=(CardView)mLostGrid.getChildAt(5);
        final CardView LostChargerCard=(CardView)mLostGrid.getChildAt(6);
        final CardView LostPowerBankCard=(CardView)mLostGrid.getChildAt(7);
        final CardView LostOthersCard=(CardView)mLostGrid.getChildAt(8);

        LostWatchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LostPageActivity.this, "Watch", Toast.LENGTH_SHORT).show();
                LostWatchCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(LostPageActivity.this,LostWatchActivity.class));
            }
        });

        LostAudioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LostPageActivity.this, "Audio Devices", Toast.LENGTH_SHORT).show();
                LostAudioCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(LostPageActivity.this,LostAudioActivity.class));
            }
        });

        LostusbCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LostPageActivity.this, "USB Devices", Toast.LENGTH_SHORT).show();
                LostusbCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(LostPageActivity.this,LostusbActivity.class));
            }
        });

        LostBookCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LostPageActivity.this, "Book", Toast.LENGTH_SHORT).show();
                LostBookCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(LostPageActivity.this,LostBookActivity.class));
            }
        });

        LostBagCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LostPageActivity.this, "Bag", Toast.LENGTH_SHORT).show();
                LostBagCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(LostPageActivity.this,LostBagActivity.class));
            }
        });

        LostChargerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LostPageActivity.this, "Chargers", Toast.LENGTH_SHORT).show();
                LostChargerCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(LostPageActivity.this,LostChargerActivity.class));
            }
        });

        LostChargerCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LostPageActivity.this, "Chargers", Toast.LENGTH_SHORT).show();
                LostChargerCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(LostPageActivity.this,LostChargerActivity.class));
            }
        });

        LostPowerBankCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LostPageActivity.this, "PowerBank", Toast.LENGTH_SHORT).show();
                LostPowerBankCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(LostPageActivity.this,LostPowerBankActivity.class));
            }
        });

        LostMobileCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LostPageActivity.this, "Mobile", Toast.LENGTH_SHORT).show();
                LostMobileCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(LostPageActivity.this,LostMobileActivity.class));
            }
        });

        LostOthersCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LostPageActivity.this, "Others", Toast.LENGTH_SHORT).show();
                LostOthersCard.setCardBackgroundColor(Color.YELLOW);
                startActivity(new Intent(LostPageActivity.this,LostOthersActivity.class));
            }
        });
    }
    public void onBackPressed(){
        super.onBackPressed();
        Intent i=new Intent(LostPageActivity.this,TwoButtonsActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }
}
