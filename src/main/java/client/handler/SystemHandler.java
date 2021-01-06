package client.handler;

import client.BettySocket;
import client.MessageHandler;
import transport.message.SM;

public class SystemHandler implements MessageHandler.SubHandler {
  BettySocket socket;
  public SystemHandler(BettySocket socket) {
    this.socket = socket;
  }
  @Override
  public void handle(Object msg) {
    SM.SystemMessage sysMsg = (SM.SystemMessage)(msg);
    switch (sysMsg.getCommand()) {
      case LOGIN:
        processLoginResp(sysMsg.getLoginResp());
        break;
      case PING:
        processPingResp(sysMsg.getPingResp());
        break;
      default:
        break;
    }
  }

  private void processPingResp(SM.PingResp pingResp) {
    System.out.println("server time: " + pingResp.getServerTime());
  }

  private void processLoginResp(SM.LoginResp loginResp) {
    if (loginResp.getResult() == SM.Result.LOGIN_SUCCESS) {
      System.out.println("login success - name: " + loginResp.getName());
      SM.PingReq req = SM.PingReq.newBuilder().setClientTime(System.currentTimeMillis()).build();
      socket.sendSystemMsg(SM.SMCmd.PING, req);
    }
    else {
      System.out.println("login fail");
    }
  }
}