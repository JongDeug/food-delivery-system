package network.connect;

import lombok.*;

import java.io.IOException;
import java.net.Socket;

@Getter
@Setter
public class ClientIO extends IO {

    /**
     * 클라이언트 전용
     * singleton pattern
     */

    private String pk;
    private int role = -1;   //0:관리자, 1:점주, 2:고객
    private String id;
    private String pw;
    private String name;
    private String tel;
    private int age;
    private String storePK;

    private static ClientIO instance;

    static {
        try {
            instance = new ClientIO();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ClientIO() throws IOException {
//        super(new Socket("193.186.4.167", 4000));
        super(new Socket("127.0.0.1", 5000));
        System.out.println("Connection Successful");
    }

    public static ClientIO getInstance(){
        return instance;
    }
}
