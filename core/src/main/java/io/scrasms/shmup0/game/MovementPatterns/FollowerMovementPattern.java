package io.scrasms.shmup0.game.MovementPatterns;

import com.badlogic.gdx.math.Vector2;

public interface FollowerMovementPattern extends MovementPattern {
    
    public void setTargetLivePos(Vector2 livePosition);
    
}
