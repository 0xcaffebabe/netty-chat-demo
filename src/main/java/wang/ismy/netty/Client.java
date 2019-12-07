package wang.ismy.netty;

import com.sun.jdi.BooleanType;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.util.Scanner;

/**
 * @author MY
 * @date 2019/12/6 15:19
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {

        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ClientHandler());
                    }
                });
        ChannelFuture future = bootstrap.connect("127.0.0.1", 1999).sync();
        Channel channel = future.channel();
        System.out.println("客户端准备完毕，"+ channel.remoteAddress());
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()){

            channel.writeAndFlush(Unpooled.copiedBuffer(scanner.nextLine(),CharsetUtil.UTF_8));
        }
        channel.closeFuture().sync();
    }
}
