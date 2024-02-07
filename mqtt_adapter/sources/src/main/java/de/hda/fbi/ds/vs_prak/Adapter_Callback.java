package de.hda.fbi.ds.vs_prak;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Adapter_Callback implements MqttCallback {

    public String data = "";
    public int received;

    public Adapter_Callback() {

    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection to MQTT broker lost!");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        data = new String(mqttMessage.getPayload());
        System.out.println("Message received: "+ data );
        received++;

    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken mqttDeliveryToken) {
        try {
            System.out.println("Delivery completed: "+ mqttDeliveryToken.getMessage() );
        } catch (MqttException e) {
            System.out.println("Failed to get delivery token message: " + e.getMessage());
        }

    }
}
