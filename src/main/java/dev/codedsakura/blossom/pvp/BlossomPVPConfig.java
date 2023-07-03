package dev.codedsakura.blossom.pvp;

public class BlossomPVPConfig {
    // whether data containing player's uuid means "enabled" (false) or "disabled" (true)
    // so if `false` - player uuid list indicates players with pvp enabled
    // and if `true` - player uuid list indicates players with pvp disabled
    boolean enabledByDefault = true;
    boolean defaultActionIsQuery = false;
}
