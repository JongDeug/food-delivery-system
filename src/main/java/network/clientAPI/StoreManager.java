package network.clientAPI;

import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;

import java.io.IOException;

public class StoreManager extends Crud {


    public StoreManager(ClientIO clientIO) {
        super(clientIO);
    }

    /**
     * 로그인
     */
    public Message login(String id, String password) throws IOException {
        String str = id + "#" + password;
        Message msg = Message.makeMessage(Packet.TYPE_LOGIN, Packet.CODE_LOGIN_STORE_MANAGER, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_LOGIN);
        return msg;
    }

    /**
     * 등록
     */
    public Message registerSignUp(String id, String password, String name, String age, String tel) throws IOException {
        Message msg = create.registerSignUpForStoreManager(id, password, name, age, tel);
        return msg;
    }

    public Message registerStore(String storeName, String storeIntro, String storeAddress, String storeTel, String storeStartHours, String storeEndHours, String storeManagerFk) throws IOException {
        Message msg = create.registerStore(storeName, storeIntro, storeAddress, storeTel, storeStartHours, storeEndHours, storeManagerFk);
        return msg;
    }

    public Message registerMenuOption(String storeFk, String menuOptionName, String menuOptionPrice) throws IOException {
        Message msg = create.registerMenuOption(storeFk, menuOptionName, menuOptionPrice);
        return msg;
    }

    public Message registerMenu(String storeFk, String menuCategory, String menuName, String menuPrice, String menuAvailableOption, String menuAmount) throws IOException {
        Message msg = create.registerMenu(storeFk, menuCategory, menuName, menuPrice, menuAvailableOption, menuAmount);
        return msg;
    }

    public Message registerReplyToReview(String reviewPk, String storeManagerId, String replyToReviewContent) throws IOException {
        Message msg = create.registerReplyToReview(reviewPk, storeManagerId, replyToReviewContent);
        return msg;
    }

    /**
     * 조회
     */
    public Message readStatisticInfo(String storeFk) throws IOException {
        Message msg = read.readStatisticInfoForStoreManager(storeFk);
        return msg;
    }

    public Message readMyStoreReview(String storePk) throws IOException {
        Message msg = read.readMyStoreReview(storePk);
        return msg;
    }

    public Message readOrderInfo(String storeFk) throws IOException {
        Message msg = read.readOrderInfo(storeFk);
        return msg;
    }

    public Message responseReadOrder(String storeFk) throws IOException {
        Message msg = read.responseReadOrder(storeFk);
        return msg;
    }

    public Message responseReadCancleOrder(String storeFk) throws IOException {
        Message msg = read.responseReadCancleOrder(storeFk);
        return msg;
    }

    /**
     * 수정
     */
    public Message updateBusinessHours(String storePk, String storeStartHours, String storeEndHours) throws IOException {
        Message msg = update.updateBusinessHours(storePk, storeStartHours, storeEndHours);
        return msg;
    }

    public Message updateDeliveryFinish(String orderPk, String storeFk) throws IOException {
        Message msg = update.updateDeliveryFinish(orderPk, storeFk);
        return msg;
    }


    /**
     * 응답
     */
    public Message responseOrder(String index, String response) throws IOException {
        Message msg = res.responseOrder(index, response);
        return msg;
    }

    public Message responseCancleOrder(String index, String response) throws IOException {
        Message msg = res.responseCancleOrder(index, response);
        return msg;
    }
}
