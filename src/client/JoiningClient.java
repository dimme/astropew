package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import common.MessageType;
import common.PackageType;
import common.Util;

public class JoiningClient {
	

	public static void main(String args[]){
		if(args.length != 3){
			System.out.println("Proper use: port adress name");
			System.exit(1);
		}
		
		int port = Integer.parseInt(args[0]);
                NetworkThread nt = new NetworkThread(args[2], args[1], port);
                nt.start();
			
		//Generate world with the seed. etc..	
	}
}
