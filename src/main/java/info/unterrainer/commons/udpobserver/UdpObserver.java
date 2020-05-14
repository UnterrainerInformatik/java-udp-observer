package info.unterrainer.commons.udpobserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.collections4.queue.CircularFifoQueue;

public class UdpObserver extends Thread {

	private DatagramSocket socket;
	private boolean running;
	private byte[] buffer;
	private CircularFifoQueue<UdpDatagram> outputs;

	public UdpObserver(final int port, final int messageBufferSize, final int datagramQueueSize)
			throws SocketException {
		buffer = new byte[messageBufferSize];
		socket = new DatagramSocket(port);
		outputs = new CircularFifoQueue<>(datagramQueueSize);
	}

	@Override
	public void run() {
		try {
			running = true;
			while (running) {
				DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
				try {
					socket.receive(packet);
					String received = new String(packet.getData(), 0, packet.getLength());
					InetSocketAddress adr = (InetSocketAddress) packet.getSocketAddress();
					outputs.add(UdpDatagram.builder()
							.metaData(UdpDatagramMetaData.builder()
									.hostString(adr.getHostString())
									.sender(adr.getAddress())
									.sendPort(adr.getPort())
									.timestamp(LocalDateTime.now(ZoneOffset.UTC))
									.build())
							.content(received)
							.bytes(Arrays.copyOf(packet.getData(), packet.getLength()))
							.build());
				} catch (IOException e) {
					// NOOP
				}
			}
		} finally {
			if (!socket.isClosed())
				socket.close();
			running = false;
		}
	}

	public Collection<UdpDatagram> getReceivedDatagrams() {
		List<UdpDatagram> result = outputs.stream().filter(Objects::nonNull).collect(Collectors.toList());
		outputs.clear();
		return result;
	}

	public void close() {
		running = false;
		socket.close();
	}
}
