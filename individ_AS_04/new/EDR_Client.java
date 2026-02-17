import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.Random;

public class EDR_Client {
    public static void main(String[] argv) {     
        if (argv.length == 0) {        
            System.out.println("Start the client:");        
            System.out.println("java WeatherForecastClient serve_host 18510");        
            return;     
        }     
        String host = argv[0];     
        String car_id = argv[1];
        try {            
            String[] registeredOperations = Naming.list("//" + host);       
            for (int i = 0; i < registeredOperations.length; i++) {          
                System.out.println(registeredOperations[i]);       
            }            
            EDR_Server edr = (EDR_Server) Naming.lookup("//" + host + "/EDR");   
            System.out.println("Connected to server.\n");
            runEventSequence(edr, car_id);
        } catch (Exception e) { 
            System.out.println("EDRClient exception: " + e);
            System.out.println("EDRClient exception: " + e.getMessage());
        } 
    }

    // Sample event sequence to be run
    public static void runEventSequence(EDR_Server edr, String car_id) {
        Random random = new Random();
        runEvent(car_id, Event.START, edr);
        for (int i = 0; i <= 10; i++) {
            int randNumber = random.nextInt(3);
            if (randNumber == 0) { // stop car
                runEvent(car_id, Event.STOP, edr);
                waitingInterval();
                runEvent(car_id, Event.RESUME, edr);
            } else if (randNumber == 1) { // left 
                runEvent(car_id, Event.ON_LEFT, edr);
                waitingInterval();
                runEvent(car_id, Event.GONE_LEFT, edr);
            } else { // right
                runEvent(car_id, Event.ON_RIGHT, edr);
                waitingInterval();
                runEvent(car_id, Event.GONE_RIGHT, edr);
            }
            waitingInterval();
        }
    }

    // Simulate the time between events
    public static void waitingInterval() {
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(10) * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Methods that ARE NOT specific to an event type

    public static void runEvent(String car_id, int eventType, EDR_Server edr) {
        Event event = new Event(car_id, eventType);
        switch (eventType) {
            case (Event.START):
                start(car_id, event, edr);
                break;
            case (Event.STOP):
                stop(car_id, event, edr);
                break;
            case (Event.RESUME):
                resume(car_id, event, edr);
                break;
            case (Event.ON_LEFT):
                onLeft(car_id, event, edr);
                break;
            case (Event.ON_RIGHT):
                onRight(car_id, event, edr);
                break;
            case (Event.GONE_LEFT):
                goneLeft(car_id, event, edr);
                break;
            case (Event.GONE_RIGHT):
                goneRight(car_id, event, edr);
                break;
        }
        enqueueEvent(event, edr);
    }

    public static void enqueueEvent(Event event, EDR_Server edr) {
        try {
            edr.addEvent(event);
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        }
    }

    public static void handleEvent(Event event, EDR_Server edr, String successMsg, String failureMsg) {
        try {
            if (edr.handleEvent(event) == 0) {
                System.out.println(successMsg);
            } else {
                System.out.println(failureMsg);
            }
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        }
        System.out.println();
    }

    // Methods that ARE specific to an event type

    public static void start(String car_id, Event event, EDR_Server edr) {
        try {
            System.out.println(car_id + ", START, " + edr.getGeneralTimestamp() + " sec");
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        }
        handleEvent(event, edr, "Recorded", "Failure");
    }

    public static void stop(String car_id, Event event, EDR_Server edr) {
        try {
            System.out.println(car_id + ", STOP, " + edr.getGeneralTimestamp() + " sec");
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        }
        handleEvent(event, edr, "Recorded", "Failure");
    }

    public static void resume(String car_id, Event event, EDR_Server edr) {
        try {
            System.out.println(car_id + ", RESUME, " + edr.getGeneralTimestamp() + " sec");
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        }
        handleEvent(event, edr, "Recorded", "Stop sign violation");
    }

    public static void onLeft(String car_id, Event event, EDR_Server edr) {
        try {
            System.out.println(car_id + ", + LEFT, " + event.getLeftImage() + ", " + edr.getGeneralTimestamp() + " sec");
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        }
        handleEvent(event, edr, "ORANGE -- close", "RED -- too close");
    }

    public static void onRight(String car_id, Event event, EDR_Server edr) {
        try {
            System.out.println(car_id + ", + RIGHT, " + event.getRightImage() + ", " + edr.getGeneralTimestamp() + " sec");
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        }
        handleEvent(event, edr, "ORANGE -- close", "RED -- too close");
    }

    public static void goneLeft(String car_id, Event event, EDR_Server edr) {
        try {
            System.out.println(car_id + ", - LEFT, " + " " + edr.getGeneralTimestamp() + " sec");
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        }
        handleEvent(event, edr, "Recorded", "Failed");
    }

    public static void goneRight(String car_id, Event event, EDR_Server edr) {
        try {
            System.out.println(car_id + ", - RIGHT, " + " " + edr.getGeneralTimestamp() + " sec");
        } catch (RemoteException e) {
            System.out.println("RemoteException: " + e);
        }
        handleEvent(event, edr, "Recorded", "Failed");
    }
}
