package de.hda.fbi.ds.vs_prak.util;

import de.hda.fbi.ds.vs_prak.thrift.DatabaseServiceDelete;
import org.apache.thrift.TException;

public class DatabaseServiceDeleteHandler implements DatabaseServiceDelete.Iface {
    @Override
    public void remove(int id) throws TException {
        String deleted = String.valueOf(DataList.Sensordatalist.get(id));
        DataList.Sensordatalist.remove(id);

        System.out.println("Deleted: " + deleted);

    }
}
