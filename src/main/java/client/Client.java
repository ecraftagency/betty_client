package client;
import client.handler.NetworkEvtListener;

public class Client {
  public static void main(String[] args) throws InterruptedException {
    MessageHandler handler = new MessageHandler(new NetworkEvtListener());
    BettySocket.connect("a15a8265ed8044d5c9d374b662a7a552-890398595.ap-southeast-1.elb.amazonaws.com", 8888, handler);
    Thread.sleep(10000);
  }
}