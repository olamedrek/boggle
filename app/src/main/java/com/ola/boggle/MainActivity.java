package com.ola.boggle;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RelativeLayout boardLayout;
    private TextView timerView;

    private SwipeMenuListView players;
    private SwipeListAdapter adapter = new SwipeListAdapter();

    private List<TextView> letterViews = new ArrayList<>();
    private Board board = new Board();

    private long startTime = 0;
    private Handler timerHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardLayout = (RelativeLayout) findViewById(R.id.board_layout);
        timerView = (TextView) findViewById(R.id.timer_view);

        players = (SwipeMenuListView) findViewById(R.id.players);
        players.setAdapter(adapter);
        players.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        createListOfLetterViews();

        boardLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                for(TextView letterView : letterViews) {
                    letterView.setWidth(letterView.getHeight());
                    letterView.setBackgroundResource(R.drawable.rounded_corner);
                    letterView.setGravity(Gravity.CENTER);
                    letterView.setTextSize(96);
                }
            }
        });

        players.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                players.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                adapter.removePlayer(position);
            }
        });

        players.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem dummyItem = new SwipeMenuItem(getApplicationContext());
                dummyItem.setWidth(300);
                menu.addMenuItem(dummyItem);
            }
        });

        players.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Points:");
                final EditText input = new EditText(MainActivity.this);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.addPoints(i, Integer.parseInt(input.getText().toString()));
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacksAndMessages(null);
    }

    public void onClickNewBoardButton(View view) {
        setBoardWithAnimation(board.getNewBoard());
        timerView.setText("3:00");
        timerHandler.removeCallbacksAndMessages(null);
    }

    public void onClickStartButton(View view) {
        startTime = System.currentTimeMillis();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                long seconds = (System.currentTimeMillis() - startTime) / 1000;
                long secondsLeft = 10 - seconds;
                displayTime(secondsLeft);
                if(secondsLeft > 0) {
                    timerHandler.postDelayed(this, 1000);
                } else {
                    Toast.makeText(MainActivity.this, "Time is up", Toast.LENGTH_SHORT).show();
                    setBoard(null);
                    timerHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setBoard(board.getCurrentBoard());
                            timerView.setText("3:00");
                        }
                    }, 2000);
                }
            }
        };
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void onClickAddPlayerButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Name:");
        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                adapter.addPlayer(input.getText().toString());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    private void displayTime(long secondsLeft) {
        long minutes = secondsLeft / 60;
        long seconds = secondsLeft % 60;
        String pattern = (seconds > 9 ? "%s:%s" : "%s:0%s");
        timerView.setText(String.format(pattern, minutes, seconds));
    }

    private void setBoardWithAnimation(String board) {
        for(int i = 0; i < letterViews.size(); i++) {
            final String value = (board != null ? String.valueOf(board.charAt(i)) : "");
            final TextView letterView = letterViews.get(i);
            letterView.setText("");
            Rotate3dAnimation rotation = new Rotate3dAnimation(180, 360, letterView.getWidth()/2,
                    letterView.getHeight()/2, 100, false);
            rotation.setDuration(500);
            rotation.setFillAfter(true);
            rotation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}
                @Override
                public void onAnimationEnd(Animation animation) {
                    letterView.setText(value);
                }
                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            letterView.startAnimation(rotation);
        }
    }

    private void setBoard(String board) {
        for(int i = 0; i < letterViews.size(); i++) {
            final String value = (board != null ? String.valueOf(board.charAt(i)) : "");
            final TextView letterView = letterViews.get(i);
            letterView.setText(value);
        }
    }

    private void createListOfLetterViews() {
        letterViews.add((TextView) findViewById(R.id.letter_view1));
        letterViews.add((TextView) findViewById(R.id.letter_view2));
        letterViews.add((TextView) findViewById(R.id.letter_view3));
        letterViews.add((TextView) findViewById(R.id.letter_view4));
        letterViews.add((TextView) findViewById(R.id.letter_view5));
        letterViews.add((TextView) findViewById(R.id.letter_view6));
        letterViews.add((TextView) findViewById(R.id.letter_view7));
        letterViews.add((TextView) findViewById(R.id.letter_view8));
        letterViews.add((TextView) findViewById(R.id.letter_view9));
        letterViews.add((TextView) findViewById(R.id.letter_view10));
        letterViews.add((TextView) findViewById(R.id.letter_view11));
        letterViews.add((TextView) findViewById(R.id.letter_view12));
        letterViews.add((TextView) findViewById(R.id.letter_view13));
        letterViews.add((TextView) findViewById(R.id.letter_view14));
        letterViews.add((TextView) findViewById(R.id.letter_view15));
        letterViews.add((TextView) findViewById(R.id.letter_view16));
    }
}
