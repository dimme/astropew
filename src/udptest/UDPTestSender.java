package udptest;

import java.net.*;


public class UDPTestSender {

	public static void main(String[] args) throws Exception {
		new UDPTestSender(args[0], Integer.parseInt(args[1]));
	}
	
	public UDPTestSender(String ip, int port) throws Exception {
		InetAddress addr = InetAddress.getByName(ip);
		DatagramSocket sock = new DatagramSocket();
		
		DatagramPacket pack = new DatagramPacket(new byte[(2<<15) - 1], (2<<15)-1, addr, port);
		
		for (int i=0; i<10000; i++) {
			String data = i + " a b c d e f g h i j k l m n o p q r s t u v w x y z å ä ö Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer tortor ligula, venenatis ultricies, tempus in, tristique quis, elit. Ut molestie libero elementum eros. Phasellus eget justo sit amet arcu cursus malesuada. Nullam velit arcu, posuere congue, volutpat vel, scelerisque elementum, neque. Nam suscipit. Nulla sollicitudin. Maecenas posuere congue enim. Etiam tempus sem. Vivamus faucibus fringilla sem. Donec euismod, turpis sit amet elementum vestibulum, tortor nibh pharetra urna, eget tristique sem tortor ultrices enim. Sed in erat.";
			//String data = i + "";
			pack.setData( data.getBytes() );
			sock.send(pack);
			//Thread.sleep(1);
		}
		
		Thread.sleep(5000);
		
		pack.setData("exit".getBytes(),0,"exit".length());
		sock.send(pack);
		sock.send(pack);
		sock.send(pack);
		sock.send(pack);
		sock.send(pack);
		sock.send(pack);
		sock.send(pack);
		sock.send(pack);
		sock.send(pack);
		sock.send(pack);
	}
	
}
