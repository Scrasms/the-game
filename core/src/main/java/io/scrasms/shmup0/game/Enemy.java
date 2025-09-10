package io.scrasms.shmup0.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

import io.scrasms.shmup0.game.MovementPatterns.MovementPattern;

public class Enemy implements Drawable, Updates, LivePosition {
    private float health;
    private Sprite sprite;
    private Vector2 origin;
    private ProjectileCollection projCol;
    private boolean dead;

    private MovementPattern movePattern;

    private Vector2 livePosition;

    public Enemy(Texture texture, ProjectileCollection projCol, MovementPattern movePattern, Vector2 startPos, float speed, float health) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth(), texture.getHeight());
        sprite.setCenterX(startPos.x);
        sprite.setCenterY(startPos.y);
        origin = new Vector2(startPos);
        livePosition = new Vector2(startPos);
        this.movePattern = movePattern;
        this.movePattern.setSpeed(speed);
        this.projCol = projCol;
        this.health = health;
        dead = false;
    }

    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float deltaTime) {
        checkCollisions();
        movePattern.update(deltaTime);
        Vector2 nextPos = movePattern.getNextPosition().add(origin);
        sprite.setCenterX(nextPos.x);
        sprite.setCenterY(nextPos.y);
        updateLivePosition();
    }

    @Override
    public Vector2 getLivePosition() {
        return livePosition;
    }

    @Override
    public void updateLivePosition() {
        livePosition.x = sprite.getX();
        livePosition.y = sprite.getY();
    }

    public void checkCollisions() {
        health -= projCol.checkCollisions(sprite.getBoundingRectangle());
    }

    public boolean isInBounds(float worldWidth, float worldHeight) {
        boolean topCondition = (sprite.getY() < worldHeight + 2 * sprite.getHeight());
        boolean bottomCondition = (sprite.getY() > -2 * sprite.getHeight());
        boolean rightCondition = (sprite.getX() < worldWidth + 2 * sprite.getWidth());
        boolean leftCondition = (sprite.getX() > -2 * sprite.getWidth());
        return topCondition && bottomCondition && rightCondition && leftCondition;
    }

    public boolean checkRemoval() {
        return health <= 0;
    }
}
