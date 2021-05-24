package netty.transfer.oio;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/24 17:54
 * @time 17:54
 * oio 客户端:
 * 直接从流中读取服务端发送的数据:
 */
public class PlainOioClient {

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("127.0.0.1", 8080);
        InputStream inputStream = socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String info = null;
        while ((info = bufferedReader.readLine()) != null) {
            System.out.println("我是客户端，服务器返回信息：" + info);
        }

    }

}
