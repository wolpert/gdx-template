package com.codeheadsystems.gdx;

import static org.mockito.Mockito.*;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class GdxTest implements BeforeEachCallback, BeforeAllCallback {

    @Override
    public void beforeAll(final ExtensionContext context) throws Exception {
        HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
        new HeadlessApplication(new ApplicationAdapter() {
            @Override
            public void create() {
                super.create();
            }

            @Override
            public void resize(int width, int height) {
                super.resize(width, height);
            }

            @Override
            public void render() {
                super.render();
            }

            @Override
            public void pause() {
                super.pause();
            }

            @Override
            public void resume() {
                super.resume();
            }

            @Override
            public void dispose() {
                super.dispose();
            }
        }, conf);
    }

    @Override
    public void beforeEach(final ExtensionContext context) throws Exception {
        Gdx.app = mock(Application.class);
        Gdx.files = mock(Files.class);
        Gdx.graphics = mock(Graphics.class);
        Gdx.gl = mock(GL20.class);
        Gdx.gl20 = mock(GL20.class);
    }
}
