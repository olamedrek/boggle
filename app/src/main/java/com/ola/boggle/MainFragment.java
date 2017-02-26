package com.ola.boggle;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class MainFragment extends Fragment {

    private RelativeLayout solutionLayout;
    private RelativeLayout playersLayout;
    private TextView timerView;
    private Button showSolutionButton;
    private SwipeMenuListView players;
    private RelativeLayout boardLayout;

    private SolutionListAdapter solutionAdapter = new SolutionListAdapter();
    private PlayersListAdapter playersAdapter = new PlayersListAdapter();
    private Board board = new Board();
    private Handler timerHandler = new Handler();

    private List<TextView> letterViews = new ArrayList<>();
    private long startTime = 0;
    private int gameDuration = 180;


    public MainFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        solutionLayout = (RelativeLayout) view.findViewById(R.id.solution_layout);
        playersLayout = (RelativeLayout) view.findViewById(R.id.players_layout);
        timerView = (TextView) view.findViewById(R.id.timer_view);
        showSolutionButton = (Button) view.findViewById(R.id.solution_button);
        players = (SwipeMenuListView) view.findViewById(R.id.players);
        boardLayout = (RelativeLayout) view.findViewById(R.id.board_layout);
        ListView solutionView = (ListView) view.findViewById(R.id.solution_view);

        displayTime(gameDuration);
        solutionView.setAdapter(solutionAdapter);
        players.setAdapter(playersAdapter);
        players.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);

        for(int i = 0; i < boardLayout.getChildCount(); i++) {
            letterViews.add((TextView) boardLayout.getChildAt(i));
        }

        boardLayout.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            for(TextView letterView : letterViews) {
                letterView.setWidth(letterView.getHeight());
                letterView.setBackgroundResource(R.drawable.letter);
                letterView.setTextColor(getResources().getColor(R.color.colorLetters));
                letterView.setGravity(Gravity.CENTER);
                letterView.setTextSize(96);
            }
        });

        players.setMenuCreator(menu -> {
            SwipeMenuItem dummyItem = new SwipeMenuItem(view.getContext());
            dummyItem.setWidth(300);
            menu.addMenuItem(dummyItem);
        });

        setListenersForPlayersView();

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacksAndMessages(null);
    }

    public void onClickNewBoardButton(View newBoardButton) {
        showSolutionButton.setEnabled(false);
        newBoardButton.setEnabled(false);
        setBoardWithAnimation(board.getNewBoard());
        displayTime(gameDuration);
        timerHandler.removeCallbacksAndMessages(null);

        new AsyncTask<Void, Void, Set<String>>() {
            @Override
            protected Set<String> doInBackground(Void... voids) {
                Solution solution = new Solution(getResources().openRawResource(R.raw.dictionary));
                return solution.findAllWords(board.getCurrentBoard());
            }

            @Override
            protected void onPostExecute(Set<String> strings) {
                solutionAdapter.replaceWords(strings);
                showSolutionButton.setEnabled(true);
                newBoardButton.setEnabled(true);
            }
        }.execute();
    }

    public void onClickStartButton(final View view) {
        startTime = System.currentTimeMillis();
        Runnable timerRunnable = new Runnable() {
            @Override
            public void run() {
                long secondsPassed = (System.currentTimeMillis() - startTime) / 1000;
                long secondsLeft = gameDuration - secondsPassed;
                displayTime(secondsLeft);
                if(secondsLeft > 0) {
                    timerHandler.postDelayed(this, 1000);
                } else {
                    Toast.makeText(view.getContext(), "Time is up", Toast.LENGTH_SHORT).show();
                    setBoard(null);
                    timerHandler.postDelayed(() -> {
                        setBoard(board.getCurrentBoard());
                        displayTime(gameDuration);
                    }, 2500);
                }
            }
        };
        timerHandler.postDelayed(timerRunnable, 0);
    }

    public void onClickAddPlayerButton(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        builder.setTitle("Enter players' names:");
        final EditText input = new EditText(view.getContext());
        input.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {
            String[] names = input.getText().toString().split(" ");
            for(String name : names) {
                playersAdapter.addPlayer(name);
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> {
            dialog.cancel();
        });
        builder.show();
    }

    public void onClickBackButton(View view) {
        playersLayout.setVisibility(View.VISIBLE);
        solutionLayout.setVisibility(View.INVISIBLE);
        showSolutionButton.setVisibility(View.VISIBLE);
    }

    public void onClickShowSolutionButton(View view) {
        playersLayout.setVisibility(View.INVISIBLE);
        solutionLayout.setVisibility(View.VISIBLE);
        showSolutionButton.setVisibility(View.INVISIBLE);
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
            String value = (board != null ? String.valueOf(board.charAt(i)) : "");
            letterViews.get(i).setText(value);
        }
    }

    private void setListenersForPlayersView() {
        players.setOnItemLongClickListener((adapterView, itemView, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle("Add points:");
            final EditText input = new EditText(itemView.getContext());
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            builder.setView(input);

            builder.setPositiveButton("OK", (dialog, which) -> {
                try {
                    int points = Integer.parseInt(input.getText().toString());
                    playersAdapter.addPoints(i, points);
                } catch (NumberFormatException e) {}
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
            builder.show();
            return true;
        });

        players.setOnTouchListener((playersView, motionEvent) -> {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                players.getParent().requestDisallowInterceptTouchEvent(true);
            }
            return false;
        });

        players.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {
                players.smoothOpenMenu(position);
            }

            @Override
            public void onSwipeEnd(int position) {
                playersAdapter.removePlayer(position);
            }
        });
    }
}
