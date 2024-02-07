package de.hda.fbi.ds.vs_prak;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;
import java.util.TimerTask;

public class udp_Sensor {
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

    public static void exec() throws IOException, InterruptedException {

        // Simulates Temperature in a simulated temperature unit
        DataMeasureUnit tempUnit = new DataMeasureUnit();

        // Step 1:Create the socket object for
        // carrying the data.
        DatagramSocket ds = new DatagramSocket(1237);

        byte[] receive = new byte[65535];
        byte[] buf;
        String sendinit = "add_me";
        buf = sendinit.getBytes();


        DatagramPacket DpSend = new DatagramPacket(buf, buf.length, InetAddress.getByName("gateway"), 1234);

        ds.send(DpSend);

        System.out.println("sent registration");

        buf = null;

        try {
            while (true) {

                // Activates temperature unit in sensor that simulates temperature change
                tempUnit.run();
                float temperature = tempUnit.getTemperature();
                int humidity = tempUnit.getHumidity();
                String currentTemp = Float.toString(temperature);
                String currentHumidity = Integer.toString(humidity);


                DpSend = new DatagramPacket(receive, receive.length);
                ds.receive(DpSend);


                String inp = "Temperature: " + currentTemp + " degree Celsius" + " | " + "Humidity: " + currentHumidity + "%";

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