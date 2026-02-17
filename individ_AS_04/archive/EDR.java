import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EDR extends Remote { 
    public EventQueue getEventQueue() throws RemoteException;
    public long getLatestStopTimestamp();
    public void setEventQueue(EventQueue eventQueue);
    public void setLatestStopTimestamp(long timestamp);
    public void addEvent(int car_id, int type);
    public int handleEvent(Event event);
    public int isStopSignViolation(Event event);
    public int isTooCloseLeft(Event event);
    public int isTooCloseRight(Event event);
}
