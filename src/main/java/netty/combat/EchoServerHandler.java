package netty.combat;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/17 19:28
 * @time 19:28
 * Netty 服务器端，接收客户端接收数据的处理。
 *  ChannelInboundHandler
 * ChannelInboundHandlerAdapter 用户定义响应入站事件的方法:
 */
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 对于每个传入的消息都要调用
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in =(ByteBuf)msg;
        //打印服务端接收到的数据:
        System.out.println("服务端接收到的数据:"+in.toString(CharsetUtil.UTF_8));
        //将接收到的数据转发给发送者。
        ctx.write(in);
    }

    /**
     * 通知ChannelInboundHandler 最后一次对 channelRead()的调用是当前
     * 批量读取中的最后一条信息。
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        //将未决消息发送到远程节点，并且关闭改Channel
       ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
               .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 在读取操作期间，有异常抛出时会调用:
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
       cause.printStackTrace(); //打印异常栈跟踪
        ctx.close();//关闭改channel
    }
}
