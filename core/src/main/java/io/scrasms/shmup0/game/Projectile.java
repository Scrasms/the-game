package io.scrasms.shmup0.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;

public class Projectile {
    Sprite sprite;
    Vector2 velocity;
    float worldHeight;
    float worldWidth;

    public Projectile(Texture texture, Vector2 velocity, Vector2 position, float worldHeight, float worldWidth) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth(), texture.getHeight());
        sprite.setCenter(position.x, position.y);
        this.velocity = velocity;
        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;
    }

    public void draw(Batch batch){
        sprite.draw(batch);
    }

    public void update(float deltaTime) {
        sprite.translate(deltaTime*velocity.x, deltaTime*velocity.y);
    }

    public boolean isInBounds(float worldHeight, float worldWidth) {
        boolean topCondition = (sprite.getY() < worldHeight + sprite.getHeight());
        boolean bottomCondition = (sprite.getY() > -sprite.getHeight());
        boolean rightCondition = (sprite.getX() < worldWidth + sprite.getWidth());
        boolean leftCondition = (sprite.getX() > -sprite.getWidth());
        return topCondition && bottomCondition && rightCondition && leftCondition;
    }

    public boolean checkRemoval() {
        return !isInBounds(worldWidth, worldHeight);
    }
}
