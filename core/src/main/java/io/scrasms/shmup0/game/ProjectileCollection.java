package io.scrasms.shmup0.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class ProjectileCollection {
    Array<Projectile> collection;
    Array<Projectile> removalQueue;

    public ProjectileCollection(float worldHeight, float worldWidth) {
        collection = new Array<>();
        removalQueue = new Array<>();
    }

    public void newProjectile(Projectile projectile) {
        collection.add(projectile);
    }

    public void draw(Batch batch) {
        for (Projectile projectile : collection) {
            projectile.draw(batch);
        }
    }

    public void queueRemoval(Projectile projectile) {
        removalQueue.add(projectile);
    }

    private void checkRemoval() {
        for (Projectile projectile : collection) {
            if (projectile.checkRemoval()) {
                queueRemoval(projectile);
            }
        }
    }

    public void update(float deltaTime) {
        //checkRemoval();
        //collection.removeAll(removalQueue, false);
        //removalQueue.clear();

        for (Projectile projectile : collection) {
            projectile.update(deltaTime);
        }
    }
}
