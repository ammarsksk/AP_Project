package io.github.approject.ap_project.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
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

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

public class LevelScreen implements Screen, InputProcessor {

    public final Main game;
    private Texture bg;
    private Texture selectep;
    private Texture level1;
    private Texture level2;
    private Texture level3;
    private Stage stagen;
    private Texture bb;

    public LevelScreen(Main game) {
        this.game = game;
        bg = new Texture("menuscreenbg.jpg");
        selectep = new Texture("select episode.png");
        level1 = new Texture("level1.png");
        level2 = new Texture("level2.png");
        level3 = new Texture("level3.png");
        bb = new Texture("backbutton.png");

        stagen = new Stage(game.game_port);

        Button l1button = new Button(new TextureRegionDrawable(new TextureRegion(level1)));
        Button l2button = new Button(new TextureRegionDrawable(new TextureRegion(level2)));
        Button l3button = new Button(new TextureRegionDrawable(new TextureRegion(level3)));
        Button backButton = new Button(new TextureRegionDrawable(new TextureRegion(bb)));

        l1button.setPosition(176, 250);
        l2button.setPosition(544, 250);
        l3button.setPosition(912, 250);
        backButton.setPosition(50, Main.V_HEIGHT - 150);


        l1button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level1!");
                String savePath = "game1_state.ser";

                GameState savedState = deserializeGameState(savePath);
                if (savedState != null) {
                    Level1 level1Screen = new Level1(game);
                    level1Screen.setSavedGameState(savedState); // Save the state to be loaded later
                    game.setScreen(level1Screen);
                } else {
                    System.out.println("No saved game state found. Starting a new game.");
                    game.setScreen(new Level1(game));
                }
            }
        });
        l2button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level2!");
                String savePath = "game2_state.ser";

                GameState savedState = deserializeGameState(savePath);
                if (savedState != null) {
                    Level2 level2Screen = new Level2(game);
                    level2Screen.setSavedGameState(savedState); // Save the state to be loaded later
                    game.setScreen(level2Screen);
                } else {
                    System.out.println("No saved game state found. Starting a new game.");
                    game.setScreen(new Level2(game));
                }
            }
        });
        l3button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Level3!");
                String savePath = "game3_state.ser";

                GameState savedState = deserializeGameState(savePath);
                if (savedState != null) {
                    Level3 level3Screen = new Level3(game);
                    level3Screen.setSavedGameState(savedState); // Save the state to be loaded later
                    game.setScreen(level3Screen);
                } else {
                    System.out.println("No saved game state found. Starting a new game.");
                    game.setScreen(new Level3(game));
                }
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Going back to HomeScreen!");
                game.setScreen(new HomeScreen(game));
            }
        });

        stagen.addActor(backButton);
        stagen.addActor(l1button);
        stagen.addActor(l2button);
        stagen.addActor(l3button);
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stagen);
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        System.out.println("LevelScreen touched");
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(game.game_cam.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 0);
        game.batch.draw(selectep,(float) Main.V_WIDTH /2 - selectep.getWidth()/2, (float) Main.V_HEIGHT /2 - selectep.getHeight()/2 + 250);
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
    }

    private GameState deserializeGameState(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            return (GameState) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println();
            return null;
        }
    }
}
