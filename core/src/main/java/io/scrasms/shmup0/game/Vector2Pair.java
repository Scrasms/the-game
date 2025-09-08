package io.scrasms.shmup0.game;

import com.badlogic.gdx.math.Vector2;

public class Vector2Pair {
    private Vector2 first;
    private Vector2 second;

    public Vector2Pair(Vector2 first, Vector2 second) {
        this.first = first;
        this.second = second;
    }

    public Vector2 first() {
        return first;
    }

    public Vector2 second() {
        return second;
    }

    public void setFirst(Vector2 vect) {
        first = vect;
    }

    public void setSecond(Vector2 vect) {
        second = vect;
    }
}