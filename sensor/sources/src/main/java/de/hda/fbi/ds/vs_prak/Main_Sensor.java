
package de.hda.fbi.ds.vs_prak;


import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class Main_Sensor {


    public static void main(String[] args) throws InterruptedException, IOException {

        TimeUnit.SECONDS.sleep(20);
        System.out.println("Sensor online!");


        udp_Sensor sensor = new udp_Sensor();

        sensor.exec();

    }

}
