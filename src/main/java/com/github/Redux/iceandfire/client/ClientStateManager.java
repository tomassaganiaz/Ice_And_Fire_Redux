package com.github.Redux.iceandfire.client;

import com.github.Redux.iceandfire.entity.util.MyrmexHive;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
/** ClientStateManager — Client State Manager */


public class ClientStateManager {

    public static final List<UUID> currentDragonRiders = new ArrayList<>();
    private static MyrmexHive referedClientHive = null;
    private static int thirdPersonViewDragon = 0;

    public static int getDragon3rdPersonView() {
        return thirdPersonViewDragon;
    }

    public static void setDragon3rdPersonView(int view) {
        thirdPersonViewDragon = view;
    }

    public static MyrmexHive getReferedClientHive(){
        return referedClientHive;
    }

    public static void setReferedClientHive(MyrmexHive hive){
        referedClientHive = hive;
    }
}
