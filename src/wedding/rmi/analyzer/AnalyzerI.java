package wedding.rmi.analyzer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Antonina on 6/27/2017.
 */
public interface AnalyzerI extends Remote {
    public List getBestCouples(List brides, List grooms) throws RemoteException;
}
