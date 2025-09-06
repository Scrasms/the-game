package io.scrasms.shmup0.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.scrasms.shmup0.game.paths.StraightPath;

public class Player implements Drawable, Updates {
    private Sprite sprite;

    private ProjectileCollection projCol;

    private float worldHeight;
    private float worldWidth;

    private Shooter mainWeapon;
    private Shooter secondaryWeapon;

    public Player(Texture texture, Vector2 position, ProjectileCollection projCol, float worldHeight, float worldWidth) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth(), texture.getHeight());
        sprite.setCenter(position.x, position.y);

        this.projCol = projCol;

        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;

        Texture mainFireTexture = new Texture("PHprojectile.png");
        Path mainPath = new StraightPath(-90);
        Projectile mainFire = new Projectile(mainFireTexture, mainPath, 80, worldWidth, worldHeight);

        Texture secondaryFireTexture = new Texture("PHsecondary.png");
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

        mainWeapon.update(deltaTime);
        secondaryWeapon.update(deltaTime);
    }

    private void move(float speed, float deltaTime) {
        if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
            speed *= 0.67;
        }

        float x = 0;
        float y = 0;

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
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            
        }
    }
}
