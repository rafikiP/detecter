package br.ufba.descoberta.bonjour;

import br.ufba.descoberta.bonjour.Resolve;

import com.apple.dnssd.*;

public class Listener implements BrowseListener
{
	Resolve resolver;
// Display error message on failure
public void operationFailed(DNSSDService service, int errorCode)
  {
  System.out.println("Browse failed " + errorCode);
  System.exit(-1);
  }

// Display services we discover
public void serviceFound(DNSSDService browser, int flags, int ifIndex,
          String name, String regType, String domain)
  {
  System.out.println("Add flags:" + flags + ", ifIndex:" + ifIndex +
    ", Name:" + name + ", Type:" + regType + ", Domain:" + domain);
  
  String[] args= new String[2];
  args[0]=name;
  args[1]=domain;
  try {
	 resolver=new Resolve(name,domain);
} catch (DNSSDException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
} catch (InterruptedException e) {
	// TODO Auto-generated catch block
	e.printStackTrace();
}
  }

// Print a line when services go away
public void serviceLost(DNSSDService browser, int flags, int ifIndex,
          String name, String regType, String domain)
  {
  System.out.println("Rmv flags:" + flags + ", ifIndex:" + ifIndex +
    ", Name:" + name + ", Type:" + regType + ", Domain:" + domain);
  }

public Listener(  ) throws DNSSDException, InterruptedException
  {
  System.out.println("TestBrowse Starting");
  DNSSDService b = DNSSD.browse("_http._tcp", this);
  System.out.println("TestBrowse Running");
  //Thread.sleep(3000000);
 // System.out.println("TestBrowse Stopping");
// if (resolver!=null) 
 // resolver.stopResolving();
 // b.stop(  );
  }

public static void main(String[] args)
  {
  try { new Listener(  ); }
  catch(Exception e)
    {
    e.printStackTrace(  );
    System.exit(-1);
    }
  }
}
