package com.avighnash.cmdsystem;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by avigh on 8/4/2016.
 */
public class CustomCommand {

    private final String name,
            permission,
            usage,
            desc;

    private final String[] aliases;
    private Method method;
    private CustomCommand parent = null;
    private Object executor;

    private final List<CustomCommand> subCommands;


    public CustomCommand(String name, String permission, String usage, String desc, String[] aliases, Method method,
                         Object executor) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.usage = usage;
        this.desc = desc;
        this.executor = executor;
        this.method = method;

        subCommands = new ArrayList<>();
    }

    public void addSubcommand(CustomCommand subCommand) {
        subCommand.parent = this;

        if (!subCommands.contains(subCommand))
            subCommands.add(subCommand);
    }

    public void removeSubcommand(CustomCommand subCommand) {
        if (subCommands.contains(subCommand))
            subCommands.remove(subCommand);
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }

    public String getDesc() {
        return desc;
    }

    public List<CustomCommand> getSubCommands() {
        return subCommands;
    }

    public Method getMethod() {
        return method;
    }

    public Object getExecutor() {
        return executor;
    }
}
