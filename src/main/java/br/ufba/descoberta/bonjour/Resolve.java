package br.ufba.descoberta.bonjour;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.security.Security;

import javax.ws.rs.core.MediaType;

import com.apple.dnssd.DNSSD;
import com.apple.dnssd.DNSSDException;
import com.apple.dnssd.DNSSDService;
import com.apple.dnssd.ResolveListener;
import com.apple.dnssd.TXTRecord;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;



public class Resolve implements ResolveListener
{
	DNSSDService r;
// Display error message on failure
public void operationFailed(DNSSDService service, int errorCode)
  {
  System.out.println("Resolve failed " + errorCode);
  System.exit(-1);
  }

// Display information when service is resolved
public void serviceResolved(DNSSDService resolver, int flags, int ifIndex,
  String fullName, String hostName, int port, TXTRecord txtRecord)
  {
	
  String deviceIp="";
try {
	deviceIp = Inet4Address.getByName(hostName).getHostAddress();
} catch (UnknownHostException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}	
  System.out.println("Service Resolved: " + hostName + ":" + port);
  System.out.println("Flags: " + flags +
    ", ifIndex: " + ifIndex + ", FQDN: " + fullName+ ", IP: "+deviceIp );
  

	
  comunicaServico(hostName);

  

  for (int i = 0; i < txtRecord.size(  ); i++)
    {
    String key = txtRecord.getKey(i);
    String value = txtRecord.getValueAsString(i);
    if (key.length(  ) > 0) System.out.println("\t" + key + "=" + value);
    }
  }

public Resolve(String name, String domain)
  throws DNSSDException, InterruptedException
  {
  System.out.println("TestResolve Starting");
   r = DNSSD.resolve(0, DNSSD.ALL_INTERFACES,
    name, "_http._tcp", domain, this);
  System.out.println("TestResolve Running");
 
  //System.out.println("TestResolve Stopping");
  //r.stop(  );
  }

public  void stopResolving()
{
	
	
	r.stop();
  System.out.println("TestResolve Stopping");
}

private void comunicaServico(String name)
{
	ClientConfig config = new DefaultClientConfig();
	Client client = Client.create(config);
	

	WebResource service = client.resource("http://localhost:8080/GeraDevs/geraApp");

	Form form = new Form();
	
	form.add("name", name);
	ClientResponse response = service
			.type(MediaType.APPLICATION_FORM_URLENCODED)
			.post(ClientResponse.class, form);
	
	String StringResponse = response.getEntity(String.class).toString();
	System.out.println(StringResponse);
}

public static void main(String[] args)
  {
  if (args.length != 2)
    {
    System.out.println("Usage: java TestResolve name domain");
    System.exit(-1);
    }
  else
    {
    try
      {
      new Resolve(args[0], args[1]);
      }
    catch(Exception e)
      {
      e.printStackTrace(  );
      System.exit(-1);
      }
    }
  }
}
