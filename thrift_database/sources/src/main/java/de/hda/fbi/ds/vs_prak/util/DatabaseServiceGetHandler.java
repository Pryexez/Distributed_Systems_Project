package de.hda.fbi.ds.vs_prak.util;

import de.hda.fbi.ds.vs_prak.thrift.DatabaseServiceRead;
import de.hda.fbi.ds.vs_prak.thrift.Sensordata;
import org.apache.thrift.TException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DatabaseServiceGetHandler implements DatabaseServiceRead.Iface {
    @Override
    public List<Sensordata> read() throws TException {
        ArrayList<Sensordata> unreverseddata = DataList.Sensordatalist;
        Collections.reverse(unreverseddata);
        ArrayList<Sensordata> data = new ArrayList<>();

        if(unreverseddata.size() < 20) {
            for (int i = 0; i < unreverseddata.size(); i++) {
                data.add(unreverseddata.get(i));
            }
        }
        else {
            for (int i = 0; i < 20; i++) {
                data.add(unreverseddata.get(i));
            }
        }

        return data;
    }
}

