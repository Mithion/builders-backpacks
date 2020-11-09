package com.buildersbackpacks.network;

public abstract class BaseMessage {
	protected boolean messageIsValid = false;
	
	public final boolean isMessageValid() {
		return messageIsValid;
	}
}
