import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class EDR_Server_Impl extends UnicastRemoteObject implements EDR_Server {
    private static final long serialVersionUID = 7915803477888497141L;  
    private EventQueue eventQueue;
    private long firstGeneralTimestamp;
    private long latestStopTimestamp;

    public EDR_Server_Impl() throws RemoteException {
        eventQueue = new EventQueue();
        firstGeneralTimestamp = System.currentTimeMillis() / 1000;
        latestStopTimestamp = firstGeneralTimestamp;
    }

    @Override
    public long getGeneralTimestamp() throws RemoteException {
        long currentTime = System.currentTimeMillis() / 1000;
        return currentTime - firstGeneralTimestamp;
    }

    @Override
    public long getLatestStopTimestamp() throws RemoteException {
        return latestStopTimestamp;
    }

    @Override
    public void setLatestStopTimestamp(long timestamp) throws RemoteException {
        this.latestStopTimestamp = timestamp;
    }

    @Override
    public void addEvent(Event event) throws RemoteException {
        eventQueue.enqueue(event);
    }

    @Override
    public int handleEvent(Event event) throws RemoteException {
        System.out.println();
        switch (event.getType()) {
            case Event.START:
                System.out.println(event.getCarID() + ", START, " + getGeneralTimestamp() + " sec");
                return 0;
            case Event.STOP:
                System.out.println(event.getCarID() + ", STOP, " + getGeneralTimestamp() + " sec");
                latestStopTimestamp = getGeneralTimestamp();
                return 0;
            case Event.RESUME:
                System.out.println(event.getCarID() + ", RESUME, " + getGeneralTimestamp() + " sec");
                return isStopSignViolation(event);
            case Event.ON_LEFT:
                System.out.println(event.getCarID() + ", + LEFT, " + event.getLeftImage() + ", " + getGeneralTimestamp() + " sec");
                return isTooCloseLeft(event);
            case Event.ON_RIGHT:
                System.out.println(event.getCarID() + ", + RIGHT, " + event.getRightImage() + ", " + getGeneralTimestamp() + " sec");
                return isTooCloseRight(event);
            case Event.GONE_LEFT:
                System.out.println(event.getCarID() + ", - LEFT, " + " " + getGeneralTimestamp() + " sec");
                return 0;
            case Event.GONE_RIGHT:
                System.out.println(event.getCarID() + ", - RIGHT, " + " " + getGeneralTimestamp() + " sec");
                return 0;
            default:
                return 2;
        }
    }

    @Override
    public int isStopSignViolation(Event event) throws RemoteException {
        if ((getGeneralTimestamp() - latestStopTimestamp) < 4) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public int isTooCloseLeft(Event event) throws RemoteException {
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
