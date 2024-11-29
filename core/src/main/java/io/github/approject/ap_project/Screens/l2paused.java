package io.github.approject.ap_project.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.approject.ap_project.Main;

import java.io.*;

public class l2paused implements Screen {
    public final Main game;
    private Texture bg;
    private Texture box;
    private Texture restart;
    private Texture resume;
    private Texture menu;
    private Stage stagen;

    public l2paused(Main game) {
        this.game = game;
        bg = new Texture("l2blurred.png");
        box = new Texture("bbox.png");
        restart = new Texture("restart.png");
        resume = new Texture("resume.png");
        menu = new Texture("menu.png");

        stagen = new Stage(game.game_port);

        Button restartButton = new Button(new TextureRegionDrawable(new TextureRegion(restart)));
        Button resumeButton = new Button(new TextureRegionDrawable(new TextureRegion(resume)));
        Button menuButton = new Button(new TextureRegionDrawable(new TextureRegion(menu)));

        restartButton.setPosition(Main.V_WIDTH / 2 - restart.getWidth() / 2, Main.V_HEIGHT / 2 - restart.getHeight() / 2);
        resumeButton.setPosition(Main.V_WIDTH / 2 - restart.getWidth() / 2 - 200, Main.V_HEIGHT / 2 - restart.getHeight() / 2);
        menuButton.setPosition(Main.V_WIDTH / 2 - restart.getWidth() / 2 + 200, Main.V_HEIGHT / 2 - restart.getHeight() / 2);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Restarting!");
                game.setScreen(new Level2(game));
            }
        });

        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Resuming!");

                String savePath = "game2_state.ser";
                GameState savedState = deserializeGameState(savePath);

                if (savedState != null) {
                    // Pass the saved state to Level1 and let Level1 load it after initialization
                    Level2 level2Screen = new Level2(game);
                    level2Screen.setSavedGameState(savedState); // Save the state to be loaded later
                    game.setScreen(level2Screen);
                } else {
                    System.out.println("No saved game state found. Starting a new game.");
                    game.setScreen(new Level2(game));
                }
            }
        });


        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Going back to level screen!");
                game.setScreen(new LevelScreen(game));
            }
        });

        stagen.addActor(restartButton);
        stagen.addActor(resumeButton);
        stagen.addActor(menuButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stagen);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game.game_cam.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 0);
        game.batch.draw(box, (float) Main.V_WIDTH / 2 - (float) box.getWidth() / 2, (float) Main.V_HEIGHT / 2 - (float) box.getHeight() / 2);
        game.batch.end();
        stagen.act(Gdx.graphics.getDeltaTime());
        stagen.draw();
    }

    @Override
    public void resize(int width, int height) {
        stagen.getViewport().update(width, height, true);
        game.game_port.update(width, height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stagen.dispose();
        bg.dispose();
        box.dispose();
        restart.dispose();
        resume.dispose();
        menu.dispose();
    }

    // Serialization helper methods
    private GameState deserializeGameState(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
