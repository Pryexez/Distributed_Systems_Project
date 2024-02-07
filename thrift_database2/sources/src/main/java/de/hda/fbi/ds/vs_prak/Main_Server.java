package de.hda.fbi.ds.vs_prak;

import org.apache.thrift.transport.TTransportException;

public class Main_Server {

    public static void main(String[] args) throws TTransportException {
        Database db = new Database();
        db.StartsDatabase();
    }

}



