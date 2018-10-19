import java.io.*;
import java.net.Socket;

/**
 * @title:the thread of every socket client
 * @gmail jefferyleeeg@gmail.com
 * @author:Lee
 * @date: 2018/10/20
 */
public class SocketThread extends Thread {

    private Socket socket;
    private SocketServer server;
    private DataInputStream in = null;
    private byte buffer[] = new byte[68];//每次读取数据容器（此处写死，实际中大小一般根据包头信息设置）
    private String temp;

    public SocketThread(Socket socket, SocketServer server) {
        this.socket = socket;
        this.server = server;
        init();
    }

    //初始化后保存当前线程客户端的输入流
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
                    server.sendMsgToAll(socket, buffer);
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
