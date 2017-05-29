package ar.com.gpio;


import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;


public class Main {

	// create gpio controller



	public static void main(String args[]) {

		HttpServer server;
		
		try {
			server = HttpServer.create(new InetSocketAddress(8000), 0);
			server.createContext("/test", new HttpRequestHandler());
			server.start();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// myLed.setState(PinState.HIGH);

		// use convenience wrapper method to set state on the pin object
		// myLed.low();

		try {
			Thread.sleep(1000);
		} catch (Exception e) {

		}

		// myLed.high();

		// use toggle method to apply inverse state on the pin object
		// myLed.toggle();

		// use pulse method to set the pin to the HIGH state for
		// an explicit length of time in milliseconds

	}
}
