package netty.combat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/20 14:37
 * @time 14:37
 *  netty 客户端:
 */
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {


    /**
     * 当被通知 channel 是活跃时候，发送一条消息；
     * @param ctx
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
    ctx.writeAndFlush(Unpooled.copiedBuffer("你好,Netty ",
                CharsetUtil.UTF_8));
    }

    /**
     * 接收转发的信息:
     * @param channelHandlerContext
     * @param in
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf in) throws Exception {
        System.out.println("接收服务端转发过来的数据:" +in.toString(CharsetUtil.UTF_8));
    }


    /**
     * 发生异常时候，打印异常信息:
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception{
            cause.printStackTrace();
            ctx.close();
    }



}
