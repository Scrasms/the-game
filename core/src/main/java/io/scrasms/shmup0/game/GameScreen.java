package io.scrasms.shmup0.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameScreen implements Screen {
    private final Shmup game;

    private float worldHeight;
    private float worldWidth;

    private Texture playerTexture;
    private Texture playerProjectileTexture;

    private Player player;
    private ProjectileCollection projCol;

    public GameScreen(final Shmup game) {
        this.game = game;

        playerTexture = new Texture("PHplayer.png");
        playerProjectileTexture = new Texture("PHprojectile.png");

        worldHeight = game.viewport.getWorldHeight();
        worldWidth = game.viewport.getWorldWidth();

        projCol = new ProjectileCollection(worldWidth, worldHeight);

        Vector2 startPos = new Vector2(worldWidth/2, worldHeight/6);
        player = new Player(playerTexture, startPos, projCol, worldWidth, worldHeight);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        logic();
        draw();
    }

    private void logic() {
        float deltaTime = Gdx.graphics.getDeltaTime();

        player.update(deltaTime);
        projCol.update(deltaTime);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        projCol.draw(game.batch);
        player.draw(game.batch);

        game.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        game.viewport.update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        playerTexture.dispose();
    }
    
}
