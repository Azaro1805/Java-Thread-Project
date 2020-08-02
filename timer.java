//package kalpi;

public  class timer extends Thread {

	
	public boolean kalpiIsOpen2;
	
	
	public timer () {
		kalpiIsOpen2=true;
	}
	
	public void setKalpiIsOpen() {
			kalpiIsOpen2=false;
			System.out.println("kalpiIsOpen2 = "+ kalpiIsOpen2);
		}
	
	public synchronized void run() {
		System.out.println("timer kalpiIsOpen2 = "+ kalpiIsOpen2);
		kalpiIsOpen2=false;
		System.out.println("timer kalpiIsOpen2 = "+ kalpiIsOpen2);
		notifyAll();
		System.out.println("timer is dead");
	}
	
	
}
