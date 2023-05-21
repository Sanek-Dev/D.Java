/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Interaction events marked as DeferEvent will make defer reply request before they will be executed**/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DeferEvent {
}
