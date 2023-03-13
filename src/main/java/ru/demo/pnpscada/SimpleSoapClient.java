package ru.demo.pnpscada;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SimpleSoapClient {

	static final Logger log = LoggerFactory.getLogger(SimpleSoapClient.class);

	private static final String URL_PNPSCADA_SERVICE_SOAP12 = "http://brightspark-adam.pnpscada.com/soap/NESserviceSoap12";
	
	public static final String xml = "<soapenv:Envelope"
			+ " xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\""
			+ " xmlns:tem=\"http://tempuri.org/\">\r\n"
			+ "   <soapenv:Header/>\r\n"
			+ "   <soapenv:Body>\r\n"
			+ "      <tem:HelloWorld/>\r\n"
			+ "   </soapenv:Body>\r\n"
			+ "</soapenv:Envelope>";
	
	public static void main(String args[]) {
		String response = callSoapService(xml);
		log.info(response);
	}

	public static String callSoapService(String soapRequest) {
		log.info("callSoapService started");
		try {
			HttpURLConnection con = initConnection();
			runSoapRequest(soapRequest, con);
			String responseStatus = con.getResponseMessage();
			log.info("responseStatus = " + responseStatus);
			String response = getSoapResponse(con);
			log.debug("response = " + response);
			response = response.substring(response.indexOf("<HelloWorldResponse") + "<HelloWorldResponse xmlns=\"http://tempuri.org/\">".length(), 
					response.indexOf("</HelloWorldResponse>"));
			return "HelloWorldResponse: " + response;
		} catch (IOException e) {
			log.error("error", e);
			return e.getMessage();
		}
	}

	private static String getSoapResponse(HttpURLConnection con) throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
		return response.toString();
	}

	private static void runSoapRequest(String soapRequest, HttpURLConnection con) throws IOException {
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(soapRequest);
		wr.flush();
		wr.close();
	}

	private static HttpURLConnection initConnection() throws MalformedURLException, IOException, ProtocolException {
		URL url = new URL(URL_PNPSCADA_SERVICE_SOAP12);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
		con.setDoOutput(true);
		return con;
	}
}
