package com.codeheadsystems.engine.module;

import com.badlogic.gdx.utils.Disposable;
import com.codeheadsystems.engine.manager.LevelManager;
import com.codeheadsystems.engine.scene.Launcher;
import com.codeheadsystems.engine.scene.Title;
import dagger.Component;
import java.util.Set;
import javax.inject.Singleton;

@Component(modules = {
    TopApplicationModule.class,
    EntitySystemsModule.class,
    GdxModule.class
})
@Singleton
public interface GameComponent {

    Title titleScene();

    Launcher launcher();

    LevelManager levelManager();

    Set<Disposable> disposables();

}
