package Lab6.src.B8;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FlightInterface extends Remote {
    boolean bookTicket(String flightNumber, int seats) throws RemoteException;

    int availableSeats(String flightNumber) throws RemoteException;

    boolean cancelBooking(String flightNumber, int seats) throws RemoteException;
}
