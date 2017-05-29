package ar.com.gpio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;

import ar.com.model.OutputData;
import ar.com.model.Response;

import com.google.gson.Gson;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpOutputRequestHandler implements HttpHandler {
	
	
	@Override
	public void handle(HttpExchange t) throws IOException {

		Gson gson = new Gson();

		final GpioController gpio = GpioFactory.getInstance();

		
		InputStreamReader isr = new InputStreamReader(t.getRequestBody(), "utf-8");
        BufferedReader br = new BufferedReader(isr);
        String query = br.readLine();
		
        OutputData data = gson.fromJson(query, OutputData.class);

        GpioPinDigitalOutput gpioPin = null;
        String response = "";
        
		try {
			
			gpioPin = (GpioPinDigitalOutput)gpio.getProvisionedPin(data.convertPinToPI4J());
			
			if (gpioPin == null) {
				gpioPin = gpio.provisionDigitalOutputPin(
						data.convertPinToPI4J(), "GPIO Pin", PinState.LOW);
			}
			
			
			
			
			if(data.getState().equalsIgnoreCase("high")) {
				response = gson.toJson(new Response(200, "Command Ok"));
				gpioPin.setShutdownOptions(true, PinState.HIGH);
				gpioPin.high();
			}else if(data.getState().equalsIgnoreCase("low")) {
				response = gson.toJson(new Response(200, "Command Ok"));
				gpioPin.setShutdownOptions(true, PinState.LOW);
				gpioPin.low();
			}else if(data.getState().equalsIgnoreCase("toggle")) {
				response = gson.toJson(new Response(200, "Command Ok"));
				gpioPin.toggle();
			} else {
				
				int time = 0;
				try {
					time = Integer.parseInt(data.getState());
					gpioPin.pulse(time, true);
					response = gson.toJson(new Response(200, "Command Ok"));
				}catch(Exception e) {
					response = gson.toJson(new Response(501, e.getMessage()));
				}
				
			}
			
		}catch(Exception e) {
			response = gson.toJson(new Response(503, "Gpio error"));
		}
		
	
		gpio.shutdown();

		t.sendResponseHeaders(200, response.length());
		
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		
	}

	
}
