package com.codeheadsystems.engine.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.utils.Pool;
import java.util.function.Supplier;

/**
 * The timer here is represented as a supplier that returns a boolean.  If the boolean is true, the timer will
 * run again
 */
public class TimerComponent implements Pool.Poolable, Component {

    private Supplier<Boolean> timer;
    private float cycle;
    private float timeElapsed;
    private String label;

    @Override
    public void reset() {
        timer = null;
        cycle = 0;
        timeElapsed = 0;
        label = null;
    }

    public TimerComponent withTimer(Supplier<Boolean> timer) {
        this.timer = timer;
        return this;
    }

    public TimerComponent withCycle(float cycle) {
        this.cycle = cycle;
        return this;
    }

    public TimerComponent withLabel(String label) {
        this.label = label;
        return this;
    }

    public String label() {
        return label;
    }

    public Supplier<Boolean> timer() {
        return timer;
    }

    // updates the clock, returning true if the timer is ready for
    // execution.
    public boolean moveClockForward(float deltaTime) {
        timeElapsed += deltaTime;
        if (timeElapsed >= cycle) {
            timeElapsed -= cycle;
            return true;
        } else {
            return false;
        }
    }
}
