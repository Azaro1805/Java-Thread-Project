//package kalpi;

import java.util.Vector;

public class Manager implements Runnable{

	private  BoundedQueue managerQueue;
	protected Vector <Integer> idVoters;
	protected  Queue securityGuardsQueue;
	public  boolean  kalpiIsOpen ;




	public Manager( BoundedQueue managerQueue , Vector <Integer> idVoters ,  Queue securityGuardsQueue) {
		this.managerQueue = managerQueue;
		this.securityGuardsQueue = securityGuardsQueue;
		this.idVoters= idVoters;
		kalpiIsOpen=true;
	}


	public void checkAge(Voter v) {
		if (v.firstName.equals("closer1"))
			System.out.println("closer1 is in the manager !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
		double processTime = (Math.random()*3+3);
		try {Thread.sleep((long) (processTime*1000));} catch (InterruptedException e) {}
		double  x = Math.random();
		System.out.println( "x = " + x);
		if (x<=0.9 && v.getVoterAge()>=17 && kalpiIsOpen==true) {
			sendToSecurityGuardsQueue(v);
			System.out.println("voter = " + v);
			System.out.println("v queue after move to sec = " + v.currentQueue);
			return;
		}
		v.setQueue(null);
		System.out.println("                     last voter = " + v);
		System.out.println("                    v queue after move to null = " + v.currentQueue);
	}

	public void sendToSecurityGuardsQueue(Voter v) {
		idVoters.add(v.getVoterId());
		securityGuardsQueue.insertVoter(v);
		v.setQueue(securityGuardsQueue);
	}

	public void setKalpiIsOpen() {
		kalpiIsOpen=false;
		//System.out.println("kalpiIsOpen = "+ kalpiIsOpen);
	}

	public void run() {
		while(kalpiIsOpen==true ) { 
			Voter v= managerQueue.extractVoter();
			if(v!=null) {
				checkAge(v);
			}
		}

		while(!managerQueue.isEmpty() || !securityGuardsQueue.isEmpty()) { // queues is empty ?  go home 
			if(!managerQueue.isEmpty()) {
				Voter v= managerQueue.extractVoter();
				v.setQueue(null);
				System.out.println(" xxxx last voter = " + v);
			}
		}




		System.out.println("manager dead");


	}


}
