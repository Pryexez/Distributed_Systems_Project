package de.hda.fbi.ds.vs_prak.core;

import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerListenerThread extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);
    private int port;
    private String webroot;
    private ServerSocket serverSocket;

    private TSocket dbtransport;

    private TSocket dbtransport2;




    public ServerListenerThread(int port, String webroot, TSocket dbtransport, TSocket dbtransport2) throws IOException, TTransportException {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
        this.dbtransport = dbtransport;
        this.dbtransport2 = dbtransport2;
    }

    @Override
    public void run() {

        try {

            while ( serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                LOGGER.info(" * Connection accepted: " + socket.getInetAddress());

                HttpConnectionWorker httpConnectionWorker = new HttpConnectionWorker(socket, dbtransport, dbtransport2);
                httpConnectionWorker.start();

            }

        } catch (IOException e) {
           e.printStackTrace();
           LOGGER.error("Problem with setting socket", e);
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        } finally {
            if(serverSocket!=null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                }
            }
        }

    }

}

