/*

*/

package serverFramework;

/**
   Implement the HttpMethods interface with concrete methods
   that send an error message. The error code is 501 for a
   "Not Implemented" method.
   See
      https://developer.mozilla.org/en-US/docs/Web/HTTP/Status/501
<p>
   This adaptor can be used in this framework to implement
   a complete HTTP server that does nothing but return
   error responses.
*/
public class HttpMethodsAdaptor implements HttpMethods
{
   public HttpApplicationServer server;

   /**
      Give this object a reference to the server
      that it is working for. This allows the HTTP
      methods to find information about the server,
      if that is needed.
   */
   @Override
   public void setServer(HttpApplicationServer server)
   {
      this.server = server;
   }


   /**
      Override this method to implement the HTTP GET command.
   */
   @Override
   public void doGet(HttpRequest req, HttpResponse res)
   {
      sendErrorResponse(req, res,
                "501",
                "Not Implemented",
                "This server cannot handle GET requests.");
   }


   /**
      Override this method to implement the HTTP HEAD command.
   */
   @Override
   public void doHead(HttpRequest req, HttpResponse res)
   {
      sendErrorResponse(req, res,
                "501",
                "Not Implemented",
                "This server cannot handle HEAD requests.");
   }


   /**
      Override this method to implement the HTTP POST command.
   */
   @Override
   public void doPost(HttpRequest req, HttpResponse res)
   {
      sendErrorResponse(req, res,
                "501",
                "Not Implemented",
                "This server cannot handle POST requests.");
   }


   /**
      Override this method to implement the HTTP PUT command.
   */
   @Override
   public void doPut(HttpRequest req, HttpResponse res)
   {
      sendErrorResponse(req, res,
                "501",
                "Not Implemented",
                "This server cannot handle PUT requests.");
   }


   /**
      Override this method to implement the HTTP PATCH command.
   */
   @Override
   public void doPatch(HttpRequest req, HttpResponse res)
   {
      sendErrorResponse(req, res,
                "501",
                "Not Implemented",
                "This server cannot handle PATCH requests.");
   }


   /**
      Override this method to implement the HTTP DELETE command.
   */
   @Override
   public void doDelete(HttpRequest req, HttpResponse res)
   {
      sendErrorResponse(req, res,
                "501",
                "Not Implemented",
                "This server cannot handle DELETE requests.");
   }


   /**
      Override this method to implement the HTTP OPTIONS command.
   */
   @Override
   public void doOptions(HttpRequest req, HttpResponse res)
   {
      sendErrorResponse(req, res,
                "501",
                "Not Implemented",
                "This server cannot handle OPTIONS requests.");
   }


   /**
      Override this method to implement the HTTP TRACE command.
   */
   @Override
   public void doTrace(HttpRequest req, HttpResponse res)
   {
      sendErrorResponse(req, res,
                "501",
                "Not Implemented",
                "This server cannot handle TRACE requests.");
   }


   /**
      Override this method to implement the HTTP CONNECT command.
   */
   @Override
   public void doConnect(HttpRequest req, HttpResponse res)
   {
      sendErrorResponse(req, res,
                "501",
                "Not Implemented",
                "This server cannot handle CONNECT requests.");
   }
}
