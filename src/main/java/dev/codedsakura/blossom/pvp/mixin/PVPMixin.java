package dev.codedsakura.blossom.pvp.mixin;

import dev.codedsakura.blossom.lib.text.TextUtils;
import dev.codedsakura.blossom.pvp.BlossomPVP;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.MutableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.UUID;

@Mixin(ServerPlayerEntity.class)
public class PVPMixin {

    @Inject(method = "shouldDamagePlayer", at = @At("HEAD"), cancellable = true)
    void BlossomPVP$pvpMixin(PlayerEntity attacker, CallbackInfoReturnable<Boolean> cir) {
        ServerPlayerEntity defender = ((ServerPlayerEntity) (Object) this);

        UUID defenderUuid = defender.getUuid();
        UUID attackerUuid = attacker.getUuid();

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
    void messageHelper(PlayerEntity attacker, PlayerEntity defender, String key) {
        MutableText attackerMessage = TextUtils.fTranslation(key + ".attacker", TextUtils.Type.WARN, defender);
        if (!attackerMessage.asTruncatedString(1).isEmpty()) {
            attacker.sendMessage(attackerMessage, false);
        }

        MutableText defenderMessage = TextUtils.fTranslation(key + ".defender", TextUtils.Type.WARN, attacker);
        if (!defenderMessage.asTruncatedString(1).isEmpty()) {
            defender.sendMessage(defenderMessage, false);
        }
    }
}
