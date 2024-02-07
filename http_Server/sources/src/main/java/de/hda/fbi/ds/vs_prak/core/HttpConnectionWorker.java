package de.hda.fbi.ds.vs_prak.core;

import de.hda.fbi.ds.vs_prak.thrift.CheckAvailable;
import de.hda.fbi.ds.vs_prak.thrift.DatabaseServiceCreate;
import de.hda.fbi.ds.vs_prak.thrift.DatabaseServiceRead;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLDecoder;

public class HttpConnectionWorker extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(HttpConnectionWorker.class);
    private Socket socket;

    final TSocket dbtransport;
    final TSocket dbtransport2;



    public HttpConnectionWorker(Socket socket, TSocket dbtransport, TSocket dbtransport2) throws TTransportException, SocketException {
        this.socket = socket;
        this.dbtransport = dbtransport;
        this.dbtransport2 = dbtransport2;


    }

    @Override
    public void run() {

        TBinaryProtocol protocol = new TBinaryProtocol(dbtransport);
        TBinaryProtocol protocol2 = new TBinaryProtocol(dbtransport2);
        InputStream inputStream = null;
        OutputStream outputStream = null;
        final String CRLF = "\n\r";
        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            InputStream is = socket.getInputStream();
            InputStreamReader isReader = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isReader);

            String headerLine = null;

            String method = "";
            int firstline = 1;

            while((headerLine = br.readLine()).length() != 0){
                if(firstline > 0){
                    int index = headerLine.indexOf(' ');
                    method = headerLine.substring(0, index);
                    firstline--;
                }
                //System.out.println(headerLine);
            }

            System.out.println("Method: " + method);

            if(method.equals("GET")){

                TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "Read");
                DatabaseServiceRead.Client service = new DatabaseServiceRead.Client(mp);



                String htmllist = "";
                Double rttms = 0.0;
                long startTime = System.nanoTime();
                for(int i = service.read().size() - 1; i > 0; i--) {
                    if(i == service.read().size() - 1){
                        long rttns = System.nanoTime() - startTime;
                        rttms = (rttns * 1.0) / 1000000;
                    }

                    htmllist += "<li>" + "Sensor: " + service.read().get(i).sensorid + " | Temperatur: " +  service.read().get(i).temperature + " Grad C | Luftfeuchtigkeit: "
                            + service.read().get(i).humidity + " % | Zeitstempel: " + service.read().get(i).date + "</li>";
                }



                String html = "<html><head><title>Simple Java Http</title></head><body><h1>Sensor data:</h1><ul>" + htmllist + "</ul><p>Response Time: " + rttms + "</p></body></html>";

                String response =
                        "HTTP/1.1 200 OK" + CRLF +
                                "Content-Length: " + html.getBytes().length + CRLF +
                                CRLF +
                                html +
                                CRLF + CRLF;

                outputStream.write(response.getBytes());


            } else if(method.equals("POST")){

                StringBuilder payload = new StringBuilder();
                while(br.ready()){
                    payload.append((char) br.read());
                }

                payload = new StringBuilder(URLDecoder.decode(String.valueOf(payload), "UTF8"));
                payload.replace(0, payload.indexOf("sensor"), "");

                //SensorData.addSensordatalist(payload.toString());

                System.out.println("Payload data is: "+payload.toString());


                int id = Integer.parseInt(payload.substring(14,15));
                String temperatureString = payload.substring(29,33);

                double temperature = Double.parseDouble(temperatureString);

                String humidityString = payload.substring(59,61);

                if(humidityString.contains("%")){
                    humidityString.replace("%","");
                }

                double humidity = Double.parseDouble(humidityString);


                String date = payload.substring(75);


                boolean databaseAvailable = false;
                boolean databaseAvailable2 = false;

                TMultiplexedProtocol mpA = new TMultiplexedProtocol(protocol, "Check");
                TMultiplexedProtocol mpA2 = new TMultiplexedProtocol(protocol2, "Check");
                CheckAvailable.Client serviceA = new CheckAvailable.Client(mpA);
                CheckAvailable.Client serviceA2 = new CheckAvailable.Client(mpA2);



                databaseAvailable = serviceA.isAvailable();



                databaseAvailable2 = serviceA2.isAvailable();

                if(databaseAvailable && databaseAvailable2)
                {

                    TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "Create");
                    TMultiplexedProtocol mp2 = new TMultiplexedProtocol(protocol2, "Create");
                    DatabaseServiceCreate.Client service = new DatabaseServiceCreate.Client(mp);
                    DatabaseServiceCreate.Client service2 = new DatabaseServiceCreate.Client(mp2);


                    service.create(id, temperature, humidity, date);



                    service2.create(id, temperature, humidity, date);


                }



                final String answer = "received: " + payload;

                String response =
                        "HTTP/1.1 200 OK" + CRLF +
                                "Content-Length: " + answer.getBytes().length + CRLF +
                                CRLF +
                                answer +
                                CRLF + CRLF;

                outputStream.write(response.getBytes());

            }

            LOGGER.info("Connection Processing finished");
        } catch (IOException e) {
            LOGGER.error("Problem with communication", e);
            e.printStackTrace();

        } catch (TException e) {
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                }
            }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                }
            }
        }
    }
}
