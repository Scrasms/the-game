package io.scrasms.shmup0.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Player {
    Sprite sprite;

    ProjectileCollection projCol;

    Texture projTexture;

    float worldHeight;
    float worldWidth;

    float shotTimer;

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

        input(deltaTime);

        sprite.setX(MathUtils.clamp(sprite.getX(), 0, worldWidth - playerWidth));
        sprite.setY(MathUtils.clamp(sprite.getY(), 0, worldHeight - playerHeight));

        shotTimer += deltaTime;
    }

    private void input(float deltaTime) {
        float speed = 75f;

        Vector2 move = dirrectionFromInput().scl(speed * deltaTime);

        sprite.translate(move.x, move.y);

        float shotDelay = 0.2f;

        if (Gdx.input.isKeyPressed(Input.Keys.J) && shotDelay < shotTimer) {
            shotTimer = 0;
            Vector2 spawnPos = new Vector2(sprite.getX() + sprite.getWidth()/2, sprite.getY() + sprite.getHeight()*2/3);
            Vector2 vel = new Vector2(120f, 120f);
            Projectile newProjectile = new Projectile(projTexture, vel, spawnPos, worldHeight, worldWidth);
            projCol.newProjectile(newProjectile);
        }
    }

    // Input slop
    private Vector2 dirrectionFromInput() {
        float sqrt2 = 1.4142f;
        Vector2 unitVec;

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                unitVec = new Vector2(sqrt2/2, sqrt2/2);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                unitVec = new Vector2(sqrt2/2, -sqrt2/2);
            }
            else {
                unitVec = new Vector2(1,0);
            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                unitVec = new Vector2(-sqrt2/2, sqrt2/2);
            }
            else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                unitVec = new Vector2(-sqrt2/2, -sqrt2/2);
            }
            else {
                unitVec = new Vector2(-1,0);
            }
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            unitVec = new Vector2(0,1);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                unitVec = new Vector2(0,-1);
        }
        else {
            unitVec = new Vector2(0,0);
        }

        return unitVec;
    }
}
