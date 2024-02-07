package de.hda.fbi.ds.vs_prak;

import de.hda.fbi.ds.vs_prak.config.Configuration;
import de.hda.fbi.ds.vs_prak.config.ConfigurationManager;
import de.hda.fbi.ds.vs_prak.core.ServerListenerThread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class Server {

    private final static Logger LOGGER = LoggerFactory.getLogger(Server.class);
    public static void exec() {

        LOGGER.info("Server online!");

        ConfigurationManager.getInstance().loadConfigurationFile("app/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        ServerListenerThread serverListenerThread = null;
        try {
            serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.start();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}


