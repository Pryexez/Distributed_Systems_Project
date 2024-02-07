package de.hda.fbi.ds.vs_prak;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;

public class adapter {

    public static void exec() throws IOException, InterruptedException, MqttException {

        // Simulates Temperature in a simulated temperature unit

        // Step 1:Create the socket object for
        // carrying the data.
        DatagramSocket ds = new DatagramSocket(1237);
        String id = MqttClient.generateClientId();
        IMqttClient subscriber = new MqttClient("tcp://mosquitto:1883", id);
        String TOPIC = "mqtt_sensordata";
        Adapter_Callback callback = new Adapter_Callback();
        FlowRateMeasure flowRateMeasure = new FlowRateMeasure(callback, ds);
        subscriber.setCallback(callback);
        flowRateMeasure.start();


        byte[] receive = new byte[65535];
        byte[] buf;
        String sendinit = "add_me";
        buf = sendinit.getBytes();

        TimeUnit.SECONDS.sleep(10);

        DatagramPacket DpSend = new DatagramPacket(buf, buf.length, InetAddress.getByName("gateway"), 1234);

        ds.send(DpSend);

        System.out.println("sent registration");

        buf = null;

        try {
            subscriber.connect();
            if ( subscriber.isConnected()) {
                System.out.println("MQTT_Adapter" + id + " connected");
            }
            subscriber.subscribe(TOPIC);

            while (true) {
                if ( !subscriber.isConnected()) {
                    System.out.println("Retry Connect: MQTT_Sensor" + id);
                    subscriber.connect();

                    if ( subscriber.isConnected()) {
                        System.out.println("MQTT_Adapter" + id + " connected");
                    }
                }




                DpSend = new DatagramPacket(receive, receive.length);
                ds.receive(DpSend);

                String inp = callback.data;



                // convert the String input into the byte array.
                buf = inp.getBytes();

                // Step 2 : Create the datagramPacket for sending
               // the data.
                DpSend =
                        new DatagramPacket(buf, buf.length, DpSend.getAddress(), DpSend.getPort());

                // Step 3 : invoke the send call to actually send
                // the data.
                ds.send(DpSend);
                System.out.println("Data sent");
                flowRateMeasure.sentMessages++;
                receive = new byte[65535];

            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static StringBuilder data(byte[] a) {
        if (a == null)
            return null;
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (a[i] != 0) {
            ret.append((char) a[i]);
            i++;
        }
        return ret;
    }
}