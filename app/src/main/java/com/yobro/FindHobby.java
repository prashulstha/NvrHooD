package com.yobro;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yobro.JavaClasses.FirebaseHelper;

import java.util.ArrayList;


public class FindHobby extends Fragment
{
    GridLayout mainGrid;
    ArrayList<String> HobbyList;
    FirebaseHelper firebaseHelper = new FirebaseHelper();
    Button mbutton;

    int finalI;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find_hobby, container, false);


        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mainGrid = view.findViewById(R.id.mainGrid);
        HobbyList= storeHobby(mainGrid);
        HobbyList = new ArrayList<String>();
        mbutton =view.findViewById(R.id.nextButton);
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                firebaseHelper.saveHobby(HobbyList);


                for(String list: HobbyList)
                    Log.d("LOBBY", "onClick: " + list);
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
    }



    private ArrayList<String> storeHobby(GridLayout mainGrid)
    {

        for (int i=0;i<mainGrid.getChildCount();i++)
        {
            CardView cardView =(CardView) mainGrid.getChildAt(i);
             finalI = i;

            cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    if (finalI ==0)
                        HobbyList.add("Music");
                    else if (finalI==1)
                        HobbyList.add("Sports");
                    else if (finalI==2)
                        HobbyList.add("Photography");
                    else if (finalI==3)
                        HobbyList.add("Video Game");
                    else if (finalI==4)
                        HobbyList.add("Hiking");
                    else if (finalI==5)
                        HobbyList.add("Dance");
                    else if (finalI==6)
                        HobbyList.add("Writing");
                    else if (finalI==7)
                        HobbyList.add("Shopping");
                    else if (finalI==8)
                        HobbyList.add("Cycling");
                    else if (finalI==9)
                        HobbyList.add("Poker");
                    else if (finalI==10)
                        HobbyList.add("Programming");
                    else if (finalI==11)
                        HobbyList.add("Drama");


                }
            });


        }

        return HobbyList;


    }

}

