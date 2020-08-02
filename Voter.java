//package kalpi;

import java.io.IOException;

public class Voter implements Runnable{


	protected int id ;
	protected String firstName;
	protected String lastName;
	protected int age;
	protected String mayorSelection ;
	protected String listSelection ;
	protected long arrivalTime ;
	protected Queue currentQueue;


	public Voter (String [] VoterData , Queue q) {
		this.firstName= VoterData[0];
		this.lastName= VoterData[1];
		this.id= Integer.parseInt(VoterData[2]);
		this.age= Integer.parseInt(VoterData[3]);
		this.mayorSelection=VoterData[4];
		this.listSelection= VoterData[5];
		this.arrivalTime= Integer.parseInt(VoterData[6]);
		System.out.println("                                            " +firstName+ " "+ arrivalTime);
		this.currentQueue=q;
	}

	public Voter (  String firstName , Queue q) {
		this.firstName=firstName;
		this.currentQueue=q;
	}

	protected int getVoterAge() {
		return age;
	}

	public void setQueue(Queue q) {
		this.currentQueue=q;
	}

	protected int getVoterId() {
		return id;
	}

	public void run() {
		
		 
			System.out.println(this.firstName+" "+ arrivalTime);
			long x= arrivalTime*1000;
			System.out.println("x"+firstName+ +x);
			try {	Thread.sleep(x); } catch (Exception e) {}
			//System.out.println("voter");
			if (arrivalTime<=startGUI.durationTimeOfKalpi)
			currentQueue.insertVoter(this);
			else setQueue(null);
				
		
		System.out.println(this.firstName+ "Voter dead");
	}


	public String toString () {
		return firstName+" "+lastName;
	}







}
