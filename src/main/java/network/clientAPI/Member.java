package network.clientAPI;

import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;

import java.io.IOException;

public class Member extends Crud {
    public Member(ClientIO clientIO) {
        super(clientIO);
    }

    /**
     * 로그인
     */
    public Message login(String id, String password) throws IOException {
        String str = id + "#" + password;
        Message msg = Message.makeMessage(Packet.TYPE_LOGIN, Packet.CODE_LOGIN_MEMBER, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_LOGIN);
        return msg;
    }

    /**
     * 등록
     */
    public Message registerSignUp(String id, String password, String name, String age) throws IOException {
        Message msg = create.registerSignUpForMember(id, password, name, age);
        return msg;
    }

    public Message registerOrder(String storeFk, String whoOrdered, String menu, String menuOptions) throws IOException {
        Message msg = create.registerOrder(storeFk, whoOrdered, menu, menuOptions);
        return msg;
    }

    public Message registerCancleOrder(String orderPk) throws IOException {
        Message msg = create.registerCancleOrder(orderPk);
        return msg;
    }

    public Message registerReviewAndStarRating(String orderFk, String writer, String reviewContent, String starRating) throws IOException {
        Message msg = create.registerReviewAndStarRating(orderFk, writer, reviewContent, starRating);
        return msg;
    }

    /**
     * 조회
     */
    public Message readStoreInfoForClient(String storePk) throws IOException {
        Message msg = read.readStoreInfoForClient(storePk);
        return msg;
    }

    public Message readMyOrderInfo(String memberId) throws IOException {
        Message msg = read.readMyOrderInfo(memberId);
        return msg;
    }

    public Message readMyOrderResInfo(String memberId) throws IOException {
        Message msg = read.readMyOrderResInfo(memberId);
        return msg;
    }

    /**
     * 수정
     */
    public Message updateProfile(String memberPk, String password, String name, String age) throws IOException {
        Message msg = update.updateProfile(memberPk, password, name, age);
        return msg;
    }

}
