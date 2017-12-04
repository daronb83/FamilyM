package fox.glass.com.family.model;

import java.io.Serializable;

/**
 * Holds the Server connection data
 */
public class Server implements Serializable {

    private static Server _inst;
    private static String host;
    private static String port;

    private Server() {
        host = "";
        port = "";
    }

    public static Server instance() {

        if (_inst == null) {
            _inst = new Server();
        }

        return _inst;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String hostName) {
        host = hostName;
    }

    public static String getPort() {
        return port;
    }

    public static void setPort(String portNumber) {
        port = portNumber;
    }

    public static boolean isInstantiated() {
        return (host.length() > 0 && port.length() > 0);
    }

    @Override
    public String toString() {
        return host + ":" + port;
    }
}
