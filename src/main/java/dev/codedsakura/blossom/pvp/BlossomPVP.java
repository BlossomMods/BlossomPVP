package dev.codedsakura.blossom.pvp;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dev.codedsakura.blossom.lib.BlossomLib;
import dev.codedsakura.blossom.lib.config.ConfigManager;
import dev.codedsakura.blossom.lib.permissions.Permissions;
import dev.codedsakura.blossom.lib.text.TextUtils;
import dev.codedsakura.blossom.lib.utils.CustomLogger;
import net.fabricmc.api.ModInitializer;
import net.minecraft.command.argument.EntityArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.core.Logger;

import java.util.UUID;

import static net.minecraft.server.command.CommandManager.argument;
import static net.minecraft.server.command.CommandManager.literal;

public class BlossomPVP implements ModInitializer {
    public static BlossomPVPConfig CONFIG = ConfigManager.register(BlossomPVPConfig.class, "BlossomPVP.json", newConfig -> CONFIG = newConfig);
    public static final Logger LOGGER = CustomLogger.createLogger("BlossomPVP");
    public static PVPController pvpController;

    @Override
    public void onInitialize() {
        LOGGER.info("BlossomPVP started!");

        pvpController = new PVPController();

        BlossomLib.addCommand(literal("pvp")
                .requires(Permissions.require("blossom.pvp", true))
                .executes(this::toggle)
                .then(argument("state", BoolArgumentType.bool())
                        .executes(this::change))
                .then(literal("query")
                        .executes(this::query)
                        .then(argument("player", EntityArgumentType.player())
                                .requires(Permissions.require("blossom.pvp.query-player", true))
                                .executes(this::queryPlayer))));
    }

    private int toggle(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        if (CONFIG.defaultActionIsQuery) {
            return query(ctx);
        }

        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        UUID uuid = player.getUuid();
        boolean state = !pvpController.isPVPEnabled(uuid);

        if (state) {
            pvpController.enablePVP(uuid);
            TextUtils.send(ctx, "blossom.pvp.toggle.enabled");
        } else {
            pvpController.disablePVP(uuid);
            TextUtils.send(ctx, "blossom.pvp.toggle.disabled");
        }

        return Command.SINGLE_SUCCESS;
    }

    private int change(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        UUID uuid = player.getUuid();
        boolean state = BoolArgumentType.getBool(ctx, "state");

        if (state) {
            pvpController.enablePVP(uuid);
            TextUtils.send(ctx, "blossom.pvp.change.enabled");
        } else {
            pvpController.disablePVP(uuid);
            TextUtils.send(ctx, "blossom.pvp.change.disabled");
        }

        return Command.SINGLE_SUCCESS;
    }

    private int query(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = ctx.getSource().getPlayerOrThrow();
        UUID uuid = player.getUuid();
        boolean state = pvpController.isPVPEnabled(uuid);

        if (state) {
            TextUtils.send(ctx, "blossom.pvp.query.enabled");
        } else {
            TextUtils.send(ctx, "blossom.pvp.query.disabled");
        }

        return Command.SINGLE_SUCCESS;
    }

    private int queryPlayer(CommandContext<ServerCommandSource> ctx) throws CommandSyntaxException {
        ServerPlayerEntity player = EntityArgumentType.getPlayer(ctx, "player");
        UUID uuid = player.getUuid();
        boolean state = pvpController.isPVPEnabled(uuid);

        if (state) {
            TextUtils.send(ctx, "blossom.pvp.query.player.enabled", player);
        } else {
            TextUtils.send(ctx, "blossom.pvp.query.player.disabled", player);
        }

        return Command.SINGLE_SUCCESS;
    }
}
