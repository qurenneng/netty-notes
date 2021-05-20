package netty.combat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/17 20:05
 * @time 20:05
 * 服务端引导类；
 */
public class EchoServer {

    private static final  int POST = 8080;
    private static final  String HOST = "127.0.0.1";

    public static void main(String[] args) throws  Exception{
        new EchoServer().start();
    }

    /**
     * 启动服务器:
     * @throws Exception
     */
    public void start() throws  Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
                   .localAddress(new InetSocketAddress(HOST,POST))
                   .childHandler(new ChannelInitializer<SocketChannel>() {
                       @Override
                       protected void initChannel(SocketChannel ch) throws Exception {
                         ch.pipeline().addLast(new EchoServerHandler());
                       }
                   });
            //异步的绑定服务器，调用sync方法阻塞，等待知道绑定完成
            ChannelFuture f = b.bind().sync();
            //获取channel 的 closeFuture 并阻塞当前线程直到他完成
            f.channel().closeFuture().sync();

        } finally {
            // 关闭 EventLoopGroup 释放所有资源:
            group.shutdownGracefully().sync();
        }

    }

}
