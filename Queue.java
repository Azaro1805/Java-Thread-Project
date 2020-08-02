import java.util.Vector;

public class Queue  {

	protected Vector <Voter> queue;
	protected String name ;
	public boolean kalpiIsOpen; //true- open, false- close

	//constructor
	public Queue (String name) {
		queue= new  Vector <Voter> () ;
		this.name=name;
		this.kalpiIsOpen= true;
	}

	//constructor
	public Queue ( Vector <Voter> queue) {
		this.queue = queue;
		this.kalpiIsOpen= true;
	}

	//insert voter to the queue
	public synchronized void insertVoter(Voter v) {
		queue.add(v);
		notifyAll();
	}

	//checks if the queue is empty
	public synchronized boolean isEmpty() {
		return queue.isEmpty();
	}

	//close the kalpi
	public void setKalpiIsOpen() {
		kalpiIsOpen= false;
	}

	//remove all elements from queue
	public void clean () {
		queue.removeAllElements();
	}

	//extract voter from the queue
	public synchronized Voter extractVoter() {
		while(isEmpty()==true && kalpiIsOpen==true  )
			try { wait(); } catch (InterruptedException e) {}
		if(isEmpty()==true)
			return null;
		return queue.remove(0);
	}

	//toString function of the queue- returns the queue's name
	public String toString() {
		return name;
	}

}

