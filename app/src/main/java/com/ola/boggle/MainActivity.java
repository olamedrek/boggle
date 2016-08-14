package com.ola.boggle;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends FragmentActivity {

    private MainFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainFragment = (MainFragment) getSupportFragmentManager().findFragmentById(R.id.main_fragment);
    }

    public void onClickNewBoardButton(View view) {
        mainFragment.onClickNewBoardButton(view);
    }

    public void onClickStartButton(final View view) {
        mainFragment.onClickStartButton(view);
    }

    public void onClickAddPlayerButton(View view) {
        mainFragment.onClickAddPlayerButton(view);
    }

}
