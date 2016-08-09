package com.avighnash.cmdsystem;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by avigh on 8/1/2016.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Command {

    String name();

    String[] aliases() default {};

    String permission() default "";

    String usage() default "";

    String desc() default "";

    String noPermission() default "";

    boolean isPlayerOnly() default false;
}
