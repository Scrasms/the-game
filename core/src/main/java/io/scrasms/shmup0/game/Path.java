package io.scrasms.shmup0.game;

import com.badlogic.gdx.math.Vector2;

public interface Path {
    Vector2 pathPos(float time, float speed);
    void movePath(Vector2 displacement);
}