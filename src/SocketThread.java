import java.io.*;
import java.net.Socket;

public class SocketThread extends Thread {

    private Socket socket;
    private SocketServer server;
    private DataInputStream in = null;
    private byte buffer[] = new byte[68];
    private String temp;

    public SocketThread(Socket socket, SocketServer server) {
        this.socket = socket;
        this.server = server;
        init();
    }

    private void init() {
        try {
            in = new DataInputStream(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        super.run();
        System.out.println("子线程开始工作");
        while (true) {
            try {
                System.out.println("线程" + this.getId() + ":开始从客户端读取数据——>");
                while (in.read(buffer) != -1) {
                    server.receiveMsg(buffer);
                }
                if (socket.getKeepAlive() == false) {
                    in.close();
                    temp = "客户端" + socket.getPort() + ":退出";
                    System.out.println(temp);
                    socket.close();
                    this.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    in.close();
                    socket.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }

            }
        }

    }
}
