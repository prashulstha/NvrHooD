package com.yobro;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.yobro.JavaClasses.FirebaseHelper;

public class HobbyFinder extends AppCompatActivity {

    Button submit;
    String hobbies= " ";
    FirebaseHelper firebaseHelper = new FirebaseHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby_finder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        submit =(Button) findViewById(R.id.nextButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitHobbies();
            }
        });
    }

    private void submitHobbies()
    {
        firebaseHelper.saveHobby(hobbies);

    }

    public void musicClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView music = (CardView) (findViewById(R.id.music));
        music.setCardBackgroundColor(Color.WHITE);
        hobbies += "music ";




    }

    public void SportsClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Sports = (CardView) (findViewById(R.id.Sports));
        Sports.setCardBackgroundColor(Color.WHITE);
        hobbies += "Sports ";
    }

    public void PhotographyClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Photography = (CardView) (findViewById(R.id.Photography));
        Photography.setCardBackgroundColor(Color.WHITE);
        hobbies +="Photography ";
    }

    public void VideoGameClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView VideoGame = (CardView) (findViewById(R.id.VideoGame));
        VideoGame.setCardBackgroundColor(Color.WHITE);
        hobbies +="Video_Game ";
    }

    public void HikingClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Hiking = (CardView) (findViewById(R.id.Hiking));
        Hiking.setCardBackgroundColor(Color.WHITE);
        hobbies +="Hiking ";
    }

    public void DanceClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Dance = (CardView) (findViewById(R.id.Dance));
        Dance.setCardBackgroundColor(Color.WHITE);
        hobbies +="Dance ";
    }

    public void WritingClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Writing = (CardView) (findViewById(R.id.Writing));
        Writing.setCardBackgroundColor(Color.WHITE);
        hobbies +="Writing ";
    }

    public void ShoppingClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Shopping = (CardView) (findViewById(R.id.Shopping));
        Shopping.setCardBackgroundColor(Color.WHITE);
        hobbies+="Shopping ";
    }

    public void CyclingClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Cycling = (CardView) (findViewById(R.id.Cycling));
        Cycling.setCardBackgroundColor(Color.WHITE);
        hobbies+="Cycling ";
    }

    public void PokerClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Poker = (CardView) (findViewById(R.id.Poker));
        Poker.setCardBackgroundColor(Color.WHITE);
        hobbies+="Poker ";
    }

    public void ProgrammingClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Programming = (CardView) (findViewById(R.id.Programming));
        Programming.setCardBackgroundColor(Color.WHITE);
        hobbies+="Programming ";
    }

    public void DramaClick(View view)
    {
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Drama = (CardView) (findViewById(R.id.Drama));
        Drama.setCardBackgroundColor(Color.WHITE);
        hobbies+="Drama ";
    }
}
