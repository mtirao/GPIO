package ar.com.gpio;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ar.com.model.OutputData;
import ar.com.model.Response;

import com.google.gson.Gson;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpInputRequestHandler implements HttpHandler{
	
	final GpioController gpio = GpioFactory.getInstance();
	
	@Override
	public void handle(HttpExchange t) throws IOException {
		
		Gson gson = new Gson();

		String response = ""; 

		Map<String, Object> parameters = new HashMap<String, Object>();

		URI requestedUri = t.getRequestURI();
		String query = requestedUri.getRawQuery();

		parseQuery(query, parameters);

		
		
		if (parameters.get("gpio") != null) {
			
			try {
				int pin = Integer.parseInt(parameters.get("gpio").toString());
				OutputData data = new OutputData(pin, "");
				
				GpioPinDigitalOutput gpioPin = gpio.provisionDigitalOutputPin(
						data.convertPinToPI4J(), "GPIO Pin", PinState.LOW);
				
				response = gson.toJson(new Response(200, "command ok", gpioPin.isHigh()));
			}catch(Exception e) {
				response = gson.toJson(new Response(502, "Invalid GPIO number"));
			}
		}
		
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
		
	}
	
	
	@SuppressWarnings("unchecked")
	private void parseQuery(String query, Map<String, Object> parameters)
			throws UnsupportedEncodingException {

		if (query != null) {
			String pairs[] = query.split("[&]");
			for (String pair : pairs) {
				String param[] = pair.split("[=]");
				String key = null;
				String value = null;
				if (param.length > 0) {
					key = URLDecoder.decode(param[0],
							System.getProperty("file.encoding"));
				}

				if (param.length > 1) {
					value = URLDecoder.decode(param[1],
							System.getProperty("file.encoding"));
				}

				if (parameters.containsKey(key)) {
					Object obj = parameters.get(key);
					if (obj instanceof List<?>) {
						List<String> values = (List<String>) obj;
						values.add(value);

					} else if (obj instanceof String) {
						List<String> values = new ArrayList<String>();
						values.add((String) obj);
						values.add(value);
						parameters.put(key, values);
					}
				} else {
					parameters.put(key, value);
				}
			}
		}
	}


}
