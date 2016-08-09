package com.avighnash.cmdsystem;

import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * Created by avigh on 8/2/2016.
 */
public class BukkitCommand extends org.bukkit.command.Command {

    private CustomCommand command;
    private CommandExecutor executor;

    protected BukkitCommand(CustomCommand command, CommandExecutor executor) {
        super(
                command.getName(),
                command.getDesc(),
                command.getUsage(),
                Arrays.asList(command.getAliases())
        );

        this.command = command;
        this.executor = executor;
    }

    @Override
    public boolean execute(CommandSender commandSender, String s, String[] strings) {
        try {
            executor.onCommand(commandSender, this, s, strings);
        } catch (CommandException e) {
            e.printStackTrace();
        }

        return false;
    }
}

