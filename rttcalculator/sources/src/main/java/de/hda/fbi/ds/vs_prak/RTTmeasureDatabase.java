package de.hda.fbi.ds.vs_prak;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.TimeUnit;

public class RTTmeasureDatabase extends Thread {
    @Override
    public void run() {

        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(1240);
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
            double rttquote = 0;
            int count = 0;
            long starttime = System.currentTimeMillis();

            while(System.currentTimeMillis() - starttime < 60000) {
                try {
                    DpReceive = new DatagramPacket(receive, receive.length);

                    ds.receive(DpReceive);

                    count++;

                    rttquote += Double.valueOf(String.valueOf(data(receive)));

                    receive = new byte[65535];

                } catch (IOException se) {
                    throw new RuntimeException(se);
                }
            }
            System.out.println("Response Time Durchschnitt pro Minute fuer Datenbank: " + (rttquote / count) + " ms");
            System.out.println("______________________________________________________________________________________");
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

