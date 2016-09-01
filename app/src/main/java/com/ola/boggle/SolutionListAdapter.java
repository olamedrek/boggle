package com.ola.boggle;

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
    private WordsComparator wordsComparator = new WordsComparator();

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
            ((TextView) view).setTextColor(view.getResources().getColor(R.color.colorTextViews));
        }
        ((TextView) view).setText(words.get(i));

        return view;
    }

    public void replaceWords(Set<String> newWords) {
        List<String> newList = new ArrayList<>(newWords);
        Collections.sort(newList, wordsComparator);
        words = newList;
        notifyDataSetChanged();
    }

    private class WordsComparator implements java.util.Comparator<String> {

        private StringComparator stringComparator = new StringComparator();

        @Override
        public int compare(String s, String t) {
            return t.length() - s.length() != 0 ?
                    t.length() - s.length() : stringComparator.compare(s, t);
        }
    }
}
