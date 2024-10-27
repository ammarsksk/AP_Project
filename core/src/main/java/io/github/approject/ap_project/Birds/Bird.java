package io.github.approject.ap_project.Birds;
import com.badlogic.gdx.graphics.Texture;

public class Bird {
    private final Texture texture;

    public Bird(String textureFilePath) {
        this.texture = new Texture(textureFilePath);
    }

    public Texture getTexture() {
        return texture;
    }
}
