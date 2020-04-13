module udpserver {
	requires org.apache.commons.collections4;
	
	requires lombok;
	requires com.fasterxml.jackson.annotation;
	requires com.fasterxml.jackson.databind;
	requires junit;
	
	opens info.unterrainer.udpobserver to com.fasterxml.jackson.databind;
}