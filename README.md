# GPIO

This is an HTTP Server to controll a Raspberry GPIO. With this server you can read and write information to GPIO.
As this it HTTP server the uri to read GPIO state would be /api/v1/input?gpio=gpio_number, where gpio number is according to 
PI4J project, the same happend to output something in this case the URI is /api/vi/output and in the body 
you send a JSON like this {"gpio" : "0", "state" : "high"}.


