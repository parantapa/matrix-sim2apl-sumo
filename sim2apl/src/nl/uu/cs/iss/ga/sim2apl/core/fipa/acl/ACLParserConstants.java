/* Generated By:JavaCC: Do not edit this line. ACLParserConstants.java */
package nl.uu.cs.iss.ga.sim2apl.core.fipa.acl;

public interface ACLParserConstants {

	int EOF = 0;
	int START = 5;
	int MESSAGETYPE = 10;
	int SENDER = 15;
	int RECEIVER = 16;
	int CONTENT = 17;
	int REPLY_WITH = 18;
	int REPLY_BY = 19;
	int IN_REPLY_TO = 20;
	int REPLY_TO = 21;
	int ENCODING = 22;
	int LANGUAGE = 23;
	int ONTOLOGY = 24;
	int PROTOCOL = 25;
	int CONVERSATION_ID = 26;
	int USERDEFINEDPARAM = 27;
	int END = 28;
	int DATETIME = 33;
	int WORD = 34;
	int STRINGLITERAL = 35;
	int DIGIT = 36;
	int INTEGER = 37;
	int FLOATONE = 38;
	int FLOATTWO = 39;
	int PREFIXBYTELENGTHENCODEDSTRING = 40;
	int RBRACE = 41;
	int LBRACE = 42;
	int SET = 47;
	int SEQUENCE = 48;
	int AID = 49;
	int NAME = 50;
	int ADDRESSES = 51;
	int RESOLVERS = 52;
	int USERDEFINEDSLOT = 53;
	int RBRACE2 = 54;
	int LBRACE2 = 55;

	int DEFAULT = 0;
	int MESSAGETYPESTATE = 1;
	int MESSAGEPARAMETERSTATE = 2;
	int CONTENTSTATE = 3;
	int AIDSTATE = 4;

	String[] tokenImage = { "<EOF>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\"(\"", "\" \"", "\"\\t\"", "\"\\n\"",
			"\"\\r\"", "<MESSAGETYPE>", "\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "\":sender\"", "\":receiver\"",
			"\":content\"", "\":reply-with\"", "\":reply-by\"", "\":in-reply-to\"", "\":reply-to\"", "\":encoding\"",
			"\":language\"", "\":ontology\"", "\":protocol\"", "\":conversation-id\"", "<USERDEFINEDPARAM>", "\")\"",
			"\" \"", "\"\\t\"", "\"\\n\"", "\"\\r\"", "<DATETIME>", "<WORD>", "<STRINGLITERAL>", "<DIGIT>", "<INTEGER>",
			"<FLOATONE>", "<FLOATTWO>", "<PREFIXBYTELENGTHENCODEDSTRING>", "\")\"", "\"(\"", "\" \"", "\"\\t\"",
			"\"\\n\"", "\"\\r\"", "\"set\"", "\"sequence\"", "\"agent-identifier\"", "\":name\"", "\":addresses\"",
			"\":resolvers\"", "<USERDEFINEDSLOT>", "\")\"", "\"(\"", };

}
