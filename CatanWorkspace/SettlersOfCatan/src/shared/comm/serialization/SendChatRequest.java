package shared.comm.serialization;

/**
 * Serialization pojo class for easy toJson using gson for sendChat requests
 * @author Cory Beutler
 *
 */
public class SendChatRequest extends AbstractMovesRequest
{
	String content;
	
	public SendChatRequest(int playerIndex, String content)
	{
		super("sendChat", playerIndex);
		
		this.content = content;
	}
	
	public String getContent()
	{
		return content;
	}
}
