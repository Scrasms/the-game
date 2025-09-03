package com.badlogic.drop;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main implements ApplicationListener {
    // Game resources
    private Texture backgroundTexture;
    private Texture bucketTexture;
    private Texture dropTexture;
    private Sound dropSound;
    private Music music;

    // Rendering
    private SpriteBatch spriteBatch;
    private FitViewport viewport;

    // Sprites
    private Sprite bucketSprite;
    private Array<Sprite> dropSprites;

    // Tracks time since last drop was created
    private float dropTimer;

    // Input polling
    private Vector2 touchPos;

    // Collision Detection
    private Rectangle bucketRect;
    private Rectangle dropRect;

    // Loads assets into memory after libGDX has started
    @Override
    public void create() {
        // Textures are images kept in VRAM
        backgroundTexture = new Texture("background.png");
        bucketTexture = new Texture("bucket.png");
        dropTexture = new Texture("drop.png");

        // Sounds are audio loaded fully into RAM
        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.mp3"));

        // Music is audio too large to be fully loaded in RAM so it's streamed from the file as chunks
        music = Gdx.audio.newMusic(Gdx.files.internal("music.mp3"));

        // The process of drawing an individual texture is called a draw call
        // To prevent poor FPS, it is more efficient to send all your draw calls at once to the GPU
        // SpriteBatch is how libGDX batches (combines) these draw calls together
        spriteBatch = new SpriteBatch();

        // Viewport controls the size of the application window.
        // Fitviewport ensures the entire game is always visible no matter the size of the window (basically no overflow).
        // Each viewport has an associated Camera which controls what part of the game is visible and its zoom level.
        viewport = new FitViewport(8, 5);

        // Sprites track the position of a texture
        bucketSprite = new Sprite(bucketTexture);
        bucketSprite.setSize(1, 1);

        // 2D Vector tracking where the user last touched the screen
        touchPos = new Vector2();

        // Array storing drop sprites
        dropSprites = new Array<>();

        // Hitboxes
        bucketRect = new Rectangle();
        dropRect = new Rectangle();

        // Keep playing music
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    @Override
    public void resize(int width, int height) {
        // If the window is minimized on a desktop (LWJGL3) platform, width and height are 0, which causes problems.
        // In that case, we don't resize anything, and wait for the window to be a normal size before updating.
        if(width <= 0 || height <= 0) return;

        // Resize your application here. The parameters represent the new window size.
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        // Draw your application here.
        input();
        logic();
        draw();
    }

    private void input() {
        // Because our input polling logic is inside the render method, hardware with higher framerates see higher speeds.
        // Delta time is the time measured between each frame.
        // Multiplying speed by delta time forces the speed to be consistent regardless of framerate.

        float speed = 4f;
        float delta = Gdx.graphics.getDeltaTime(); // retrieve current delta time

        // Key polling
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            bucketSprite.translateX(speed * delta); // move bucket right
        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            bucketSprite.translateX(-speed * delta); // move bucket left
        }

        if (Gdx.input.isTouched()) {
            // getX() and getY() are in window coordinates which has (0,0) at top left. Viewport coords start at bottom left.
            touchPos.set(Gdx.input.getX(), Gdx.input.getY()); // Get and store where touch occured on screen
            viewport.unproject(touchPos); // Convert touch coordinates to world/viewport coordinates
            bucketSprite.setCenterX(touchPos.x); // Move bucket to where user touched screen
        }
    }

    // Random game logic method
    private void logic() {
        float worldWidth = viewport.getWorldWidth();
        float bucketHeight = bucketSprite.getWidth();
        float bucketWidth = bucketSprite.getWidth();
        float delta = Gdx.graphics.getDeltaTime();

        // Prevent bucket from moving more left than 0 and more right than worldWidth
        // Bucket width needs to be subtracted because the sprite's origin is at its bottom left
        bucketSprite.setX(MathUtils.clamp(bucketSprite.getX(), 0, worldWidth - bucketWidth));

        // Apply the bucket position and size to the bucketRectangle
        bucketRect.set(bucketSprite.getX(), bucketSprite.getY(), bucketWidth, bucketHeight);

        // Drop each dropSprite, we loop backwards to prevent concurrent modification exceptions
        for (int i = dropSprites.size - 1; i >= 0; i--) {
            Sprite dropSprite = dropSprites.get(i); // Get the sprite from the list
            float dropWidth = dropSprite.getWidth();
            float dropHeight = dropSprite.getHeight();

            dropSprite.translateY(-2f * delta);

            // Apply the drop position and size to the dropRectangle
            dropRect.set(dropSprite.getX(), dropSprite.getY(), dropWidth, dropHeight);

            // If the top of the drop goes below the bottom of the view, remove it
            if (dropSprite.getY() < -dropHeight) {
                dropSprites.removeIndex(i);

            // If drop's hitbox overlaps with bucket's hitbox, remove it
            } else if (bucketRect.overlaps(dropRect)) {
                dropSprites.removeIndex(i);
                dropSound.play();
            }
        }

        // Update timer
        dropTimer += delta;

        // Create a new droplet to be drawn in the current frame if it's been over 1 second
        if (dropTimer > 1f) {
            dropTimer = 0; // reset timer
            createDroplet();
        }
    }

    private void draw() {
        // Good practice to clear the screen every time to prevent weird graphical errors
        ScreenUtils.clear(com.badlogic.gdx.graphics.Color.BLACK);
        viewport.apply();
        // Viewport is applied to the SpriteBatch, ensuring images are drawn in the correct places
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        // Batch all draw calls within begin and end method calls
        spriteBatch.begin();

        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        // Draw order MATTERS
        // Draw backmost textures FIRST
        spriteBatch.draw(backgroundTexture, 0, 0, worldWidth, worldHeight); // Viewport coords are Q1 of Cartesian plane
        bucketSprite.draw(spriteBatch); // Sprites have their own draw method

        for (Sprite dropSprite : dropSprites) {
            dropSprite.draw(spriteBatch);
        }

        spriteBatch.end();
    }

    // Create new dropSprite and add to dropSprites
    private void createDroplet() {
        float dropWidth = 1;
        float dropHeight = 1;
        float worldWidth = viewport.getWorldWidth();
        float worldHeight = viewport.getWorldHeight();

        Sprite dropSprite = new Sprite(dropTexture);
        dropSprite.setSize(dropWidth, dropHeight);
        // Spawn at random x coordinate each time
        dropSprite.setX(MathUtils.random(0, worldWidth - dropWidth));
        dropSprite.setY(worldHeight);
        dropSprites.add(dropSprite);
    }

    @Override
    public void pause() {
        // Invoked when your application is paused.
    }

    @Override
    public void resume() {
        // Invoked when your application is resumed after pause.
    }

    @Override
    public void dispose() {
        // Destroy application's resources here.
    }
}