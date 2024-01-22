package network.connect;

import java.io.IOException;
import java.net.Socket;

public class ServerIO extends IO{

    /**
     * Server 전용
     */
    public ServerIO(Socket socket) throws IOException {
        super(socket);
    }

}
