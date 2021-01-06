package client;
import client.handler.NetworkEvtListener;

public class Client {
  public static void main(String[] args) throws InterruptedException {
    MessageHandler handler = new MessageHandler(new NetworkEvtListener());
    BettySocket.connect("a894df1985ef048f88bda6af7bd93b09-58679745.ap-southeast-1.elb.amazonaws.com", 8888, handler);
    Thread.sleep(10000);
  }
}