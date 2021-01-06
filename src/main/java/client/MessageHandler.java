package client;

import com.google.protobuf.MessageLite;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.HashMap;

public class MessageHandler extends SimpleChannelInboundHandler<MessageLite> {

  @FunctionalInterface
  public interface SubHandler {
    void handle(Object msg);
  }

  public interface SocketEvtListener {
    void error(Throwable cause);
    void active(MessageHandler handler, BettySocket bettySocket);
    void close();
  }

  private HashMap<String, SubHandler> subHandlers;
  private SocketEvtListener sockEvtListener;

  public MessageHandler(SocketEvtListener listener) {
    this.sockEvtListener = listener;
    subHandlers = new HashMap<>();
  }

  public void addSubHandler(String typeName, SubHandler subHandler) {
    subHandlers.put(typeName, subHandler);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    sockEvtListener.active(this, new BettySocket(ctx));
    super.channelActive(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    sockEvtListener.error(cause);
    super.exceptionCaught(ctx, cause);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    ctx.close();
    sockEvtListener.close();
    super.channelInactive(ctx);
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, MessageLite msg) throws Exception {
    if (subHandlers.containsKey(msg.getClass().getName())) {
      subHandlers.get(msg.getClass().getName()).handle(msg);
    }
  }
}