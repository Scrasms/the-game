package io.scrasms.shmup0.game.paths;

import java.util.function.Function;

import com.badlogic.gdx.math.Vector2;

import io.scrasms.shmup0.game.Path;

public class FunctionPath implements Path {
    private Function<Float, Float> func;
    private float angle;

    public FunctionPath(Function<Float, Float> func, float angle) {
        this.func = func;
        this.angle = angle;
    }

    @Override
    public Vector2 pathPos(float distance) {
        Vector2 functionVector = new Vector2(distance, func.apply(distance));
        return functionVector.rotateDeg(angle);
    }    
}
