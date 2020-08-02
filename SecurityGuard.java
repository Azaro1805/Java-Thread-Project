import java.util.Vector;

public class SecurityGuard implements Runnable{

	protected Vector <Integer> idVoters;
	protected  Queue securityGuardsQueue;
	protected  Queue votingSystemsQueue;
	protected  BoundedQueue managerQueue;
	protected boolean kalpiIsOpen; //true- open, false- close
	protected double timeKalpiOpen; 

	//constructor
	public SecurityGuard ( Vector <Integer> idVoters , Queue securityGuardsQueue ,  Queue votingSystemsQueue , BoundedQueue managerQueue , double timeKalpiOpen ) {
		this.idVoters = idVoters;
		this.securityGuardsQueue = securityGuardsQueue;
		this.votingSystemsQueue = votingSystemsQueue;
		this.managerQueue = managerQueue;
		this.timeKalpiOpen = timeKalpiOpen;
		kalpiIsOpen=true;

	}

	//close the kalpi
	public synchronized void setKalpiIsOpen() {
		kalpiIsOpen=false;
		notifyAll();
	}

	//checks if voter exists in the voters list (by ID)
	public synchronized boolean voterIsInTheList(int id) {
		double x = (Math.random()*3+2);
		try {Thread.sleep((long) (x*1000));} catch (InterruptedException e) {}
		return 	idVoters.contains(id);
	}

	//send the voter to the next queue- manager queue or voting system queue
	public void sendVoterToNextQueue(Queue q , Voter v) {
		q.insertVoter(v);
	}

	//return the voters list
	public Vector <Integer> getidVotersVector() {
		return idVoters;
	}

	//the security guards treat voters as long as the kalpi is open
	public void workWhenKalpiIsOpen() {
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
			}
			if ( v!=null && v.firstName.equals("closer1")) {	// closer go to the manager.
				try {Thread.sleep(6000);} catch (InterruptedException e) {}
				sendVoterToNextQueue(managerQueue , v);
			}
		}

	}

	public void treatVotersAfterKalpiIsClosed(Voter v) {
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
	
	//the kalpi is closed
	public void workWhenKalpiIsClose() {
		try {Thread.sleep(2000);} catch (InterruptedException e) {}
		while(!securityGuardsQueue.isEmpty()) { 
			Voter v= securityGuardsQueue.extractVoter();
			if (  v!=null && v.firstName.equals("closer1")) {
				try {Thread.sleep(6000);} catch (InterruptedException e) {}
				sendVoterToNextQueue(managerQueue , v);
			}
			treatVotersAfterKalpiIsClosed(v);
		}
	}

	//run function
	public void run() {
		workWhenKalpiIsOpen();
		workWhenKalpiIsClose();

	}

}
