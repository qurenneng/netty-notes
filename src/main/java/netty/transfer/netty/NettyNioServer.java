package netty.transfer.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/24 19:52
 * @time 19:52
 * 传统非阻塞io
 *
 * 与传统阻塞io的区别就在于
 * OioEventLoopGroup() 替换  NioEventLoopGroup
 * OioServerSocketChannel 替换 NioServerSocketChannel
 * 就可以了
 *
 */
public class NettyNioServer {


    public void server(int port) throws  Exception{
        final ByteBuf buf =  Unpooled.unreleasableBuffer(
                Unpooled.copiedBuffer("Hi! \r\n", Charset.forName("UTF-8"))
        );
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group).channel(NioServerSocketChannel.class)
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
