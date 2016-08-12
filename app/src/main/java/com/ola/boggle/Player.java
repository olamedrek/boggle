package com.ola.boggle;

/**
 * Created by ola on 12.08.16.
 */
public class Player {
    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;
    private Integer score;
}
