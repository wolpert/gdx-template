/*
 *   Copyright (c) 2023. Ned Wolpert <ned.wolpert@gmail.com>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 */

package com.codeheadsystems.engine.utility;

import com.badlogic.gdx.utils.Pool;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * The interface Pooler.
 *
 * @param <T> the type parameter
 */
public interface Pooler<T> {

    /**
     * Of pooler.
     *
     * @param <T>      the type parameter
     * @param supplier the supplier
     * @return the pooler
     */
    static <T> Pooler<T> of(final Supplier<T> supplier) {
        return new PoolerImpl<>(supplier);
    }

    /**
     * Disabled pooler.
     *
     * @param <T>      the type parameter
     * @param supplier the supplier
     * @return the pooler
     */
    static <T> Pooler<T> disabled(final Supplier<T> supplier) {
        return new DisabledPoolerImpl<>(supplier);
    }

    /**
     * Obtain t.
     *
     * @return the t
     */
    T obtain();

    /**
     * Free.
     *
     * @param tInstance the t instance
     */
    void free(T tInstance);

    /**
     * Pool size int.
     *
     * @return the int
     */
    int poolSize();

    /**
     * Provides an easy way to obtain and free an object.
     *
     * @param consumer who is consuming the object.
     */
    default void with(final Consumer<T> consumer) {
        final T t = obtain();
        try {
            consumer.accept(t);
        } finally {
            free(t);
        }
    }

    /**
     * The type Pooler.
     *
     * @param <T> the type parameter
     */
    class PoolerImpl<T> implements Pooler<T> {

        final private Pool<T> pool;

        /**
         * Instantiates a new Pooler.
         *
         * @param supplier the supplier
         */
        protected PoolerImpl(final Supplier<T> supplier) {
            this.pool = new Pool<>() {
                @Override
                protected T newObject() {
                    return supplier.get();
                }
            };
        }

        @Override
        public T obtain() {
            return pool.obtain();
        }

        @Override
        public void free(T tInstance) {
            pool.free(tInstance);
        }

        @Override
        public int poolSize() {
            return pool.getFree();
        }
    }

    /**
     * A disabled pool. Used when you want caching off.
     *
     * @param <T> type of object to supply.
     */
    class DisabledPoolerImpl<T> implements Pooler<T> {

        private final Supplier<T> supplier;

        /**
         * Instantiates a new Disabled pooler.
         *
         * @param supplier the supplier
         */
        protected DisabledPoolerImpl(final Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T obtain() {
            return supplier.get();
        }

        @Override
        public void free(T tInstance) {

        }

        @Override
        public int poolSize() {
            return 0;
        }
    }
}
