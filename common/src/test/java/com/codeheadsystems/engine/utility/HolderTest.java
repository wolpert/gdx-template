package com.codeheadsystems.engine.utility;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class HolderTest {

    private Holder<Boolean> holder;

    @BeforeEach
    void setUp() {
        holder = new Holder<>();
    }

    @Test
    void testSet() {
        holder.set(true);
        assertThat(holder.get()).isTrue();
    }

    @Test
    void testSetNull() {
        holder.set(null);
        assertThat(holder.get()).isNull();
    }

    @Test
    void testAddOnSetListener() {
        AtomicBoolean value = new AtomicBoolean(false);
        holder.addOnSetListener(value::set);
        holder.set(true);
        assertThat(value.get()).isTrue();
    }

    @Test
    void testRemoveOnUnsetListener() {
        AtomicBoolean value = new AtomicBoolean(true);
        Holder.onUnset listener = () -> value.set(false);
        holder.addOnUnsetListener(listener);
        holder.removeOnUnsetListener(listener);
        holder.set(null);
        assertThat(value.get()).isTrue();
    }

    @Test
    void testAddOnUnsetListener() {
        AtomicBoolean value = new AtomicBoolean(true);
        holder.addOnUnsetListener(() -> value.set(false));
        holder.set(null);
        assertThat(value.get()).isFalse();
    }

    @Test
    void testRemoveOnSetListener() {
        AtomicBoolean value = new AtomicBoolean(false);
        Holder.onSet<Boolean> listener = value::set;
        holder.addOnSetListener(listener);
        holder.removeOnSetListener(listener);
        holder.set(true);
        assertThat(value.get()).isFalse();
    }
}
