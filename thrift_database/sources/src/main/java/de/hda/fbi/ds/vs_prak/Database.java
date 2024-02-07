package de.hda.fbi.ds.vs_prak;

import de.hda.fbi.ds.vs_prak.thrift.*;
import de.hda.fbi.ds.vs_prak.util.*;
import org.apache.thrift.TMultiplexedProcessor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;
import org.apache.thrift.transport.TTransportException;

public class Database {

    public static void StartsDatabase() throws TTransportException {

        TMultiplexedProcessor processor = new TMultiplexedProcessor();

        processor.registerProcessor(
                "Create",
                new DatabaseServiceCreate.Processor(new DatabaseServiceCreateHandler())
        );
        processor.registerProcessor(
                "Read",
                new DatabaseServiceRead.Processor(new DatabaseServiceGetHandler())
        );
        processor.registerProcessor(
                "Update",
                new DatabaseServiceUpdate.Processor(new DatabaseServiceUpdateHandler())
        );
        processor.registerProcessor(
                "Delete",
                new DatabaseServiceDelete.Processor(new DatabaseServiceDeleteHandler())
        );

        processor.registerProcessor(
                "Check",
                new CheckAvailable.Processor(new CheckAvailableHandler())
        );

        TServerTransport serverTransport = new TServerSocket(9987);

        TServer database = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));

        System.out.println("Datenbank gestartet");
        database.serve();

    }
}
