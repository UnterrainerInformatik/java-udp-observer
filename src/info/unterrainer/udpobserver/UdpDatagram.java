package info.unterrainer.udpobserver;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UdpDatagram {
	private LocalDateTime timestamp;
	private String content;
}
