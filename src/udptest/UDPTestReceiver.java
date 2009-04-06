package udptest;
import java.net.DatagramPacket;
import java.net.DatagramSocket;


public class UDPTestReceiver {
	
	private boolean[] received = new boolean[10000];
	private int counter = 0;
	
	public static void main(String[] args){
		try {
			new UDPTestReceiver();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public UDPTestReceiver() throws Exception{
		
		boolean running = true;
		
		DatagramSocket sock = new DatagramSocket(30000);
		
		DatagramPacket pack = new DatagramPacket(new byte[(2<<15) - 1], (2<<15)-1);
		
		while (running) {
			pack.setLength(pack.getData().length);
			sock.receive(pack);
			
			String s = new String(pack.getData()).substring(0,pack.getLength());
			System.out.println(s);
			
			if (s.equals("exit")) {
				running = false;
			} else {
				counter++;
				int num = Integer.parseInt(s.split(" ")[0]);
				received[num] = true;
			}
		}
		
		for (int i=0; i<10000; i++) {
			if (!received[i]) {
				System.out.println("Didn't receive packet " + i);
			}
		}
		System.out.println("Received " + counter + " of 10000 packets (" + 100*(counter/10000f) + "%)");
		
	}
}
