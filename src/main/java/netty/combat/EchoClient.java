package netty.combat;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/20 14:55
 * @time 14:55
 * 客户端，引导类:
 */
public class EchoClient {
    private static final  int POST = 8080;
    private static final  String HOST = "127.0.0.1";

    //启动项目:
    public void start() throws  Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(HOST,POST))
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new EchoClientHandler());
                        }
                    });
            ChannelFuture channelFuture = b.connect().sync();
            channelFuture.channel().closeFuture().sync();
        }catch (Exception e){

        }finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws  Exception {
        new EchoClient().start();
    }

}
