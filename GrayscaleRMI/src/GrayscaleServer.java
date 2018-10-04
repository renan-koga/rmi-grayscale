import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;

public class GrayscaleServer extends UnicastRemoteObject implements GrayscaleInterface {

	protected GrayscaleServer() throws RemoteException {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public int[] convert(int[] image) throws RemoteException {
		// TODO Auto-generated method stub
		int size = image.length;
		
		for(int y = 0; y < size; y++){
	        int p = image[y];
	
	        int a = (p>>24)&0xff;
	        int r = (p>>16)&0xff;
	        int g = (p>>8)&0xff;
	        int b = p&0xff;
	
	        //calculando média
	        int avg = (r+g+b)/3;
	
	        //trocando o valor RGB pela média calculada
	        p = (a<<24) | (avg<<16) | (avg<<8) | avg;
	
	        image[y] = p;
	    }
				
		return image;
	}
	
	@SuppressWarnings("deprecation")
	public static void main(String [] args) {
		// install RMI security manager
		System.setSecurityManager(new RMISecurityManager());
		// arg. 0 = rmi url
		if (args.length!=1) {
			System.err.println("Usage: GrayscaleServer <server-rmi-url>");
			System.exit(-1);
		}
		try {
			// name with which we can find it = user name
			String name = args[0];
			//create new instance
			GrayscaleServer server = new GrayscaleServer();
			// register with nameserver
			Naming.rebind(name, server);
			System.out.println("Started GrayscaleInterface, registered as " + name);
			}
			catch(Exception e) {
			System.out.println("Caught exception while registering: " + e);
			System.exit(-1);
		}
	}
}
