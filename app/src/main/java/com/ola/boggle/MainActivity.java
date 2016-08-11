package com.ola.boggle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TextView> letterViews = new ArrayList<>();
    private RelativeLayout boardLayout;
    private Board board = new Board();
    private Button newButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardLayout = (RelativeLayout) findViewById(R.id.board_layout);
        newButton = (Button) findViewById(R.id.new_button);

        letterViews.add((TextView) findViewById(R.id.letter_view1));
        letterViews.add((TextView) findViewById(R.id.letter_view2));
        letterViews.add((TextView) findViewById(R.id.letter_view3));
        letterViews.add((TextView) findViewById(R.id.letter_view4));

        boardLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for(TextView letterView : letterViews) {
                    letterView.setWidth(letterView.getHeight());
                    letterView.setBackgroundResource(R.drawable.rounded_corner);
                    letterView.setGravity(Gravity.CENTER);
                    letterView.setTextSize(80);
                }
            }
        });

        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = board.getNewBoard();
                for(int i = 0; i < letterViews.size(); i++) {
                    letterViews.get(i).setText(String.valueOf(text.charAt(i)));
                }
            }
        });





    }

}
