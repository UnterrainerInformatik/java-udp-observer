package info.unterrainer.udpobserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class UdpObserver extends Thread {

	private DatagramSocket socket;
	private boolean running;
	private byte[] buffer;
	private CircularFifoQueue<UdpDatagram> outputs;

	public UdpObserver(int port, int messageBufferSize, int datagramQueueSize) throws SocketException {
		buffer = new byte[messageBufferSize];
		socket = new DatagramSocket(port);
		outputs = new CircularFifoQueue<>(datagramQueueSize);
	}

	public void run() {
		try {
			running = true;
			while (running) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(packet);
					String received = new String(packet.getData(), 0, packet.getLength());
					outputs.add(UdpDatagram.builder().timestamp(LocalDateTime.now(ZoneOffset.UTC)).content(received).build());
				} catch (IOException e) {
					// NOOP
				}
			}
		} finally {
			if (!socket.isClosed()) {
				socket.close();
			}
			running = false;
		}
	}

	public Collection<UdpDatagram> getReceivedDatagrams() {
		List<UdpDatagram> result = outputs.stream().collect(Collectors.toList());
		outputs.clear();
		return result;
	}

	public void close() {
		running = false;
		socket.close();
	}
}
