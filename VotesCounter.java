//package kalpi;

import java.util.Vector;

public class VotesCounter {

	protected Vector <VoteTicket> VoteTickets;

	public String calculateMayorResults() {
		Vector <String> MayorVotes= new Vector <String>();
		for(int i=0; i<VoteTickets.size(); i++) {
			String mayor=VoteTickets.elementAt(i).mayorSelection;
			MayorVotes.add(mayor);	
		}
		int num=0;
		int max=0;
		String mayorwinner= "";
		while (!MayorVotes.isEmpty()){
			num=0;
			String s= MayorVotes.elementAt(0);
			for(int i=0; i<MayorVotes.size(); i++) {
				if(MayorVotes.elementAt(i).equals(s)){
					num++;
					MayorVotes.removeElementAt(i);
					i--;
				}
			}
			if(num>max){
				max=num;
				mayorwinner= s;
			}


		}
		return mayorwinner;
	}

	public String calculateListResults() {
		Vector <String> ListVotes= new Vector <String>();
		for(int i=0; i<VoteTickets.size(); i++) {
			String list=VoteTickets.elementAt(i).listSelection;
			ListVotes.add(list);
		}
		int num=0;
		int max=0;
		String listwinner= "";
		while (!ListVotes.isEmpty()){
			num=0;
			String s= ListVotes.elementAt(0);
			for(int i=0; i<ListVotes.size(); i++) {
				if(ListVotes.elementAt(i).equals(s)){
					num++;
					ListVotes.removeElementAt(i);
					i--;
				}
			}
			if(num>max){
				max=num;
				listwinner= s;
			}


		}
		return listwinner;
	}

	public void printResults() {

		System.out.println("The next mayor is :" + calculateMayorResults());
		System.out.println("The list with most votes is :" + calculateListResults());

	}

	public void calculatePercent() {
		Vector <VoteTicket> VoteTickets2= new  Vector <VoteTicket> ();
		VoteTickets2.addAll(VoteTickets);
		System.out.println(VoteTickets2.size()+"xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx");
		while (!VoteTickets2.isEmpty()) {
			String mayor= VoteTickets2.elementAt(0).mayorSelection;
			double num=0;
			for(int i=0; i<VoteTickets2.size(); i++){
				if (mayor.equals(VoteTickets2.elementAt(i).mayorSelection)) {
					num=num+1;
					VoteTickets2.removeElementAt(i);
					i--;
				}	 
			}
			double percent= (num/VoteTickets.size())*100;
			System.out.println("Percent voted for "+ mayor+ " : "+ percent+"%");
		}
	}

	public VotesCounter (Vector <VoteTicket> VoteTickets) {
		this.VoteTickets=VoteTickets;
	}

}
