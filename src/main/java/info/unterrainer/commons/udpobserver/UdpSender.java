package info.unterrainer.commons.udpobserver;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Objects;

import info.unterrainer.commons.udpobserver.exceptions.UdpSendException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UdpSender {

	private final int senderPort;
	private DatagramSocket sendSocket;
	private List<InetAddress> broadcastAddresses;

	protected List<InetAddress> getBroadcastAddresses() {
		if (broadcastAddresses != null)
			return broadcastAddresses;

		try {
			broadcastAddresses = getAllBroadcastAddresses();
		} catch (SocketException e) {
			return List.of();
		}
		return broadcastAddresses;
	}

	protected DatagramSocket getSendSocket() {
		if (sendSocket == null) {
			try {
				sendSocket = new DatagramSocket(senderPort, InetAddress.getLocalHost());
			} catch (SocketException | UnknownHostException e) {
				log.error("Error retrieving socket.", e);
				throw new UdpSendException(e);
			}
			try {
				sendSocket.setBroadcast(true);
			} catch (SocketException e) {
				log.error("Error setting broadcast-mode.", e);
				throw new UdpSendException(e);
			}
		}
		return sendSocket;
	}

	public void broadcast(final int targetPort, final String content) {
		broadcast(targetPort, content.getBytes(StandardCharsets.UTF_8));
	}

	public void broadcast(final int targetPort, final byte[] bytes) {
		for (InetAddress address : getBroadcastAddresses())
			try {
				send(address, targetPort, bytes);
			} catch (Exception e) {
				// NOOP
			}
	}

	public void send(final InetAddress targetAddress, final int targetPort, final String content) {
		send(targetAddress.getHostAddress(), targetPort, content);
	}

	public void send(final InetAddress targetAddress, final int targetPort, final byte[] bytes) {
		send(targetAddress.getHostAddress(), targetPort, bytes);
	}

	public void send(final String targetAddress, final int targetPort, final String content) {
		send(targetAddress, targetPort, content.getBytes(StandardCharsets.UTF_8));
	}

	public void send(final String targetAddress, final int targetPort, final byte[] bytes) {
		DatagramPacket packet;
		try {
			packet = new DatagramPacket(bytes, bytes.length, InetAddress.getByName(targetAddress), targetPort);
		} catch (UnknownHostException e) {
			log.error("Error creating new packet.", e);
			throw new UdpSendException(e);
		}
		try {
			getSendSocket().send(packet);
		} catch (IOException e) {
			log.error("Error sending packet.", e);
			throw new UdpSendException(e);
		}
	}

	public List<InetAddress> getAllBroadcastAddresses() throws SocketException {
		List<InetAddress> broadcastList = new ArrayList<>();
		Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
		while (interfaces.hasMoreElements()) {
			NetworkInterface networkInterface = interfaces.nextElement();

			if (networkInterface.isLoopback() || !networkInterface.isUp())
				continue;

			networkInterface.getInterfaceAddresses()
					.stream()
					.map(InterfaceAddress::getBroadcast)
					.filter(Objects::nonNull)
					.forEach(broadcastList::add);
		}
		return broadcastList;
	}
}
