
package de.hda.fbi.ds.vs_prak;


import java.io.IOException;

public class Main_calculator {



    public static void main(String[] args) throws InterruptedException, IOException {



        RTTmeasureSensor rttmeasurer = new RTTmeasureSensor();
        RTTmeasureServer rtTmeasureServer = new RTTmeasureServer();
        FlowrateMeasureMQTT flowrateMeasureMQTT = new FlowrateMeasureMQTT();
        //RTTmeasureDatabase rtTmeasureDatabase = new RTTmeasureDatabase();

        // Run the UDP socket server.
        rttmeasurer.start();
        rtTmeasureServer.start();
        flowrateMeasureMQTT.start();
        //rtTmeasureDatabase.start();

    }

}
