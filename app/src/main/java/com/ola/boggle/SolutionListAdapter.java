package com.ola.boggle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Created by ola on 16.08.16.
 */
public class SolutionListAdapter extends BaseAdapter {

    private List<String> words = new ArrayList<>();

    @Override
    public int getCount() {
        return words.size();
    }

    @Override
    public Object getItem(int i) {
        return words.get(i);
    }

    @Override
    public long getItemId(int i) {
        return words.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = new TextView(viewGroup.getContext());
            ((TextView) view).setTextSize(20);
        }
        ((TextView) view).setText(words.get(i));

        return view;
    }

    public void changeWords(Set<String> newWords) {
        List<String> newList = new ArrayList<>();
        for(String s : newWords) newList.add(s);
        Collections.sort(newList, new StringComparator());
        words = newList;
        notifyDataSetChanged();
    }

    private class StringComparator implements java.util.Comparator<String> {
        @Override
        public int compare(String s, String t) {
            return t.length() - s.length();
        }
    }
}
