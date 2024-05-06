/*

*/

package serverFramework;

/**
   This class represents an HTTP response message.
<p>
   The HttpMessage super class holds the message
   headers and the (optional) entity body. This
   class holds the HTTP response line.
<p>
   It is the responsibility of the methods that
   implement the HttpMethods interface to fill
   in the members of this object. Filling in these
   data members is how a "web app" computes its
   results.
<p>
   The ClientConnection class from this framework
   uses the data from this class to complete a
   client's request to the web app.
*/
public class HttpResponse extends HttpMessage
{
   public String responseLine;
   public boolean connectionClose;
   public boolean connectionKeepAlive;

   public HttpResponse()
   {
      this.responseLine = "";
   }

   public String getResponseLine()
   {
      return responseLine;
   }

   public void setResponseLine(String responseLine)
   {
      this.responseLine = responseLine;
   }
}
