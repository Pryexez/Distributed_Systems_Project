
package de.hda.fbi.ds.vs_prak;


import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;


public class Main_Sensor {


    public static void main(String[] args) throws InterruptedException, IOException, MqttException {


        System.out.println("MQTT_Sensor online!");


        mqtt_Sensor sensor = new mqtt_Sensor();

        sensor.exec();

    }

}
