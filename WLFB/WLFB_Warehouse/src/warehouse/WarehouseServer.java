package warehouse;

import java.net.*;
import java.io.*;

public class WarehouseServer {
    public static void main(String[] args) throws IOException {

        ServerSocket WarehouserSocket = null;
        boolean listening = true;
        String WarehouseServerName = "Warehouse Server";
        int WarehouseServerNumber = 4545;

        int apples = 1000;
        int oranges = 1000;

      //constructor calls
        SharedWarehouseState ourSharedWarehouseStateObject =
            new SharedWarehouseState(apples, oranges);

        try {
            WarehouserSocket = new ServerSocket(WarehouseServerNumber);
        } catch (IOException e) {
            System.err.println("Could not start " + WarehouseServerName + " specified port.");
            System.exit(-1);
        }

        System.out.println(WarehouseServerName + " started successfully...");

        while (listening) {
            new WarehouseServerThread(WarehouserSocket.accept(), "WarehouseServerThread1", ourSharedWarehouseStateObject).start();
            new WarehouseServerThread(WarehouserSocket.accept(), "WarehouseServerThread2", ourSharedWarehouseStateObject).start();
            new WarehouseServerThread(WarehouserSocket.accept(), "WarehouseServerThread3", ourSharedWarehouseStateObject).start();
            new WarehouseServerThread(WarehouserSocket.accept(), "WarehouseServerThread4", ourSharedWarehouseStateObject).start();

            System.out.println("New " + WarehouseServerName + " threads started.");
        }

        WarehouserSocket.close();
    }
}
