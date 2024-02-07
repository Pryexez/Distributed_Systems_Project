package de.hda.fbi.ds.vs_prak;

import org.apache.thrift.transport.TTransportException;

import java.util.concurrent.TimeUnit;

public class Main_Server {

    public static void main(String[] args) throws TTransportException, InterruptedException {

        TimeUnit.SECONDS.sleep(10);
        Server server = new Server();
        server.exec();




    }

}



