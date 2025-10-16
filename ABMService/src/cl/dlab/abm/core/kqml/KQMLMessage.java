package cl.dlab.abm.core.kqml;

import org.json.JSONObject;

public class KQMLMessage
{
	private MessageType performative;
    private String sender;
    private String receiver;
    private JSONObject content;
    private String language;
    private String ontology;

    public KQMLMessage(MessageType performative, String sender, String receiver, String content, String language, String ontology) 
    {
    	this(performative, sender, receiver, new JSONObject().put("message", content), language, ontology);
    }
    public KQMLMessage(MessageType performative, String sender, String receiver, JSONObject content, String language, String ontology) {
        this.performative = performative;
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.language = language;
        this.ontology = ontology;
    }

	/**
	 * @return the performative
	 */
	public MessageType getPerformative()
	{
		return performative;
	}

	/**
	 * @return the sender
	 */
	public String getSender()
	{
		return sender;
	}

	/**
	 * @return the receiver
	 */
	public String getReceiver()
	{
		return receiver;
	}

	/**
	 * @return the content
	 */
	public JSONObject getContent()
	{
		return content;
	}

	/**
	 * @return the language
	 */
	public String getLanguage()
	{
		return language;
	}

	/**
	 * @return the ontology
	 */
	public String getOntology()
	{
		return ontology;
	}
	public String getMessage()
	{
		if (content.has("message"))
		{
			return content.getString("message");
		}
		return null;
	}
	public double get(String key)
	{
		Number value = (Number)content.get(key); 
		return value instanceof Double ? (Double)value : value.doubleValue();
	}
	public <D> void set(String key, D value)
	{
		content.put(key, value);
	}
}
