package com.codeheadsystems.engine.entitysystems;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.codeheadsystems.engine.component.TimerComponent;
import com.codeheadsystems.engine.module.EntitySystemsModule;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TimerExecutor extends IteratingSystem {

    private static final String TAG = TimerExecutor.class.getSimpleName();
    private static final Family FAMILY = Family.all(TimerComponent.class).get();
    private static final ComponentMapper<TimerComponent> TIMER_MAPPER = ComponentMapper.getFor(TimerComponent.class);

    @Inject
    public TimerExecutor() {
        super(FAMILY, EntitySystemsModule.PRIORITIES.get(TimerExecutor.class));
        log.debug(TAG, "constructor()");
    }

    @Override
    protected void processEntity(final Entity entity, final float deltaTime) {
        final TimerComponent timerComponent = TIMER_MAPPER.get(entity);
        if (timerComponent.moveClockForward(deltaTime)) {
            if (!timerComponent.timer().get()) {
                getEngine().removeEntity(entity);
            }
        }
    }
}
