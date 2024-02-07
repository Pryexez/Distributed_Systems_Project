package de.hda.fbi.ds.vs_prak;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

public class FlowrateMeasureMQTT extends Thread {
    @Override
    public void run() {

        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(1241);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        byte[] receive = new byte[65535];
        DatagramPacket DpReceive = null;

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        while (true) {

                try {
                    DpReceive = new DatagramPacket(receive, receive.length);

                    ds.receive(DpReceive);

                    System.out.println(String.valueOf(data(receive)));
                    System.out.println("______________________________________________________________________________________");

                    receive = new byte[65535];

                } catch (IOException se) {
                    throw new RuntimeException(se);
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

