package server;

import server.clientdb.ClientDB;


public interface Command extends Comparable<Command> {

	public void perform(ClientDB cdb, float delta);

	public long getTime();

}
