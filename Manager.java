import java.util.Vector;

public class Manager implements Runnable{

	private  BoundedQueue managerQueue;
	protected Vector <Integer> idVoters;
	protected  Queue securityGuardsQueue;
	public boolean  kalpiIsOpen; //true- open, false- close

	//constructor
	public Manager( BoundedQueue managerQueue , Vector <Integer> idVoters ,  Queue securityGuardsQueue) {
		this.managerQueue = managerQueue;
		this.securityGuardsQueue = securityGuardsQueue;
		this.idVoters= idVoters;
		kalpiIsOpen=true;
	}

	//checks if let the voter get in the kalpi
	public void checkVoter(Voter v) {
		double processTime = (Math.random()*3+3);
		try {Thread.sleep((long) (processTime*1000));} catch (InterruptedException e) {}
		double  x = Math.random();
		if (x<=0.9 && v.getVoterAge()>=17 && kalpiIsOpen==true) {
			sendToSecurityGuardsQueue(v); //the manager agreed to accept the voter
			return;
		}
		v.setQueue(null); //the manager didn't agree to accept the voter
	}

	//send the voter back to the security guards queue
	public void sendToSecurityGuardsQueue(Voter v) {
		idVoters.add(v.getVoterId());
		securityGuardsQueue.insertVoter(v);
		v.setQueue(securityGuardsQueue);
	}

	//close the kalpi
	public void setKalpiIsOpen() {
		kalpiIsOpen=false;
	}

	//the manager treats voters as long as the kalpi is open
	public void workWhenKalpiIsOpen() {
		while(kalpiIsOpen==true ) { 
			Voter v= managerQueue.extractVoter();
			if(v!=null) 
				checkVoter(v);
		}
	}

	//the kalpi is closed. the manager send home all the voters
	public void workWhenKalpiIsClose() {
		while(!managerQueue.isEmpty() || !securityGuardsQueue.isEmpty())  
			if(!managerQueue.isEmpty()) {
				Voter v= managerQueue.extractVoter();
				v.setQueue(null);
			}
	}

	//run function
	public void run() {
		workWhenKalpiIsOpen();
		workWhenKalpiIsClose();
	}

}
