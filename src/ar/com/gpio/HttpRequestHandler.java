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

import ar.com.model.Data;

import com.google.gson.Gson;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class HttpRequestHandler implements HttpHandler {

	final GpioController gpio = GpioFactory.getInstance();
	final GpioPinDigitalOutput led1 = gpio.provisionDigitalOutputPin(
			RaspiPin.GPIO_00, "My LED", PinState.LOW);
	final GpioPinDigitalOutput led2 = gpio.provisionDigitalOutputPin(
			RaspiPin.GPIO_02, "My LED", PinState.LOW);
	final GpioPinDigitalOutput led3 = gpio.provisionDigitalOutputPin(
			RaspiPin.GPIO_03, "My LED", PinState.LOW);

	@Override
	public void handle(HttpExchange t) throws IOException {

		Gson gson = new Gson();

		String response = gson.toJson(new Data("This is the response", 1, 1));

		Map<String, Object> parameters = new HashMap<String, Object>();
		t.sendResponseHeaders(200, response.length());

		URI requestedUri = t.getRequestURI();
		String query = requestedUri.getRawQuery();

		parseQuery(query, parameters);

		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();

		if (parameters.get("led") != null) {
			String value = parameters.get("led").toString();
			switch (Integer.parseInt(value)) {
			case 1:
				led1.pulse(1000);
				break;
			case 2:
				led2.pulse(1000);
				break;
			case 3:
				led3.pulse(1000);
				break;
			default:
				led1.pulse(1000);
				led2.pulse(1000);
				led3.pulse(1000);
			}
		}
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
