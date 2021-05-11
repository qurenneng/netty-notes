package nio.groupchat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author qrn
 * @Date 2021/5/11 下午9:45
 * @Version 1.0
 * @blog https://blog.csdn.net/qq_41971087
 * nio 服务端：实现简单的群聊系统：
 */
public class GroupChatServer {

    //定义全局属性：
    /**
     * 选择器
     */
    private Selector selector;
    /**
     * 通道
     */
    private ServerSocketChannel listenChannel;
    /**
     * 服务端端口:
     */
    private static final int PORT = 6667;

    //构造器完成初始化：
    public GroupChatServer(){
        try{
            //获取选择器
            selector = Selector.open();
            //获取通道
            listenChannel = ServerSocketChannel.open();
            //绑定端口
            listenChannel.socket().bind(new InetSocketAddress("127.0.0.1",PORT));
            //设置非阻塞
            listenChannel.configureBlocking(false);
            //将 listenChannel 注册到  selector
            listenChannel.register(selector, SelectionKey.OP_ACCEPT);
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //  监听
    public void listen(){
        System.out.println("监听线程: " + Thread.currentThread().getName());
        try{

            while (true){
                int count = selector.select();
                if(count > 0){ //有事件才需要处理
                 //从选择器中，获取SelectionKey 拿到通道，提示用户上线
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()){
                        SelectionKey key = iterator.next();
                        //监听到accept
                        if(key.isAcceptable()){
                            SocketChannel accept = listenChannel.accept();
                            accept.configureBlocking(false);
                            accept.register(selector,SelectionKey.OP_READ);
                            //提示
                            System.out.println(accept.getRemoteAddress() + " 上线 ");
                        }
                        if(key.isReadable()){ //通道发送read事件，即通道是可读的状态
                            //处理读 (专门写方法..)
                            readData(key);
                        }
                        //当前的key 删除，防止重复处理
                        iterator.remove();
                    }
                }else{
                    System.out.println("等待....");
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //读取客户端消息
    private void readData(SelectionKey key) {
        SocketChannel channel  = null;
        try{
            //得到channel
            channel = (SocketChannel)key.channel();

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            int read = channel.read(buffer);
            //根据 read 到值做处理
            if(read > 0){
                //把缓存区到数据转成字符串
                String msg  = new String(buffer.array());
                //输出该消息
                System.out.println("form 客户端: " + msg);
                //向其它的客户端转发消息(去掉自己), 专门写一个方法来处理
                sendInfoToOtherClients(msg, channel);
            }

        }catch (IOException e){
            try {
                System.out.println(channel.getRemoteAddress() + " 离线了..");
                //取消注册
                key.cancel();
                //关闭通道
                channel.close();
            }catch (IOException e2) {
                e2.printStackTrace();;
            }
        }
    }

    //转发消息给其它客户(通道)
    private void sendInfoToOtherClients(String msg, SocketChannel self ) throws  IOException{
        System.out.println("服务器转发消息中...");
        System.out.println("服务器转发数据给客户端线程: " + Thread.currentThread().getName());

        for(SelectionKey key:selector.keys()){
            Channel targetChannel = key.channel();
            //获取当前选择器中到所有通道，然后排除掉当前方法到通道
            if(targetChannel instanceof  SocketChannel && targetChannel != self){
                //转型
                SocketChannel dest = (SocketChannel)targetChannel;

                //将msg到值存入 buffer 然后写入到通道中
                ByteBuffer wrap = ByteBuffer.wrap(msg.getBytes());
                //将buffer 的数据写入 通道
                dest.write(wrap);
            }
        }
    }

    public static void main(String[] args) {
        //创建服务器对象
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

}
