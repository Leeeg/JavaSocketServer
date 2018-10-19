import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SocketServer {

    private static Map<Integer, Socket> clients = new HashMap<Integer, Socket>();

    public static void main(String[] args) {

        new SocketServer().init();

    }

    private void init() {
        try {
            //1.创建一个服务器端Socket，即SocketThread，指定绑定的端口，并监听此端口
            ServerSocket serverSocket = new ServerSocket(8888);
            Socket socket = null;
            InetAddress address = null;
            //记录客户端的数量
            int count = 0;
            System.out.println("***服务器启动，等待客户端的连接***");
            //循环监听等待客户端的连接
            while (true) {
                //调用accept()方法开始监听，等待客户端的连接
                socket = serverSocket.accept();
                //创建一个新的线程
                new SocketThread(socket, this).start();

                address = socket.getInetAddress();
                System.out.println("当前客户端的IP：" + address.getHostAddress() + "    " + socket.getPort());

                clients.put(socket.getPort(), socket);
                count++;//统计客户端的数量
                System.out.println("客户端的数量：" + count);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void receiveMsg(Socket fSocket, byte[] data) {
        System.out.println("message receive : lenth = " + data.length);

    }

    public void sendMsgToAll(Socket fromSocket, String msg) {
        Set<Integer> keset = this.clients.keySet();
        java.util.Iterator<Integer> iter = keset.iterator();
        while (iter.hasNext()) {
            int key = iter.next();
            Socket socket = clients.get(key);
            if (socket != fromSocket) {
                try {
                    if (socket.isClosed() == false) {
                        if (socket.isOutputShutdown() == false) {

                            Writer writer = new OutputStreamWriter(
                                    socket.getOutputStream());
                            writer.write(msg);
                            writer.flush();
                        }

                    }
                } catch (SocketException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
    }


}
