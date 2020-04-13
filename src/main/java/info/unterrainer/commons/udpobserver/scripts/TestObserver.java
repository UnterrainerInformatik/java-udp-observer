package info.unterrainer.commons.udpobserver.scripts;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import info.unterrainer.commons.udpobserver.UdpDatagram;
import info.unterrainer.commons.udpobserver.UdpObserver;

public class TestObserver {

	/**
	 * STA:{"type":"ENOCEAN","adr":"fef2b30d","data":"eltako_button4","vendor":"eltako","state":{"BI":"pressed","BO":"released","AO":"released","AI":"released"}}
	 */
	private static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	public static void main(final String[] args) throws InterruptedException, IOException {
		UdpObserver observer = new UdpObserver(1901, 256, 1000);
		observer.start();
		System.out.println("listening for UDP packets...");
		System.out.println("press <enter> to stop.");
		while (System.in.available() == 0) {
			Thread.sleep(100L);
			outputList(observer.getReceivedDatagrams());
		}
		observer.close();
		System.out.println("done.");
	}

	private static void outputList(final Collection<UdpDatagram> list) {
		for (UdpDatagram data : list)
			System.out.println(String.format("[%s] %s", data.getTimestamp().format(formatter), data.getContent()));
	}
}
