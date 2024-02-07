package de.hda.fbi.ds.vs_prak;

import de.hda.fbi.ds.vs_prak.config.Configuration;
import de.hda.fbi.ds.vs_prak.config.ConfigurationManager;
import de.hda.fbi.ds.vs_prak.core.ServerListenerThread;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Server {

    private final static Logger LOGGER = LoggerFactory.getLogger(Server.class);
    public Server() throws TTransportException {
    }

    public static void exec() throws TTransportException {

        final TSocket dbtransport = new TSocket("database",9987);
        final TSocket dbtransport2 = new TSocket("database2",9997);
        LOGGER.info("Server online!");

        dbtransport.open();
        dbtransport2.open();

        ConfigurationManager.getInstance().loadConfigurationFile("app/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        ServerListenerThread serverListenerThread = null;
        try {
            serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot(),dbtransport, dbtransport2);
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }


    }
}



