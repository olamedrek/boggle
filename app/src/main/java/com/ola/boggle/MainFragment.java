package com.ola.boggle;

import android.content.DialogInterface;
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
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.util.Arrays;
import java.util.List;
import java.util.Set;


public class MainFragment extends Fragment {

    private RelativeLayout solutionLayout;
    private RelativeLayout playersLayout;
    private TextView timerView;
    private Button showSolutionButton;

    private SolutionListAdapter solutionAdapter = new SolutionListAdapter();
    private Solution solution;

    private SwipeMenuListView players;
    private SwipeListAdapter playersAdapter = new SwipeListAdapter();

    private List<TextView> letterViews;
    private Board board = new Board();

    private long startTime = 0;
    private Handler timerHandler = new Handler();
    private int gameDuration = 180;



    public MainFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main, container, false);

        RelativeLayout boardLayout = (RelativeLayout) view.findViewById(R.id.board_layout);
        solutionLayout = (RelativeLayout) view.findViewById(R.id.solution_layout);
        playersLayout = (RelativeLayout) view.findViewById(R.id.players_layout);

        timerView = (TextView) view.findViewById(R.id.timer_view);
        Button newBoardButton = (Button) view.findViewById(R.id.newboard_button);
        Button startButton = (Button) view.findViewById(R.id.start_button);
        Button addPlayerButton = (Button) view.findViewById(R.id.addplayer_button);
        showSolutionButton = (Button) view.findViewById(R.id.solution_button);
        Button backButton = (Button) view.findViewById(R.id.back2_button);

        ListView solutionView = (ListView) view.findViewById(R.id.solution_view);
        solutionView.setAdapter(solutionAdapter);
        solution = new Solution(getResources().openRawResource(R.raw.dictionary));

        players = (SwipeMenuListView) view.findViewById(R.id.players);
        players.setAdapter(playersAdapter);
        players.setSwipeDirection(SwipeMenuListView.DIRECTION_RIGHT);
        createListOfLetterViews(view);


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
                playersAdapter.removePlayer(position);
            }
        });

        players.setMenuCreator(new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                SwipeMenuItem dummyItem = new SwipeMenuItem(view.getContext());
                dummyItem.setWidth(300);
                menu.addMenuItem(dummyItem);
            }
        });

        players.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Points:");
                final EditText input = new EditText(view.getContext());
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        playersAdapter.addPoints(i, Integer.parseInt(input.getText().toString()));
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

        players.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    players.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickStartButton(view);
            }
        });

        addPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickAddPlayerButton(view);
            }
        });

        newBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickNewBoardButton(view);
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickBackButton(view);
            }
        });

        showSolutionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playersLayout.setVisibility(View.INVISIBLE);
                solutionLayout.setVisibility(View.VISIBLE);
            }
        });

        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacksAndMessages(null);
    }

    public void onClickNewBoardButton(View view) {
        showSolutionButton.setEnabled(false);
        setBoardWithAnimation(board.getNewBoard());
        displayTime(gameDuration);
        timerHandler.removeCallbacksAndMessages(null);

        new AsyncTask<Void, Void, Set<String>>() {
            @Override
            protected Set<String> doInBackground(Void... voids) {
                return solution.findAllWords(board.getCurrentBoard());
            }

            @Override
            protected void onPostExecute(Set<String> strings) {
                solutionAdapter.changeWords(strings);
                showSolutionButton.setEnabled(true);
            }
        }.execute();
    }

    public void onClickStartButton(final View view) {
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
                    Toast.makeText(view.getContext(), "Time is up", Toast.LENGTH_SHORT).show();
                    setBoard(null);
                    timerHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            setBoard(board.getCurrentBoard());
                            displayTime(gameDuration);
                        }
                    }, 2000);
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

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String[] names = input.getText().toString().split(" ");
                for(String name : names) {
                    playersAdapter.addPlayer(name);
                }
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

    public void onClickBackButton(View view) {
        playersLayout.setVisibility(View.VISIBLE);
        solutionLayout.setVisibility(View.INVISIBLE);
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

    private void createListOfLetterViews(View view ) {
        letterViews = Arrays.asList((TextView) view.findViewById(R.id.letter_view1),
                (TextView) view.findViewById(R.id.letter_view2),
                (TextView) view.findViewById(R.id.letter_view3),
                (TextView) view.findViewById(R.id.letter_view4),
                (TextView) view.findViewById(R.id.letter_view5),
                (TextView) view.findViewById(R.id.letter_view6),
                (TextView) view.findViewById(R.id.letter_view7),
                (TextView) view.findViewById(R.id.letter_view8),
                (TextView) view.findViewById(R.id.letter_view9),
                (TextView) view.findViewById(R.id.letter_view10),
                (TextView) view.findViewById(R.id.letter_view11),
                (TextView) view.findViewById(R.id.letter_view12),
                (TextView) view.findViewById(R.id.letter_view13),
                (TextView) view.findViewById(R.id.letter_view14),
                (TextView) view.findViewById(R.id.letter_view15),
                (TextView) view.findViewById(R.id.letter_view16));
    }

}
