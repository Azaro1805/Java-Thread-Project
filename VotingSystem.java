import java.util.Vector;

public class VotingSystem implements Runnable {

	private  Queue securityGuardsQueue;
	protected  Queue votingSystemsQueue;
	protected  BoundedQueue policemenQueue;
	protected static Vector <VoteTicket> VoteTickets;
	protected static int index=0;
	public  boolean  kalpiIsOpen ;

	//constructor
	public VotingSystem (Queue votingSystemsQueue ,BoundedQueue policemenQueue ,Vector <VoteTicket>VoteTickets, Queue securityGuardsQueue) {
		this.votingSystemsQueue = votingSystemsQueue;
		this.policemenQueue = policemenQueue;
		kalpiIsOpen = true;
		this.VoteTickets = VoteTickets;
		this.securityGuardsQueue = securityGuardsQueue;
	}

	//close the kalpi
	public synchronized void setKalpiIsOpen() {
		kalpiIsOpen=false;
		notifyAll();
	}

	//
	public static synchronized void createVoteTicket(Voter v) {
		index++;
		VoteTicket vt = new VoteTicket (index , v.id , v.age , v.mayorSelection , v.listSelection );
		VoteTickets.add(vt);

	}

	//
	public void votingProcess (Voter v) {
		try {Thread.sleep(2000);} catch (InterruptedException e) {}
		try {
			double x = Math.random();
			if (x>=0.2) {
				createVoteTicket(v);
			}else 
				throw new FailToCreateVoteTicketException ();
		}catch(FailToCreateVoteTicketException e) {
			System.err.println("Voting system didn't succeeded to create vote ticket, " + v.firstName + " " + v.lastName +" didn't vote!");
			policemenQueue.insertVoter(v);
			v.setQueue(policemenQueue);
		}
	}

	//run function
	public void run() {
		int i =0;
		while(!votingSystemsQueue.isEmpty() || kalpiIsOpen==true || !securityGuardsQueue.isEmpty() ) {
			Voter v= votingSystemsQueue.extractVoter();
			if( v!=null && !v.firstName.equals("closer2")) 
				votingProcess(v);
			if ( v!=null && v.firstName.equals("closer2")) { // closer go to the manager.
				policemenQueue.insertVoter(v);				
			}
			i++;
		}
		try {Thread.sleep(2000);} catch (InterruptedException e) {}
		if (!votingSystemsQueue.isEmpty()) {
			Voter v= votingSystemsQueue.extractVoter();
			if ( v!=null && v.firstName.equals("closer2"))  // closer go to the manager.
				policemenQueue.insertVoter(v);				
		}
	}

}







