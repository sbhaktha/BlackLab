package org.allenai.blacklab.queryParser.corpusql;

public class MalformedTagException extends ParseException {
	public MalformedTagException() {
		super("Malformed XML tag in query");
	}

	public MalformedTagException(String message) {
		super(message);
	}

}
