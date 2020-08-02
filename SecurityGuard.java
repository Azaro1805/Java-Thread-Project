//package kalpi;

import java.util.Vector;

public class SecurityGuard implements Runnable{

	protected Vector <Integer> idVoters;
	protected  Queue securityGuardsQueue;
	protected  Queue votingSystemsQueue;
	protected  BoundedQueue managerQueue;
	public  boolean  kalpiIsOpen  ;
	protected double timeKalpiOpen;

	public SecurityGuard ( Vector <Integer> idVoters , Queue securityGuardsQueue ,  Queue votingSystemsQueue , BoundedQueue managerQueue , double timeKalpiOpen ) {
		this.idVoters = idVoters;
		this.securityGuardsQueue = securityGuardsQueue;
		this.votingSystemsQueue = votingSystemsQueue;
		this.managerQueue = managerQueue;
		this.timeKalpiOpen = timeKalpiOpen;
		kalpiIsOpen=true;

	}

	public synchronized void setKalpiIsOpen() {
		kalpiIsOpen=false;
		notifyAll();
		//System.out.println("kalpiIsOpen = "+ kalpiIsOpen);
	}


	public synchronized boolean voterIsInTheList(int id) {
		double x = (Math.random()*3+2);
		System.out.println("x sc = "+ x);
		try {Thread.sleep((long) (x*1000));} catch (InterruptedException e) {}
		//System.out.println("                                                                       " +x);
		return 	idVoters.contains(id);
	}

	public void sendVoterToNextQueue(Queue q , Voter v) {
		q.insertVoter(v);
	}

	// אולי לא צריך 
	public Vector <Integer> getidVotersVector() {
		return idVoters;
	}


	public void run() {

		while(kalpiIsOpen==true) {
			Voter v= securityGuardsQueue.extractVoter();
			if(v!=null && kalpiIsOpen==true ) { 
				if(voterIsInTheList(v.id)) {
					v.setQueue(votingSystemsQueue);
					sendVoterToNextQueue(votingSystemsQueue , v);
				}else {
					v.setQueue(managerQueue);
					sendVoterToNextQueue(managerQueue , v);
				}
				//System.out.println(v);
				//System.out.println( "currentQueue is (after) :"+ v.currentQueue);
				//System.out.println();
				//	System.out.println("check the mood of the kalpi is: " +kalpiIsOpen2);	
			}
			if ( v!=null && v.firstName.equals("closer1")) {	// closer go to the manager.
				System.out.println("closer while 1");
				try {Thread.sleep(6000);} catch (InterruptedException e) {}
				sendVoterToNextQueue(managerQueue , v);
			}
		}

		try {Thread.sleep(2000);} catch (InterruptedException e) {}

		while(!securityGuardsQueue.isEmpty()) { // queues is empty ?  go home 
			Voter v= securityGuardsQueue.extractVoter();
			if (  v!=null && v.firstName.equals("closer1")) {	// closer go to the manager.
				System.out.println("closer while 2");
				try {Thread.sleep(6000);} catch (InterruptedException e) {}
				sendVoterToNextQueue(managerQueue , v);
			}
			if (v!=null && v.arrivalTime>timeKalpiOpen) {
				if(voterIsInTheList(v.id)) {
					v.setQueue(votingSystemsQueue);
					sendVoterToNextQueue(votingSystemsQueue , v);
				}else {
					v.setQueue(managerQueue);
					sendVoterToNextQueue(managerQueue , v);
				}
			}else
				if(v!=null)
					v.setQueue(null);
		}


		System.out.println("sg dead");

	}

}
