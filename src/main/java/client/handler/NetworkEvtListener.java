package client.handler;

import client.BettySocket;
import client.MessageHandler;
import transport.message.IM;
import transport.message.SM;

public class NetworkEvtListener implements MessageHandler.SocketEvtListener {
  @Override
  public void error(Throwable cause) {
    System.out.println(cause.getMessage());
  }

  @Override
  public void active(MessageHandler handler, BettySocket bettySocket) {
    handler.addSubHandler(SM.SystemMessage.class.getName(), new SystemHandler(bettySocket));
    handler.addSubHandler(IM.IngameMessage.class.getName(), new InGameHandler(bettySocket));
    SM.LoginReq.Builder builder = SM.LoginReq.newBuilder();
    builder.setUid(1000000);
    builder.setPwd("password");
    bettySocket.sendSystemMsg(SM.SMCmd.LOGIN, builder.build());
  }

  @Override
  public void close() {
    System.out.println("disconnected from server");
  }
}
