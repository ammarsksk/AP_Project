package io.github.approject.ap_project.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import io.github.approject.ap_project.Main;
import io.github.approject.ap_project.Birds.Blue;
import io.github.approject.ap_project.Birds.White;
import io.github.approject.ap_project.Pigs.FreakPig;
import io.github.approject.ap_project.Pigs.KingPig;
import io.github.approject.ap_project.Pigs.NPCPig;
import io.github.approject.ap_project.Materials.GoldBlock;
import io.github.approject.ap_project.Materials.IceBlock;
import io.github.approject.ap_project.Materials.WoodBlock;

public class Level1 implements Screen, InputProcessor {
    public final Main game;
    private Texture bg;
    private Texture pause;
    private GoldBlock goldblock;
    private IceBlock iceblock;
    private WoodBlock woodblock;
    private Texture slingshot;
    private Blue blue;
    private White white;
    private NPCPig pig1;
    private KingPig pig2;
    private FreakPig pig3;
    private Stage stagen;

    public Level1(Main game) {
        this.game = game;
        bg = new Texture("l1bg.png");
        pause = new Texture("pause.png");
        goldblock = new GoldBlock();
        iceblock = new IceBlock();
        woodblock = new WoodBlock();
        slingshot = new Texture("slingshotstretched.png");
        blue = new Blue();
        white = new White();

        pig1 = new NPCPig();
        pig2 = new KingPig();
        pig3 = new FreakPig();

        stagen = new Stage(game.game_port);
        Button backButton = new Button(new TextureRegionDrawable(new TextureRegion(pause)));
        backButton.setPosition(50, Main.V_HEIGHT - 150);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Pausing!");
                game.setScreen(new l1paused(game));
            }
        });
        stagen.addActor(backButton);
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stagen);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.setProjectionMatrix(game.game_cam.combined);
        game.batch.begin();
        game.batch.draw(bg, 0, 0);
        game.batch.draw(white.getTexture(), slingshot.getWidth() / 2 + white.getTexture().getWidth() / 2 + 50, slingshot.getHeight() + 225 - 110);
        game.batch.draw(slingshot, slingshot.getWidth(), 225);
        game.batch.draw(blue.getTexture(), blue.getTexture().getWidth(), 225);
        game.batch.draw(goldblock.getTexture(), Main.V_WIDTH - goldblock.getTexture().getWidth() - 50, 225);
        game.batch.draw(pig1.getTexture(), Main.V_WIDTH - goldblock.getTexture().getWidth() - pig1.getTexture().getWidth() / 2, 225 + goldblock.getTexture().getHeight());
        game.batch.draw(iceblock.getTexture(), Main.V_WIDTH - goldblock.getTexture().getWidth() - iceblock.getTexture().getWidth() - 50, 225);
        game.batch.draw(iceblock.getTexture(), Main.V_WIDTH - goldblock.getTexture().getWidth() - iceblock.getTexture().getWidth() - 50, 225 + iceblock.getTexture().getHeight());
        game.batch.draw(iceblock.getTexture(), Main.V_WIDTH - goldblock.getTexture().getWidth() - iceblock.getTexture().getWidth() - 50, 225 + iceblock.getTexture().getHeight() * 2);
        game.batch.draw(pig2.getTexture(), Main.V_WIDTH - goldblock.getTexture().getWidth() - iceblock.getTexture().getWidth() - 50, 225 + iceblock.getTexture().getHeight() * 3);
        game.batch.draw(woodblock.getTexture(), Main.V_WIDTH - goldblock.getTexture().getWidth() - iceblock.getTexture().getWidth() - woodblock.getTexture().getWidth() - 50, 225);
        game.batch.draw(woodblock.getTexture(), Main.V_WIDTH - goldblock.getTexture().getWidth() - iceblock.getTexture().getWidth() - woodblock.getTexture().getWidth() - 50, 225 + woodblock.getTexture().getHeight());
        game.batch.draw(pig3.getTexture(), Main.V_WIDTH - goldblock.getTexture().getWidth() - iceblock.getTexture().getWidth() - woodblock.getTexture().getWidth() - 60, 225 + woodblock.getTexture().getHeight() * 2);
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
        // Dispose resources
        bg.dispose();
        pause.dispose();
        slingshot.dispose();
        goldblock.getTexture().dispose();
        iceblock.getTexture().dispose();
        woodblock.getTexture().dispose();
        pig1.getTexture().dispose();
        pig2.getTexture().dispose();
        pig3.getTexture().dispose();
        stagen.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.V) {
            System.out.println("Victory!");
            game.setScreen(new VictoryScreen(game));
            return true;
        } else if (keycode == Input.Keys.L) {
            System.out.println("You lost!");
            game.setScreen(new LoseScreen(game));
            return true;
        }
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
}
