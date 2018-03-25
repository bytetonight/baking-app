/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package android.example.com.foodoo.network;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A very simple implementation of {@link IdlingResource}.
 * <p>
 * Consider using CountingIdlingResource from espresso-contrib package if you use this class from
 * multiple threads or need to keep a count of pending operations.
 */

public class NetworkIdlingResource implements IdlingResource {

    @Nullable
    private volatile ResourceCallback resourceCallback;

    /* using an AtomicBoolean for thread-safety ... who would have thought that */
    private AtomicBoolean isIdleNow = new AtomicBoolean(true);



    /**
     * Returns the name of the resources (used for logging and idempotency  of registration).
     */
    @Override
    public String getName() {
        return this.getClass().getName();
    }

    /**
     * Returns {@code true} if resource is currently idle. Espresso will <b>always</b> call this
     * method from the main thread, therefore it should be non-blocking and return immediately.
     */
    @Override
    public boolean isIdleNow() {
        return isIdleNow.get();
    }

    /**
     * Registers the given {@link ResourceCallback} with the resource. Espresso will call this method:
     * <ul>
     * <li>with its implementation of {@link ResourceCallback} so it can be notified asynchronously
     * that your resource is idle
     * <li>from the main thread, but you are free to execute the callback's onTransitionToIdle from
     * any thread
     * <li>once (when it is initially given a reference to your IdlingResource)
     * </ul>
     * <br>
     * You only need to call this upon transition from busy to idle - if the resource is already idle
     * when the method is called invoking the call back is optional and has no significant impact.
     *
     * @param callback
     */
    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        resourceCallback = callback;
    }

    /**
     * Sets the new idle state, if isIdleNow is true, it pings the {@link ResourceCallback}.
     * @param isIdleNow false if there are pending operations, true if idle.
     */
    public void setIdleState(boolean isIdleNow) {
        this.isIdleNow.set(isIdleNow);
        if (isIdleNow && resourceCallback != null) {
            resourceCallback.onTransitionToIdle();
        }
    }
}
