package io.scrasms.shmup0.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Projectile implements Drawable, Updates {
    private Sprite sprite;
    private Path path;
    private Vector2 startPos;
    private float speed;
    private float worldHeight;
    private float worldWidth;
    private float timeElapsed;
    private float damage;
    private boolean collided;

    public Projectile(Texture texture, Path path, Vector2 startPos, float speed, float worldWidth, float worldHeight, float damage) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth(), texture.getHeight());
        this.path = path;
        this.speed = speed;
        this.startPos = startPos;
        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;
        this.damage = damage;

        timeElapsed = 0;
        collided = false;
    }

    public Projectile(Texture texture, Path path, float speed, float worldWidth, float worldHeight, float damage) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth(), texture.getHeight());
        this.path = path;
        this.speed = speed;
        this.startPos = null;
        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;
        this.damage = damage;

        this.timeElapsed = 0;
    }

    public void draw(Batch batch){
        sprite.draw(batch);
    }

    public void update(float deltaTime) {
        timeElapsed += deltaTime;
        Vector2 pos = path.pathPos(timeElapsed * speed);
        sprite.setCenter(pos.x + startPos.x, pos.y + startPos.y);
    }

    public boolean isInBounds(float worldWidth, float worldHeight) {
        boolean topCondition = (sprite.getY() < worldHeight + sprite.getHeight());
        boolean bottomCondition = (sprite.getY() > -sprite.getHeight());
        boolean rightCondition = (sprite.getX() < worldWidth);
        boolean leftCondition = (sprite.getX() > -sprite.getWidth());
        return topCondition && bottomCondition && rightCondition && leftCondition;
    }

    public boolean checkCollision(Rectangle hitbox) {
        if (hitbox.overlaps(sprite.getBoundingRectangle()) && collided == false) {
            collided = true;
            return true;
        }
        return false;
    }

    public float getDamage() {
        return damage;
    }

    public boolean checkRemoval() {
        return !isInBounds(worldWidth, worldHeight) || collided;
    }

    public Projectile spawnCopy(Vector2 startPos) {
        return new Projectile(sprite.getTexture(), path, startPos, speed, worldWidth, worldHeight, damage);
    }
}
