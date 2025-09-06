package io.scrasms.shmup0.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

public class Shooter implements Updates {
    private ProjectileCollection projCol;
    private float shotDelay;
    private float shotTimer;

    public void shoot() {
        if (shotTimer > shotDelay) {
            shotTimer = 0;
            Projectile newProjectile = new Projectile(projTexture, path, speed, worldHeight, worldWidth);
            projCol.newProjectile(newProjectile);
        }
    }

    @Override
    public void update(float deltaTime) {
        shotTimer += deltaTime;
    }
}
