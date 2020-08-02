import java.util.Vector;

public class Policeman implements Runnable{

	protected BoundedQueue policemenQueue;
	public boolean  kalpiIsOpen ; //true- open, false- close
	protected  Queue votingSystemsQueue;
	protected Vector <Voter> WasInThePolicemanQueue;

	//constructor
	public Policeman( BoundedQueue policemenQueue ,Vector <Voter> WasInThePolicemanQueue , Queue votingSystemsQueue) {
		this.policemenQueue = policemenQueue;
		this.WasInThePolicemanQueue=WasInThePolicemanQueue;
		this.votingSystemsQueue= votingSystemsQueue;
		kalpiIsOpen=true;
	}

	//close the kalpi
	public synchronized void setKalpiIsOpen() {
		kalpiIsOpen=false;
		notifyAll();
	}

	//send the voter to the voting system queue
	public void sendTovotingSystemsQueue(Voter v) {
		votingSystemsQueue.insertVoter(v);
		v.setQueue(votingSystemsQueue);
	}

	//the policeman checks if the voting process was fine
	public void checkVoting(Voter v) {
		double processTime = (Math.random()+4);
		try {Thread.sleep((long) (processTime*1000));} catch (InterruptedException e) {}
		if (!WasInThePolicemanQueue.contains(v) && kalpiIsOpen==true ) {
			double xp = Math.random();
			if(xp<=0.5) { 
				sendTovotingSystemsQueue(v); //the policeman agreed to let the voter vote again
				WasInThePolicemanQueue.add(v);
				return;
			}
		}
		v.setQueue(null); //the policeman didn't agree to let the voter vote again
	}

	//the policemem treat voters as long as the kalpi is open
	public void workWhenKalpiIsOpen() {
		while(kalpiIsOpen==true) {
			Voter v= policemenQueue.extractVoter();
			if(v!=null) 
				checkVoting(v);	
		}
	}

	//the kalpi is closed. the policemen send home all the voters
	public void workWhenKalpiIsClose() {
		while(!policemenQueue.isEmpty() || !votingSystemsQueue.isEmpty()) // queues is empty ?  go home
			if(!policemenQueue.isEmpty()) {
				Voter v= policemenQueue.extractVoter();
				if(v!=null)
					v.setQueue(null);
			}
	}

	//run function
	public void run() {
		workWhenKalpiIsOpen();
		workWhenKalpiIsClose();
	}
	
}
