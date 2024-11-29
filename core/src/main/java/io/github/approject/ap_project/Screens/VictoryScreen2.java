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

import java.io.File;

public class VictoryScreen2 implements Screen {
    public final Main game;
    private Texture bg;
    private Texture box;
    private Texture menu;
    private Texture restart;
    private Texture next;
    private Texture lose;
    private Stage stagen;

    public VictoryScreen2(Main game) {
        this.game = game;
        bg = new Texture("l2blurred.png");
        box = new Texture("gradbg.png");
        lose = new Texture("win.png");
        restart = new Texture("restart.png");
        menu = new Texture("menu.png");
        next = new Texture("nextLevel.png");

        stagen = new Stage(game.game_port);

        Button restartButton = new Button(new TextureRegionDrawable(new TextureRegion(restart)));
        Button menuButton = new Button(new TextureRegionDrawable(new TextureRegion(menu)));
        Button nextButton = new Button(new TextureRegionDrawable(new TextureRegion(next)));

        restartButton.setPosition(Main.V_WIDTH/2 - restart.getWidth()/2, Main.V_HEIGHT/2 - restart.getHeight()/2);
        menuButton.setPosition(Main.V_WIDTH/2 - restart.getWidth()/2 + 100, Main.V_HEIGHT/2 - restart.getHeight()/2);
        nextButton.setPosition(Main.V_WIDTH/2 - restart.getWidth()/2 - 100, Main.V_HEIGHT/2 - restart.getHeight()/2);

        restartButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Restarting!");
                game.setScreen(new Level2(game));
            }
        });
        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Going back to level screen!");
                File file = new File("game2_state.ser");
                if(file.delete()){
                    System.out.println("Game Reset!");
                }
                game.setScreen(new LevelScreen(game));
            }
        });
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Next Level!");
                game.setScreen(new Level3(game));
            }
        });
        stagen.addActor(restartButton);
        stagen.addActor(menuButton);
        stagen.addActor(nextButton);

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
        game.batch.draw(box, Main.V_WIDTH/2 - box.getWidth()/2, 50);
        game.batch.draw(lose, Main.V_WIDTH/2 - lose.getWidth()/2, 400);
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

    }
}

