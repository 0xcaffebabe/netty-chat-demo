package wang.ismy.netty;

import com.sun.tools.javac.Main;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author MY
 * @date 2019/12/6 15:19
 */
public class Server {
    public static void main(String[] args) throws InterruptedException {
        // 接收客户连接
        NioEventLoopGroup mainGroup = new NioEventLoopGroup();
        // 处理网络操作
        NioEventLoopGroup subGroup = new NioEventLoopGroup();
        // 启动助手
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(mainGroup,subGroup)
                // 服务器端通道实现
                .channel(NioServerSocketChannel.class)
                // 线程队列中等待连接的个数
                .option(ChannelOption.SO_BACKLOG,128)
                // 让连接保持活动状态
                .childOption(ChannelOption.SO_KEEPALIVE,true)
                // 添加服务端业务逻辑处理类
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast(new ServerHandler());
                    }
                });
        ChannelFuture future = bootstrap.bind(1999).sync();
        System.out.println("服务端准备完毕");

        // 关闭
        future.channel().closeFuture().sync();
        mainGroup.shutdownGracefully();
        subGroup.shutdownGracefully();
    }
}
