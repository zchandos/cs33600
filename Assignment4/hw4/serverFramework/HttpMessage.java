/*

*/

package serverFramework;

import java.util.List;
import java.util.ArrayList;

/**
   This is the base class for HTTP messages. It
   has two subclasses, one for HTTP request
   messages and one for HTTP response messages.
<p>
   This class holds what is common to both request
   and response messages, a List of message headers
   and an (optional) entity body.
*/
public abstract class HttpMessage
{
   public final List<String> headers;
   public byte[] entityBody;
   public int entityLength;

   protected HttpMessage()
   {
      headers = new ArrayList<>();
      entityBody = new byte[0];
      entityLength = 0;
   }

   public void addHeader(String header)
   {
      headers.add(header);
   }

   public void setEntityBody(byte[] entityBody)
   {
      this.entityBody = entityBody;
      this.entityLength = entityBody.length;
   }

   public byte[] getEntityBody()
   {
      return entityBody;
   }

}
