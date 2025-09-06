package io.scrasms.shmup0.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Player implements Drawable, Updates {
    private Sprite sprite;

    private ProjectileCollection projCol;

    private Texture projTexture;

    private float worldHeight;
    private float worldWidth;

    private float shotTimer;

    public Player(Texture texture, Vector2 position, ProjectileCollection projCol, Texture projTexture, float worldHeight, float worldWidth) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth(), texture.getHeight());
        sprite.setCenter(position.x, position.y);

        this.projCol = projCol;

        this.projTexture = projTexture;

        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;

        shotTimer = 0f;
    }

    public void draw(Batch batch) {
        sprite.draw(batch);
    }

    public void update(float deltaTime) {
        float playerWidth = sprite.getWidth();
        float playerHeight = sprite.getHeight();

        move(80, deltaTime);
        shoot(deltaTime);

        sprite.setX(MathUtils.clamp(sprite.getX(), 0, worldWidth - playerWidth));
        sprite.setY(MathUtils.clamp(sprite.getY(), 0, worldHeight - playerHeight));

        shotTimer += deltaTime;
    }

    private void move(float speed, float deltaTime) {
        float x = 0;
        float y = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed *= 0.66;
        }

        x = 0;
        y = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) x += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) x -= 1;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) y += 1;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) y -= 1;

        float normFactor = 1;

        if (x != 0 && y != 0) normFactor = 0.7071f; //sqrt(2)/2

        x *= (speed * deltaTime * normFactor);
        y *= (speed * deltaTime * normFactor);

        sprite.translate(x, y);
    }
    
    private void shoot(float deltaTime) {
        float shotDelay = 0.07f;

        if (Gdx.input.isKeyPressed(Input.Keys.J) && shotDelay < shotTimer) {
            shotTimer = 0;
            Vector2 spawnPos = new Vector2(sprite.getX() + sprite.getWidth()/2, sprite.getY() + sprite.getHeight()*2/3);
            Vector2 vel = new Vector2(0f, 120f);
            Projectile newProjectile = new Projectile(projTexture, vel, spawnPos, worldHeight, worldWidth);
            projCol.newProjectile(newProjectile);
        }
    }
}
