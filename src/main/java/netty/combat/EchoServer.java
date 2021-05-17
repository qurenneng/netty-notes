package netty.combat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/17 20:05
 * @time 20:05
 * 服务端引导类；
 */
public class EchoServer {

    private final  int port;

    public EchoServer(int port) {
        this.port = port;
    }

    /**
     * 启动服务器:
     * @throws Exception
     */
    public void start() throws  Exception {
        final EchoServerHandler echoServerHandler = new EchoServerHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
//            bootstrap.group(group).channel()
        } finally {
            // 关闭 EventLoopGroup 释放所有资源:
            group.shutdownGracefully().sync();
        }

    }

}
