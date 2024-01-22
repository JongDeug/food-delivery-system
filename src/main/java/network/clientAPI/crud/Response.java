package network.clientAPI.crud;

import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;

import java.io.IOException;

public class Response {

    private final ClientIO clientIO;

    public Response(ClientIO clientIO) {
        this.clientIO = clientIO;
    }

    /**
     * 점주
     * responseOrder
     * responseStore
     * 관리자
     * responseStore
     * responseMenu
     * responseMenuOption
     * responseMenu
     */

    // 점주
    public Message responseOrder(String index, String response) throws IOException {
        String str = index + "#" + response;
        Message msg = Message.makeMessage(Packet.TYPE_RESPONSE, Packet.CODE_RESPONSE_ORDER, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_RESPONSE);
        return msg;
    }

    public Message responseCancleOrder(String index, String response) throws IOException {
        String str = index + "#" + response;
        Message msg = Message.makeMessage(Packet.TYPE_RESPONSE, Packet.CODE_RESPONSE_ORDER_CANCLE, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_RESPONSE);
        return msg;
    }

    // 관리자
    public Message responseStore(String index, String response) throws IOException {
        String str = index + "#" + response;
        Message msg = Message.makeMessage(Packet.TYPE_RESPONSE, Packet.CODE_RESPONSE_STORE, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_RESPONSE);
        return msg;
    }

    public Message responseStoreManager(String index, String response) throws IOException {
        String str = index + "#" + response;
        Message msg = Message.makeMessage(Packet.TYPE_RESPONSE, Packet.CODE_RESPONSE_STORE_MANAGER_SIGNUP, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_RESPONSE);
        return msg;
    }

    public Message responseMenuOption(String index, String response) throws IOException {
        String str = index + "#" + response;
        Message msg = Message.makeMessage(Packet.TYPE_RESPONSE, Packet.CODE_RESPONSE_OPTION, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_RESPONSE);
        return msg;
    }

    public Message responseMenu(String index, String response) throws IOException {
        String str = index + "#" + response;
        Message msg = Message.makeMessage(Packet.TYPE_RESPONSE, Packet.CODE_RESPONSE_MENU, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_RESPONSE);
        return msg;
    }
}
