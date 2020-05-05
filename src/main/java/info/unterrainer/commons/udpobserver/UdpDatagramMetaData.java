package info.unterrainer.commons.udpobserver;

import java.net.InetAddress;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UdpDatagramMetaData {

	private String hostString;
	private InetAddress sender;
	private int sendPort;

	private LocalDateTime timestamp;
}
