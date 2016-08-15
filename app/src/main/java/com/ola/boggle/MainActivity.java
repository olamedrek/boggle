package com.ola.boggle;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

public class MainActivity extends FragmentActivity {

    private MainFragment mainFragment;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
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

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position == 1) {
                return new WebDictionaryFragment();
            } else {
                mainFragment = new MainFragment();
                return mainFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
