//package kalpi;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;

public class main{

	private static String [][] votersData;
	private static Vector<Integer> idVoters;
	private static Vector<Voter> voters; // לא בטוח צריך לבדוק !
	private static Vector<Policeman> Policemen;
	private static Vector<SecurityGuard> SecurityGuards;
	private static Vector<VotingSystem> VotingSystems;
	private static Manager manager;
	private static Queue securityGuardsQueue;
	private static Queue votingSystemsQueue;
	private static BoundedQueue policemenQueue;
	private static BoundedQueue managerQueue;
	public static Vector <VoteTicket> VoteTickets;
	public static Vector <Voter> WasInThePolicemanQueue;
	public static Vector <Thread> ThreadsVector;


	public static double TimeOfOpenKalpi=20;


	private static void readidVoters (String fileLocation)  throws IOException {
		idVoters= new  Vector<Integer>();
		FileReader file=  new FileReader(fileLocation);
		BufferedReader reader= new BufferedReader(file);
		String line=reader.readLine();
		while(line!=null) {
			line=reader.readLine();
			if (line != null)
				idVoters.add(Integer.parseInt(line));
		}
	}

	private static String readVotersData (String fileLocation)  throws IOException {
		FileReader file=  new FileReader(fileLocation);
		BufferedReader reader= new BufferedReader(file);
		String text= "";
		String line=reader.readLine();
		while(line!=null) {
			line=reader.readLine();
			text+=(" @ "+line);
		}
		text=text.substring(0, text.indexOf("null"));
		return text;
	}

	private static String [][] buildDataBase(String data )   throws IOException {
		String dataFile= readVotersData(data);
		String [] Data2 = dataFile.split(" @ ");
		String [][] Data= new String[Data2.length-1][7];
		for(int i=0; i<Data2.length-1; i++) {
			String [] personsData3=Data2[i+1].split("	");
			for(int j=0; j<personsData3.length; j++) 
				Data[i][j]=personsData3[j];
		}
		return Data;
	}

	private static void createVoters() {
		voters = new Vector < Voter> ();
		for(int i=0; i<1; i++) {
			Voter v = new Voter (votersData[i] , securityGuardsQueue);
			v.setQueue(securityGuardsQueue);
			Thread threadVoter = new Thread(v);
			//ThreadsVector.add(threadVoter);
			threadVoter.start();
			voters.add(v);
		}
	}

	public static void createSecurityGuards(int x) {
		SecurityGuards= new Vector<SecurityGuard>();
		for (int i = 0  ; i<x ; i++) {
			SecurityGuard sg = new SecurityGuard(idVoters , securityGuardsQueue , votingSystemsQueue , managerQueue , TimeOfOpenKalpi);
			SecurityGuards.add(sg);
			Thread SecurityGuardThread = new Thread (sg);
			ThreadsVector.add(SecurityGuardThread);
			SecurityGuardThread.start();
		}
	}

	private static void createPolicemen() {
		WasInThePolicemanQueue= new Vector <Voter>();
		Policemen= new Vector<Policeman>();
		for (int i = 0  ; i<3 ; i++) {
			Policeman pm = new Policeman(policemenQueue , WasInThePolicemanQueue, votingSystemsQueue);
			Policemen.add(pm);
			Thread threadPoliceman = new Thread(pm);
			ThreadsVector.add(threadPoliceman);
			threadPoliceman.start();

		}
	}

	private static void createVotingSystems() {
		VoteTickets= new Vector <VoteTicket> (); 
		VotingSystems= new Vector<VotingSystem>();
		for (int i = 0  ; i<2 ; i++) {
			VotingSystem vs = new VotingSystem(votingSystemsQueue , policemenQueue, VoteTickets , securityGuardsQueue);
			VotingSystems.add(vs);
			Thread threadVotingSystem = new Thread(vs);
			ThreadsVector.add(threadVotingSystem);
			threadVotingSystem.start();
		}
	}

	private static void createManager() {
		manager = new Manager(managerQueue , idVoters , securityGuardsQueue );
		Thread threadmanager = new Thread(manager);
		ThreadsVector.add(threadmanager);
		threadmanager.start();
	}


	public static void closeKalpi () {

		Voter closer = new Voter ( "closer1"  ,  securityGuardsQueue);
		Thread threadVotercloser = new Thread(closer);
		ThreadsVector.add(threadVotercloser);


		Voter closer2 = new Voter ( "closer2" , votingSystemsQueue);
		Thread threadVotercloser2 = new Thread(closer2);
		ThreadsVector.add(threadVotercloser2);
		WasInThePolicemanQueue.add(closer2);

		//System.out.println("start the kalpi");
		try {Thread.sleep((long) (1000*TimeOfOpenKalpi));} catch (InterruptedException e) {}
		//System.out.println("stop the kalpi");



		securityGuardsQueue.setKalpiIsOpen();
		managerQueue.setKalpiIsOpen();
		manager.setKalpiIsOpen();
		votingSystemsQueue.setKalpiIsOpen();
		policemenQueue.setKalpiIsOpen();
		for ( int i =0 ; i<VotingSystems.size() ; i++) 
			VotingSystems.elementAt(i).setKalpiIsOpen();

		for ( int i =0 ; i<Policemen.size() ; i++) 
			Policemen.elementAt(i).setKalpiIsOpen();

		for ( int i =0 ; i<SecurityGuards.size() ; i++) 
			SecurityGuards.elementAt(i).setKalpiIsOpen();


		threadVotercloser.start();	
		threadVotercloser2.start();


	}

	public static void startTheVoteCounter() {

		for (int  i=0 ; i<ThreadsVector.size() ; i++) 
			try {ThreadsVector.elementAt(i).join();} catch (InterruptedException e) {}
		System.out.println();
		System.out.println("Voting is over , let's start counting");

		VotesCounter votesCounter = new VotesCounter(VoteTickets);
		try {Thread.sleep(200);} catch (InterruptedException e) {}
		System.out.println();
		votesCounter.printResults();
	}

	public static void main(String[] args)  throws IOException{
		
		
		//GUI
		
		
		
		
		
		
		//Start main
		
		ThreadsVector=new Vector <Thread>();
		votersData= buildDataBase( "C:/Users/oraza/Desktop/voters data.txt");
		readidVoters ("C:/Users/oraza/Desktop/id list.txt");

		securityGuardsQueue= new Queue("securityGuardsQueue");
		votingSystemsQueue= new Queue("votingSystemsQueue");
		policemenQueue= new BoundedQueue("policemenQueue"); 
		managerQueue= new BoundedQueue("managerQueue"); 

		int x = 3;
		createSecurityGuards(x);
		createManager();
		createVotingSystems();
		createPolicemen();
		createVoters();


		closeKalpi ();

		startTheVoteCounter();
		System.out.println("VoteTickets size = "+ VoteTickets.size());
		System.out.println("main end");










	}
}
