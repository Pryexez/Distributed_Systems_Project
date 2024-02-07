package de.hda.fbi.ds.vs_prak;

import java.io.*;
import java.net.*;

public class ServerConnection extends Thread{

    String data;
    final Socket socket = new Socket("server", 2424);
    final String datasend;
    final String sensorid;
    final String payloaddatatemp;
    final String payloaddatahumidity;
    final String timestamp;
    final String CRLF = "\n\r";
    final String path = "";

    final DatagramSocket ds;

    public ServerConnection(String unformateddata, DatagramSocket ds) throws IOException {
        this.ds = ds;
        data = unformateddata;
        int startindex;
        int endindex;
        endindex = data.indexOf(':');
        sensorid = data.substring(0, endindex);
        startindex = endindex + data.indexOf("Temperature: ") + 5;
        endindex = data.indexOf(" |");
        payloaddatatemp = data.substring(startindex, endindex);
        startindex = endindex + 13;
        endindex = data.indexOf("|", data.indexOf("|") + 1);
        payloaddatahumidity = data.substring(startindex, endindex);
        startindex = endindex;
        timestamp = data.substring(startindex + 2);

        this.datasend = URLEncoder.encode("sensor", "UTF-8") + "=" + URLEncoder.encode(sensorid, "UTF-8") + ";\n"
        + URLEncoder.encode("temperature", "UTF-8") + "=" + URLEncoder.encode(payloaddatatemp, "UTF-8") + ";\n"
                + URLEncoder.encode("humidity", "UTF-8") + "=" + URLEncoder.encode(payloaddatahumidity, "UTF-8") + ";\n"
        + URLEncoder.encode("timestamp", "UTF-8") + "=" + URLEncoder.encode(timestamp, "UTF-8");
    }

    @Override
    public void run() {

        byte buf[] = null;

        try {
        BufferedWriter wr = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8"));
        wr.write("POST " + path + " HTTP/1.1" + CRLF);
        wr.write("Content-Length: " + datasend.length() + CRLF);
        wr.write("Content-Type: application/x-www-form-urlencoded" + CRLF);
        wr.write(CRLF);

        wr.write(datasend);
        wr.flush();
        long startTime = System.nanoTime();

        BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        String line;
        int count = 0;
        while ((line = rd.readLine()) != null) {
            if(count == 0){
                long rttns = System.nanoTime() - startTime;
                Double rttms = (rttns * 1.0) / 1000000;

                System.out.println("RTT to Server: " + rttms + " ms");

                buf = String.valueOf(rttms).getBytes();

                DatagramPacket DpSendTest =
                        new DatagramPacket(buf, buf.length, InetAddress.getByName("host.docker.internal"), 1239);
                ds.send(DpSendTest);

            }
            System.out.println(line);
            count++;
        }

        wr.close();
        rd.close();


    }catch(Exception e){}
}
}
