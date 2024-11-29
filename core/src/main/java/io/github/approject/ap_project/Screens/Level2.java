package io.github.approject.ap_project.Screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import io.github.approject.ap_project.Birds.*;
import io.github.approject.ap_project.Main;
import io.github.approject.ap_project.Materials.Material;
import io.github.approject.ap_project.Pigs.FreakPig;
import io.github.approject.ap_project.Pigs.KingPig;
import io.github.approject.ap_project.Pigs.NPCPig;
import io.github.approject.ap_project.Materials.GoldBlock;
import io.github.approject.ap_project.Materials.IceBlock;
import io.github.approject.ap_project.Materials.WoodBlock;
import io.github.approject.ap_project.Pigs.Pig;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Level2 implements Screen, InputProcessor {

    private boolean dragging = false;
    private boolean currentBirdStopped = false;
    private boolean isdeadBird = false;
    private Vector2 initialPosition;
    private Vector2 currentDragPosition;
    private float maxDragDistance = 1.0f;
    private GameState savedGameState = null;

    public final Main game;
    private static final float STOP_VELOCITY_THRESHOLD = 0.1f;
    private float PPM = 100f; // Pixels per meter
    private int BirdIndex = 0;
    private Texture bg;
    private Texture pause;
    private Texture slingshot;
    private GoldBlock goldblock;
    private GoldBlock goldblock2;
    private WoodBlock woodblock3;
    private IceBlock iceblock4;
    private IceBlock iceblock1;
    private IceBlock iceblock2;
    private IceBlock iceblock3;
    private WoodBlock woodblock1;
    private WoodBlock woodblock2;
    private Yellow yellow;
    private Red red;
    private Blue blue;
    private NPCPig pig1;
    private KingPig pig2;
    private FreakPig pig3;
    private NPCPig pig4;
    private FreakPig pig5;
    private Body CurrentBirdBody;
    private Body bird1;
    private Body bird2;
    private Body bird3;
    private Body pig1body;
    private Body pig2body;
    private Body pig3body;
    private Body pig4body;
    private Body pig5body;
    private Body goldboxbody;
    private Body goldboxbody2;
    private Body icebody1;
    private Body icebody2;
    private Body icebody3;
    private Body icebody4;
    private Body woodbody1;
    private Body woodbody2;
    private Body woodbody3;
    private ShapeRenderer shapeRenderer;
    private Body groundBody;
    private ArrayList<Body> birds = new ArrayList<>();
    private ArrayList<Body> mats = new ArrayList<>();
    private ArrayList<Body> pigs = new ArrayList<>();
    private ArrayList<Bird> bird_objs = new ArrayList<>();
    private ArrayList<Material> mat_objs = new ArrayList<>();
    private ArrayList<Pig> pig_objs = new ArrayList<>();
    public ArrayList<Integer> bird_obj_numbers = new ArrayList<>();
    public ArrayList<Integer> pig_obj_numbers = new ArrayList<>();
    public ArrayList<Integer> mat_obj_numbers = new ArrayList<>();
    private final ArrayList<Body> bodiesToDestroy = new ArrayList<>();
    private Stage stagen;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    public Level2(Main game) {
        this.game = game;

        bg = new Texture("l2bg.png");
        pause = new Texture("pause.png");
        slingshot = new Texture("slingshotstretched.png");
        goldblock = new GoldBlock();
        goldblock2 = new GoldBlock();
        iceblock1 = new IceBlock();
        iceblock2 = new IceBlock();
        iceblock3 = new IceBlock();
        iceblock4 = new IceBlock();
        woodblock3 = new WoodBlock();
        woodblock1 = new WoodBlock();
        woodblock2 = new WoodBlock();
        yellow = new Yellow();
        red = new Red();
        blue = new Blue();

        pig1 = new NPCPig();
        pig2 = new KingPig();
        pig3 = new FreakPig();
        pig4 = new NPCPig();
        pig5 = new FreakPig();

        bird_objs.add(yellow);bird_objs.add(red);bird_objs.add(blue);
        mat_objs.add(goldblock);mat_objs.add(iceblock1);mat_objs.add(iceblock2);mat_objs.add(iceblock3);mat_objs.add(woodblock1);mat_objs.add(woodblock2);mat_objs.add(iceblock4);mat_objs.add(woodblock3);mat_objs.add(goldblock2);
        pig_objs.add(pig1);pig_objs.add(pig2);pig_objs.add(pig3);pig_objs.add(pig4);pig_objs.add(pig5);

        initialPosition = new Vector2(2.5f, (2.8f + yellow.getTexture().getHeight() / PPM));
        currentDragPosition = new Vector2(initialPosition);

        stagen = new Stage(game.game_port);
        Button backButton = new Button(new TextureRegionDrawable(new TextureRegion(pause)));
        backButton.setPosition(50, Main.V_HEIGHT - 150);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Pausing!");
                game.setScreen(new l2paused(game));
            }
        });
        stagen.addActor(backButton);
    }

    @Override
    public void show() {
        // Set up input processors for stage and custom input handling
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(this);
        multiplexer.addProcessor(stagen);
        Gdx.input.setInputProcessor(multiplexer);
        shapeRenderer = new ShapeRenderer();

        // Initialize the physics world
        world = new World(new Vector2(0, -9.81f), true);
        world.setContactListener(new GameContactListener());
        debugRenderer = new Box2DDebugRenderer();

        // Ground body definition
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(0, 0);

        // Ground shape and fixture
        ChainShape groundShape = new ChainShape();
        groundShape.createChain(new Vector2[]{
            new Vector2(0, 2.25f),   // Ground start (meters)
            new Vector2(12.8f, 2.25f) // Ground end (meters)
        });

        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;
        groundFixtureDef.friction = 1f;
        groundFixtureDef.restitution = 0.5f;

// Create the ground body in the world
        groundBody = world.createBody(groundBodyDef).createFixture(groundFixtureDef).getBody();
        groundShape.dispose(); // Dispose the first shape after use

// Wall shape and fixture
        ChainShape wallShape = new ChainShape();
        wallShape.createChain(new Vector2[]{
            new Vector2(12.8f, 7.2f),   // Wall start (meters)
            new Vector2(12.8f, 0f) // Wall end (meters)
        });

        FixtureDef wallFixtureDef = new FixtureDef();
        wallFixtureDef.shape = wallShape;
        wallFixtureDef.restitution = 0.5f;

        // Create the wall body in the world
        world.createBody(groundBodyDef).createFixture(wallFixtureDef);
        wallShape.dispose();

        // If there's a saved state, load it after initialization
        if (savedGameState != null) {
            loadGameState(savedGameState);
            savedGameState = null;
        }
        else{
            initializeObjects();
        }

        // Initialize the stage with the back button
        Button backButton = new Button(new TextureRegionDrawable(new TextureRegion(pause)));
        backButton.setPosition(50, Main.V_HEIGHT - 150);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Pausing!");
                GameState state = saveGameState();
                serializeGameState(state, "game2_state.ser");
                game.setScreen(new l2paused(game));
            }
        });
        stagen.addActor(backButton);
    }

    private void renderTrajectory(Vector2 startPosition, Vector2 launchVelocity, Vector2 gravity, int numPoints, float timeStep) {
        shapeRenderer.setProjectionMatrix(game.game_cam.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        float fadeFactor = 1.0f;
        float fadeStep = 1.0f / numPoints;
        float initialPointSize = 5f;
        float minPointSize = 2f;

        for (int i = 1; i <= numPoints; i++) {
            float time = i * timeStep;
            Vector2 nextPosition = new Vector2(
                startPosition.x + launchVelocity.x * time,
                startPosition.y + launchVelocity.y * time + 0.5f * gravity.y * time * time
            );
            float currentPointSize = initialPointSize - (i * (initialPointSize - minPointSize) / numPoints);
            shapeRenderer.circle(nextPosition.x * PPM, nextPosition.y * PPM, currentPointSize);
            fadeFactor -= fadeStep;
            if (fadeFactor < 0) fadeFactor = 0;
            if (nextPosition.y < 2.25f) break;
        }

        shapeRenderer.end();
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Step the physics simulation
        world.step(1 / 60f, 8, 3);
        game.game_cam.update();

        // Destroy queued bodies
        for (Body body : bodiesToDestroy) {
            world.destroyBody(body);
        }
        bodiesToDestroy.clear();

        if(isdeadBird){
            isdeadBird = false;
            CurrentBirdBody.setTransform(2.5f, 2.8f + yellow.getTexture().getHeight() / PPM, 0);
        }

        // Check if the current bird has stopped moving
        if (!currentBirdStopped && CurrentBirdBody.getType().equals(BodyDef.BodyType.DynamicBody)) {
            Vector2 velocity = CurrentBirdBody.getLinearVelocity();
            Vector2 position = CurrentBirdBody.getPosition();
            dragging = false;

            if (velocity.len() < STOP_VELOCITY_THRESHOLD || position.x > 12.7f || position.y < 0) {
                System.out.println("Bird has stopped or is out of bounds.");
                currentBirdStopped = true;

                if (birds.size() > 1) {
                    bird_obj_numbers.add(BirdIndex);
                    birds.set(0, null);
                    bird_objs.set(0, null);

                    birds.removeIf(Objects::isNull);
                    bird_objs.removeIf(Objects::isNull);

                    BirdIndex++;
                    world.destroyBody(CurrentBirdBody);

                    CurrentBirdBody = birds.get(0);
                    CurrentBirdBody.setTransform(2.5f, 2.8f + yellow.getTexture().getHeight() / PPM, 0);
                    currentBirdStopped = false;
                }
                else{
                    birds.clear();
                }
            }
        }

        if(pigs.isEmpty()){
            game.setScreen(new VictoryScreen2(game));
        }
        if(birds.isEmpty()){
            game.setScreen(new LoseScreen2(game));
        }

        // Begin rendering
        game.batch.setProjectionMatrix(game.game_cam.combined);
        game.batch.begin();

        // Draw background
        game.batch.draw(bg, 0, 0);

        // Draw slingshot
        game.batch.draw(slingshot, 1.75f * PPM, 2.1f * PPM);

        // Render birds
        for (int i = 0; i < birds.size(); i++) {
            Body birdBody = birds.get(i);
            Bird birdObj = bird_objs.get(i);
            if (birdBody != null && birdObj != null && CurrentBirdBody == birdBody) {
                game.batch.draw(
                    birdObj.getTextureRegion(),
                    birdBody.getPosition().x * PPM - birdObj.getTexture().getWidth() / 2,
                    birdBody.getPosition().y * PPM - birdObj.getTexture().getHeight() / 2,
                    birdObj.getTexture().getWidth() / 2,
                    birdObj.getTexture().getHeight() / 2,
                    birdObj.getTexture().getWidth(),
                    birdObj.getTexture().getHeight(),
                    1.0f,
                    1.0f,
                    birdBody.getAngle() * (180f / (float) Math.PI)
                );
            }
        }

        for (int i = 0; i < pigs.size(); i++) {
            Body pigBody = pigs.get(i);
            Pig pigObj = pig_objs.get(i);
            if (pigBody != null && pigObj != null) {
                game.batch.draw(
                    pigObj.getTextureRegion(),
                    pigBody.getPosition().x * PPM - pigObj.getTexture().getWidth() / 2,
                    pigBody.getPosition().y * PPM - pigObj.getTexture().getHeight() / 2,
                    pigObj.getTexture().getWidth() / 2,
                    pigObj.getTexture().getHeight() / 2,
                    pigObj.getTexture().getWidth(),
                    pigObj.getTexture().getHeight(),
                    1.0f,
                    1.0f,
                    pigBody.getAngle() * (180f / (float) Math.PI)
                );
            }
        }


        for (int i = 0; i < mats.size(); i++) {
            Body matBody = mats.get(i);
            Material matObj = mat_objs.get(i);
            if (matBody != null && matObj != null) {
                game.batch.draw(
                    matObj.getTextureRegion(),
                    matBody.getPosition().x * PPM - matObj.getTexture().getWidth() / 2,
                    matBody.getPosition().y * PPM - matObj.getTexture().getHeight() / 2,
                    matObj.getTexture().getWidth() / 2,
                    matObj.getTexture().getHeight() / 2,
                    matObj.getTexture().getWidth(),
                    matObj.getTexture().getHeight(),
                    1.0f,
                    1.0f,
                    matBody.getAngle() * (180f / (float) Math.PI)
                );
            }
        }

        // End rendering
        game.batch.end();

        // Render stage (UI elements)
        stagen.act(Gdx.graphics.getDeltaTime());
        stagen.draw();

        // Render trajectory if dragging
        if (dragging) {
            Vector2 launchForce = initialPosition.cpy().sub(currentDragPosition).scl(75f);
            Vector2 gravity = world.getGravity();
            Vector2 initialVelocity = launchForce.scl((float) (1 / 3.926991));
            renderTrajectory(CurrentBirdBody.getPosition(), initialVelocity, gravity, 15, 0.08f);
        }

        // Handle dragging and launching
        handleDraggingAndLaunching(CurrentBirdBody);

        // Render debug information
        debugRenderer.render(world, game.game_cam.combined);
    }



    private void handleDraggingAndLaunching(Body birdie) {

        Vector3 touchPoint = game.game_cam.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
        Vector2 touchPosition = new Vector2(touchPoint.x / PPM, touchPoint.y / PPM); // Convert to meters

        if (Gdx.input.isTouched()) {
            if (!dragging) {
                // Start dragging if the touch is near the bird
                if (touchPosition.dst(birdie.getPosition()) < 0.5f) {
                    dragging = true;
                    initialPosition.set(birdie.getPosition());
                }
            }

            if (dragging) {
                // Calculate drag delta
                Vector2 dragDelta = touchPosition.cpy().sub(initialPosition);

                if (dragDelta.len() > maxDragDistance) {
                    dragDelta.setLength(maxDragDistance);
                }

                currentDragPosition.set(initialPosition.cpy().add(dragDelta));

                if (currentDragPosition.y < 1.5f) {
                    currentDragPosition.y = 1.5f;
                }

                birdie.setTransform(currentDragPosition, 0);
            }
        } else if (dragging) {
            dragging = false;
            birdie.setType(BodyDef.BodyType.DynamicBody);
            Vector2 launchForce = initialPosition.cpy().sub(birdie.getPosition()).scl(75f); // Scale force

            birdie.setLinearVelocity(0, 0);
            birdie.applyLinearImpulse(launchForce, birdie.getWorldCenter(), true);
        }
    }

    private GameState saveGameState(){

        GameState state = new GameState();
        state.StateBirdIndex = BirdIndex;

        state.bird_obj_numbers.addAll(bird_obj_numbers);
        state.pig_obj_numbers.addAll(pig_obj_numbers);
        state.mat_obj_numbers.addAll(mat_obj_numbers);

        for (Body bird : birds) {
            if (bird != null) {
                float[] position = new float[]{bird.getPosition().x, bird.getPosition().y};
                float[] impulse = new float[]{bird.getMass() * bird.getLinearVelocity().x, bird.getMass() * bird.getLinearVelocity().y};
                float angle = bird.getAngle(); // Save the rotation
                String bodyType = bird.getType().toString();
                state.birds.add(new BodyState(position, impulse, angle, bodyType, false));
            }
        }

        for (Body pig : pigs) {
            if (pig != null) {
                float[] position = new float[]{pig.getPosition().x, pig.getPosition().y};
                float[] impulse = new float[]{pig.getMass() * pig.getLinearVelocity().x, pig.getMass() * pig.getLinearVelocity().y};
                float angle = pig.getAngle(); // Save the rotation
                String bodyType = pig.getType().toString();
                state.pigs.add(new BodyState(position, impulse, angle, bodyType, false));
            }
        }

        for (Body mat : mats) {
            if (mat != null) {
                float[] position = new float[]{mat.getPosition().x, mat.getPosition().y};
                float[] impulse = new float[]{mat.getMass() * mat.getLinearVelocity().x, mat.getMass() * mat.getLinearVelocity().y};
                float angle = mat.getAngle(); // Save the rotation
                String bodyType = mat.getType().toString();
                state.materials.add(new BodyState(position, impulse, angle, bodyType, false));
            }
        }
        return state;
    }

    void loadGameState(GameState state) {
        // Load birds
        birds.clear();

        BirdIndex = state.StateBirdIndex;
        bird_obj_numbers.addAll(state.bird_obj_numbers);

        for (int i = bird_obj_numbers.size() - 1; i >= 0; i--) {
            int index = bird_obj_numbers.get(i);
            bird_objs.remove(index);
        }

        int i = 0;
        for (BodyState birdState : state.birds) {
            if (birdState != null) {
                Bird birdObj = bird_objs.get(i);
                Body birdBody = birdObj.getBirdBody(world, birdState.position[0], birdState.position[1]);
                birdBody.setType(BodyDef.BodyType.valueOf(birdState.bodyType));
                birdBody.setTransform(birdState.position[0], birdState.position[1], birdState.angle); // Restore position and rotation
                birdBody.setLinearVelocity(0, 0); // Reset velocity
                birdBody.applyLinearImpulse(
                    new Vector2(birdState.impulse[0], birdState.impulse[1]), // Restore impulse
                    birdBody.getWorldCenter(),
                    true
                );
                birds.add(birdBody);
            }
            i++;
        }

        CurrentBirdBody = birds.get(0);

        // Load pigs
        pigs.clear();

        pig_obj_numbers.addAll(state.pig_obj_numbers);

        for(int a: pig_obj_numbers){
            pig_objs.remove(a);
        }

        int j = 0;
        for (BodyState pigState : state.pigs) {
            if (pigState != null) {
                Pig pigObj = pig_objs.get(j);
                Body pigBody = pigObj.getPigBody(world, pigState.position[0], pigState.position[1], pigObj.getTexture().getWidth());
                pigBody.setType(BodyDef.BodyType.valueOf(pigState.bodyType));
                pigBody.setTransform(pigState.position[0], pigState.position[1], pigState.angle); // Restore position and rotation
                pigBody.setLinearVelocity(0, 0); // Reset velocity
                pigBody.applyLinearImpulse(
                    new Vector2(pigState.impulse[0], pigState.impulse[1]), // Restore impulse
                    pigBody.getWorldCenter(),
                    true
                );
                pigs.add(pigBody);
            }
            j++;
        }

        // Load materials
        mats.clear();

        mat_obj_numbers.addAll(state.mat_obj_numbers);

        for(int a: mat_obj_numbers){
            mat_objs.remove(a);
        }

        int k = 0;
        for (BodyState matState : state.materials) {
            if (matState != null) {
                Material matObj = mat_objs.get(k);
                Body matBody = matObj.getBoxBody(world, matState.position[0], matState.position[1], matObj.getTexture().getWidth(), matObj.getTexture().getHeight());
                matBody.setType(BodyDef.BodyType.valueOf(matState.bodyType));
                matBody.setTransform(matState.position[0], matState.position[1], matState.angle); // Restore position and rotation
                matBody.setLinearVelocity(0, 0); // Reset velocity
                matBody.applyLinearImpulse(
                    new Vector2(matState.impulse[0], matState.impulse[1]), // Restore impulse
                    matBody.getWorldCenter(),
                    true
                );
                mats.add(matBody);
            }
            k++;
        }
    }

    private void serializeGameState(GameState state, String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(state);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resize(int width, int height) {
        stagen.getViewport().update(width, height, true);
        game.game_port.update(width, height);
        game.game_cam.update();
    }

    @Override
    public void pause() {
        // Handle pause logic if needed
    }

    @Override
    public void resume() {
        // Handle resume logic if needed
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        // Dispose of all resources to avoid memory leaks
        bg.dispose();
        pause.dispose();
        slingshot.dispose();
        yellow.getTexture().dispose();
        red.getTexture().dispose();

        // Destroy all bodies in the world
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
        for (Body body : bodies) {
            world.destroyBody(body);
        }

        // Dispose the Box2D world
        world.dispose();
        debugRenderer.dispose();
        stagen.dispose();
    }

    // InputProcessor methods
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
        else if (keycode == Input.Keys.SPACE) {
            if (!birds.isEmpty() && bird_objs.get(0) instanceof Yellow) {
                if (CurrentBirdBody.getType() == BodyDef.BodyType.DynamicBody) {
                    Vector2 currentForce = new Vector2(CurrentBirdBody.getMass() * CurrentBirdBody.getLinearVelocity().x, 0);
                    Vector2 straightLineForce = new Vector2(currentForce.len() * 10f, 0);
                    CurrentBirdBody.setLinearVelocity(0, 0);
                    CurrentBirdBody.applyLinearImpulse(straightLineForce, CurrentBirdBody.getWorldCenter(), true);
                }
            }
            else if (!birds.isEmpty() && bird_objs.get(0) instanceof Red) {
                Body currentBird = birds.get(0);
                if (currentBird.getType() == BodyDef.BodyType.DynamicBody) {
                    Vector2 currentForce = new Vector2(0, currentBird.getMass() * currentBird.getLinearVelocity().y);
                    Vector2 straightDownForce = new Vector2(0, -currentForce.len() * 10f);
                    currentBird.setLinearVelocity(0, 0);
                    currentBird.applyLinearImpulse(straightDownForce, currentBird.getWorldCenter(), true);
                    System.out.println("Red bird special ability activated! Force applied: " + straightDownForce);
                }
            }
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
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
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
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    public void setSavedGameState(GameState savedGameState) {
        this.savedGameState = savedGameState;
    }

    private void initializeObjects() {
        // Initialize birds
        bird1 = yellow.getBirdBody(world, 2.5f, 2.8f + yellow.getTexture().getHeight() / PPM);
        bird2 = red.getBirdBody(world, 0f, 0f);
        bird3 = blue.getBirdBody(world, 0f, 0f);

        birds.add(bird1);
        birds.add(bird2);
        birds.add(bird3);

        CurrentBirdBody = birds.get(0);

        pig1body = pig1.getPigBody(world, 8f, 2.5f + woodblock2.getTexture().getHeight()*2/PPM,pig1.getTexture().getWidth());
        pig2body = pig2.getPigBody(world, 9f, 2.5f + iceblock2.getTexture().getHeight()*3/PPM,pig2.getTexture().getWidth());
        pig3body = pig3.getPigBody(world, 10f, 2.5f + goldblock.getTexture().getHeight()/PPM + iceblock2.getTexture().getHeight()/PPM + woodblock1.getTexture().getHeight()/PPM ,pig3.getTexture().getWidth());
        pig4body = pig4.getPigBody(world, 11f, 2.5f + goldblock2.getTexture().getHeight()/PPM + iceblock4.getTexture().getHeight()/PPM, pig4.getTexture().getWidth());
        pig5body = pig5.getPigBody(world, 12f, 2.5f + woodblock3.getTexture().getHeight()/PPM, pig5.getTexture().getWidth());

        // Initialize blocks
        icebody1 = iceblock1.getBoxBody(world, 10f, 2.5f + goldblock.getTexture().getHeight()/PPM,iceblock1.getTexture().getWidth(),iceblock1.getTexture().getHeight());
        icebody2 = iceblock2.getBoxBody(world, 9f, 2.5f + iceblock2.getTexture().getHeight()/PPM ,iceblock2.getTexture().getWidth(),iceblock2.getTexture().getHeight());
        icebody3 = iceblock3.getBoxBody(world, 9f, 2.5f + iceblock2.getTexture().getHeight()*2/PPM ,iceblock3.getTexture().getWidth(),iceblock3.getTexture().getHeight());
        icebody4 = iceblock4.getBoxBody(world, 11f, 2.5f + goldblock2.getTexture().getHeight()/PPM, iceblock4.getTexture().getWidth(), iceblock4.getTexture().getHeight());
        woodbody1 = woodblock1.getBoxBody(world, 10f, 2.5f + goldblock.getTexture().getHeight()/PPM + iceblock2.getTexture().getHeight()/PPM ,woodblock1.getTexture().getWidth(),woodblock1.getTexture().getHeight());
        woodbody2 = woodblock2.getBoxBody(world, 8f, 2.5f + woodblock2.getTexture().getHeight()/PPM,woodblock2.getTexture().getWidth(),woodblock2.getTexture().getHeight());
        woodbody3 = woodblock3.getBoxBody(world, 12f, 2.5f, woodblock3.getTexture().getWidth(), woodblock3.getTexture().getHeight());
        goldboxbody = goldblock.getBoxBody(world, 10f, 2.5f,goldblock.getTexture().getWidth(),goldblock.getTexture().getHeight());
        goldboxbody2 = goldblock2.getBoxBody(world, 11f, 2.5f, goldblock2.getTexture().getWidth(), goldblock2.getTexture().getHeight());

        pigs.add(pig1body);
        pigs.add(pig2body);
        pigs.add(pig3body);
        pigs.add(pig4body);
        pigs.add(pig5body);

        mats.add(goldboxbody);
        mats.add(icebody1);
        mats.add(icebody2);
        mats.add(icebody3);
        mats.add(woodbody1);
        mats.add(woodbody2);
        mats.add(icebody4);
        mats.add(woodbody3);
        mats.add(goldboxbody2);
    }

    private void logAllBodies() {
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);

        for (Body body : bodies) {
            Vector2 position = body.getPosition();
            System.out.println("Body at position: " + position + ", type: " + body.getType());
        }
    }

    private class GameContactListener implements ContactListener {
        @Override
        public void beginContact(Contact contact) {
            Fixture fixtureA = contact.getFixtureA();
            Fixture fixtureB = contact.getFixtureB();

            Body bodyA = fixtureA.getBody();
            Body bodyB = fixtureB.getBody();

            // Handle pig collisions
            if (isPigBody(bodyA)) {
                if (isMaterialBody(bodyB) || isPigBody(bodyB) || isGroundBody(bodyB)) handlePigCollision(bodyA,1);
                else if (isBirdBody(bodyB)){
                    int bird_index = birds.indexOf(bodyB);
                    Bird bird = bird_objs.get(bird_index);
                    handlePigCollision(bodyA,bird.getDamage());
                };
            } else if (isPigBody(bodyB)) {
                if (isMaterialBody(bodyA) || isPigBody(bodyA) || isGroundBody(bodyA)) handlePigCollision(bodyB,1);
                else if (isBirdBody(bodyA)){
                    int bird_index = birds.indexOf(bodyA);
                    Bird bird = bird_objs.get(bird_index);
                    handlePigCollision(bodyB,bird.getDamage());
                };
            }
            if (isBirdBody(bodyA) && isGroundBody(bodyB) && bodyA.getType().equals(BodyDef.BodyType.DynamicBody)) handleBirdGroundCollision(bodyA);
            else if(isBirdBody(bodyB) && isGroundBody(bodyA) && bodyB.getType().equals(BodyDef.BodyType.DynamicBody)) handleBirdGroundCollision(bodyB);

            // Handle material collisions
            if (isMaterialBody(bodyA)) {
                if (isMaterialBody(bodyB) || isPigBody(bodyB) || isGroundBody(bodyB)) handleMaterialCollision(bodyA,1);
                else if (isBirdBody(bodyB)){
                    int bird_index = birds.indexOf(bodyB);
                    Bird bird = bird_objs.get(bird_index);
                    handleMaterialCollision(bodyA,bird.getDamage());
                };
            } else if (isMaterialBody(bodyB)) {
                if (isMaterialBody(bodyA) || isPigBody(bodyA) || isGroundBody(bodyA)) handleMaterialCollision(bodyB,1);
                else if (isBirdBody(bodyA)){
                    int bird_index = birds.indexOf(bodyA);
                    Bird bird = bird_objs.get(bird_index);
                    handleMaterialCollision(bodyB,bird.getDamage());
                };
            }
        }

        private final Set<Body> birdsToBeKilled = new HashSet<>();

        private void handleBirdGroundCollision(Body birdBody) {
            if (birdsToBeKilled.contains(birdBody)) {
                return;
            }

            birdsToBeKilled.add(birdBody);
            birdBody.setLinearDamping(2.0f);
            birdBody.setAngularDamping(2.0f);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    killBird(birdBody);
                }
            }, 2);
        }

        private void handlePigCollision(Body pigBody,int damage) {
            int index = pigs.indexOf(pigBody);
            if (index != -1) {
                Pig pig = pig_objs.get(index);
                System.out.println("Pig"+(index+1)+" took hit "+(pig.getCur_hits()+1));
                pig.hitPig(1);
                //float impactForce = 15f; // Simulated impact force
                pig.hitPig(damage);

                if (pig.isDead()) {
                    System.out.println("Pig destroyed! Queuing for destruction.");
                    bodiesToDestroy.add(pigBody);
                    pig_obj_numbers.add(index); // Track which pig is destroyed

                    // Nullify references for rendering
                    pigs.set(index, null);
                    pig_objs.set(index, null);
                    pigs.removeIf(Objects::isNull);
                    pig_objs.removeIf(Objects::isNull);
                }
            }
        }

        private void handleMaterialCollision(Body materialBody,int damage) {
            int index = mats.indexOf(materialBody);
            if (index != -1) {
                Material material = mat_objs.get(index);
                System.out.println("Material"+(index+1)+" took hit "+(material.getCur_hits()+1));
                material.hitMaterial(damage); // Simulated impact force

                if (material.isDestroyed()) {
                    System.out.println("Material destroyed! Queuing for destruction.");
                    bodiesToDestroy.add(materialBody);
                    mat_obj_numbers.add(index); // Track which material is destroyed

                    // Nullify references for rendering
                    mats.set(index, null);
                    mat_objs.set(index, null);
                    mats.removeIf(Objects::isNull);
                    mat_objs.removeIf(Objects::isNull);
                }
            }
        }

        private void killBird(Body birdBody){
            if (birdsToBeKilled.contains(birdBody)) {
                return;
            }

            birdsToBeKilled.add(birdBody);

            int a = birds.indexOf(birdBody);

            birds.remove(birdBody);
            bird_objs.remove(a);

            bird_obj_numbers.add(BirdIndex);

            if(!(birds.isEmpty())){
                CurrentBirdBody = birds.get(0);
            }
            bodiesToDestroy.add(birdBody);

            currentBirdStopped = false;
            isdeadBird = true;

            BirdIndex++;
        }

        @Override
        public void endContact(Contact contact) {}

        @Override
        public void preSolve(Contact contact, Manifold oldManifold) {}

        @Override
        public void postSolve(Contact contact, ContactImpulse impulse) {}

        private boolean isPigBody(Body body) {
            return pigs.contains(body);
        }

        private boolean isMaterialBody(Body body) {
            return mats.contains(body);
        }

        private boolean isBirdBody(Body body){
            return birds.contains(body);
        }

        private boolean isGroundBody(Body body){
            return body == groundBody;
        }

    }

    public static class Level1Tests {
    }
}
