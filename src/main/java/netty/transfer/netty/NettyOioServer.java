package netty.transfer.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @Author qrn
 * @Title https://blog.csdn.net/qq_41971087
 * @Date 2021/5/24 19:34
 * @time 19:34
 * netty oio 传统阻塞io:
 * OioEventLoopGroup()
 * OioServerSocketChannel
 * 表示当前是阻塞的io模型
 */
public class NettyOioServer {

    public void server(int port) throws  Exception{
      final ByteBuf buf =  Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi! \r\n", Charset.forName("UTF-8"))
        );
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){

                            @Override
                            public void channelActive(
                                    ChannelHandlerContext ctx)
                                    throws Exception {
                                ctx.writeAndFlush(buf.duplicate())
                                        .addListener(
                                                ChannelFutureListener.CLOSE);
                            }
                        });
                        }
                    });
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        }finally {
            //释放所有资源:
            group.shutdownGracefully().sync();
        }
    }
}
