//package kalpi;

import java.util.Vector;

public class Policeman implements Runnable{

	private  BoundedQueue policemenQueue;
	public  boolean  kalpiIsOpen ;
	protected  Queue votingSystemsQueue;
	protected Vector <Voter> WasInThePolicemanQueue;


	public Policeman( BoundedQueue policemenQueue ,Vector <Voter> WasInThePolicemanQueue , Queue votingSystemsQueue) {
		this.policemenQueue = policemenQueue;
		this.WasInThePolicemanQueue=WasInThePolicemanQueue;
		this.votingSystemsQueue= votingSystemsQueue;
		kalpiIsOpen=true;
	}

	public synchronized void setKalpiIsOpen() {
		kalpiIsOpen=false;
		notifyAll();
		//System.out.println("kalpiIsOpen = "+ kalpiIsOpen);
	}

	public void sendTovotingSystemsQueue(Voter v) {
		votingSystemsQueue.insertVoter(v);
		v.setQueue(votingSystemsQueue);
	}

	public void checkVoting(Voter v) {
		double processTime = (Math.random()+4);
		try {Thread.sleep((long) (processTime*1000));} catch (InterruptedException e) {}
		if (!WasInThePolicemanQueue.contains(v) && kalpiIsOpen==true ) {
			System.out.println( "                                        check closer2 " + v);
			double xp = Math.random();
			System.out.println("xp = "+  xp);
			if(xp<=0.5) {
				sendTovotingSystemsQueue(v);
				WasInThePolicemanQueue.add(v);
				return;

			}
		}
		System.out.println(WasInThePolicemanQueue);
		System.out.println("voter go home : "+v);
		v.setQueue(null);
	}

	public void run() {

		while(kalpiIsOpen==true) {
			Voter v= policemenQueue.extractVoter();
			if(v!=null) {
				checkVoting(v);
			}
			System.out.println("                           policemenQueue 1111111111111111111111         ");

		}
		//System.out.println("kalpiIsOpen policeman = " + kalpiIsOpen);
		//System.out.println("policemenQueue = " + policemenQueue.queue);
		//System.out.println("votingSystemsQueue = " + votingSystemsQueue.queue);
		while(!policemenQueue.isEmpty() || !votingSystemsQueue.isEmpty()) {// queues is empty ?  go home
			if(!policemenQueue.isEmpty()) {
				Voter v= policemenQueue.extractVoter();
				if(v!=null)
					v.setQueue(null);
			}
			
			//System.out.println("votingSystemsQueue =  " +votingSystemsQueue.queue.size());
			//System.out.println("                           policemenQueue 222222222222222222         ");
		}


		System.out.println( "policman dead ");
	}



}
