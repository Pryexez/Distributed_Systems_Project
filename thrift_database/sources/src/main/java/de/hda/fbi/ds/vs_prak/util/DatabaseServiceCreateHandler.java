package de.hda.fbi.ds.vs_prak.util;

import de.hda.fbi.ds.vs_prak.thrift.DatabaseServiceCreate;
import de.hda.fbi.ds.vs_prak.thrift.Sensordata;
import org.apache.thrift.TException;

public class DatabaseServiceCreateHandler implements DatabaseServiceCreate.Iface {

    @Override
    public void create(int sensorid, double temperature, double humidity, String date) throws TException {
        Sensordata newdata = new Sensordata(sensorid, temperature, humidity, date);

        if(DataList.Sensordatalist.size() == 10000) {
            DataList.Sensordatalist.remove(0);
        }

        DataList.Sensordatalist.add(newdata);
        System.out.println("Database create: " + newdata);

    }
}
