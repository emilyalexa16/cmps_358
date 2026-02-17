import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EDR_Server extends Remote { 
    public long getGeneralTimestamp() throws RemoteException;
    public long getLatestStopTimestamp() throws RemoteException;
    public void setLatestStopTimestamp(long timestamp) throws RemoteException;
    public void addEvent(Event event) throws RemoteException;
    public int handleEvent(Event event) throws RemoteException;
    public int isStopSignViolation(Event event) throws RemoteException;
    public int isTooCloseLeft(Event event) throws RemoteException;
    public int isTooCloseRight(Event event) throws RemoteException;
}

