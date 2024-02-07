package de.hda.fbi.ds.vs_prak;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

public class FlowRateMeasure extends Thread{

    final Adapter_Callback callback;
    final DatagramSocket ds;
    public int sentMessages;

    byte buf[];

    public FlowRateMeasure(Adapter_Callback callback, DatagramSocket ds) {
        this.callback = callback;
        this.ds = ds;
    }


    @Override
    public void run() {
        while (true) {
            sentMessages = 0;
            Float percentage;


            try {
                TimeUnit.SECONDS.sleep(60);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            percentage = ((float)sentMessages) / callback.received;

            String measureString = "MQTT_Adapter: Nachrichten erhalten: " + callback.received + "; Nachrichten gesendet: " + sentMessages + "; Durchsatz: " + percentage * 100 + " Prozent";

            callback.received = 0;
            buf = measureString.getBytes();

            DatagramPacket DpSendTest;
            try {
                DpSendTest = new DatagramPacket(buf, buf.length, InetAddress.getByName("host.docker.internal"), 1241);
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }

            try {
                ds.send(DpSendTest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
}
}