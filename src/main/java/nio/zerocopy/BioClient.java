package nio.zerocopy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.net.Socket;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/14 19:07
 * @time 19:07
 * java io 客户端 ，发送一个 zip包到服务端，传输查看花费的时间。
 */
public class BioClient {

    public static void main(String[] args) throws  Exception{
        Socket socket = new Socket("127.0.0.1",7001);
        String fileName = "protoc-3.6.1-win32.rar";
        //输入流
        FileInputStream fileInputStream = new FileInputStream(fileName);
        //输出流，发送文件给服务端:
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        byte[] buffer = new byte[4096];
        long readcout;
        long total = 0;
        long startTime = System.currentTimeMillis();
        while ((readcout = fileInputStream.read(buffer)) >= 0){
            total += readcout;
            dataOutputStream.write(buffer);
        }

        System.out.println("发送总字节数: "+total +", 耗时: "+(System.currentTimeMillis() - startTime));
        dataOutputStream.close();;
        socket.close();
        fileInputStream.close();

    }
}
