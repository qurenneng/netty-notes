package nio.zerocopy;

import java.io.DataInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/14 10:06
 * @time 10:06
 * java io 拷贝 服务端:
 */
public class BioServer {

    public static void main(String[] args) throws  Exception  {
        ServerSocket  serverSocket = new ServerSocket(7001);
        while (true){
            //等待连接请求
            Socket socket = serverSocket.accept();
            //获取 输入流，客户端发送的数据流:
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        try{
            byte[] byteArray = new byte[4096];
            while (true){
                //读取数据 等于 -1的时候 就表示数据读取完，就跳出循环
                int read = dataInputStream.read(byteArray, 0, byteArray.length);
                if(-1 == read){
                    break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        }
    }

}
