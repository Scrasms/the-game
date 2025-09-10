package io.scrasms.shmup0.game;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Array;

public class EnemyCollection implements Drawable, Updates {
    private Array<Enemy> enemyCol;
    private Array<Enemy> removalQueue;

    public EnemyCollection() {
        enemyCol = new Array<>();
        removalQueue = new Array<>();
    }

    public void newEnemy(Enemy enemy) {
        enemyCol.add(enemy);
    }

    public void queueRemoval(Enemy enemy) {
        removalQueue.add(enemy);
    }

    private void checkRemoval() {
        for (Enemy enemy : enemyCol) {
            if (enemy.checkRemoval()) {
                queueRemoval(enemy);
            }
        }
    }

    @Override
    public void update(float deltaTime) {
        checkRemoval();
        enemyCol.removeAll(removalQueue, false);
        removalQueue.clear();

        for (Enemy enemy : enemyCol) {
            enemy.update(deltaTime);
        }
    }

    @Override
    public void draw(Batch batch) {
        for (Enemy enemy : enemyCol) {
            enemy.draw(batch);
        }
    }

}
