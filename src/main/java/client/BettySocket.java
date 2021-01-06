package client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import transport.codec.BinaryMessage;
import transport.codec.MsgGroup;
import transport.codec.OnceDecoder;
import transport.codec.OnceEncoder;
import transport.message.SM;

public class BettySocket {
  private ChannelHandlerContext ctx;

  public BettySocket(ChannelHandlerContext ctx) {
    this.ctx = ctx;
  }

  public <T> void sendSystemMsg(SM.SMCmd cmd, T message) {
    SM.SystemMessage.Builder sbm = SM.SystemMessage.newBuilder();
    String setMethod = "set" + message.getClass().getSimpleName();
    sbm.setCommand(cmd);
    try {
      sbm.getClass().getMethod(setMethod, message.getClass()).invoke(sbm, message);
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
      return;
    }
    ctx.writeAndFlush(new BinaryMessage(MsgGroup.SYSTEM, sbm.build()));
  }

  public static void connect(String host, int port, MessageHandler handler) {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      Bootstrap bootstrap = new Bootstrap();
      bootstrap.group(group)
              .channel(NioSocketChannel.class)
              .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                  ch.pipeline().addLast(new OnceDecoder()).addLast(new OnceEncoder()).addLast(handler);
                }
              });

      bootstrap.connect(host, port).sync().channel();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public void close() {
    ctx.close();
  }
}