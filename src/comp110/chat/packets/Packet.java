package comp110.chat.packets;

/**
 * A Packet represents a single bundle of information sent to or from the server.
 * 
 * The Chat110 protocol is simplistic for illustrative purposes. Each packet is a colon-delimited String beginning with
 * the type of packet it is and followed by one or more depending on its type.
 * 
 * type:param:param:param
 * 
 * Examples:
 * 
 * auth:onyen:key
 * 
 * dm:onyen:message
 * 
 * who
 * 
 * We are providing the implementation of this class, because it is fairly boring. You should familiarize yourself with
 * its constructor and methods. Notice it makes use of the split method of the String class which is very similar to the
 * split method you may have implemented in SillyString.
 * 
 * You should not modify this class.
 */
public class Packet {

    private String _data;
    private String[] _chunks;

    /**
     * A Packet is constructed from a String.
     * 
     * @param data
     */
    public Packet(String data) {
        _data = data;
        _chunks = data.split(":");
    }

    /**
     * Each packet's first chunk is always its type. This convenience method allows you to ask a Packet what type of
     * packet it is.
     */
    public String getType() {
        return _chunks[0];
    }

    /**
     * The size method will tell you how many parameters a Packet has.
     */
    public int size() {
        return _chunks.length - 1;
    }

    /**
     * The chunks which follow the first type chunk are considered "parameters" to a packet. This convenience method
     * allows you to ask a packet for a parameter at a specific index.
     * 
     * Notice there is no check as to whether this parameter exists. In your code, you'll need to be careful to check
     * the size of a Packet (above) before asking for a given parameter.
     * 
     * @param n
     */
    public String getParameter(int n) {
        return _chunks[n + 1];
    }

    /**
     * Ask a packet for *all* chunks starting from some parameter # until the end. This is useful for packets which may
     * contain text that makes use of the : in the text. For example:
     * 
     * dm:krisj:Hey, Kris, what happens when I put a : in the middle of a dm packet?
     * 
     * The dm type only has 3 parts: type, onyen, and message. Notice the above dm has 4 parts. The join method below
     * allows you to ignore that colon in the middle of the message.
     * 
     * @param n
     */
    public String join(int n) {
        String result = null;
        for (int i = n + 1; i < _chunks.length; i++) {
            if (result == null) {
                result = _chunks[i];
            } else {
                result = result + ":" + _chunks[i];
            }
        }
        return result;
    }

    /**
     * String representation of a Packet sent to/from the Server.
     */
    public String toString() {
        return _data;
    }

}