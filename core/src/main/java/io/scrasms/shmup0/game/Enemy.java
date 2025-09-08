package io.scrasms.shmup0.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Enemy implements Drawable, Updates, LivePosition {
    float health;
    Sprite sprite;

    @Override
    public void draw(Batch batch) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void update(float deltaTime) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Vector2 getLivePosition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void updateLivePosition() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
