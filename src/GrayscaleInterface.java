import java.rmi.Remote;
import java.rmi.RemoteException;

public interface GrayscaleInterface extends Remote {
	int[] convert(int[] image) throws RemoteException	;
}
