package io.scrasms.shmup0.game;

import com.badlogic.gdx.math.Vector2;

public class Shooter implements Updates {
    private ProjectileCollection projCol;
    private Projectile projectile;
    private Vector2 relativePosition;
    private float shotDelay;
    private float shotTimer;

    // Relative position is the position on the shooting entity where the projectile is to be emitted from.
    public Shooter(ProjectileCollection projCol, float shotDelay, Vector2 relativePosition, Projectile templateProjectile) {
        this.projCol = projCol;
        this.shotDelay = shotDelay;
        this.projectile = templateProjectile;
        this.relativePosition = relativePosition;
    }

    public void shoot(Vector2 shooterPosition) {
        if (shotTimer > shotDelay) {
            shotTimer = 0;
            projCol.newProjectile(projectile.spawnCopy(shooterPosition.add(relativePosition)));
        }
    }

    @Override
    public void update(float deltaTime) {
        shotTimer += deltaTime;
    }
}
