package com.buildersbackpacks.network;

import net.minecraft.network.PacketBuffer;

public class BackpackSlotChangeMessage extends BaseMessage{
	private int index;
	
	public BackpackSlotChangeMessage() {
		messageIsValid = false;
	}
	
	public BackpackSlotChangeMessage(int index) {
		this.index = index;
		this.messageIsValid = true;
	}
	
	public int getIndex() {
		return index;
	}
	
	public static final BackpackSlotChangeMessage decode(PacketBuffer buf) {		
		BackpackSlotChangeMessage msg = new BackpackSlotChangeMessage();
		try {
			msg.index = buf.readInt();
		}catch (IllegalArgumentException | IndexOutOfBoundsException e) {			
			return msg;
		}
		
		msg.messageIsValid = true;
		return msg;
	}
	
	public static final void encode(final BackpackSlotChangeMessage msg, PacketBuffer buf) {
		buf.writeInt(msg.getIndex());
	}
}
