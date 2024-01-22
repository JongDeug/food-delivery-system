package network.clientAPI;

import network.clientAPI.crud.Create;
import network.clientAPI.crud.Read;
import network.clientAPI.crud.Response;
import network.clientAPI.crud.Update;
import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;

import java.io.IOException;

public class Crud {
    protected final Create create;
    protected final Read read;
    protected final Update update;
    protected final Response res;
    protected final ClientIO clientIO;

    public Crud(ClientIO clientIO) {
        this.create = new Create(clientIO);
        this.read = new Read(clientIO);
        this.update = new Update(clientIO);
        this.res= new Response(clientIO);
        this.clientIO = clientIO;
    }

    /**
     *  공통 조회
     */
    public Message readStoreInfoAll() throws IOException {
        Message msg = read.readStoreInfoAll();
        return msg;
    }

    public Message readStoreMenuInfo(String storeFk) throws IOException {
        Message msg = read.readStoreMenuInfo(storeFk);
        return msg;
    }

    public Message readStoreMenuOptionInfo(String storeFk) throws IOException {
        Message msg = read.readStoreMenuOptionInfo(storeFk);
        return msg;
    }

    public Message exit() throws IOException {
        Message msg = new Message();
        msg.setType(Packet.TYPE_EXIT);
        clientIO.send(msg);
        return msg;
    }
}
