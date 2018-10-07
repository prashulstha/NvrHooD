package com.yobro;

import android.content.Intent;
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

    String hobbies= " ";
    FirebaseHelper firebaseHelper = new FirebaseHelper();
     FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby_finder);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

       fab  = findViewById(R.id.fab);

       fab.setEnabled(false);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitHobbies();
                Intent intent = new Intent(HobbyFinder.this,MapActivityHome.class);
                startActivity(intent);
            }
        });


    }

    private void submitHobbies()
    {
        firebaseHelper.saveHobby(hobbies);

    }

    public void musicClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView music = findViewById(R.id.music);
        music.setCardBackgroundColor(Color.WHITE);
        hobbies += "music ";




    }

    public void SportsClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Sports =  findViewById(R.id.Sports);
        Sports.setCardBackgroundColor(Color.WHITE);
        hobbies += "Sports ";
    }

    public void PhotographyClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Photography =  findViewById(R.id.Photography);
        Photography.setCardBackgroundColor(Color.WHITE);
        hobbies +="Photography ";
    }

    public void VideoGameClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView VideoGame = findViewById(R.id.VideoGame);
        VideoGame.setCardBackgroundColor(Color.WHITE);
        hobbies +="Video_Game ";
    }

    public void HikingClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Hiking = findViewById(R.id.Hiking);
        Hiking.setCardBackgroundColor(Color.WHITE);
        hobbies +="Hiking ";
    }

    public void DanceClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Dance = findViewById(R.id.Dance);
        Dance.setCardBackgroundColor(Color.WHITE);
        hobbies +="Dance ";
    }

    public void WritingClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Writing = findViewById(R.id.Writing);
        Writing.setCardBackgroundColor(Color.WHITE);
        hobbies +="Writing ";
    }

    public void ShoppingClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Shopping = findViewById(R.id.Shopping);
        Shopping.setCardBackgroundColor(Color.WHITE);
        hobbies+="Shopping ";
    }

    public void CyclingClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Cycling = findViewById(R.id.Cycling);
        Cycling.setCardBackgroundColor(Color.WHITE);
        hobbies+="Cycling ";
    }

    public void PokerClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Poker = findViewById(R.id.Poker);
        Poker.setCardBackgroundColor(Color.WHITE);
        hobbies+="Poker ";
    }

    public void ProgrammingClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Programming = findViewById(R.id.Programming);
        Programming.setCardBackgroundColor(Color.WHITE);
        hobbies+="Programming ";
    }

    public void DramaClick(View view)
    {
        fab.setEnabled(true);
        Toast.makeText(getApplicationContext(), "Item is Selected", Toast.LENGTH_LONG).show();
        CardView Drama = findViewById(R.id.Drama);
        Drama.setCardBackgroundColor(Color.WHITE);
        hobbies+="Drama ";
    }
}
