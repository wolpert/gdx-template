package com.codeheadsystems.engine.inputadapter;

import static com.codeheadsystems.engine.utility.Log.log;

import com.badlogic.ashley.core.PooledEngine;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.codeheadsystems.engine.factory.StateMachineFactory;
import com.codeheadsystems.engine.model.ActiveLevelDetails;
import com.codeheadsystems.engine.model.ActiveLevelDetailsHolder;
import com.codeheadsystems.engine.model.GameConfig;
import com.codeheadsystems.smr.Callback;
import com.codeheadsystems.smr.Event;
import com.codeheadsystems.smr.Phase;
import com.codeheadsystems.smr.StateMachine;
import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This class manages the setting of the hex paths in the game by the player. It is NOT used to render the entities,
 * but rather the input adapter that takes the input and calculates the path in the pool update thread.
 */
@Singleton
public class HexInputProcessor {
    private final String TAG = getClass().getSimpleName();
    private final PooledEngine pooledEngine;


    /**
     * This is the path being defined.
     */
    private final GameConfig.Globals globals;
    private final InputProxy inputProxy;
    private final StateMachine stateMachine;

    @Inject
    public HexInputProcessor(final PooledEngine pooledEngine,
                             final ActiveLevelDetailsHolder activeLevelDetailsHolder,
                             final GameConfig.Globals globals,
                             final StateMachineFactory stateMachineFactory) {
        this.globals = globals;
        this.inputProxy = new InputProxy();
        this.pooledEngine = pooledEngine;
        this.stateMachine = setupStateMachine(stateMachineFactory.inputStateMachine());
        activeLevelDetailsHolder.addOnSetListener(this::levelChanged);
        log.debug(TAG, "constructor()");
    }

    public void levelChanged(final ActiveLevelDetails activeLevelDetails) {
    }

    /**
     * Really we want the input processor to be easily modifiable so we can update it when our state changes.
     * That's why we use the input proxy instead of just a standard input processor. The processor changes
     * based on our state.
     *
     * @return input processor.
     */
    public InputProcessor inputProcessor() {
        return inputProxy;
    }

    /**
     * We setup our state machine for the run. We have to force going into idle since that sets up our
     * initial input processor.
     *
     * @param stateMachine to enable our state transitions.
     * @return configured state machine.
     */
    private StateMachine setupStateMachine(final StateMachine stateMachine) {
        stateMachine.enable(StateMachineFactory.IDLE, Phase.ENTER, this::enterIdle);
        stateMachine.enable(StateMachineFactory.PATH_DRAWING, Phase.ENTER, this::enterPathDrawing);
        stateMachine.enable(StateMachineFactory.PATH_PICKING, Phase.ENTER, this::enterPathPicking);
        // finally, setup the idle state
        enterIdle(null);
        return stateMachine;
    }

    // Helper method to dispatch events to add common logging.
    private void dispatch(Event event) {
        if (globals.debugStateMachine) {
            log.debug(TAG, "dispatch(" + event + ") while in " + stateMachine.state());
        }
        stateMachine.dispatch(event);
    }

    /**
     * In the idle state, we onoly listen for mouse down events to start our timer and start the path.
     *
     * @param callback ignored.
     */
    private void enterIdle(Callback callback) {
        final InputAdapter idleInputProcessor = new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                dispatch(StateMachineFactory.MOUSE_DOWN);
                return true;
            }
        };
        inputProxy.setInputProcessor(idleInputProcessor);
    }

    /**
     * Once we are picking a path, we only need to detect the mouse went up.
     *
     * @param callback ignored.
     */
    private void enterPathPicking(Callback callback) {
        log.debug(TAG, "enterPathPicking()");
        inputProxy.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
                dispatch(StateMachineFactory.MOUSE_UP);
                return true;
            }
        });
    }


    /**
     * Basically let the system draw the paths and when we touch up, go into verify mode.
     *
     * @param callback
     */
    private void enterPathDrawing(Callback callback) {
        inputProxy.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(final int screenX, final int screenY, final int pointer, final int button) {
                dispatch(StateMachineFactory.MOUSE_UP);
                return true;
            }
        });
    }


}
