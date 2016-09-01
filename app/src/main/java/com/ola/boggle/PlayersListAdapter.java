package com.ola.boggle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ola on 13.08.16.
 */
public class PlayersListAdapter extends BaseAdapter {

    private List<Player> players = new ArrayList<>();

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
            LayoutInflater inflater = (LayoutInflater) viewGroup.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.player, null);
        }
        Player player = players.get(i);
        ((TextView) view.findViewById(R.id.player_name)).setText(player.getName());
        ((TextView) view.findViewById(R.id.player_score)).setText(String.valueOf(player.getScore()));

        return view;
    }

    public void addPlayer(String name) {
        Player player = new Player(name);
        players.add(player);
        notifyDataSetChanged();
    }

    public void removePlayer(int position) {
        if(position >= 0 && position < players.size()) {
            players.remove(position);
            notifyDataSetChanged();
        }
    }

    public void addPoints(int position, int points) {
        int currentScore = players.get(position).getScore();
        players.get(position).setScore(currentScore + points);
        notifyDataSetChanged();
    }

    private static class Player {

        private String name;
        private Integer score;

        public Player(String name) {
            this.name = name;
            this.score = 0;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getScore() {
            return score;
        }

        public void setScore(Integer score) {
            this.score = score;
        }
    }
}
