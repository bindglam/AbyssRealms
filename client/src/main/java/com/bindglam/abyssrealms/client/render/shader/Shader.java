package com.bindglam.abyssrealms.client.render.shader;

import com.bindglam.abyssrealms.client.AbyssRealmsClient;
import com.bindglam.abyssrealms.util.Destroyable;
import com.bindglam.abyssrealms.util.ResourceUtil;
import com.bindglam.abyssrealms.util.ResourceKey;
import org.tinylog.Logger;

import static org.lwjgl.opengles.GLES20.*;

public class Shader implements Destroyable {
    private int id;

    public Shader(ResourceKey vertexKey, ResourceKey fragmentKey) {
        String vertexCode = ResourceUtil.readResource(vertexKey, AbyssRealmsClient.class.getClassLoader());
        String fragmentCode = ResourceUtil.readResource(fragmentKey, AbyssRealmsClient.class.getClassLoader());

        if(vertexCode == null || fragmentCode == null) {
            Logger.error("The shader is not found.");
            return;
        }

        int vertexShader = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vertexShader, vertexCode);
        glCompileShader(vertexShader);
        compileErrors(vertexShader, ShaderType.VERTEX);

        int fragmentShader = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fragmentShader, fragmentCode);
        glCompileShader(fragmentShader);
        compileErrors(fragmentShader, ShaderType.FRAGMENT);

        this.id = glCreateProgram();
        glAttachShader(this.id, vertexShader);
        glAttachShader(this.id, fragmentShader);
        glLinkProgram(this.id);
        compileErrors(this.id, ShaderType.PROGRAM);

        glDeleteShader(vertexShader);
        glDeleteShader(fragmentShader);
    }

    private void compileErrors(int shader, ShaderType type) {
        if(type != ShaderType.PROGRAM) {
            if(glGetShaderi(shader, GL_COMPILE_STATUS) == GL_FALSE) {
                Logger.error("SHADER_COMPILATION_ERROR for: " + type.toString() + "\n" + glGetShaderInfoLog(shader));
            }
        } else {
            if(glGetProgrami(shader, GL_LINK_STATUS) == GL_FALSE) {
                Logger.error("SHADER_LINKING_ERROR for: " + type + "\n" + glGetProgramInfoLog(shader));
            }
        }
    }

    public void activate() {
        glUseProgram(id);
    }

    public void deactivate() {
        glUseProgram(0);
    }

    @Override
    public void destroy() {
        glDeleteShader(id);
    }
}
