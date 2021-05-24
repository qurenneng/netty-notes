package netty.transfer.oio;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * @Author qrn
 * @Title https://blog.csdn.net/qq_41971087
 * @Date 2021/5/24 17:35
 * @time 17:35
 * 使用 oio 进行 服务端与客户端连接通信，并且 发送一个简单 hi 给客户端
 * 传输 实例:
 *
 * 1.创建 socker 服务，定义好ip 和端口
 * 2.获取客户端链接accept
 * 3.循环一直等待请求连接，使用 线程 ，每一个请求都创建一个线程去处理
 * 4. 连接成功后，写入数据 hi到 客户端
 * 5.关闭流
 */
public class PlainOioServer {


    /**
     * 启动server 方法
     * @param port 端口:
     * @throws IOException
     */
    public static  void server(int port) throws IOException{
        final ServerSocket socket = new ServerSocket(port);
        try{
            for(;;){
               final Socket clientSocket = socket.accept();
                System.out.println("Accpted connection from "+clientSocket);
                //创建线程去处理:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OutputStream out;
                        try{
                            out =  clientSocket.getOutputStream();
                            //写数据到 客户端
                            out.write("Hi ! \r\n".getBytes(Charset.forName("UTF-8")));
                            out.flush();
                            clientSocket.close();
                        }catch (IOException e){
                            e.printStackTrace();
                        }finally {
                            try{
                                clientSocket.close();
                            }catch (IOException ex){
                                // ignorte on close
                            }
                        }
                    }
                }).start();

            }
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public static void main(String[] args) throws  Exception{
        server(8080);

    }


}
