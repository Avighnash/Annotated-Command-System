package com.avighnash;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.tuple.Pair;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.stream.Stream;

/**
 * OutdatedVersion
 * Oct/03/2016 (5:51 PM)
 */

public class Commands implements Listener
{

    private final Map<String, Pair<Command, Method>> commands;

    public Commands(JavaPlugin plugin)
    {
        Validate.notNull(plugin, "You must provide a plugin");

        commands = Maps.newHashMap();
    }

    public Commands addCommand(final Class<?> containingClass)
    {
        for (Method method : containingClass.getMethods())
        {
            if (!method.isAnnotationPresent(Command.class))
                continue;

            final Command _annotation = method.getAnnotation(Command.class);
            String _command = _annotation.name().toLowerCase();

            if (commands.containsKey(_command))
                continue;

            Validate.isTrue(method.getParameterCount() == 2, "You may only have two arguments to command execution methods.");

            if (!Stream.of(method.getParameterTypes()).allMatch(type -> type.equals(Player.class) || type.equals(String[].class)))
                throw new RuntimeException("Encountered issue whilst validating the method for a command by the name of `" + _annotation.name() + "`");

            final Pair<Command, Method> _pair = Pair.of(_annotation, method);

            commands.put(_command, _pair);
            Stream.of(_annotation.aliases()).forEach(alias -> commands.put(alias, _pair));
        }

        return this;
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void processCommands(PlayerCommandPreprocessEvent event) throws Exception
    {
        final Player _player = event.getPlayer();
        String _command = event.getMessage().substring(1).toLowerCase();
        String[] _args = new String[0];

        if (_command.contains(" "))
        {
            _command = _command.split(" ")[0];
            _args = event.getMessage().substring(event.getMessage().indexOf(' ') + 1).split(" ");
        }

        final Pair<Command, Method> _pair = commands.get(_command);

        if (_pair != null)
        {
            final Command _annotation = _pair.getLeft();
            final Method _method = _pair.getRight();

            if (!_annotation.permission().equals(""))
            {
                if (_player.hasPermission(_annotation.permission()))
                    _method.invoke(this, _player, _args);
                else
                    _player.sendMessage(ChatColor.translateAlternateColorCodes('&', _annotation.permissionMessage()));
            }
        }
    }

}
