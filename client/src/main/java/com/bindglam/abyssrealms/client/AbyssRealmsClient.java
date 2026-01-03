package com.bindglam.abyssrealms.client;

import com.bindglam.abyssrealms.client.io.Window;
import com.bindglam.abyssrealms.client.render.shader.Shader;
import com.bindglam.abyssrealms.client.render.vertex.EBO;
import com.bindglam.abyssrealms.client.render.vertex.VAO;
import com.bindglam.abyssrealms.client.render.vertex.VBO;
import com.bindglam.abyssrealms.util.ResourceKey;
import lombok.Getter;
import org.joml.Vector4f;

import static org.lwjgl.opengles.GLES20.*;

public final class AbyssRealmsClient implements Runnable {
    @Getter
    private static AbyssRealmsClient instance;

    @Getter
    private final Window window = new Window(1280, 720, "AbyssRealms");

    public AbyssRealmsClient() {
        if(AbyssRealmsClient.instance != null)
            throw new IllegalStateException();

        AbyssRealmsClient.instance = this;
    }

    @Override
    public void run() {
        window.init();
        window.setBackgroundColor(new Vector4f(1f, 0f, 0f, 1f));

        float[] vertices = new float[] {
                -0.5f, -0.5f * (float) Math.sqrt(3) / 3, 0.0f, // Lower left corner
                0.5f, -0.5f * (float) Math.sqrt(3) / 3, 0.0f, // Lower right corner
                0.0f, 0.5f * (float) Math.sqrt(3) * 2 / 3, 0.0f, // Upper corner
                -0.5f / 2, 0.5f * (float) Math.sqrt(3) / 6, 0.0f, // Inner left
                0.5f / 2, 0.5f * (float) Math.sqrt(3) / 6, 0.0f, // Inner right
                0.0f, -0.5f * (float) Math.sqrt(3) / 3, 0.0f // Inner down
        };

        int[] indices = new int[] {
                0, 3, 5, // Lower left triangle
                3, 2, 4, // Lower right triangle
                5, 4, 1 // Upper triangle
        };

        Shader shader = new Shader(new ResourceKey("shaders", "default.vert"), new ResourceKey("shaders", "default.frag"));

        VAO vao1 = new VAO();
        vao1.bind();

        VBO vbo1 = new VBO(vertices);
        EBO ebo1 = new EBO(indices);

        vao1.linkVBO(vbo1, 0, 3, GL_FLOAT, 3 * Float.BYTES, 0L);

        vao1.unbind();
        vbo1.unbind();
        ebo1.unbind();

        while(!window.shouldClose()) {
            window.clear();

            shader.activate();
            vao1.bind();
            glDrawElements(GL_TRIANGLES, 9, GL_UNSIGNED_INT, 0);
            //shader.deactivate();

            window.update();
        }

        vao1.destroy();
        vbo1.destroy();
        ebo1.destroy();
        shader.destroy();

        window.destroy();
    }
}
