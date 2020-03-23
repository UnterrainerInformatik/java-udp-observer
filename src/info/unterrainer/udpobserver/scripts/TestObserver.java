package info.unterrainer.udpobserver.scripts;

import java.net.SocketException;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import info.unterrainer.udpobserver.UdpDatagram;
import info.unterrainer.udpobserver.UdpObserver;

public class TestObserver {

	/**
	 * STA:{"type":"ENOCEAN","adr":"fef2b30d","data":"eltako_button4","vendor":"eltako","state":{"BI":"pressed","BO":"released","AO":"released","AI":"released"}}
	 */
	private static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
	
	public static void main(String[] args) throws SocketException, InterruptedException {
		UdpObserver observer = new UdpObserver(1901, 256, 1000);
		observer.start();
		System.out.println("listening to socket for 5 seconds...");
		Thread.sleep(5000L);
		outputList(observer.getReceivedDatagrams());
		observer.close();
		System.out.println("done.");
	}
	
	private static void outputList(Collection<UdpDatagram> list) {
		for (UdpDatagram data : list) {
			System.out.println(String.format("[%s] %s", data.getTimestamp().format(formatter), data.getContent()));
		}
	}
}
