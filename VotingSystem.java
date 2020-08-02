//package kalpi;

import java.util.Vector;

public class VotingSystem implements Runnable {

	private  Queue securityGuardsQueue;
	protected  Queue votingSystemsQueue;
	protected  BoundedQueue policemenQueue;
	protected Vector <VoteTicket> VoteTickets;
	public  boolean  kalpiIsOpen ;


	public VotingSystem (  Queue votingSystemsQueue , BoundedQueue policemenQueue , Vector <VoteTicket>VoteTickets ,  Queue securityGuardsQueue) {
		this.votingSystemsQueue = votingSystemsQueue;
		this.policemenQueue = policemenQueue;
		kalpiIsOpen = true  ;
		this.VoteTickets =  VoteTickets;
		this.securityGuardsQueue = securityGuardsQueue;
	}

	public synchronized void setKalpiIsOpen() {
		kalpiIsOpen=false;
		notifyAll();
	}

	public void createVoteTicket (Voter v , int i) {
		try {Thread.sleep(2000);} catch (InterruptedException e) {}
		try {
			double x = Math.random();
			System.out.println("vs x= "+ x);
			if (x>=0.2) {
				VoteTicket vt = new VoteTicket (i , v.id , v.age , v.mayorSelection , v.listSelection );
				VoteTickets.add(vt);
			}else 
				throw new FailToCreateVoteTicketException ();
		}catch(FailToCreateVoteTicketException e) {
			System.err.println("Voting system didn't succeeded to create vote ticket!");
			policemenQueue.insertVoter(v);
			v.setQueue(policemenQueue);
		}
	}


	public void run() {
		int i =0;
		while(!votingSystemsQueue.isEmpty() || kalpiIsOpen==true || !securityGuardsQueue.isEmpty() ) {
			Voter v= votingSystemsQueue.extractVoter();
			System.out.println(v);
			if( v!=null && !v.firstName.equals("closer2")) 
				createVoteTicket ( v ,  i);
			if ( v!=null && v.firstName.equals("closer2")) { // closer go to the manager.
				System.out.println("               while 1       colser 2 int the police");
				policemenQueue.insertVoter(v);				
			}
			i++;
			//System.out.println("kalpiIsOpen   vs = " + kalpiIsOpen);
		}
		
		try {Thread.sleep(2000);} catch (InterruptedException e) {}

		if (!votingSystemsQueue.isEmpty()) {
			Voter v= votingSystemsQueue.extractVoter();
			if ( v!=null && v.firstName.equals("closer2")) { // closer go to the manager.
				System.out.println("                if 2         colser 2 int the police");
				policemenQueue.insertVoter(v);				
			}
		}
		System.out.println("Vs dead");

	}





}






