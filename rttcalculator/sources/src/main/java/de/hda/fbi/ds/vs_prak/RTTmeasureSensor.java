package de.hda.fbi.ds.vs_prak;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class RTTmeasureSensor extends Thread {
    @Override
    public void run() {

        DatagramSocket ds = null;
        try {
            ds = new DatagramSocket(1236);
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        byte[] receive = new byte[65535];
        DatagramPacket DpReceive = null;

        while (true) {
            double rttquotesensor = 0;
            int countsensor = 0;
            long starttime = System.currentTimeMillis();

            while(System.currentTimeMillis() - starttime < 60000) {
                try {
                    DpReceive = new DatagramPacket(receive, receive.length);

                    ds.receive(DpReceive);

                    countsensor++;

                    rttquotesensor += Double.valueOf(String.valueOf(data(receive)));


                    receive = new byte[65535];

                } catch (IOException se) {
                    throw new RuntimeException(se);
                }
            }
            System.out.println("Round Trip Time Durchschnitt pro Minute fuer Sensoren: " + (rttquotesensor / countsensor) + " ms");
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

