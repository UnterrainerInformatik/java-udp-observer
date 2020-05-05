package info.unterrainer.commons.udpobserver;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class UdpDatagram {

	private UdpDatagramMetaData metaData;

	private String content;
	private byte[] bytes;
}
