package netty.transfer.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author qrn
 * @Title https://blog.csdn.net/qq_41971087
 * @Date 2021/5/24 18:08
 * @time 18:08
 *
 * nio 服务端传输:
 *
 */
public class PlainNioServer {

    public void server(int port)throws IOException{
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);// 开启非阻塞
        //通过获取 socket 然后注册 端口和ip
        ServerSocket ssocket = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        ssocket.bind(address); //绑定
        //选择器，获取选择器 把通道注册进选中器中
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);

        final  ByteBuffer msg = ByteBuffer.wrap("Hi! \r\n".getBytes());

        for(;;){
            try{
                //等待需要处理的新事件，阻塞将一直持续下一个传入事件
            selector.select();
            }catch (IOException e){
                e.printStackTrace();
                break;
            }
            //获取所有接收事件的SelectionKey 实例，
            //通过 SelectionKey 可以获取到1 通道，这样就可以把接收的数据
            //转发给客户端。
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                try{
                    //连接事件
                    if(key.isAcceptable()){
                        ServerSocketChannel  server =(ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(selector, SelectionKey.OP_WRITE
                        |SelectionKey.OP_READ,msg.duplicate());
                        System.out.println("当前创建连接的客户端是:"+client);
                    }
                    //检查套接字是否已经装备好写数据
                    if(key.isWritable()){
                        SocketChannel  client =(SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer)key.attachment();
                        while (buffer.hasRemaining()){
                            if(client.write(buffer) == 0){
                                break;
                            }
                        }
                        client.close();
                    }
                }catch (IOException ex){
                    key.cancel();
                    try{
                        key.channel().close();
                    }catch (IOException cex){
                        //close
                    }
                }
            }
        }

    }

}
