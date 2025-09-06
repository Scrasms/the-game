package io.scrasms.shmup0.game.paths;

import com.badlogic.gdx.math.Vector2;

import io.scrasms.shmup0.game.Path;

public class StraightPath implements Path {
    float angle;

    public StraightPath(float angle) {
        this.angle = angle;
    }

    @Override
    public Vector2 pathPos(float time, float speed) {
        Vector2 functionVector = new Vector2(time * speed, 0);
        return functionVector.setAngleDeg(angle);
    }    
}