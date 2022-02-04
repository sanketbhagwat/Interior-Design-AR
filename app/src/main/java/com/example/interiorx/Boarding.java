package com.example.interiorx;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class Boarding extends AppCompatActivity{
    ViewPager2 viewPager2;
    ArrayList<onboardingItems> onboardingItemsArrayList;
    LinearLayout onDotlayout;
    Button Skip,Back,Nextto;
    TextView[] dots;
    DotsIndicator dotsIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        checkFirstOpen();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_on_boarding);
        viewPager2 = findViewById(R.id.slider);
        int image[]= {
                R.drawable.livingroom,
                R.drawable.livingroom2,
                R.drawable.bathroom,
                R.drawable.kidsroom
        };
        String  headings[]={
                getString(R.string.heading1),
                getString(R.string.heading2),
                getString(R.string.heading3),
                getString(R.string.heading1),

        };
        String  explanations[]={getString(R.string.expl1),
                getString(R.string.expl2),
                getString(R.string.expl3),
                getString(R.string.expl1),

        };


        Skip=findViewById(R.id.skip);
//        Back=findViewById(R.id.back);
//        Nextto=findViewById(R.id.nextto);
//

//        Back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(getitem(0)>0){
//                    viewPager2.setCurrentItem(getitem(-1),true);
//                }
//
//
//            }
//        });

        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent i=new Intent(Boarding.this, MainActivity.class);
//                startActivity(i);
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();

            }
        });

//        Nextto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(getitem(0)<3){
//                    viewPager2.setCurrentItem(getitem(1),true);
//                }
//                else{
//                    Intent i=new Intent(Boarding.this, MainActivity.class);
//                    startActivity(i);
//                    finish();
//                }
//
//
//            }
//        });

        onboardingItemsArrayList=new ArrayList<>();
        for(int i=0;i<image.length;i++){
            onboardingItems boardingItem=new onboardingItems(image[i],headings[i],explanations[i]);
            onboardingItemsArrayList.add(boardingItem);

        }
        viewPageAdapter vpAdapter =new viewPageAdapter(onboardingItemsArrayList);

        viewPager2.setAdapter(vpAdapter);
        dotsIndicator=findViewById(R.id.indicator);
        dotsIndicator.setViewPager2(viewPager2);

    }

    public int getitem(int i){
        return viewPager2.getCurrentItem()+i;
    }
}