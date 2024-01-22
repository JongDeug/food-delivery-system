package network.dataPacket;

import java.io.*;
import java.util.List;

public class Packet {

    /**
     * 기본 패킷 구성
     * [Type][Code][BodyLength][Body]
     **/

    // LENGTH
    public static final int LEN_HEADER = 6;
    public static final int LEN_BODY = 4;

    // Type
    public static final byte TYPE_UNDIFINED = -1; // 프로토콜 미지정
    public static final byte TYPE_EXIT = 0; // 프로그램 종료
    public static final byte TYPE_LOGIN = 1; // 로그인 요청 및 응답
    public static final byte TYPE_CREATE = 2; // 등록 요청 및 응답
    public static final byte TYPE_READ = 3; // 조회 요청 및 응답
    public static final byte TYPE_UPDATE = 4; // 수정 요청 및 응답
    public static final byte TYPE_RESPONSE = 5; // 접수/거절 요청 및 응답

    // Code
    public static final byte CODE_LOGIN_SUCCESS = 0;
    public static final byte CODE_LOGIN_FAIL = 1;
    public static final byte CODE_LOGIN_NOTPWD = 2;
    public static final byte CODE_LOGIN_ADMIN = 4;
    public static final byte CODE_LOGIN_MEMBER = 5;
    public static final byte CODE_LOGIN_STORE_MANAGER = 6;

//    ---------------------------------------------------------------------------------------------------- Login

    public static final byte CODE_CREATE_STORE = 0;
    public static final byte CODE_CREATE_MENU = 1;
    public static final byte CODE_CREATE_MENU_OPTION = 2;
    public static final byte CODE_CREATE_REPLY_TO_REVIEW = 3;
    public static final byte CODE_CREATE_SIGNUP_FOR_STORE_MANAGER = 4;
    //    ---------------------------------------------------------------------- 점주
    public static final byte CODE_CREATE_SIGNUP_FOR_MEMBER = 5;
    public static final byte CODE_CREATE_ORDER = 6;
    public static final byte CODE_CREATE_REVIEW = 7;
    public static final byte CODE_CREATE_CANCLE_ORDER = 8;

//    ---------------------------------------------------------------------- 회원
//    ---------------------------------------------------------------------------------------------------- Create

    public static final byte CODE_READ_STORE_INFO = 0;
    public static final byte CODE_READ_STORE_MENU_INFO = 1;
    public static final byte CODE_READ_STORE_MENU_OPTION_INFO = 2;
    //    ---------------------------------------------------------------------- 공통 조회
    public static final byte CODE_READ_ORDER_INFO = 3;
    public static final byte CODE_READ_STORE_STATISTIC_INFO_FOR_STORE_MANAGER = 4;
    public static final byte CODE_READ_MY_STORE_REVIEW = 5;

    public static final byte CODE_READ_MY_ORDER_INFO_RES = 23;
    //    ---------------------------------------------------------------------- 점주 조회
    public static final byte CODE_READ_MY_ACCOUNT_INFO = 6;
    public static final byte CODE_READ_MY_ORDER_INFO = 7;
    public static final byte CODE_READ_STORE_INFO_FOR_CLIENT = 8;
    //    ---------------------------------------------------------------------- 고객 조회
    public static final byte CODE_READ_CLIENT_INFO = 9;
    public static final byte CODE_READ_STORE_MANAGER_INFO = 10;
    public static final byte CODE_READ_STORE_STATISTIC_INFO_FOR_ADMIN = 11;

    //    ---------------------------------------------------------------------- 관리자 조회
    public static final byte CODE_READ_RESPONSE_ORDER = 12;
    public static final byte CODE_READ_RESPONSE_CANCLE_ORDER = 13;
    //    ---------------------------------------------------------------------- 점주 요청 조회
    public static final byte CODE_READ_RESPONSE_STORE = 14;
    public static final byte CODE_READ_RESPONSE_STORE_MANAGER = 15;
    public static final byte CODE_READ_RESPONSE_OPTION = 16;
    public static final byte CODE_READ_RESPONSE_MENU = 17;
//    ---------------------------------------------------------------------- 관리자 요청 조회

//    ---------------------------------------------------------------------------------------------------- Read
    public static final byte CODE_UPDATE_BUSINESS_HOURS = 0; // 점주
    public static final byte CODE_UPDATE_DELIVERY_FINISH = 1;
    public static final byte CODE_UPDATE_PROFILE = 2; // 회원
//    ---------------------------------------------------------------------------------------------------- Update

    public static final byte CODE_RESPONSE_ORDER = 0; // 점주
    public static final byte CODE_RESPONSE_ORDER_CANCLE = 1;
    public static final byte CODE_RESPONSE_STORE_MANAGER_SIGNUP = 2; // 관리자
    public static final byte CODE_RESPONSE_STORE = 3;
    public static final byte CODE_RESPONSE_MENU = 4;
    public static final byte CODE_RESPONSE_OPTION = 5;
//    ---------------------------------------------------------------------------------------------------- Response


    public static byte[] makePacket(Message msg) {
        byte type = msg.getType();
        byte code = msg.getCode();
        int bodyLen = msg.getBodyLength();
        String body = msg.getBody();

        // create packet
        byte[] packet = new byte[LEN_HEADER + bodyLen];
        int index = 0;

        // [0][Code][BodyLength][Body]
        packet[index] = type;
        index += 1;

        // [Type][1][BodyLength][Body]
        packet[index] = code;
        index += 1;

        // [Type][Code][2~(bodyLen-1)][Body]
        byte[] bodyLenByte = intToBytes(bodyLen);
        System.arraycopy(bodyLenByte, 0, packet, index, bodyLenByte.length);
        index += bodyLenByte.length;

        // [Type][Code][BodyLength][~]
        if (bodyLen > 0) {
            byte[] data = body.getBytes();
            System.arraycopy(data, 0, packet, index, data.length);
        }
        return packet;
    }

    public static byte[] intToBytes(int data) {
        return new byte[]{
                (byte) ((data >> 24) & 0xff),
                (byte) ((data >> 16) & 0xff),
                (byte) ((data >> 8) & 0xff),
                (byte) ((data >> 0) & 0xff),
        };
    }

    public static int bytesToInt(byte[] data) {
        return (int) (
                (0xff & data[0]) << 24 |
                        (0xff & data[1]) << 16 |
                        (0xff & data[2]) << 8 |
                        (0xff & data[3]) << 0
        );
    }
}
