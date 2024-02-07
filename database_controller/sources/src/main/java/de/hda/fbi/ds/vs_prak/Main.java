package de.hda.fbi.ds.vs_prak;

import de.hda.fbi.ds.vs_prak.thrift.DatabaseServiceDelete;
import de.hda.fbi.ds.vs_prak.thrift.DatabaseServiceUpdate;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TMultiplexedProtocol;
import org.apache.thrift.transport.TSocket;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws TException {
        TSocket dbtransport = new TSocket("localhost",9999);
        Scanner sc = new Scanner(System.in);
        TBinaryProtocol protocol = new TBinaryProtocol(dbtransport);
        try{
        dbtransport.open();
        while (true){

            System.out.println("Operationauswahl\n" +
                    "1 für Update\n" +
                    "2 für Delete");

            int input = sc.nextInt();

            switch (input){
                case 1:
                    TMultiplexedProtocol mp = new TMultiplexedProtocol(protocol, "Update");
                    DatabaseServiceUpdate.Client service = new DatabaseServiceUpdate.Client(mp);
                    int id;
                    int sensorid;
                    double temperatur;
                    double humidity;
                    String date;

                    System.out.println("Eingabe: Database ID");
                    id = sc.nextInt();
                    System.out.println("Eingabe: Sensor ID");
                    sensorid = sc.nextInt();
                    System.out.println("Eingabe: Temperatur");
                    temperatur = sc.nextDouble();
                    System.out.println("Eingabe: Luftfeuchtigkeit");
                    humidity = sc.nextDouble();
                    System.out.println("Eingabe: Datum");
                    date = sc.next();


                    service.update(id,sensorid,temperatur,humidity,date);


                    break;
                case 2:
                    TMultiplexedProtocol mp2 = new TMultiplexedProtocol(protocol, "Delete");
                    DatabaseServiceDelete.Client service2 = new DatabaseServiceDelete.Client(mp2);
                    int id2;
                    System.out.println("Eingabe: Database ID");
                    id2 = sc.nextInt();


                    service2.remove(id2);


                    break;

                default:
                    System.out.println("Bitte gültige Eingabe!");
                    break;
            }}




        } catch (TException e) {
            e.printStackTrace();
        }finally {
            dbtransport.close();
        }

    }

}



