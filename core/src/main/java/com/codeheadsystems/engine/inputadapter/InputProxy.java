package com.codeheadsystems.engine.inputadapter;

import com.badlogic.gdx.InputProcessor;
import java.util.Optional;

public class InputProxy implements InputProcessor {

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<InputProcessor> inputProcessor;

    public InputProxy() {
        inputProcessor = Optional.empty();
    }

    public void setInputProcessor(InputProcessor InputProcessor) {
        this.inputProcessor = Optional.of(InputProcessor);
    }

    public void clearInputProcessor() {
        inputProcessor = Optional.empty();
    }

    public boolean hasInputProcessor() {
        return inputProcessor.isPresent();
    }

    @Override
    public boolean keyDown(final int keycode) {
        return inputProcessor.map(adapter -> adapter.keyDown(keycode)).orElse(false);
    }

    @Override
    public boolean keyUp(final int keycode) {
        return inputProcessor.map(adapter -> adapter.keyUp(keycode)).orElse(false);
    }

    @Override
    public boolean keyTyped(final char character) {
        return inputProcessor.map(adapter -> adapter.keyTyped(character)).orElse(false);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return inputProcessor.map(adapter -> adapter.touchDown(screenX, screenY, pointer, button)).orElse(false);
    }

    @Override
    public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
        return inputProcessor.map(adapter -> adapter.touchUp(screenX, screenY, pointer, button)).orElse(false);
    }

    @Override
    public boolean touchCancelled(final int screenX, final int screenY, final int pointer, final int button) {
        return inputProcessor.map(adapter -> adapter.touchCancelled(screenX, screenY, pointer, button)).orElse(false);
    }

    @Override
    public boolean touchDragged(final int screenX, final int screenY, final int pointer) {
        return inputProcessor.map(adapter -> adapter.touchDragged(screenX, screenY, pointer)).orElse(false);
    }

    @Override
    public boolean mouseMoved(final int screenX, final int screenY) {
        return inputProcessor.map(adapter -> adapter.mouseMoved(screenX, screenY)).orElse(false);
    }

    @Override
    public boolean scrolled(final float amountX, final float amountY) {
        return inputProcessor.map(adapter -> adapter.scrolled(amountX, amountY)).orElse(false);
    }

}
