package network.clientAPI.crud;

import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;

import java.io.IOException;

public class Read {

    private final ClientIO clientIO;

    public Read(ClientIO clientIO) {
        this.clientIO = clientIO;
    }

    /**
     * 공통
     * readStoreInfo
     * readStoreMenuInfo
     * readStoreMenuOptionInfo
     * 점주
     * readOrderInfo
     * readStatisticInfoForAdmin
     * readStatisticInfoForStoreManager
     * readMyStoreReview
     * responseReadOrder
     * responseReadCancleOrder
     * 고객
     * readMyOrderInfo
     * readStoreInfoForClient
     * 관리자
     * readStoreInfoForClient
     * readClientInfo
     * readStoreManagerInfo
     * responseReadStore
     * responseReadStoreManager
     * responseReadMenuOption
     * responseReadMenu
     */


    // 공통
    public Message readStoreInfoAll() throws IOException {
        Message msg = new Message();
        msg.setType(Packet.TYPE_READ);
        msg.setCode(Packet.CODE_READ_STORE_INFO);

        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        // 음 이거는 List로 받을텐데..
        return msg;
    }

    public Message readStoreMenuInfo(String storeFk) throws IOException {
        String str = storeFk;
        Message msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_STORE_MENU_INFO, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    public Message readStoreMenuOptionInfo(String storeFk) throws IOException {
        String str = storeFk;
        Message msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_STORE_MENU_OPTION_INFO, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    // 점주
    public Message readOrderInfo(String storeFk) throws IOException {
        String str = storeFk;
        Message msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_ORDER_INFO, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    public Message readStatisticInfoForAdmin(String storeFk) throws IOException {
        String str = storeFk;
        Message msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_STORE_STATISTIC_INFO_FOR_ADMIN, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    public Message readStatisticInfoForStoreManager(String storeFk) throws IOException {
        String str = storeFk;
        Message msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_STORE_STATISTIC_INFO_FOR_STORE_MANAGER, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    public Message readMyStoreReview(String storePk) throws IOException {
        String str = storePk;
        Message msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_MY_STORE_REVIEW, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    // 고객
    public Message readMyOrderInfo(String memberId) throws IOException {
        String str = memberId;
        Message msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_MY_ORDER_INFO, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    public Message readMyOrderResInfo(String memberId) throws IOException {
        String str = memberId;
        Message msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_MY_ORDER_INFO_RES, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    // 관리자
    public Message readStoreInfoForClient(String storePk) throws IOException {
        String str = storePk;
        Message msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_STORE_INFO_FOR_CLIENT, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }


    public Message readClientInfo() throws IOException {
        Message msg = new Message();
        msg.setType(Packet.TYPE_READ);
        msg.setCode(Packet.CODE_READ_CLIENT_INFO);

        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    public Message readStoreManagerInfo() throws IOException {
        Message msg = new Message();
        msg.setType(Packet.TYPE_READ);
        msg.setCode(Packet.CODE_READ_STORE_MANAGER_INFO);

        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }


    // 점주 요청 출력
    public Message responseReadOrder(String storeFk) throws IOException {
        Message msg = new Message();
        msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_RESPONSE_ORDER, storeFk);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    public Message responseReadCancleOrder(String storeFk) throws IOException {
        Message msg = new Message();
        msg = Message.makeMessage(Packet.TYPE_READ, Packet.CODE_READ_RESPONSE_CANCLE_ORDER, storeFk);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    // 관리자 요청 출력
    public Message responseReadStore() throws IOException {
        Message msg = new Message();
        msg.setType(Packet.TYPE_READ);
        msg.setCode(Packet.CODE_READ_RESPONSE_STORE);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    public Message responseReadStoreManager() throws IOException {
        Message msg = new Message();
        msg.setType(Packet.TYPE_READ);
        msg.setCode(Packet.CODE_READ_RESPONSE_STORE_MANAGER);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    public Message responseReadMenuOption() throws IOException {
        Message msg = new Message();
        msg.setType(Packet.TYPE_READ);
        msg.setCode(Packet.CODE_READ_RESPONSE_OPTION);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }

    public Message responseReadMenu() throws IOException {
        Message msg = new Message();
        msg.setType(Packet.TYPE_READ);
        msg.setCode(Packet.CODE_READ_RESPONSE_MENU);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_READ);
        return msg;
    }
}
