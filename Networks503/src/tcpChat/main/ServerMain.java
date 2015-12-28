package tcpChat.main;

import java.util.Scanner;

import tcpChat.hosts.Server;

public class ServerMain {
	
	
	public static void main(String[] args) 
	{
		Scanner sc = new Scanner(System.in);
		System.out.print("Enter DNS server hostname: ");
		String dns = sc.next();
		System.out.print("Enter port number for this server: ");
		int port = sc.nextInt();
		sc.close();

		new Server(dns, port);
	}

}
