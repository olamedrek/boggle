package com.ola.boggle;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private MainFragment mainFragment;
    private WebDictionaryFragment webDictionaryFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new SwipeFragmentPagerAdapter(getSupportFragmentManager()));
        mainFragment = new MainFragment();
        webDictionaryFragment = new WebDictionaryFragment();
    }

    public void onClickNewBoardButton(View view) {
        mainFragment.onClickNewBoardButton(view);
    }

    public void onClickStartButton(View view) {
        mainFragment.onClickStartButton(view);
    }

    public void onClickAddPlayerButton(View view) {
        mainFragment.onClickAddPlayerButton(view);
    }

    public void onClickBackButton(View view) {
        mainFragment.onClickBackButton(view);
    }

    public void onClickShowSolutionButton(View view) {
        mainFragment.onClickShowSolutionButton(view);
    }

    private class SwipeFragmentPagerAdapter extends FragmentPagerAdapter {

        public SwipeFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return position == 0 ? mainFragment : webDictionaryFragment;
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
