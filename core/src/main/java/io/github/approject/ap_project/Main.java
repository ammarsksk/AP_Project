package io.github.approject.ap_project;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.approject.ap_project.Screens.HomeScreen;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {
    public static final int V_WIDTH = 1280;
    public static final int V_HEIGHT =720;
    public SpriteBatch batch;
    public OrthographicCamera game_cam;
    public Viewport game_port;

    @Override
    public void create() {
        batch = new SpriteBatch();
        game_cam = new OrthographicCamera();
        game_cam.setToOrtho(false, V_WIDTH, V_HEIGHT);
        game_port = new FitViewport(V_WIDTH, V_HEIGHT, game_cam);
        game_port.apply();
        setScreen(new HomeScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
