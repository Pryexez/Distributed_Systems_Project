package de.hda.fbi.ds.vs_prak.util;

import de.hda.fbi.ds.vs_prak.thrift.DatabaseServiceUpdate;
import de.hda.fbi.ds.vs_prak.thrift.Sensordata;
import org.apache.thrift.TException;

public class DatabaseServiceUpdateHandler implements DatabaseServiceUpdate.Iface {

    @Override
    public void update(int id, int sensorid, double temperature, double humidity, String date) throws TException {

        Sensordata update = new Sensordata(sensorid, temperature, humidity, date);
        String old = String.valueOf(DataList.Sensordatalist.get(id));
        DataList.Sensordatalist.set(id, update);

        System.out.println("Updated: " + old + " to " + DataList.Sensordatalist.get(id));
    }
}
