package ru.demo.pnpscada;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSoapClientTest {
	static final Logger log = LoggerFactory.getLogger(SimpleSoapClientTest.class);

	@Test
	public void testCallSoapService() throws Exception {
		log.info("testCallSoapService started");
		Assert.assertEquals("HelloWorldResponse: <HelloWorldResult></HelloWorldResult>",
				SimpleSoapClient.callSoapService(SimpleSoapClient.xml));
	}
}
