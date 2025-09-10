package io.scrasms.shmup0.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;

import io.scrasms.shmup0.game.MovementPatterns.MovementPattern;
import io.scrasms.shmup0.game.MovementPatterns.PathMovement;
import io.scrasms.shmup0.game.paths.NodePath;

public class GameScreen implements Screen {
    private final Shmup game;

    private float worldHeight;
    private float worldWidth;

    private Texture playerTexture;
    private Texture playerProjectileTexture;

    private Texture enemyTexture;
    private Vector2 enemyPos;
    private MovementPattern enemyPattern;

    private Player player;
    private ProjectileCollection projCol;

    private EnemyCollection enemyCol;

    private float spawnTimer;

    public GameScreen(final Shmup game) {
        this.game = game;

        playerTexture = new Texture("PHplayer.png");
        playerProjectileTexture = new Texture("PHprojectile.png");

        worldHeight = game.viewport.getWorldHeight();
        worldWidth = game.viewport.getWorldWidth();

        projCol = new ProjectileCollection(worldWidth, worldHeight);
        enemyCol = new EnemyCollection();

        Vector2 startPos = new Vector2(worldWidth/2, worldHeight/6);
        player = new Player(playerTexture, startPos, projCol, worldWidth, worldHeight);

        Array<Vector2Pair> pathPairs = new Array<>();
        pathPairs.add(new Vector2Pair(new Vector2(0,0), new Vector2(60, 0)));
        pathPairs.add(new Vector2Pair(new Vector2(60,-10), new Vector2(10, -10)));
        pathPairs.add(new Vector2Pair(new Vector2(10,-20), new Vector2(30, -20)));
        pathPairs.add(new Vector2Pair(new Vector2(40,0), new Vector2(45, 45)));
        Path enemyPath = new NodePath(pathPairs);
        enemyPattern = new PathMovement(enemyPath);

        enemyTexture = new Texture("PHpopcorn.png");
        enemyPos = new Vector2 (0, 80);
        enemyCol.newEnemy(new Enemy(enemyTexture, projCol, enemyPattern, enemyPos, 40f, 10f));

        spawnTimer = 0;
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
        enemyCol.update(deltaTime);

        spawnTimer += deltaTime;
        if (spawnTimer > 0.7) {
            spawnTimer = 0;
            enemyCol.newEnemy(new Enemy(enemyTexture, projCol, enemyPattern.copy(), enemyPos, 40f, 10f));
        }
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        game.viewport.apply();
        game.batch.setProjectionMatrix(game.viewport.getCamera().combined);
        game.batch.begin();

        projCol.draw(game.batch);
        enemyCol.draw(game.batch);
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
