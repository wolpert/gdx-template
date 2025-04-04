package com.codeheadsystems.engine.module;

import com.badlogic.ashley.core.EntitySystem;
import com.codeheadsystems.engine.entitysystems.RunnableExecutor;
import com.codeheadsystems.engine.entitysystems.SpriteRenderer;
import com.codeheadsystems.engine.entitysystems.TimerExecutor;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Module
public interface EntitySystemsModule {

    List<Class<? extends EntitySystem>> ENTITY_SYSTEMS = List.of(
        RunnableExecutor.class,
        TimerExecutor.class,
        SpriteRenderer.class
    );

    Map<Class<? extends EntitySystem>, Integer> PRIORITIES = ENTITY_SYSTEMS.stream()
        .collect(Collectors.toMap(Function.identity(), ENTITY_SYSTEMS::indexOf));

    @Binds
    @IntoSet
    EntitySystem provideRunnableEntitySystem(RunnableExecutor entitySystem);

    @Binds
    @IntoSet
    EntitySystem provideSpriteRenderingSystem(SpriteRenderer entitySystem);

    @Binds
    @IntoSet
    EntitySystem provideTimerExecutor(TimerExecutor entitySystem);
}
