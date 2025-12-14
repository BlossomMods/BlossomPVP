package dev.codedsakura.blossom.pvp.mixin;

import dev.codedsakura.blossom.lib.text.TextUtils;
import dev.codedsakura.blossom.pvp.BlossomPVP;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ServerPlayer.class)
public class PVPMixin {

    @Inject(method = "canHarmPlayer", at = @At("HEAD"), cancellable = true)
    void BlossomPVP$pvpMixin(Player attacker, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayer defender = ((ServerPlayer) (Object) this);

        UUID defenderUuid = defender.getUUID();
        UUID attackerUuid = attacker.getUUID();

        if (defenderUuid.equals(attackerUuid)) {
            return;
        }

        boolean defenderPvpDisabled = !BlossomPVP.pvpController.isPVPEnabled(defenderUuid);
        boolean attackerPvpDisabled = !BlossomPVP.pvpController.isPVPEnabled(attackerUuid);

        if (defenderPvpDisabled || attackerPvpDisabled) {
            if (attackerPvpDisabled && defenderPvpDisabled) {
                messageHelper(attacker, defender, "blossom.pvp.fail.both");
            } else if (attackerPvpDisabled) {
                messageHelper(attacker, defender, "blossom.pvp.fail.attacker");
            } else {
                messageHelper(attacker, defender, "blossom.pvp.fail.defender");
            }
            cir.setReturnValue(false);
        }
    }

    @Unique
    void messageHelper(Player attacker, Player defender, String key) {
        MutableComponent attackerMessage = TextUtils.fTranslation(key + ".attacker", TextUtils.Type.WARN, defender);
        if (!attackerMessage.getString(1).isEmpty()) {
            attacker.displayClientMessage(attackerMessage, false);
        }

        MutableComponent defenderMessage = TextUtils.fTranslation(key + ".defender", TextUtils.Type.WARN, attacker);
        if (!defenderMessage.getString(1).isEmpty()) {
            defender.displayClientMessage(defenderMessage, false);
        }
    }
}
