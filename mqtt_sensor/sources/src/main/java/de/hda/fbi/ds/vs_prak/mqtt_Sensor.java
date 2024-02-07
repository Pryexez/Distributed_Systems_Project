package de.hda.fbi.ds.vs_prak;

import org.eclipse.paho.client.mqttv3.*;

import java.io.IOException;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class mqtt_Sensor {
    public static class DataMeasureUnit extends TimerTask {
        final float INITIAL_TEMP = 25.0F;
        final int INITIAL_HUMIDITY = 50;
        float temperature = INITIAL_TEMP;
        int humidity = INITIAL_HUMIDITY;
        Random r = new Random();

        @Override
        public void run() {
            int randomChange = r.nextInt(4);
            if (randomChange == 0) {
                if (temperature > -20) {
                    temperature = temperature - 0.5F;
                }
            } else if (randomChange == 1) {
                if (temperature < 45) {
                    temperature = temperature + 0.5F;
                }
            }

            randomChange = r.nextInt(4);
            if (randomChange == 0) {
                if (humidity > 0) {
                    humidity--;
                }
            } else if (randomChange == 1) {
                if (humidity < 100) {
                    humidity++;
                }
            }
        }

        public float getTemperature() {
            return temperature;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public static void exec() throws IOException, InterruptedException, MqttException {

        // Simulates Temperature in a simulated temperature unit
        DataMeasureUnit tempUnit = new DataMeasureUnit();

        // Step 1:Create the socket object for
        // carrying the data.

        String id = MqttClient.generateClientId();
        IMqttClient publisher = new MqttClient("tcp://mosquitto:1883", id);

        String TOPIC = "mqtt_sensordata";

        MqttConnectOptions options = new MqttConnectOptions();
        options.setAutomaticReconnect(true);
        options.setCleanSession(true);
        options.setConnectionTimeout(10);

        TimeUnit.SECONDS.sleep(10);

        System.out.println("MQTT_Sensor " +  id + " online");

        try {

            publisher.connect(options);
            if ( publisher.isConnected()) {
                System.out.println("MQTT_Sensor" + id + " connected");
            }


            while (true) {

                if ( !publisher.isConnected()) {
                    System.out.println("Retry Connect: MQTT_Sensor" + id);
                    publisher.connect(options);

                    if ( publisher.isConnected()) {
                        System.out.println("MQTT_Sensor" + id + " connected");
                    }
                }

                // Activates temperature unit in sensor that simulates temperature change
                tempUnit.run();
                float temperature = tempUnit.getTemperature();
                int humidity = tempUnit.getHumidity();
                String currentTemp = Float.toString(temperature);
                String currentHumidity = Integer.toString(humidity);

                String inp = "Temperature: " + currentTemp + " degree Celsius" + " | " + "Humidity: " + currentHumidity + "%";

                MqttMessage msg = new MqttMessage(inp.getBytes());

                msg.setQos(0);
                msg.setRetained(true);
                publisher.publish(TOPIC,msg);

                TimeUnit.SECONDS.sleep(3);

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