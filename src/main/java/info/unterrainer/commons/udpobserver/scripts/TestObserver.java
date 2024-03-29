package info.unterrainer.commons.udpobserver.scripts;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import info.unterrainer.commons.udpobserver.UdpDatagram;
import info.unterrainer.commons.udpobserver.UdpObserver;

public class TestObserver {

	private static DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;

	public static void main(final String[] args) throws InterruptedException, IOException {
		UdpObserver observer = new UdpObserver(8001, 256, 1000);
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
			System.out.println(
					String.format("[%s] %s", data.getMetaData().getTimestamp().format(formatter), data.getContent()));
	}
}
