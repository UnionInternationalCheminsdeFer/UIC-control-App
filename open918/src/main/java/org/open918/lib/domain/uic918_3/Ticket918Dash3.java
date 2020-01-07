package org.open918.lib.domain.uic918_3;

import org.open918.lib.domain.Ticket;
import org.open918.lib.domain.TicketStandard;

import java.util.HashMap;
import java.util.Map;

public class Ticket918Dash3 implements Ticket {

    String messageType;
    int messageTypeVersion;

    int ricsCode;

	int messageLength;
	byte[] message;
	byte[] compressedMessage;

    String signatureKeyId;
    byte[] signature;

    TicketHeader header = new TicketHeader();
	TicketContents contents = new TicketContents();
	private byte[] data;

	private Map<String, TicketContents> otherBlocks = new HashMap<>();

	@Override
	public TicketStandard getStandard() {
		return TicketStandard.TICKET918_3;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}

	public int getMessageTypeVersion() {
		return messageTypeVersion;
	}

	public void setMessageTypeVersion(int messageTypeVersion) {
		this.messageTypeVersion = messageTypeVersion;
	}

	public int getRicsCode() {
		return ricsCode;
	}

	public void setRicsCode(int ricsCode) {
		this.ricsCode = ricsCode;
	}

	public String getSignatureKeyId() {
		return signatureKeyId;
	}

	public void setSignatureKeyId(String signatureKeyId) {
		this.signatureKeyId = signatureKeyId;
	}

	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}

	public int getMessageLength() {
		return messageLength;
	}

	public void setMessageLength(int messageLength) {
		this.messageLength = messageLength;
	}

	public byte[] getCompressedMessage() {
		return compressedMessage;
	}

	public void setCompressedMessage(byte[] compressedMessage) {
		this.compressedMessage = compressedMessage;
	}

	public byte[] getMessage() {
		return message;
	}

	public void setMessage(byte[] message) {
		this.message = message;
	}

	public TicketHeader getHeader() {
		return header;
	}

	public void setHeader(TicketHeader header) {
		this.header = header;
	}

	public TicketContents getContents() {
		return contents;
	}

	public void setContents(TicketContents contents) {
		this.contents = contents;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	public byte[] getData() {
		return data;
	}

	public void addBlock(String type, TicketContents contents) {
		otherBlocks.put(type, contents);
	}
}
