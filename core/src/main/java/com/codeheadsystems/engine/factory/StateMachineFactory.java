package com.codeheadsystems.engine.factory;

import static com.codeheadsystems.engine.utility.Log.log;

import com.codeheadsystems.engine.model.GameConfig;
import com.codeheadsystems.smr.Callback;
import com.codeheadsystems.smr.Context;
import com.codeheadsystems.smr.Dispatcher;
import com.codeheadsystems.smr.Event;
import com.codeheadsystems.smr.Phase;
import com.codeheadsystems.smr.State;
import com.codeheadsystems.smr.StateMachine;
import com.codeheadsystems.smr.StateMachineDefinition;
import java.util.function.Consumer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class StateMachineFactory {

    public static final State IDLE = State.of("IDLE");
    public static final State PATH_PICKING = State.of("PATH_PICKING");
    public static final State PATH_DRAWING = State.of("PATH_DRAWING");
    public static final State PATH_VERIFYING = State.of("PATH_VERIFYING");
    public static final Event TIMER_EXPIRED = Event.of("TIMER_EXPIRED");
    public static final Event MOUSE_DOWN = Event.of("MOUSE_DOWN");
    public static final Event MOUSE_UP = Event.of("MOUSE_UP");
    public static final Event PATH_CANCELED = Event.of("PATH_CANCELED");
    public static final Event PATH_SAVED = Event.of("PATH_SELECTED");
    private final String TAG = getClass().getSimpleName();
    private final GameConfig.Globals globals;

    @Inject
    public StateMachineFactory(final GameConfig.Globals globals) {
        this.globals = globals;
        log.debug(TAG, "constructor()");
    }

    public StateMachine inputStateMachine() {
        final StateMachineDefinition definition = StateMachineDefinition.builder()
            .addState(IDLE).addState(PATH_PICKING)
            .addState(PATH_DRAWING).addState(PATH_VERIFYING).setInitialState(IDLE)
            .addTransition(IDLE, MOUSE_DOWN, PATH_DRAWING) // hex clicked
            .addTransition(PATH_DRAWING, TIMER_EXPIRED, PATH_PICKING)
            .addTransition(PATH_PICKING, MOUSE_UP, PATH_VERIFYING)
            .addTransition(PATH_DRAWING, MOUSE_UP, PATH_VERIFYING) // show everything
            .addTransition(PATH_VERIFYING, MOUSE_DOWN, PATH_DRAWING) // keep drawing
            .addTransition(PATH_VERIFYING, PATH_SAVED, IDLE)  // end hex selected again
            .addTransition(PATH_VERIFYING, PATH_CANCELED, IDLE) // start hex selected
            .build();
        final StateMachine.Builder builder = StateMachine.builder()
            .withStateMachineDefinition(definition)
            .withUseExceptions(false);
        if (globals.debugStateMachine) {
            builder.withDispatcherDecorator(LoggingDispatcher::new);
        }
        return builder.build();
    }

    public class LoggingDispatcher implements Dispatcher {

        private final Dispatcher dispatcher;

        public LoggingDispatcher(final Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        @Override
        public void enable(final State state, final Phase phase, final Consumer<Callback> consumer) {
            log.debug(TAG, "enable " + state + ":" + phase);
            dispatcher.enable(state, phase, consumer);
        }

        @Override
        public void disable(final State state, final Phase phase, final Consumer<Callback> consumer) {
            log.debug(TAG, "disable " + state + ":" + phase);
            dispatcher.disable(state, phase, consumer);
        }

        @Override
        public void handleTransitionEvent(final Context context, final State state, final State state1, final Event event) {
            log.debug(TAG, "handleTransitionEvent " + state + " -> " + state1 + ":" + event);
            dispatcher.handleTransitionEvent(context, state, state1);
        }

        @Override
        public void handleTransitionEvent(final Context context, final State state, final State state1) {
            dispatcher.handleTransitionEvent(context, state, state1);
        }

        @Override
        public State changeState(final Context context, final State state, final State state1) {
            log.debug(TAG, "changeState " + state + " -> " + state1);
            return dispatcher.changeState(context, state, state1);
        }

        @Override
        public void dispatchCallbacks(final Context context, final State state, final Phase phase) {
            log.debug(TAG, "dispatchCallbacks " + state + ":" + phase);
            dispatcher.dispatchCallbacks(context, state, phase);
        }

        @Override
        public void executeCallback(final Consumer<Callback> consumer, final Callback callback) {
            log.debug(TAG, "executeCallback " + callback);
            dispatcher.executeCallback(consumer, callback);
        }
    }
}
