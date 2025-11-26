package warehouse;

public class SharedWarehouseState {

    private SharedWarehouseState mySharedObj;
    private String myThreadName;
    private double mySharedVariable;
    private boolean accessing = false;
    private int threadsWaiting = 0;

    // Added missing fields
    private int apples;
    private int oranges;

    // Constructor
    public SharedWarehouseState(int initapples, int initoranges) {
        this.apples = initapples;
        this.oranges = initoranges;
    }

    public synchronized void acquireLock() throws InterruptedException {
        Thread me = Thread.currentThread();
        System.out.println(me.getName() + " is attempting to acquire a lock!");

        ++threadsWaiting;

        while (accessing) {
            System.out.println(me.getName() + " waiting to get a lock as someone else is accessing...");
            wait();
        }

        --threadsWaiting;
        accessing = true;
        System.out.println(me.getName() + " got a lock!");
    }

    public synchronized void releaseLock() {
        accessing = false;
        notifyAll();

        Thread me = Thread.currentThread();
        System.out.println(me.getName() + " released a lock!");
    }

    public synchronized String processInput(String myThreadName, String theInput) {

        System.out.println(myThreadName + " received " + theInput);

        String theOutput;

        if (theInput.equalsIgnoreCase("CHECK")) {
            theOutput = "Apples = " + apples + ", Oranges = " + oranges;
        }

        //ADD_APPLES
        else if (theInput.startsWith("ADD_APPLES")) {
            String[] words = theInput.split(" ");
            int amount = Integer.parseInt(words[1]);
            if (amount < 0) {
                theOutput = "OUT_OF_STOCK";
            } 
            else {
                apples += amount;
                theOutput = "Added " + amount + " apples. Total = " + apples;
            }
        }

        // ADD_ORANGES 
        else if (theInput.startsWith("ADD_ORANGES")) {
            String[] words = theInput.split(" ");
            int amount = Integer.parseInt(words[1]);
            oranges += amount;
            theOutput = "Added " + amount + " oranges. Total = " + oranges;
        }

        // BUY_APPLES 
        else if (theInput.startsWith("BUY_APPLES")) {
            String[] words = theInput.split(" ");
            int amount = Integer.parseInt(words[1]);
            apples -= amount;
            theOutput = "Bought " + amount + " apples. Total = " + apples;
        }

        // BUY_ORANGES 
        else if (theInput.startsWith("BUY_ORANGES")) {
            String[] words = theInput.split(" ");
            int amount = Integer.parseInt(words[1]);
            oranges -= amount;
            theOutput = "Bought " + amount + " oranges. Total = " + oranges;
        }
        else {
        	theOutput = "ERROR";
        }
        
        System.out.println(theOutput);
        return theOutput;
    }
}
