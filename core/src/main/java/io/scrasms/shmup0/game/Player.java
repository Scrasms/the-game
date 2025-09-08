package io.scrasms.shmup0.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import io.scrasms.shmup0.game.paths.FunctionPath;
import io.scrasms.shmup0.game.paths.NodePath;

public class Player implements Drawable, Updates, LivePosition {
    private Sprite sprite;

    private ProjectileCollection projCol;

    private float worldHeight;
    private float worldWidth;

    private Shooter mainWeapon;
    private Shooter secondaryWeapon1;
    private Shooter secondaryWeapon2;

    private Vector2 livePosition;

    public Player(Texture texture, Vector2 position, ProjectileCollection projCol, float worldWidth, float worldHeight) {
        sprite = new Sprite(texture);
        sprite.setSize(texture.getWidth(), texture.getHeight());
        sprite.setCenter(position.x, position.y);

        livePosition = new Vector2();

        this.projCol = projCol;

        this.worldHeight = worldHeight;
        this.worldWidth = worldWidth;

        Texture mainFireTexture = new Texture("PHprojectile.png");
        //Path mainPath = new StraightPath(90);
        Array<Vector2Pair> pathPairs = new Array<>();
        pathPairs.add(new Vector2Pair(new Vector2(0,0), new Vector2(20, 23)));
        pathPairs.add(new Vector2Pair(new Vector2(20,7), new Vector2(0, 30)));
        pathPairs.add(new Vector2Pair(new Vector2(0,20), new Vector2(20, 40)));

        Path mainPath = new NodePath(pathPairs);
        Projectile mainFire = new Projectile(mainFireTexture, mainPath, 60, worldWidth, worldHeight);
        mainWeapon = new Shooter(projCol, 0.4f, new Vector2(4, 6), mainFire);

        Texture secondaryFireTexture = new Texture("PHsecondary.png");
        Path secondaryPath1 = new FunctionPath((x) -> (float)Math.sin(x/7)*10, 90);
        Path secondaryPath2 = new FunctionPath((x) -> -(float)Math.sin(x/7)*10, 90);
        Projectile secondaryFire1 = new Projectile(secondaryFireTexture, secondaryPath1, 80, worldWidth, worldHeight);
        Projectile secondaryFire2 = new Projectile(secondaryFireTexture, secondaryPath2, 80, worldWidth, worldHeight);
        secondaryWeapon1 = new Shooter(projCol, 0.18326f, new Vector2(2,2), secondaryFire1);
        secondaryWeapon2 = new Shooter(projCol, 0.18326f, new Vector2(2,2), secondaryFire2);
    }

    @Override
    public void draw(Batch batch) {
        sprite.draw(batch);
    }

    @Override
    public void update(float deltaTime) {
        float playerWidth = sprite.getWidth();
        float playerHeight = sprite.getHeight();

        move(80, deltaTime);
        shoot(deltaTime);

        sprite.setX(MathUtils.clamp(sprite.getX(), 0, worldWidth - playerWidth));
        sprite.setY(MathUtils.clamp(sprite.getY(), 0, worldHeight - playerHeight));

        mainWeapon.update(deltaTime);
        secondaryWeapon1.update(deltaTime);
        secondaryWeapon2.update(deltaTime);
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
        Vector2 playerPos = new Vector2(sprite.getX(), sprite.getY());

        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            mainWeapon.shoot(playerPos);
        }
        else if (Gdx.input.isKeyPressed(Input.Keys.K)) {
            secondaryWeapon1.shoot(playerPos);
            secondaryWeapon2.shoot(playerPos);
        }
    }

    @Override
    public Vector2 getLivePosition() {
        return livePosition;
    }

    @Override
    public void updateLivePosition() {
        livePosition.x = sprite.getX() + sprite.getWidth()/2;
        livePosition.y = sprite.getY() + sprite.getHeight()/2;
    }
}
