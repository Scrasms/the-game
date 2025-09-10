package io.scrasms.shmup0.game.MovementPatterns;

import com.badlogic.gdx.math.Vector2;

import io.scrasms.shmup0.game.Updates;

public interface MovementPattern extends Updates {
    
    public Vector2 getNextPosition();
    public void setSpeed(float speed);
    public void setAccel(float acceleration);
    public MovementPattern copy();
}
