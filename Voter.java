public class Voter implements Runnable{

	protected int id ;
	protected String firstName;
	protected String lastName;
	protected int age;
	protected String mayorSelection ;
	protected String listSelection ;
	protected long arrivalTime ;
	protected Queue currentQueue;

	//constructor
	public Voter (String [] VoterData , Queue q) {
		this.firstName= VoterData[0];
		this.lastName= VoterData[1];
		this.id= Integer.parseInt(VoterData[2]);
		this.age= Integer.parseInt(VoterData[3]);
		this.mayorSelection=VoterData[4];
		this.listSelection= VoterData[5];
		this.arrivalTime= Integer.parseInt(VoterData[6]);
		this.currentQueue=q;
	}

	//constructor
	public Voter (String firstName , Queue q) {
		this.firstName=firstName;
		this.currentQueue=q;
	}

	//return the voter's age
	protected int getVoterAge() {
		return age;
	}

	//set the current queue of the voter (null represents home)
	public void setQueue(Queue q) {
		this.currentQueue=q;
	}

	//return the voter's ID
	protected int getVoterId() {
		return id;
	}

	//run function
	public void run() {
		long x= arrivalTime*1000;
		try {	Thread.sleep(x); } catch (Exception e) {}
		if (arrivalTime<=MAIN.durationTimeOfKalpi)
			currentQueue.insertVoter(this);
		else setQueue(null);
	}

	//toString function- return the first and last name of the voter
	public String toString () {
		return firstName+" "+lastName;
	}

}
