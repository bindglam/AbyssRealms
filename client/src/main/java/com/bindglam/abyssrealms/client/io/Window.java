package com.bindglam.abyssrealms.client.io;

import com.bindglam.abyssrealms.util.Destroyable;
import com.bindglam.abyssrealms.util.Initializable;
import com.bindglam.abyssrealms.util.StringUtil;
import org.lwjgl.egl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengles.*;
import org.lwjgl.system.*;
import org.tinylog.Logger;

import java.lang.reflect.Field;
import java.nio.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.egl.EGL10.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFWNativeEGL.*;
import static org.lwjgl.opengles.GLES20.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public final class Window implements Initializable, Destroyable {
    private int width, height;
    private String title;

    private long handle;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }

    @Override
    public void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if ( !glfwInit() )
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        glfwWindowHint(GLFW_CONTEXT_CREATION_API, GLFW_EGL_CONTEXT_API);
        glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_ES_API);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 0);

        handle = glfwCreateWindow(width, height, title, NULL, NULL);
        if (handle == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        try ( MemoryStack stack = stackPush() ) {
            IntBuffer pWidth = stack.mallocInt(1); // int*
            IntBuffer pHeight = stack.mallocInt(1); // int*

            glfwGetWindowSize(handle, pWidth, pHeight);

            GLFWVidMode vidmode = Objects.requireNonNull(glfwGetVideoMode(glfwGetPrimaryMonitor()));

            glfwSetWindowPos(
                    handle,
                    (vidmode.width() - pWidth.get(0)) / 2,
                    (vidmode.height() - pHeight.get(0)) / 2
            );
        }

        // EGL capabilities
        long dpy = glfwGetEGLDisplay();

        EGLCapabilities egl;
        try (MemoryStack stack = stackPush()) {
            IntBuffer major = stack.mallocInt(1);
            IntBuffer minor = stack.mallocInt(1);

            if (!eglInitialize(dpy, major, minor)) {
                throw new IllegalStateException(String.format("Failed to initialize EGL [0x%X]", eglGetError()));
            }

            egl = EGL.createDisplayCapabilities(dpy, major.get(0), minor.get(0));
        }

        Logger.debug("EGL Capabilities: " +
                StringUtil.combine(Arrays.stream(EGLCapabilities.class.getFields())
                        .filter((f) -> f.getType() == boolean.class)
                        .filter((f) -> {
                            try {
                                return f.get(egl).equals(Boolean.TRUE);
                            } catch (IllegalAccessException e) {
                                Logger.error(e);
                                return false;
                            }
                        })
                        .map(Field::getName).toList(), "[\n     ", ",\n     ", "\n]")
        );

        glfwMakeContextCurrent(handle);
        GLESCapabilities gles = GLES.createCapabilities();

        Logger.debug("OpenGL ES Capabilities: " +
                StringUtil.combine(Arrays.stream(GLESCapabilities.class.getFields())
                        .filter((f) -> f.getType() == boolean.class)
                        .filter((f) -> {
                            try {
                                return f.get(gles).equals(Boolean.TRUE);
                            } catch (IllegalAccessException e) {
                                Logger.error(e);
                                return false;
                            }
                        })
                        .map(Field::getName).toList(), "[\n     ", ",\n     ", "\n]")
        );

        Logger.debug("GL_VENDOR: " + glGetString(GL_VENDOR));
        Logger.debug("GL_VERSION: " + glGetString(GL_VERSION));
        Logger.debug("GL_RENDERER: " + glGetString(GL_RENDERER));

        glfwSwapInterval(1);

        glfwShowWindow(handle);
    }

    public boolean shouldClose() {
        return glfwWindowShouldClose(handle);
    }

    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT);
    }

    public void update() {
        glfwSwapBuffers(handle);
        glfwPollEvents();
    }

    @Override
    public void destroy() {
        GLES.setCapabilities(null);

        glfwFreeCallbacks(handle);
        glfwDestroyWindow(handle);

        glfwTerminate();
        Objects.requireNonNull(glfwSetErrorCallback(null)).free();
    }
}
