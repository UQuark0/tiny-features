package me.uquark.tinyfeatures.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import me.uquark.tinyfeatures.config.Config;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;

public class PistonBlockLimitCommand {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(
            CommandManager.literal("pistonblocklimit")
            .requires(source -> source.hasPermissionLevel(4))
            .then(CommandManager.argument("number", IntegerArgumentType.integer(0, 255))
                .executes(context -> {
                    Config.PISTON_BLOCK_LIMIT = IntegerArgumentType.getInteger(context, "number");
                    context.getSource().sendFeedback(new LiteralText("Set piston block limit to " + String.valueOf(Config.PISTON_BLOCK_LIMIT)), true);
                    return 1;
                }))
            .executes(context -> {
                context.getSource().sendFeedback(new LiteralText("Invalid syntax"), false);
                return -1;
            }));
    }
}
