package de.hda.fbi.ds.vs_prak.util;

import de.hda.fbi.ds.vs_prak.thrift.CheckAvailable;
import org.apache.thrift.TException;

public class CheckAvailableHandler implements CheckAvailable.Iface {

    @Override
    public boolean isAvailable() throws TException {
        System.out.println("Database2 is available");
        return true;
    }
}
