import java.util.LinkedList;
import java.util.Queue;

public class EventQueue {
    
    private Queue<Event> eventQueue;

    public EventQueue() {
        eventQueue = new LinkedList<Event>();
    }

    public void enqueue(Event event) {
        eventQueue.add(event);
    }

    public Event peek() {
        return eventQueue.peek();
    }
}