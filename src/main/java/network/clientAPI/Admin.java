package network.clientAPI;

import lombok.Getter;
import lombok.Setter;
import network.connect.ClientIO;
import network.dataPacket.Message;
import network.dataPacket.Packet;
import persistence.dto.MemberDTO;
import persistence.dto.MenuDTO;
import persistence.dto.OptionDTO;
import persistence.dto.StoreDTO;
import persistence.service.StoreManagerService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Admin extends Crud {


    public Admin(ClientIO clientIO) {
        super(clientIO);
    }

    /**
     * 로그인
     */
    public Message login(String id, String password) throws IOException {
        String str = id + "#" + password;
        Message msg = Message.makeMessage(Packet.TYPE_LOGIN, Packet.CODE_LOGIN_ADMIN, str);
        clientIO.send(msg);
        msg = clientIO.receive(Packet.TYPE_LOGIN);
        return msg;
    }

    /**
     * 조회
     */
    public Message readClientInfo() throws IOException {
        Message msg = read.readClientInfo();
        return msg;
    }

    public Message readStoreManagerInfo() throws IOException {
        Message msg = read.readStoreManagerInfo();
        return msg;
    }

    public Message readStatisticInfo(String storeFk) throws IOException {
        Message msg = read.readStatisticInfoForAdmin(storeFk);
        return msg;
    }

    public Message responseReadStore() throws IOException {
        Message msg = read.responseReadStore();
        return msg;
    }

    public Message responseReadStoreManager() throws IOException {
        Message msg = read.responseReadStoreManager();
        return msg;
    }

    public Message responseReadMenuOption() throws IOException {
        Message msg = read.responseReadMenuOption();
        return msg;
    }

    public Message responseReadMenu() throws IOException {
        Message msg = read.responseReadMenu();
        return msg;
    }

    /**
     * 응답
     */
    public Message responseStore(String index, String response) throws IOException {
        Message msg = res.responseStore(index, response);
        return msg;
    }

    public Message responseStoreManager(String index, String response) throws IOException {
        Message msg = res.responseStoreManager(index, response);
        return msg;
    }

    public Message responseMenuOption(String index, String response) throws IOException {
        Message msg = res.responseMenuOption(index, response);
        return msg;
    }

    public Message responseMenu(String index, String response) throws IOException {
        Message msg = res.responseMenu(index, response);
        return msg;
    }

}
