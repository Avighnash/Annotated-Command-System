package com.avighnash.cmdsystem;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by avigh on 8/1/2016.
 */
public final class CommandSystem implements CommandExecutor {

    private final Map<CustomCommand, Object> commands = new HashMap<>();
    private final CommandMap map;
    private final Plugin plugin;

    public CommandSystem(JavaPlugin plugin) {
        this.plugin = plugin;
        map = ((CraftServer) plugin.getServer()).getCommandMap();
    }

    public void registerCommands(Object obj) {
        final Class<?> clazz = obj.getClass();

        final Map<String, CustomCommand> tm = new HashMap<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(Command.class)) {
                continue;
            }

            Class<?>[] pt = method.getParameterTypes();
            if (pt.length > 3)
                if (pt[0] != CommandSender.class || pt[1] != org.bukkit.command.Command.class ||
                        pt[2] != String[].class) {

                    System.out.println("Unable to register command " + method.getName()
                            + " due to unknown parameter types");
                }

            final Command command = method.getAnnotation(Command.class);

            final CustomCommand customCommand = new CustomCommand(
                    command.name(),
                    command.permission(),
                    command.usage(),
                    command.desc(),
                    command.aliases(),
                    method,
                    obj
            );

            BukkitCommand bukkitCommand = new BukkitCommand(customCommand, this);

            commands.put(customCommand, obj);
            tm.put(command.name(), customCommand);
            map.register(plugin.getName(), bukkitCommand);
        }

        for (Method method : clazz.getClass().getMethods()) {
            if (!method.isAnnotationPresent(SubCommand.class)) {
                continue;
            }

            Class<?>[] pt = method.getParameterTypes();
            if (pt.length > 3)
                if (pt[0] != CommandSender.class || pt[1] != org.bukkit.command.Command.class ||
                        pt[2] != String[].class) {

                    System.out.println("Unable to register subcommand " + method.getName()
                            + " due to unknown parameter types");
                }

            SubCommand subCommand = method.getAnnotation(SubCommand.class);
            CustomCommand parent = tm.get(subCommand.parent());

            CustomCommand command = new CustomCommand(
                    subCommand.name(),
                    subCommand.permission(),
                    subCommand.usage(),
                    subCommand.desc(),
                    new String[0],
                    method,
                    obj
            );

            parent.addSubcommand(command);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        return handleCmds(sender, cmd, label, args);
    }

    private boolean handleCmds(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        for (CustomCommand customCommand : commands.keySet()) {
            if (customCommand.getName().equalsIgnoreCase(cmd.getName())) {

                Method method = customCommand.getMethod();
                Object object = commands.get(customCommand);
                Command command = method.getAnnotation(Command.class);

                if (!command.permission().isEmpty() && !sender.hasPermission(command.permission())) {
                    sender.sendMessage(command.noPermission());
                    return true;
                }

                if (command.isPlayerOnly() && !(sender instanceof Player)) {
                    sender.sendMessage("This command is only executable by players!");
                    return true;
                }

                try {
                    method.invoke(object, sender, cmd, args);
                } catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }

            for (CustomCommand cs : customCommand.getSubCommands()) {

                Method csMethod = cs.getMethod();
                Object csObject = cs.getExecutor();
                SubCommand command = csMethod.getAnnotation(SubCommand.class);
            }
        }
        return false;
    }

}

