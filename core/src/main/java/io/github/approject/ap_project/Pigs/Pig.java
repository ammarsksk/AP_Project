package io.github.approject.ap_project.Pigs;
import com.badlogic.gdx.graphics.Texture;

public class Pig {
    private final Texture texture;

    public Pig(String textureFilePath) {
        this.texture = new Texture(textureFilePath);
    }

    public Texture getTexture() {
        return texture;
    }
}
