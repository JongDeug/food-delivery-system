package network.clientAPI.crud;

import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;
import persistence.service.MemberService;

import javax.swing.*;
import java.io.IOException;

public class Create {

    private final ClientIO clientIO;

    public Create(ClientIO clientIO) {
        this.clientIO = clientIO;
    }

    /**
     * 점주
     * registerStore
     * registerSignUpForStoreManager
     * registerMenu
     * registerMenuOption
     * registerReplyToReview
     * 고객
     * registerSignUpForMember
     * registerOrder
     * registerCancleOrder
     * registerReviewAndStarRating
     */

    // 점주
    public Message registerStore(String storeName, String storeIntro, String storeAddress, String storeTel, String storeStartHours, String storeEndHours, String storeManagerFk) throws IOException {
        String str = storeName + "#" + storeIntro + "#" + storeAddress + "#" + storeTel + "#" + storeStartHours + "#" + storeEndHours + "#" + storeManagerFk;
        Message msg = Message.makeMessage(Packet.TYPE_CREATE, Packet.CODE_CREATE_STORE, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_CREATE);
        return msg;
    }

    public Message registerSignUpForStoreManager(String id, String password, String name, String age, String tel) throws IOException {
        String str = id + "#" + password + "#" + name + "#" + age + "#" + tel;
        Message msg = Message.makeMessage(Packet.TYPE_CREATE, Packet.CODE_CREATE_SIGNUP_FOR_STORE_MANAGER, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_CREATE);
        return msg;
    }

    public Message registerMenuOption(String storeFk, String menuOptionName, String menuOptionPrice) throws IOException {
        String str = storeFk + "#" + menuOptionName + "#" + menuOptionPrice;
        Message msg = Message.makeMessage(Packet.TYPE_CREATE, Packet.CODE_CREATE_MENU_OPTION, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_CREATE);
        return msg;
    }

    public Message registerMenu(String storeFk, String menuCategory, String menuName, String menuPrice, String menuAvailableOption, String menuAmount) throws IOException {
        String str = storeFk + "#" + menuCategory + "#" + menuName + "#" + menuPrice + "#" + menuAvailableOption + "#" + menuAmount;
        Message msg = Message.makeMessage(Packet.TYPE_CREATE, Packet.CODE_CREATE_MENU, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_CREATE);
        return msg;
    }

    public Message registerReplyToReview(String reviewPk, String storeManagerId, String replyToReviewContent) throws IOException {
        String str = reviewPk + "#" + storeManagerId + "#" + replyToReviewContent;
        Message msg = Message.makeMessage(Packet.TYPE_CREATE, Packet.CODE_CREATE_REPLY_TO_REVIEW, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_CREATE);
        return msg;
    }

    // 회원
    public Message registerSignUpForMember(String id, String password, String name, String age) throws IOException {
        String str = id + "#" + password + "#" + name + "#" + age;
        Message msg = Message.makeMessage(Packet.TYPE_CREATE, Packet.CODE_CREATE_SIGNUP_FOR_MEMBER, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_CREATE);
        return msg;
    }

    public Message registerOrder(String storeFk, String whoOrdered, String menu, String menuOptions) throws IOException {
        String str = storeFk + "#" + whoOrdered + "#" + menu + "#" + menuOptions;
        Message msg = Message.makeMessage(Packet.TYPE_CREATE, Packet.CODE_CREATE_ORDER, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_CREATE);
        return msg;
    }

    public Message registerCancleOrder(String orderPk) throws IOException {
        String str = orderPk;
        Message msg = Message.makeMessage(Packet.TYPE_CREATE, Packet.CODE_CREATE_CANCLE_ORDER, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_CREATE);
        return msg;
    }

    public Message registerReviewAndStarRating(String orderFk, String writer, String reviewContent, String starRating) throws IOException {
        String str = orderFk + "#" + writer + "#" + reviewContent + "#" + starRating;
        Message msg = Message.makeMessage(Packet.TYPE_CREATE, Packet.CODE_CREATE_REVIEW, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_CREATE);
        return msg;
    }
}
