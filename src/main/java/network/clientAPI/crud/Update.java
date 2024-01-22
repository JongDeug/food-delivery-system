package network.clientAPI.crud;

import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;

import java.io.IOException;

public class Update {

    private final ClientIO clientIO;

    public Update(ClientIO clientIO) {
        this.clientIO = clientIO;
    }

    /**
     * 점주
     * updateBusinessHours
     * 고객
     * updateProfile
     * updateCancleOrder
     */

    public Message updateBusinessHours(String storePk, String storeStartHours, String storeEndHours) throws IOException {
        String str = storePk + "#" + storeStartHours + "#" + storeEndHours;
        Message msg = Message.makeMessage(Packet.TYPE_UPDATE, Packet.CODE_UPDATE_BUSINESS_HOURS, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_UPDATE);
        return msg;
    }

    public Message updateDeliveryFinish(String orderPk, String storeFk) throws IOException {
        String str = orderPk + "#" + storeFk;
        Message msg = Message.makeMessage(Packet.TYPE_UPDATE, Packet.CODE_UPDATE_DELIVERY_FINISH, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_UPDATE);
        return msg;
    }

    public Message updateProfile(String memberPk, String password, String name, String age) throws IOException {
        String str = memberPk + "#" + password + "#" + name + "#" + age;
        Message msg = Message.makeMessage(Packet.TYPE_UPDATE, Packet.CODE_UPDATE_PROFILE, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_UPDATE);
        return msg;
    }

}
