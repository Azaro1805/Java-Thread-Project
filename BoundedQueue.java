import java.util.Vector;

public class BoundedQueue extends Queue {

	protected final int  queueSize = 7; //max size of bounded queue is 7

	//constructor
	public  BoundedQueue ( Vector <Voter> queue) {
		super(queue);
	}

	//constructor
	public BoundedQueue (String name) {
		super(name) ;
	}

	//insert voter to the queue
	public synchronized void insertVoter(Voter v) {
		while (queue.size()==queueSize )
			try { wait(); } catch (InterruptedException e) {}
		queue.add(v);
		notifyAll();
	}

	//checks if the queue is empty
	public synchronized boolean isEmpty() {
		return queue.isEmpty();
	}

	//extract voter from the queue
	public synchronized Voter extractVoter() {
		while(isEmpty()==true && kalpiIsOpen==true  )
			try { wait(); } catch (InterruptedException e) {}
		notifyAll();
		if(isEmpty()==true)
			return null;
		return queue.remove(0);
	}

	//return the size of the queue
	public int getSize() {
		return queueSize;
	}

}
