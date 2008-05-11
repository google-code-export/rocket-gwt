package rocket.remoting.server.comet;

/**
 * An interface that unifies all the possible server messages to be sent to the client.
 */
public interface Message {
  int getCommand();
  Object getObject();
}
