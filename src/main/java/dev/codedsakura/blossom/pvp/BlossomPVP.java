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
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.core.Logger;

import java.util.UUID;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

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
                        .then(argument("player", EntityArgument.player())
                                .requires(Permissions.require("blossom.pvp.query-player", true))
                                .executes(this::queryPlayer))));
    }

    private int toggle(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (CONFIG.defaultActionIsQuery) {
            return query(ctx);
        }

        ServerPlayer player = ctx.getSource().getPlayerOrException();
        UUID uuid = player.getUUID();
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

    private int change(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        UUID uuid = player.getUUID();
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

    private int query(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        UUID uuid = player.getUUID();
        boolean state = pvpController.isPVPEnabled(uuid);

        if (state) {
            TextUtils.send(ctx, "blossom.pvp.query.enabled");
        } else {
            TextUtils.send(ctx, "blossom.pvp.query.disabled");
        }

        return Command.SINGLE_SUCCESS;
    }

    private int queryPlayer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        UUID uuid = player.getUUID();
        boolean state = pvpController.isPVPEnabled(uuid);

        if (state) {
            TextUtils.send(ctx, "blossom.pvp.query.player.enabled", player);
        } else {
            TextUtils.send(ctx, "blossom.pvp.query.player.disabled", player);
        }

        return Command.SINGLE_SUCCESS;
    }
}
