package de.hda.fbi.ds.vs_prak.core;

import java.util.ArrayList;

public final class SensorData {
    private SensorData() {
    }

    public static ArrayList<String> Sensordatalist = new ArrayList<>();

    public static ArrayList<String> getSensordatalist() {
        return Sensordatalist;
    }

    public static void addSensordatalist(String data) {
        Sensordatalist.add(data);
    }

    public static void removeElement(){
        Sensordatalist.remove(0);
    }

}
