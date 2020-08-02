//package kalpi;

import java.util.Vector;

public class BoundedQueue extends Queue {

	protected final int  queueSize = 7;

	public  BoundedQueue ( Vector <Voter> queue) {
		super(queue);
	}

	public BoundedQueue (String name) {
		super(name) ;
	}

	public synchronized void insertVoter(Voter v) {
		while (queue.size()==queueSize )
			try { wait(); } catch (InterruptedException e) {}
		queue.add(v);
		notifyAll();
		//System.out.println("addvoter");
	}

	public synchronized boolean isEmpty() {
		return queue.isEmpty();
	}

	public synchronized Voter extractVoter() {
		while(isEmpty()==true && kalpiIsOpen==true  )
			try { wait(); } catch (InterruptedException e) {}
		notifyAll();

		//System.out.println("currentQueue is (before) :"+ this.queue.elementAt(0).currentQueue);
		if(isEmpty()==true)
			return null;
		return queue.remove(0);
	}

//	public synchronized Voter extractVoter() {
//		while(isEmpty()==true &&  kalpiIsOpen==true  )
//			try { wait(); } catch (InterruptedException e) {}
//		notifyAll();
//		//System.out.println("currentQueue is (before) :"+ this.queue.elementAt(0).currentQueue);
//		return queue.remove(0);
//	}


	public int getSize() {
		return queueSize;
	}




}
