package wang.ismy.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * @author MY
 * @date 2019/12/6 15:21
 */
public class ServerHandler extends ChannelHandlerAdapter {

    private static final Set<Channel> channels = new HashSet<>();

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.add(channel);
        System.out.println(channel.remoteAddress()+"上线");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channels.remove(channel);
        System.out.println(channel.remoteAddress()+"离线");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
        Channel channel = ctx.channel();
        ByteBuf msg = (ByteBuf) obj;
        System.out.println("广播消息:"+msg.toString(CharsetUtil.UTF_8));
        for (Channel ch : channels) {
            if (ch != channel){
                ch.writeAndFlush(Unpooled.copiedBuffer(channel.remoteAddress()+"说了"+msg.toString(CharsetUtil.UTF_8),CharsetUtil.UTF_8));

            }
        }
    }
}
