//package kalpi;

import java.util.Vector;

public class Queue  {

	protected Vector <Voter> queue;
	protected String name ;
	public boolean kalpiIsOpen;


	public Queue (String name) {
		queue= new  Vector <Voter> () ;
		this.name=name;
		this.kalpiIsOpen= true;
	}

	public Queue ( Vector <Voter> queue) {
		this.queue = queue;
	}

	public synchronized void insertVoter(Voter v) {
		queue.add(v);
		notifyAll();
	}

	public synchronized boolean isEmpty() {
		return queue.isEmpty();
	}

	public void setKalpiIsOpen() {
		//System.out.println("queue kalpiIsOpen2 = "+ kalpiIsOpen2);
		kalpiIsOpen= false;
		//System.out.println("queue kalpiIsOpen2 = "+ kalpiIsOpen2);

	}
	
	public void clean () {
		queue.removeAllElements();
	}

	public synchronized Voter extractVoter() {
		while(isEmpty()==true && kalpiIsOpen==true  )
			try { wait(); } catch (InterruptedException e) {}
		//System.out.println("currentQueue is (before) :"+ this.queue.elementAt(0).currentQueue);
		if(isEmpty()==true)
			return null;
		return queue.remove(0);
	}

	public String toString() {
		return name;
	}

}

