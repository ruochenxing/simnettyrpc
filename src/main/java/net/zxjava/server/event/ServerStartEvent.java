package net.zxjava.server.event;

import org.springframework.context.ApplicationEvent;

public class ServerStartEvent extends ApplicationEvent {

	private static final long serialVersionUID = 1625221484433996714L;

	public ServerStartEvent(Object source) {
		super(source);
	}

}
