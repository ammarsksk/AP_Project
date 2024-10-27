package io.github.approject.ap_project.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import io.github.approject.ap_project.Main;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.approject.ap_project.Birds.Red;
import io.github.approject.ap_project.Birds.White;
import io.github.approject.ap_project.Birds.Blue;
import io.github.approject.ap_project.Materials.TNTBlock;
import io.github.approject.ap_project.Pigs.FreakPig;
import io.github.approject.ap_project.Pigs.KingPig;

public class HomeScreen implements Screen {
    private final Main game;
    private Texture home;
    private Texture logo;
    private Texture play;
    private Texture slingshot;
    private Red red;
    private White white;
    private Blue blue;
    private TNTBlock box;
    private FreakPig freakpig;
    private KingPig daddypig;
    private Stage stage;
    private Texture settings;
    private Texture quit;

    public HomeScreen(Main game) {
        this.game = game;
        home = new Texture("background.jpg");
        logo = new Texture("logo.png");
        play = new Texture("play.png");
        slingshot = new Texture("slingshot.png");
        red = new Red();
        white = new White();
        blue = new Blue();
        box = new TNTBlock();
        freakpig = new FreakPig();
        daddypig = new KingPig();
        settings = new Texture("settings.png");
        quit = new Texture("quit.png");

        stage = new Stage(game.game_port);

        Button playButton = new Button(new TextureRegionDrawable(new TextureRegion(play)));
        Button SetButton = new Button(new TextureRegionDrawable(new TextureRegion(settings)));
        Button quitButton = new Button(new TextureRegionDrawable(new TextureRegion(quit)));

        playButton.setPosition((float) Main.V_WIDTH / 2 - play.getWidth() / 2, (float) Main.V_HEIGHT / 2 - play.getHeight() / 2);
        SetButton.setPosition(10, Main.V_HEIGHT - settings.getHeight() - 20);
        quitButton.setPosition(Main.V_WIDTH - quit.getWidth() - 20, Main.V_HEIGHT - settings.getHeight() - 20);

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Play button clicked!");
                game.setScreen(new LevelScreen(game));
            }
        });
        SetButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Settings button clicked!");
                game.setScreen(new SettingsScreen(game));
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Quit button clicked!");
                Gdx.app.exit();
            }
        });

        stage.addActor(playButton);
        stage.addActor(SetButton);
        stage.addActor(quitButton);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float v) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.setProjectionMatrix(game.game_cam.combined);
        game.batch.begin();
        game.batch.draw(home, 0, 0);
        game.batch.draw(logo, (float) Main.V_WIDTH / 2 - logo.getWidth() / 2, (float) Main.V_HEIGHT / 2 - logo.getHeight() / 2 + 250);
        game.batch.draw(slingshot, 50, 100);
        game.batch.draw(red.getTexture(), (float) Main.V_WIDTH / 2 - logo.getWidth() / 2 - red.getTexture().getWidth() / 2 - 50, (float) Main.V_HEIGHT / 2 - logo.getHeight() / 2 + 250);
        game.batch.draw(white.getTexture(), (float) Main.V_WIDTH / 2 + logo.getWidth() / 2 - 10, (float) Main.V_HEIGHT / 2 - logo.getHeight() / 2 + 250);
        game.batch.draw(blue.getTexture(), 50, 100 + slingshot.getHeight() - 50);
        game.batch.draw(box.getTexture(), Main.V_WIDTH - 100 - box.getTexture().getWidth() / 2, 100);
        game.batch.draw(box.getTexture(), Main.V_WIDTH - 100 - 3 * (box.getTexture().getWidth() / 2), 100);
        game.batch.draw(box.getTexture(), Main.V_WIDTH - 100 - box.getTexture().getWidth(), 100 + box.getTexture().getHeight());
        game.batch.draw(daddypig.getTexture(), Main.V_WIDTH - 100 - box.getTexture().getWidth(), 100 + box.getTexture().getHeight() * 2);
        game.batch.draw(freakpig.getTexture(), Main.V_WIDTH - 100 - 5 * (box.getTexture().getWidth() / 2), 100);
        game.batch.end();
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
        home.dispose();
        logo.dispose();
        play.dispose();
        slingshot.dispose();
        red.getTexture().dispose();
        white.getTexture().dispose();
        blue.getTexture().dispose();
        box.getTexture().dispose();
        freakpig.getTexture().dispose();
        daddypig.getTexture().dispose();
    }
}
