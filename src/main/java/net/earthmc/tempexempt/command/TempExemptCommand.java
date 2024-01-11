package net.earthmc.tempexempt.command;

import net.earthmc.tempexempt.TempExempt;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class TempExemptCommand implements TabExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("No player specified", NamedTextColor.RED));
            return true;
        }

        OfflinePlayer player = Bukkit.getPlayer(args[0]);
        if (player == null || !player.hasPlayedBefore()) {
            sender.sendMessage(Component.text("Specified player does not exist", NamedTextColor.RED));
            return true;
        }

        int exemptTime;
        if (args.length > 1) {
            try {
                int parsedInt = Integer.parseInt(args[1]);
                if (parsedInt <= 0) parsedInt = 1;

                exemptTime = Math.min(parsedInt, 60);
            } catch (NumberFormatException e) {
                sender.sendMessage(Component.text("Invalid exemption time specified", NamedTextColor.RED));
                return true;
            }
        } else {
            exemptTime = TempExempt.INSTANCE.getConfig().getInt("exempt_time");
        }

        NodeBuilder builder = Node.builder("grim.exempt")
                        .value(true).expiry(exemptTime, TimeUnit.MINUTES);

        LuckPerms luckPerms = LuckPermsProvider.get();
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        if (user == null) return true;

        user.data().add(builder.build());
        luckPerms.getUserManager().saveUser(user);

        sender.sendMessage(Component.text("Successfully exempt " + player.getName() + " from Grim anticheat for " + exemptTime + " minutes", NamedTextColor.GREEN));

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 1: {
                List<String> onlinePlayers = new ArrayList<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    onlinePlayers.add(player.getName());
                }

                if (args[0].isEmpty()) {
                    return onlinePlayers;
                } else {
                    return onlinePlayers.stream()
                            .filter(name -> name.startsWith(args[0]))
                            .collect(Collectors.toList());
                }
            }

            case 2: return List.of("{time}");
            default: return null;
        }
    }
}
