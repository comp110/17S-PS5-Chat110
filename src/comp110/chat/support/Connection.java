package comp110.chat.support;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.websocket.DeploymentException;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.Session;

import org.glassfish.tyrus.client.ClientManager;

import javafx.application.Platform;

/**
 * This class is used to make a connection to a Chat110 server and send messages.
 * 
 * Additionally, objects which implement the PacketObserver interface can register themselves with a Connection object
 * to be notified when a Packet is sent or received.
 * 
 * This class *minimally* wraps around the Tyrus Client library to simplify usage for COMP110.
 * 
 * You should not modify this class.
 */
public class Connection extends Endpoint {

    /**
     * A "session" is the Tyrus library's notion of a single connection made to a server.
     */
    private Session _session;

    /**
     * We will hold a list of objects implementing the PacketObserver interface for notification.
     */
    private List<ConnectionObserver> _observers;

    /**
     * Constructing a new Connection object does not connect upon construction. This allows any PacketObservers to be
     * registered before messages begin coming in.
     */
    public Connection() {
        super();
        _observers = new ArrayList<ConnectionObserver>();
    }

    /**
     * Register an object to observe packets being sent or received over this Connection.
     */
    public void addObserver(ConnectionObserver observer) {
        _observers.add(observer);
    }

    /**
     * Unregister an object that was previously observing packets.
     */
    public void removeObserver(ConnectionObserver observer) {
        _observers.remove(observer);
    }

    /**
     * Open a connection to the server. This must occur before messages can be sent or received from the server.
     * 
     * @param uri
     *            The WebSocket address to connect to.
     */
    public void connect(String uri) {
        try {
            ClientManager client = ClientManager.createClient();
            _session = client.connectToServer(this, new URI(uri));
            _session.addMessageHandler(String.class, this::receive);
        } catch (IOException | DeploymentException | URISyntaxException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /**
     * Sends a packet to the server. Notifies all packet observers a packet is sent.
     * 
     * @param packet
     */
    public void send(Packet packet) {
        try {
            _session.getBasicRemote().sendText(packet.toString());
            this.notifyObserversOfSend(packet);
        } catch (IOException e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }

    /*
     * Helper methods follow! These are declared private because they are only intended for internal use by the
     * Connection class itself.
     */

    /**
     * Notify all observers of a packet being sent.
     * 
     * @param packet
     */
    private void notifyObserversOfSend(Packet packet) {
        for (ConnectionObserver observer : _observers) {
            observer.packetSent(packet);
        }
    }

    /**
     * When a data packet is received from the server, this method will be called. It is registered on _session in the
     * connect method. It turns the raw String data into a Packet object and then notifies all observers of the Packet
     * received.
     * 
     * @param data
     */
    private void receive(String data) {
        Packet packet = new Packet(data);
        for (ConnectionObserver observer : _observers) {
            // This Platform.runLater business is tricky business. To understand *why* it is needed takes some 401
            // knowledge about threads.
            Platform.runLater(() -> observer.packetReceived(packet));
        }
    }

    /**
     * This method is a noop short for "no operation" - It does nothing and is not used. It only exists because it is
     * declared abstract on the Endpoint class. You can learn all about abstract methods in 401 :)
     * 
     * This method is called when the connection to the server actually opens up.
     * 
     * Typically you would *want* to keep track of connectedness and also implement an onClose method. We are not
     * worrying about handling disconnects gracefully. In upper level courses or real world projects, you certainly
     * should!
     */
    public void onOpen(Session session, EndpointConfig config) {
    }

}