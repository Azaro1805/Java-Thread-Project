public class VoteTicket implements Comparable <VoteTicket> {

	protected int index ;
	protected int idVoter ;
	private int voterAge ;
	protected String mayorSelection ;
	protected String listSelection ;

	//constructor
	public VoteTicket ( int index , int idVoter , int voterAge  ,  String mayorSelection , String listSelection ) {
		this.index= index;
		this.idVoter= idVoter;
		this.voterAge= voterAge;
		this.mayorSelection= mayorSelection;
		this.listSelection= listSelection;

	}

	//return the voter age
	protected int getVoterAge() {
		return voterAge;
	}

	//compare tickets by index
	public int compareTo(VoteTicket vt) {
		return this.index-vt.index;
	}

	//compare tickets by the voters age
	public int compareTo( VoteTicket vt , VoteTicketComparator c) {
		return c.compare(this, vt);
	}

}
