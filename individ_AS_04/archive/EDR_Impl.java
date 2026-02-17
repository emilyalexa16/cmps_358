import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EDR_Impl extends UnicastRemoteObject implements EDR {

    private EventQueue eventQueue;
    private long latestStopTimestamp;

    public EDR_Impl() throws RemoteException {
        eventQueue = new EventQueue();
        latestStopTimestamp = 0;
    }

    @Override
    public EventQueue getEventQueue() throws RemoteException {
        return eventQueue;
    }

    @Override
    public long getLatestStopTimestamp() {
        return latestStopTimestamp;
    }

    @Override
    public void setEventQueue(EventQueue eventQueue) {
        this.eventQueue = eventQueue;
    }

    @Override
    public void setLatestStopTimestamp(long timestamp) {
        this.latestStopTimestamp = timestamp;
    }

    @Override
    public void addEvent(int car_id, int type) {
        Event event = new Event(car_id, type);
        eventQueue.enqueue(event);
    }

    @Override
    public int handleEvent(Event event) {
        switch (event.getType()) {
            case Event.START:
                System.out.println(event.getCarID() + ", START, " + event.getTimestamp());
                return 0;
            case Event.STOP:
                System.out.println(event.getCarID() + ", STOP, " + event.getTimestamp());
                latestStopTimestamp = event.getTimestamp();
                return 0;
            case Event.RESUME:
                System.out.println(event.getCarID() + ", RESUME, " + event.getTimestamp());
                return isStopSignViolation(event);
            case Event.ON_LEFT:
                System.out.println(event.getCarID() + ", + LEFT, " + event.getLeftImage() + ", " + event.getTimestamp());
                return isTooCloseLeft(event);
            case Event.ON_RIGHT:
                System.out.println(event.getCarID() + ", + RIGHT, " + event.getRightImage() + ", " + event.getTimestamp());
                return isTooCloseRight(event);
            case Event.GONE_LEFT:
                System.out.println(event.getCarID() + ", - LEFT, " + " " + event.getTimestamp());
                return 0;
            case Event.GONE_RIGHT:
                System.out.println(event.getCarID() + ", - RIGHT, " + " " + event.getTimestamp());
                return 0;
            default:
                return 2;
        }
    }

    @Override
    public int isStopSignViolation(Event event) {
        if ((event.getTimestamp() - latestStopTimestamp) < 4) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int isTooCloseLeft(Event event) {
        if (event.getLeftProximity() > 70) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int isTooCloseRight(Event event) {
        if (event.getRightProximity() > 70) {
            return 1;
        } else {
            return 0;
        }
    }

}
