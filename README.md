# udp-observer
A small and generic UDP client that listens on a given port returning the datagrams when you ask for them.

# Usage
```java
    UdpObserver observer = new UdpObserver(1901, 256, 1000);
		observer.start();
		System.out.println("listening to socket for 5 seconds...");
		Thread.sleep(5000L);
		outputList(observer.getReceivedDatagrams());
		observer.close();
		System.out.println("done.");
```
