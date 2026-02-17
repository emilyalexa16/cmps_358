import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;

public class EDR_Client {
    public static void main(String[] argv) {
        if (argv.length == 0) {
            System.out.println("Start the client:");
            System.out.println("java EDR_Client [server_host] [car_id]");
            return;
        }
        String host = argv[0];
        int car_id = Integer.parseInt(argv[1]);
        try {
            String[] registeredOperations = Naming.list("//" + host);       
            for (int i = 0; i < registeredOperations.length; i++) {          
                System.out.println(registeredOperations[i]);       
            } 
            EDR edr = (EDR) Naming.lookup("//" + host + "/EDR");
            System.out.println("Connected");

            Random rand = new Random();
            generateEventSequence(edr, car_id, rand);
            executeEventSequence(edr, car_id, rand);

        } catch (Exception e) {
            System.out.println("EDR_Client exception: " + e);
        }
    }

    public static void generateEventSequence(EDR edr, int car_id, Random rand) {
        edr.addEvent(car_id, Event.START);
        for (int i = 0; i <= 10; i++) {
            int randNumber = rand.nextInt(3);
            if (randNumber == 0) { // stop car
                edr.addEvent(car_id, Event.STOP);
                edr.addEvent(car_id, Event.RESUME);
            } else if (randNumber == 1) { // left 
                edr.addEvent(car_id, Event.ON_LEFT);
                edr.addEvent(car_id, Event.GONE_LEFT);
            } else { // right
                edr.addEvent(car_id, Event.ON_RIGHT);
                edr.addEvent(car_id, Event.GONE_RIGHT);
            }
        }
    }

    public static void executeEventSequence(EDR edr, int car_id, Random rand) {
        try {
            EventQueue queue = edr.getEventQueue();
            while (queue.peek() != null) {
                if (queue.peek().getCarID() == car_id) {
                    Event event = queue.dequeue();
                    switch (event.getType()) {
                        case Event.START:
                            handleStart(edr, event);
                        case Event.STOP:
                            handleStop(edr, event);
                        case Event.RESUME:
                            handleResume(edr, event);
                        case Event.ON_LEFT:
                            handleOnLeft(edr, event);
                        case Event.ON_RIGHT:
                            handleOnRight(edr, event);
                        case Event.GONE_LEFT:
                            handleGoneLeft(edr, event);
                        case Event.GONE_RIGHT:
                            handleGoneRight(edr, event);
                        default:
                            System.out.println("Nothing");
                    }
                    int randTime = rand.nextInt(6);
                    try {
                        Thread.sleep(randTime * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        }
    }

    public static void handleStart(EDR edr, Event event) {
        System.out.println(event.getCarID() + ", START, " + event.getTimestamp());
        if (edr.handleEvent(event) == 0) {
            System.out.println("Recorded");
        } else {
            System.out.println("Failed");
        }
    }

    public static void handleStop(EDR edr, Event event) {
        System.out.println(event.getCarID() + ", STOP, " + event.getTimestamp());
        edr.setLatestStopTimestamp(event.getTimestamp());
        if (edr.handleEvent(event) == 0) {
            System.out.println("Recorded");
        } else {
            System.out.println("Failed");
        }
    }

    public static void handleResume(EDR edr, Event event) {
        System.out.println(event.getCarID() + ", RESUME, " + event.getTimestamp());
        if (edr.handleEvent(event) == 0) {
            System.out.println("Recorded");
        } else {
            System.out.println("Stop sign violation");
        }
    }

    public static void handleOnLeft(EDR edr, Event event) {
        System.out.println(event.getCarID() + ", + LEFT, " + event.getLeftImage() + ", " + event.getTimestamp());
        if (edr.handleEvent(event) == 0) {
            System.out.println("ORANGE - close");
        } else {
            System.out.println("RED - too close");
        }
    }

    public static void handleOnRight(EDR edr, Event event) {
        System.out.println(event.getCarID() + ", + RIGHT, " + event.getRightImage() + ", " + event.getTimestamp());
        if (edr.handleEvent(event) == 0) {
            System.out.println("ORANGE - close");
        } else {
            System.out.println("RED - too close");
        }
    }

    public static void handleGoneLeft(EDR edr, Event event) {
        System.out.println(event.getCarID() + ", - LEFT, " + " " + event.getTimestamp());
        if (edr.handleEvent(event) == 0) {
            System.out.println("Recorded");
        } else {
            System.out.println("Failed");
        }
    }

    public static void handleGoneRight(EDR edr, Event event) {
        System.out.println(event.getCarID() + ", - RIGHT, " + " " + event.getTimestamp());
        if (edr.handleEvent(event) == 0) {
            System.out.println("Recorded");
        } else {
            System.out.println("Failed");
        }
    }
}
