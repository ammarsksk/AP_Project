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

public class SettingsScreen implements Screen {
    public final Main game;
    private Texture bg;
    private Texture bb;
    private Stage stagen;

    public SettingsScreen(Main game) {
        this.game = game;
        bg = new Texture("setscreen.jpg");
        bb = new Texture("backbutton.png");

        stagen = new Stage(game.game_port);
        Button backButton = new Button(new TextureRegionDrawable(new TextureRegion(bb)));
        backButton.setPosition(50, Main.V_HEIGHT - 150);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Going back to HomeScreen!");
                game.setScreen(new HomeScreen(game));
            }
        });
        stagen.addActor(backButton);

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
        game.batch.draw(bg, 0, 64.5F);
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

