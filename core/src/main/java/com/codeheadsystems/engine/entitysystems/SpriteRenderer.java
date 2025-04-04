package com.codeheadsystems.engine.entitysystems;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.codeheadsystems.engine.component.SpriteComponent;
import com.codeheadsystems.engine.module.EntitySystemsModule;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SpriteRenderer extends EntitySystem {
    private static final String TAG = SpriteRenderer.class.getSimpleName();

    private final ComponentMapper<SpriteComponent> sm = ComponentMapper.getFor(SpriteComponent.class);

    private final Family family = Family.all(SpriteComponent.class).get();
    private final SpriteBatch spriteBatch;
    private ImmutableArray<Entity> entities;

    @Inject
    public SpriteRenderer(final SpriteBatch spriteBatch) {
        super(EntitySystemsModule.PRIORITIES.get(SpriteRenderer.class));
        this.spriteBatch = spriteBatch;
        log.debug(TAG, "constructor()");
    }

    @Override
    public void addedToEngine(Engine engine) {
        entities = engine.getEntitiesFor(family);
    }

    @Override
    public void removedFromEngine(Engine engine) {
        entities = null;
    }

    @Override
    public void update(float deltaTime) {
        spriteBatch.begin();
        for (int i = 0; i < entities.size(); ++i) {
            processEntity(entities.get(i), deltaTime);
        }
        spriteBatch.end();
    }

    protected void processEntity(final Entity entity, final float deltaTime) {
        SpriteComponent spriteComponent = sm.get(entity);
        Sprite sprite = spriteComponent.sprite();
        sprite.draw(spriteBatch);
    }
}
