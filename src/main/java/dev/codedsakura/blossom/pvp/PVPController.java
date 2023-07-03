package dev.codedsakura.blossom.pvp;

import dev.codedsakura.blossom.lib.data.ListDataController;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PVPController extends ListDataController<UUID> {
    @Override
    public Class<UUID[]> getArrayClassType() {
        return UUID[].class;
    }

    @Override
    public List<UUID> defaultData() {
        return new ArrayList<>();
    }

    @Override
    public String getFilename() {
        return "BlossomPVP";
    }

    public boolean isPVPEnabled(UUID uuid) {
        if (BlossomPVP.CONFIG.enabledByDefault) {
            return !data.contains(uuid);
        }
        return data.contains(uuid);
    }


    private void addToList(UUID uuid) {
        if (data.contains(uuid)) {
            return;
        }
        data.add(uuid);

        write();
    }

    private void removeFromList(UUID uuid) {
        if (!data.contains(uuid)) {
            return;
        }
        data.remove(uuid);

        write();
    }


    void enablePVP(UUID uuid) {
        if (BlossomPVP.CONFIG.enabledByDefault) {
            removeFromList(uuid);
        } else {
            addToList(uuid);
        }
    }

    void disablePVP(UUID uuid) {
        if (BlossomPVP.CONFIG.enabledByDefault) {
            addToList(uuid);
        } else {
            removeFromList(uuid);
        }
    }
}
