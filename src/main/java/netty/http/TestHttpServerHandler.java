package netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

import java.net.URI;
import java.net.URL;

/**
 * @Author qrn
 * @Title https://blog.csdn.net/qq_41971087
 * @Date 2021/5/22 17:27
 * @time 17:27
 * 使用 netty 实现一个 简单的 http 服务器:
 * HttpObject 客户端和服务器端相互通讯的数据被封装成 HttpObject
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {


    /**
     * 读取客户端请求:
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

        System.out.println("对应的 channel="+ ctx.channel() + "pipeline = "+ctx.pipeline()+"通过pipellie 获取channel"+
                ctx.pipeline().channel());
        System.out.println("当前 ctx的 handler"+ctx.handler());

        //如果 msg 是 请求那么就去获取数据然后在写入到页面当中显示:
        if(msg instanceof HttpRequest){

            System.out.println("ctx 类型 = "+ctx.getClass());

            System.out.println("pipeline hashcode"+ctx.pipeline().hashCode()+"当前类的 hashcode"+this.hashCode());
            System.out.println("msg 的类型"+msg.getClass());
            System.out.println("客户端请求地址:"+ctx.channel().remoteAddress());
            //强转，获取到request 这一步是为了做过滤，因为浏览器会请求2次过来
            //第二次请求过来的接口是 favicon.ico 资源，这里我们拿到后，不做处理。
            HttpRequest httpRequest = (HttpRequest) msg;
            URI url = new URI(httpRequest.uri());
            if("/favicon.ico".equals(url.getPath())){
                System.out.println("请求了 favicon.ico ，无需做处理");
                return;
            }

            ByteBuf context = Unpooled.copiedBuffer("hello, Netty !", CharsetUtil.UTF_8);

            DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, context);


            System.out.println("写入到客户端的消息数据:"+context.readableBytes());

            response.headers().set(HttpHeaderNames.CONTENT_TYPE,"text/plain");
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH,context.readableBytes());

            //发送到浏览器:
            ctx.writeAndFlush(response);

        }
    }
}
