import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Vector;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;

public class MAIN extends JFrame {

	//GUI 
	private JFrame frame;
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	public static int NumberOfSecurityGuards ;
	public static double durationTimeOfKalpi ;

	//Kalpi
	private static String [][] votersData;
	private static Vector<Integer> idVoters;
	private static Vector<Voter> voters;
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


	// Launch the application.
	//**********************************************************Main**************************************************************
	public static void main(String[] args)  throws IOException{

		//GUI
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MAIN frame = new MAIN();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	//****************************************************Kalpi  Functions*********************************************************************

	// start the Election
	public static void StartKalpi( String fileLoc1 , String fileLoc2) throws IOException {
		System.out.println("The election started");
		ThreadsVector=new Vector <Thread>();
		votersData= buildDataBase(fileLoc1);
		readidVoters (fileLoc2);
		createQueues();
		createSecurityGuards(NumberOfSecurityGuards);
		createManager();
		createVotingSystems();
		createPolicemen();
		createVoters();

		closeKalpi ();
		startTheVoteCounter();
		clean();
	}

	// create all the queues for the Election
	public static void createQueues() {
		securityGuardsQueue= new Queue("securityGuardsQueue");
		votingSystemsQueue= new Queue("votingSystemsQueue");
		policemenQueue= new BoundedQueue("policemenQueue"); 
		managerQueue= new BoundedQueue("managerQueue"); 
	}

	//read id Voters file
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

	//read Voters data file 
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

	//build data base
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

	//create voters 
	private static void createVoters() {
		voters = new Vector < Voter> ();
		for(int i=0; i<votersData.length; i++) {
			Voter v = new Voter (votersData[i] , securityGuardsQueue);
			v.setQueue(securityGuardsQueue);
			Thread threadVoter = new Thread(v);
			threadVoter.start();
			voters.add(v);
		}
	}

	// create the security guards (the number of security guards is the user's decision)
	public static void createSecurityGuards(int x) {
		SecurityGuards= new Vector<SecurityGuard>();
		for (int i = 0  ; i<x ; i++) {
			SecurityGuard sg = new SecurityGuard(idVoters , securityGuardsQueue , votingSystemsQueue , managerQueue , durationTimeOfKalpi);
			SecurityGuards.add(sg);
			Thread SecurityGuardThread = new Thread (sg);
			ThreadsVector.add(SecurityGuardThread);
			SecurityGuardThread.start();
		}
	}

	// create policemen
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

	// create Voting Systems
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

	//create Manager
	private static void createManager() {
		manager = new Manager(managerQueue , idVoters , securityGuardsQueue );
		Thread threadmanager = new Thread(manager);
		ThreadsVector.add(threadmanager);
		threadmanager.start();
	}

	// Inform all the employees that the Kalpi is Close
	public static void infromEveryoneKalpiClose() {
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

	}

	// close the kalpi 
	public static void closeKalpi () {
		Voter closer = new Voter ( "closer1"  ,  securityGuardsQueue);
		Thread threadVotercloser = new Thread(closer);
		ThreadsVector.add(threadVotercloser);

		Voter closer2 = new Voter ( "closer2" , votingSystemsQueue);
		Thread threadVotercloser2 = new Thread(closer2);
		ThreadsVector.add(threadVotercloser2);
		WasInThePolicemanQueue.add(closer2);

		try {Thread.sleep((long) (1000*durationTimeOfKalpi));} catch (InterruptedException e) {}

		infromEveryoneKalpiClose();
		threadVotercloser.start();	
		threadVotercloser2.start();


	}

	// make a SQL database
	public static void CopyVotesToSQL() {
		Database DB= new Database();
		for(int i=0; i<VoteTickets.size(); i++)
			DB.insert("votes", VoteTickets.elementAt(i));
		try {Thread.sleep(200);} catch (InterruptedException e) {}
	}

	// print the results of the Election
	public static void printResults(VotesCounter votesCounter) {
		System.out.println();
		votesCounter.printResults();
		System.out.println();
		votesCounter.calculatePercent();
	}

	// Count the votes
	public static void startTheVoteCounter() {
		for (int  i=0 ; i<ThreadsVector.size() ; i++) 
			try {ThreadsVector.elementAt(i).join();} catch (InterruptedException e) {}

		System.out.println("Voting is over , let's start counting");
		VotesCounter votesCounter = new VotesCounter(VoteTickets);
		//CopyVotesToSQL();
		printResults(votesCounter);
	}

	// clean all Vectors for new Election
	public static void clean () {
		idVoters.removeAllElements();
		voters.removeAllElements(); 
		Policemen.removeAllElements();
		SecurityGuards.removeAllElements();
		VotingSystems.removeAllElements();
		VoteTickets.removeAllElements();
		WasInThePolicemanQueue.removeAllElements();
		ThreadsVector.removeAllElements();
		securityGuardsQueue.clean();
		votingSystemsQueue.clean();
		policemenQueue.clean(); 
		managerQueue.clean(); 
	}

	//********************************************************** GUI function *****************************************************************

	//GUI Function
	public MAIN() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblWelcomeToOrshoham = new JLabel("Welcome To Or & Shoham Kalpi! ");
		lblWelcomeToOrshoham.setBounds(86, 30, 242, 20);
		contentPane.add(lblWelcomeToOrshoham);

		JLabel lblNumberOfSecurity = new JLabel("Number Of Security Guards:");
		lblNumberOfSecurity.setBounds(15, 66, 241, 20);
		contentPane.add(lblNumberOfSecurity);

		JLabel lblDurarationTimeOf = new JLabel("Duration Time Of The Kalpi:");
		lblDurarationTimeOf.setBounds(15, 122, 215, 20);
		contentPane.add(lblDurarationTimeOf);

		textField = new JTextField();
		textField.setText("1");
		textField.setBounds(229, 66, 46, 26);
		contentPane.add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setText("8");
		textField_1.setBounds(229, 119, 46, 26);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		durationTimeOfKalpi=Integer.parseInt(textField.getText());

		JButton btnStart = new JButton("Start");
		contentPane.add(btnStart);
		btnStart.setBounds(28, 179, 115, 29);

		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				checkInputs(textField , textField_1   );
			}
		});
		JButton btnExit = new JButton("Exit");
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setBounds(203, 179, 115, 29);
		contentPane.add(btnExit);
	}

	//check the inputs from the user
	public static void checkInputs( JTextField textField , JTextField textField_1  ) {
		try {
			NumberOfSecurityGuards=Integer.parseInt(textField.getText());
			if (NumberOfSecurityGuards < 1 || NumberOfSecurityGuards > 4){
				JOptionPane.showMessageDialog(null, "number of security guards must be  an integer between 1-4 ");
			}
			else {
				durationTimeOfKalpi=Double.parseDouble(textField_1.getText());
				if (durationTimeOfKalpi < 0){	
					JOptionPane.showMessageDialog(null, "The duration of the kalpi must be between 0-24  , Closing time is 0 ");
					durationTimeOfKalpi = 0.0;
				}
				else if (durationTimeOfKalpi > 24){
					JOptionPane.showMessageDialog(null, "The duration of the kalpi must be between 0-24  , Closing time is 24 ");
					durationTimeOfKalpi = 24.0;
				}
				StartKalpi("C:/Users/shoha/Desktop/voters data.txt" , "C:/Users/shoha/Desktop/id list.txt");
			}
		}
		catch(NumberFormatException nfe) {
			JOptionPane.showMessageDialog(null, "Input must be a number");
		}
		catch(Exception e1){
			JOptionPane.showMessageDialog(null, "There is an error in the election system");
		}
	}




}
