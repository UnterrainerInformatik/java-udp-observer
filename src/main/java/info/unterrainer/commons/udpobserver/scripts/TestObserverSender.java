package info.unterrainer.commons.udpobserver.scripts;

import java.io.IOException;
import java.net.InetAddress;
import java.util.List;

import info.unterrainer.commons.udpobserver.UdpSender;

public class TestObserverSender {

	public static void main(final String[] args) throws InterruptedException, IOException {
		UdpSender sender = new UdpSender(8002);

		List<InetAddress> broadcastAddresses = sender.getAllBroadcastAddresses();
		System.out.println("found broadcast addresses:");
		for (InetAddress address : broadcastAddresses)
			System.out.println(" - " + address.getHostAddress());

		System.out.println("sending UDP packets to port 8001...");
		System.out.println("press <enter> to stop.");
		while (System.in.available() == 0) {
			sender.broadcast(8001, "TestPacket!");

			System.out.println("Packet sent.");
			Thread.sleep(1000L);
		}
		System.out.println("done.");
	}
}
