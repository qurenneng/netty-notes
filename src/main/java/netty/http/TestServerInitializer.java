package netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author qrn
 * @Title
 * @Date 2021/5/22 18:12
 * @time 18:12
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {



    @Override
    protected void initChannel(SocketChannel ch) throws Exception {

        ChannelPipeline pipeline = ch.pipeline();

        //netty 编-解码 器:
        pipeline.addLast("MyHttpServerCodec",new HttpServerCodec());

        pipeline.addLast("MyTestHttpServerHandler",new TestHttpServerHandler());

        System.out.println("设置成功 ok");

    }

}
