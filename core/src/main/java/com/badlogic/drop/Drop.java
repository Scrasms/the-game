package com.badlogic.drop;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;

// Screen is an interface that contains methods from ApplicationListener interface
// but also show() and hide(), which are called when the screen loses or gains focus.
// They represent menu screens, settings screens, game screens, etc.

// Game is a class that handles multiple Screens and implements ApplicationListener itself
public class Drop extends Game {
    // SpriteBatch renders objects on the screen while Bitmap renders text
    public SpriteBatch batch;
    public BitmapFont font;
    public FitViewport viewport;

    @Override
    public void create() {
        batch = new SpriteBatch();
        // use libGDX's default font
        font = new BitmapFont();
        viewport = new FitViewport(8, 5);

        //font has 15pt, but we need to scale it to our viewport by ratio of viewport height to screen height
        font.setUseIntegerPositions(false);
        font.getData().setScale(viewport.getWorldHeight() / Gdx.graphics.getHeight());

        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
