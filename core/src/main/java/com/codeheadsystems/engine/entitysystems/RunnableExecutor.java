package com.codeheadsystems.engine.entitysystems;

import static com.codeheadsystems.engine.module.TopApplicationModule.ENTITY_SYSTEM_RUNNABLE_ARRAY;
import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.gdx.utils.Array;
import com.codeheadsystems.engine.module.EntitySystemsModule;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * This class provides a method to execute Runnable interfaces, in order of adding to here, during the ashley engine update
 * at the first step. (Depending on where it is in the order of execution.)
 */
@Singleton
public class RunnableExecutor extends EntitySystem {

    private final String TAG = getClass().getSimpleName();

    private final Array<Runnable> runnableArray;

    @Inject
    public RunnableExecutor(@Named(ENTITY_SYSTEM_RUNNABLE_ARRAY) Array<Runnable> runnableArray) {
        super(EntitySystemsModule.PRIORITIES.get(RunnableExecutor.class));
        log.debug(TAG, "constructor()");
        this.runnableArray = runnableArray;
    }

    public void addRunnable(Runnable runnable) {
        runnableArray.add(runnable);
    }

    @Override
    public void update(float deltaTime) {
        runnableArray.forEach(Runnable::run);
        runnableArray.clear();
    }
}
