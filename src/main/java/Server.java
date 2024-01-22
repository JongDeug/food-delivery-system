import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    public static ServerThread serverThread[];
    public static int count;

    public Server() throws IOException {
        this.serverThread = new ServerThread[10]; // 수용 인원 10명
        this.count = 0;
        this.serverSocket = new ServerSocket(5000);
    }

    public void run() throws IOException {
        while (serverSocket != null) {
            Socket socket = serverSocket.accept();
            System.out.println("클라이언트 접속");
            addThread(socket);
        }
    }

    public synchronized void addThread(Socket socket) throws IOException {
        if (count < serverThread.length) {
            serverThread[count] = new ServerThread(socket);
            serverThread[count].start();
            count++;
        } else {
            System.out.println("서버 수용 초과");
        }
    }

    public static int findServerThread(int id) {
        for (int i = 0; i < count; i++) {
            if (serverThread[i].getID() == id) {
                return i;
            }
        }
        return -1;
    }

    public synchronized static void remove(int id) throws IOException {
        int index = findServerThread(id);
        System.out.println("index = " + index);
        if (index >= 0) {
            ServerThread thread = serverThread[index];
            if (index <= count - 1) {
                for (int i = index + 1; i < count; i++) {
                    serverThread[i-1] = serverThread[i];
                }
                count--;
                System.out.println(count);
                thread.getServerIO().close();
                thread.interrupt();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("hello");
        Server server = new Server();
        server.run();
    }
}
