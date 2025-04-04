package com.codeheadsystems.engine.utility;

import java.util.ArrayList;
import java.util.function.Consumer;

public class Holder<T> {

    private final ArrayList<onSet<T>> onSetListeners = new ArrayList<>();
    private final ArrayList<onUnset> onUnsetListeners = new ArrayList<>();
    private T value;

    public Holder() {
    }

    public Holder(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }

    public void ifSet(Consumer<T> consumer) {
        if (value != null) {
            consumer.accept(value);
        }
    }

    public void set(T value) {
        this.value = value;
        if (this.value != null) {
            onSetListeners.forEach(onSet -> onSet.value(value));
        } else {
            onUnsetListeners.forEach(onUnset::unset);
        }
    }

    public void addOnSetListener(onSet<T> listener) {
        onSetListeners.add(listener);
    }

    public void addOnUnsetListener(onUnset listener) {
        onUnsetListeners.add(listener);
    }

    public void removeOnSetListener(onSet<T> listener) {
        onSetListeners.remove(listener);
    }

    public void removeOnUnsetListener(onUnset listener) {
        onUnsetListeners.remove(listener);
    }

    public interface onSet<T> {
        void value(T value);
    }

    public interface onUnset {
        void unset();
    }
}
