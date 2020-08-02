//package kalpi;

import java.io.IOException;

public class VoteTicket implements Comparable <VoteTicket> {

	protected int index ;
	protected int idVoter ;
 	private int voterAge ;
 	protected String mayorSelection ;
	protected String listSelection ;
	
	
	public VoteTicket ( int index , int idVoter , int voterAge  ,  String mayorSelection , String listSelection ) {
		this.index= index;
		this.idVoter= idVoter;
		this.voterAge= voterAge;
		this.mayorSelection= mayorSelection;
		this.listSelection= listSelection;

	}
	
	/*
	public static synchronized int addindex(int i) {
		i= i+1; 
		return i;
	}
	*/
	protected int getVoterAge() {
		return voterAge;
	}
	
	public int compareTo(VoteTicket vt) {
		return this.index-vt.index;
	}

	public int compareTo( VoteTicket vt , VoteTicketComparator c) {
		return c.compare(this, vt);
	}
	
	
}
