package warehouse;

import java.net.*;
import java.io.*;

public class WarehouseServerThread extends Thread {

    private Socket ServerSocket = null;
    private SharedWarehouseState mySharedWarehouseStateObject;
    private String myWarehouseThreadName;

    public WarehouseServerThread(Socket warehouseSocket, String WarehouseServerThreadName, SharedWarehouseState SharedObject) {

        super(WarehouseServerThreadName);

        // FIXED — correct socket assignment
        this.ServerSocket = warehouseSocket;

        this.mySharedWarehouseStateObject = SharedObject;
        this.myWarehouseThreadName = WarehouseServerThreadName;
    }

    public void run() {
        try {
            System.out.println(myWarehouseThreadName + " initialising.");

            PrintWriter out = new PrintWriter(ServerSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(ServerSocket.getInputStream()));

            String inputLine;

            while ((inputLine = in.readLine()) != null) {

                try {
                    mySharedWarehouseStateObject.acquireLock();
                    String outputLine = mySharedWarehouseStateObject.processInput(myWarehouseThreadName, inputLine);
                    out.println(outputLine);
                    mySharedWarehouseStateObject.releaseLock();
                }
                catch (InterruptedException e) {
                    System.err.println("Failed to get lock when reading: " + e);
                }
            }

            out.close();
            in.close();
            ServerSocket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
