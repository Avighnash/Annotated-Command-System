package com.avighnash;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * OutdatedVersion
 * Oct/03/2016 (5:52 PM)
 */

@Retention ( RetentionPolicy.RUNTIME )
@Target ( ElementType.METHOD )
public @interface Command
{

    String name();

    String[] aliases() default "";

    String permission() default "";

    String usage() default "No usage has been provided.";

    String permissionMessage() default "&c&lUh oh, you're not allowed to use that!";

}
