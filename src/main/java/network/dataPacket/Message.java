package network.dataPacket;

import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public class Message {

    /**
     * 기본 패킷 구성
     * [Type][Code][BodyLength][Body]
     **/

    private byte type;
    private byte code;
    private int bodyLength;
    private String body;

    public Message() {
        this.type = 0;
        this.code = 0;
        this.bodyLength = 0;
        this.body = null;
    }

    // Message 생성
    public static Message makeMessage(byte type, byte code, String body) {
        Message message = new Message();
        message.setType(type);
        message.setCode(code);

        if (body == null) {
            message.setBodyLength(0);
        } else {
            int bodyLen = body.getBytes().length;
            message.setBodyLength(bodyLen);
            message.setBody(body);
        }
        return message;
    }

    // Message Header 설정
    public static void makeMessageHeader(Message message, byte[] header) {
        int index = 0;
        byte type = header[index];
        index += 1;
        byte code = header[index];
        index += 1;

        message.setType(type);
        message.setCode(code);

        // Body 4byte : -2,147,483,648 ~ 2,147,483,647
        byte[] bodyLenByte = new byte[Packet.LEN_BODY];
        for (int i = 0; i < Packet.LEN_BODY; i++) {
            bodyLenByte[i] = header[index];
            index++;
        }

        int bodyLen = Packet.bytesToInt(bodyLenByte);
        message.setBodyLength(bodyLen);
    }

    // Message Body 설정
    public static void makeMessageBody(Message message, byte[] body) {
        int index = 0;

        byte[] bodyByte = new byte[message.getBodyLength()];
        for (int i = 0; i < message.getBodyLength(); i++) {
            bodyByte[i] = body[index];
            index++;
        }

        message.setBody(new String(body));
    }

    // Message 내용 출력
    public static void printMessage(Message message) {
        System.out.println(">> New Packet Received");
        System.out.println("Type : " + message.getType());
        System.out.println("Code : " + message.getCode());
        System.out.println("Body Length : " + message.getBodyLength());
        System.out.println("Body : " + message.getBody());
    }
}
