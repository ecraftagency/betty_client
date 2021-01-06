package client.handler;

import client.BettySocket;
import client.MessageHandler;

public class InGameHandler implements MessageHandler.SubHandler {
  BettySocket socket;
  public InGameHandler(BettySocket socket) {
    this.socket = socket;
  }
  @Override
  public void handle(Object msg) {

  }
}