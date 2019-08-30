package com.pram.puzzlegame;

import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        final ViewPager pager = findViewById(R.id.imagePager);
        final PagerAdapter adapter = new ImagePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        Button play = findViewById(R.id.imagePlay);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PuzzleActivity.class);
                intent.putExtra(ImageStore.IMAGE_INDEX_NAME, pager.getCurrentItem());
                intent.setFlags(intent.getFlags() | Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });
    }
}
