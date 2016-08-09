package com.avighnash.cmdsystem;

/**
 * Created by avigh on 8/3/2016.
 */
public @interface SubCommand {

    String parent();

    String name();

    String permission();

    String usage();

    String desc();

}
