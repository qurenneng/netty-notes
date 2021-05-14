package nio.zerocopy;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/14 19:27
 * @time 19:27
 * nio 服务端，零拷贝实例：
 */
public class NioServer {

    public static void main(String[] args) throws  Exception {
        InetSocketAddress address = new InetSocketAddress(7001);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(address);

        ByteBuffer allocate = ByteBuffer.allocate(4096);
        while (true){

            SocketChannel socketChannel = serverSocketChannel.accept();
            int readcount = 0;
            while (-1 != readcount){
                try{
                    readcount = socketChannel.read(allocate);
                }catch (Exception e) {
                   break;
                }
                allocate.rewind();// 倒带
            }

        }

    }
}
