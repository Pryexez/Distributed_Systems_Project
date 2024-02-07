package de.hda.fbi.ds.vs_prak;

import java.io.IOException;
import java.net.*;
import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.TimeUnit;


public class IoT_Gateway {

    public void exec() throws SocketException, UnknownHostException {

        int count = 0;
        DatagramSocket ds = new DatagramSocket(1234);
        byte[] receive = new byte[65535];
        byte buf[] = null;

        System.out.println("Gateway online!");
        while (true) {
            try {

                DatagramPacket DpReceive = new DatagramPacket(receive, receive.length);
                ds.receive(DpReceive);

                if (String.valueOf(data(receive)).equals("add_me")) {
                    count++;
                    System.out.println(data(receive));
                    Thread sensorRunner = new SensorRunner(DpReceive, count);
                    sensorRunner.start();
                }
                receive = new byte[65535];

            } catch (SocketException se) {
                throw new RuntimeException(se);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }

    // A utility method to convert the byte array
    // data into a string representation.
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

class SensorHandler extends Thread {
    final String data;
    final Double rttms;

    final DatagramSocket ds;

    byte buf[] = null;
    final int id;

    // Constructor
    public SensorHandler(String data, Double rttms, DatagramSocket ds, int id) {
        this.data = data;
        this.rttms = rttms;
        this.ds = ds;
        this.id = id;
    }


    @Override
    public void run() {
        try {

            Date date = new Date();
            Timestamp timestamp = new Timestamp(date.getTime());

            String finisheddata = "Sensor " + id + ":-" + data + " | " + timestamp;

            System.out.println(finisheddata);
            System.out.println("RTT: " + rttms + " ms");

            buf = String.valueOf(rttms).getBytes();

            DatagramPacket DpSendTest =
                    new DatagramPacket(buf, buf.length, InetAddress.getByName("host.docker.internal"), 1236);

            ds.send(DpSendTest);

            ServerConnection servercon = new ServerConnection(finisheddata, ds);
            servercon.start();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class SensorRunner extends Thread {

    final String request = "send data please";
    final DatagramPacket sensor;
    final DatagramSocket gateway = new DatagramSocket();
    final int id;
    byte buf[] = null;
    byte[] receive = new byte[65535];


    // Constructor
    public SensorRunner(DatagramPacket dp, int count) throws SocketException {
        this.sensor = dp;
        this.id = count;

    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            try {

                long startTime = System.nanoTime();
                buf = request.getBytes();

                DatagramPacket DpSend =
                        new DatagramPacket(buf, buf.length, sensor.getAddress(), sensor.getPort());

                gateway.send(DpSend);

                DatagramPacket DpReceive = new DatagramPacket(receive, receive.length);
                gateway.receive(DpReceive);


                long rttns = System.nanoTime() - startTime;
                Double rttms = (rttns * 1.0) / 1000000;

                String data = String.valueOf(data(receive));

                Thread sensorthread = new SensorHandler(data, rttms, gateway, id);

                sensorthread.start();

                TimeUnit.SECONDS.sleep(3);

                receive = new byte[65535];

            } catch (SocketException | InterruptedException se) {
                throw new RuntimeException(se);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

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
