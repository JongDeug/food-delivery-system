package network.connect;

import lombok.Getter;
import lombok.Setter;
import network.dataPacket.Message;
import network.dataPacket.Packet;

import java.io.*;
import java.net.Socket;
import java.rmi.server.ExportException;

@Getter
@Setter
public abstract class IO {
    private Socket socket;
    private InputStream inputStream;
    private OutputStream outputStream;
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public IO(Socket socket) throws IOException {
        this.socket = socket;
        this.inputStream = socket.getInputStream();
        this.outputStream = socket.getOutputStream();
        this.dataInputStream = new DataInputStream(socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
    }

    // Socket 닫기
    public void close() throws IOException {
        if (socket != null) {
            socket.close();
        }
        if (dataInputStream != null) {
            dataInputStream.close();
        }
        if (dataOutputStream != null) {
            dataOutputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
        if (outputStream != null) {
            outputStream.close();
        }
    }

    public void send(Message message) throws IOException {
        byte[] packet = Packet.makePacket(message);
        dataOutputStream.write(packet);
        dataOutputStream.flush();
        System.out.println("packet 전송");
    }

    // Client 전용 함수
    public Message receive(byte type) throws IOException {
        Message message = new Message();

        byte[] header = new byte[Packet.LEN_HEADER];
        dataInputStream.readFully(header);
        Message.makeMessageHeader(message, header);

        byte[] body = new byte[message.getBodyLength()];
        dataInputStream.readFully(body);
        Message.makeMessageBody(message, body);

        if (message.getType() == Packet.TYPE_UNDIFINED) {
            throw new IOException("통신 오류 : Packet Type Undifined");
        }

        if (message.getType() == type) {
            System.out.println("Type 일치");
        }

        Message.printMessage(message);
        return message;
    }

    // Server 전용 함수
    public Message receive() throws IOException {
        Message message = new Message();

        byte[] header = new byte[Packet.LEN_HEADER];
        dataInputStream.readFully(header);
        Message.makeMessageHeader(message, header);

        byte[] body = new byte[message.getBodyLength()];
        dataInputStream.readFully(body);
        Message.makeMessageBody(message, body);

        if (message.getType() == Packet.TYPE_UNDIFINED) {
            throw new IOException("통신 오류 : Packet Type Undifined");
        }

        Message.printMessage(message);
        return message;
    }
}
