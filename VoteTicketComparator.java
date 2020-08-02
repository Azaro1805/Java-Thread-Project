//package kalpi;

import java.util.Comparator;

public class VoteTicketComparator implements Comparator <VoteTicket>{
	
	public int compare(VoteTicket vt1, VoteTicket vt2) {
		return vt1.getVoterAge()-vt2.getVoterAge();
	}
	

}
