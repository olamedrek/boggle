package com.ola.boggle;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<TextView> letterViews = new ArrayList<>();
    private RelativeLayout boardLayout;
    private Board board = new Board();
    private Button newBoardButton;
    private long startTime = 0;
    private TextView timerView;
    private Button startButton;
    private Button addPlayerButton;
    private MyAdapter myAdapter = new MyAdapter();
    private ListView players;

    Handler timerHandler = new Handler();

    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long seconds = (System.currentTimeMillis() - startTime) / 1000;
            long left = 10 - seconds;

            if(left > 0) {
                timerHandler.postDelayed(this, 1000);
            } else {
                Toast.makeText(MainActivity.this, "", Toast.LENGTH_LONG);
            }
            timerView.setText(String.format("%s:%s", left/60, left%60));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boardLayout = (RelativeLayout) findViewById(R.id.board_layout);
        newBoardButton = (Button) findViewById(R.id.newboard_button);
        timerView = (TextView) findViewById(R.id.timer_view);
        startButton = (Button) findViewById(R.id.start_button);
        players = (ListView) findViewById(R.id.players);
        players.setAdapter(myAdapter);
        addPlayerButton = (Button) findViewById(R.id.addplayer_button);

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

        newBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newBoard = board.getNewBoard();
                for(int i = 0; i < letterViews.size(); i++) {
                    letterViews.get(i).setText(String.valueOf(newBoard.charAt(i)));
                }
                timerView.setText("3:00");
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTime = System.currentTimeMillis();
                timerHandler.postDelayed(timerRunnable, 0);
            }
        });

        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAdapter.addPlayer("Player", 100);
            }
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
    }

    private class MyAdapter extends BaseAdapter {

        private List<Player> players = new ArrayList<>();

        public void addPlayer(String name, Integer point) {
            Player n = new Player();
            n.setName(name);
            n.setScore(point);
            players.add(n);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return players.size();
        }

        @Override
        public Object getItem(int i) {
            return players.get(i);
        }

        @Override
        public long getItemId(int i) {
            return players.get(i).hashCode();
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if(view == null) {
                view = getLayoutInflater().inflate(R.layout.player_layout, null);
            }
            Player player = players.get(i);
            ((TextView)(view.findViewById(R.id.player_name))).setText(player.getName());
            ((TextView)(view.findViewById(R.id.player_score))).setText(String.valueOf(player.getScore()));

            return view;
        }
    }
}
