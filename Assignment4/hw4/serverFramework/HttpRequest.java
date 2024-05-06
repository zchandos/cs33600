/*

*/

package serverFramework;

/**
   This class represents an HTTP request message.
<p>
   The HttpMessage super class holds the message
   headers and the (optional) entity body. This
   class holds the HTTP request line.
<p>
   In addition, this class holds a number of fields
   that come from parsing the HTTP request line and
   the HTTP request headers. These are here for
   convenience. The ClientConnection class from this
   framework does the work of parsing the request line
   and headers.
*/
public class HttpRequest extends HttpMessage
{
   public String requestLine;
   public String requestHTTP;          // parsed from the request line
   public String requestMethod;        // parsed from the request line
   public String requestURL;           // parsed from the request line
   public String decodedURL;
   public String resourceName;         // parsed from the request line
   public String queryString;          // parsed from the request line
   public boolean connectionClose;     // parsed from a header
   public boolean connectionKeepAlive; // parsed from a header
   public int keepAliveTimeout;        // parsed from a header
   public int keepAliveMax;            // parsed from a header

   public HttpRequest()
   {
      this.requestLine = "";
   }

   public String getRequestLine()
   {
      return requestLine;
   }

   public void setRequestLine(String requestLine)
   {
      this.requestLine = requestLine;
   }
}
