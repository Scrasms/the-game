package io.scrasms.shmup0.game;

import com.badlogic.gdx.math.Vector2;

public interface LivePosition extends Updates {
    public Vector2 getLivePosition();
    public void updateLivePosition();
}
