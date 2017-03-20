package comp110.chat.packets;

/**
 * ConnectionObservers can be registered with a Connection via a Connection's addObserver method. Once registered, the
 * ConnectionObserver object will be notified every single time a Packet is sent or received over the Connection.
 */
public interface ConnectionObserver {

    /**
     * After a Packet is sent over a Connection, every observer's packetSent method will be called with the Packet that
     * was sent.
     * 
     * @param packet
     */
    public void packetSent(Packet packet);

    /**
     * After a Packet is received over a Connection, every observer's packetReceived method will be called with the
     * packet received from the server.
     * 
     * @param packet
     */
    public void packetReceived(Packet packet);

}