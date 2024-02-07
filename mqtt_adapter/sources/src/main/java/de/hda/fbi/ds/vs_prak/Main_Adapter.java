
package de.hda.fbi.ds.vs_prak;


import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


public class Main_Adapter {


    public static void main(String[] args) throws InterruptedException, IOException, MqttException {

        TimeUnit.SECONDS.sleep(20);
        System.out.println("Adapter online!");


        adapter sensor = new adapter();

        sensor.exec();

    }

}
