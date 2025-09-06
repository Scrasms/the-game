package io.scrasms.shmup0.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Projectile implements Drawable, Updates {
    private Sprite sprite;
    private Path path;
    private float speed;
    private float worldHeight;
    private float worldWidth;
    private float timeElapsed;

    public Projectile(Texture texture, Path path, float speed, float worldWidth, float worldHeight) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth(), texture.getHeight());
        this.path = path;
        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;

        this.timeElapsed = 0;
    }

    public void draw(Batch batch){
        sprite.draw(batch);
    }

    public void update(float deltaTime) {
        timeElapsed += deltaTime;
        Vector2 pos = path.pathPos(timeElapsed, speed);
        sprite.setCenter(pos.x, pos.y);
    }

    public boolean isInBounds(float worldHeight, float worldWidth) {
        boolean topCondition = (sprite.getY() < worldHeight + 4* sprite.getHeight());
        boolean bottomCondition = (sprite.getY() > -sprite.getHeight());
        boolean rightCondition = (sprite.getX() < worldWidth + sprite.getWidth());
        boolean leftCondition = (sprite.getX() > -sprite.getWidth());
        return topCondition && bottomCondition && rightCondition && leftCondition;
    }

    public boolean checkRemoval() {
        return !isInBounds(worldWidth, worldHeight);
    }

    public Projectile cpy() {
        return new Projectile(sprite.getTexture(), path, speed, worldWidth, worldHeight);
    }

    public void movePath(Vector2 displacement) {
        path.movePath(displacement);
    }
}
