package io.scrasms.shmup0.game.MovementPatterns;

import com.badlogic.gdx.math.Vector2;

public class DirectFollower implements FollowerMovementPattern {
    Vector2 liveTarget;
    Vector2 livePos;
    float speed;
    float accel;
    float deltaTime;

    public DirectFollower(Vector2 liveTarget, Vector2 livePos) {
        this.liveTarget = liveTarget;
        this.livePos = livePos;
        speed = 0;
        accel = 0;
    }


    @Override
    public Vector2 getNextPosition() {
        return new Vector2(liveTarget).sub(livePos).nor().scl(speed * deltaTime);
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void setAccel(float acceleration) {
        accel = acceleration;
    }

    @Override
    public void update(float deltaTime) {
        this.deltaTime = deltaTime;
        speed += deltaTime * accel;
    }

    @Override
    public void setTargetLivePos(Vector2 livePosition) {
        liveTarget = livePosition;
    }
    
}
