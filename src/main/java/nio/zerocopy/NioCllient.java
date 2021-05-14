package nio.zerocopy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/14 19:38
 * @time 19:38
 * nio 零拷贝 客户端:
 */
public class NioCllient {
    public static void main(String[] args) throws  Exception{
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1", 7001));
        String fileName = "protoc-3.6.1-win32.rar";
        FileChannel fileChannel = new FileInputStream(fileName).getChannel();
        long startTime = System.currentTimeMillis();
        /**
         * transferTo  在linux 下可以完成全部传输，在Windows 下一次只能传 8 m
         */

        long transferCount = 0;
        //如果是windows 就需要循环
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            transferCount = testFileChannel(fileChannel,socketChannel,transferCount);
        }else{
            transferCount += fileChannel.transferTo(0, fileChannel.size(), socketChannel);
        }

        System.out.println("发送的总的字节数 =" + transferCount + " 耗时:" + (System.currentTimeMillis() - startTime));
        //关闭
        fileChannel.close();
    }

    public static  Long testFileChannel(FileChannel fileChannel,SocketChannel socketChannel,long transferCount) throws Exception {
        Integer num = 8*1024*1024;
        Long fiel = fileChannel.size();
        while (true){
            if(fiel <= num){
                transferCount += fileChannel.transferTo(0, fiel, socketChannel);
                break;
            }
            transferCount += fileChannel.transferTo(0, num, socketChannel);
            fiel = fiel - num;
        }
        return  transferCount;
    }
}
