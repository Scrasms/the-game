package io.scrasms.shmup0.game.MovementPatterns;

import com.badlogic.gdx.math.Vector2;

import io.scrasms.shmup0.game.Path;

public class PathMovement implements MovementPattern {
    private float speed;
    private Path path;
    private float distance; 

    public PathMovement(Path path) {
        this.path = path;
        distance = 0;
        speed = 0;
    }

    @Override
    public void update(float deltaTime) {
        distance += deltaTime * speed;
    }

    @Override
    public Vector2 getNextPosition() {
        return path.pathPos(distance);
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void setAccel(float acceleration) {}
    
    public PathMovement copy() {
        return new PathMovement(path);
    }
}
