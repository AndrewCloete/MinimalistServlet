package acza.stellee.geyserm2m;

import java.io.*;
import java.net.*; //Library required for HTTPclientPOST()

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;


public class MinimalistServlet
{
	
	private static int PORT = 9090;
	private static String aPoC = "localhost" + ":" + PORT;
	
	private static final String GEYSER_ID = "sim_geyser_1";
	private static final String APP_URI = "localhost:8181/om2m/gscl/applications";
	private static final String CONTAINER_URI = "localhost:8181/om2m/gscl/applications/" + GEYSER_ID + "/containers";
	private static final String CONTAINER_ID = "DATA";
	private static final String CONTENT_URI = "localhost:8181/om2m/gscl/applications/" + GEYSER_ID + "/containers/" + CONTAINER_ID + "/contentInstances";
	
	
	public static void main( String[] args ) throws Exception
	{
		// Create a basic jetty server object that will listen on port 8080.
		// Note that if you set this to port 0 then a randomly available port
		// will be assigned that you can either look in the logs for the port,
		// or programmatically obtain it for use in test cases.
		Server server = new Server(PORT);

		// The ServletHandler is a dead simple way to create a context handler
		// that is backed by an instance of a Servlet.
		// This handler then needs to be registered with the Server object.
		ServletHandler handler = new ServletHandler();
		server.setHandler(handler);

		// Passing in the class for the Servlet allows jetty to instantiate an
		// instance of that Servlet and mount it on a given context path.

		// IMPORTANT:
		// This is a raw Servlet, not a Servlet that has been configured
		// through a web.xml @WebServlet annotation, or anything similar.
		handler.addServletWithMapping(HelloServlet.class, "/*");

		// Start things up!
		server.start();
		
		/* PSUEDO
		 * 
		 * Create a global geyser object with get and set methods (which are synchronized)
		 * 
		 * Register with the GSCL using marshalled objects
		 * 		Give AppID
		 * 		Creates container
		 * 		Creates content instances
		 * 		(Verify that registration was successful)
		 * 
		 * Start a ControlLoopThread() (can be main thread for now)
		 * 		which wakes up every minute
		 * 		prompts the geyser to simulate next step (i.e the past minute).
		 * 		prompts the geyser for data
		 * 		posts it to the GSCL in oBIX format
		 * 		keeps a persistence.
		 * 
		 * doPost() sets parameters on the geyser object
		 * 
		 * **** IN REALITY ****
		 * There will still be a geyser object but you will instantiate it with a URL and it
		 * will simply be a proxy which communicates via TCP/UDP with geyser hardware. 
		 * NOTE: Just because you use TCP does not mean that the hardware controller is a
		 * remote machine. It could just be a different process on the same machine.
		 */

		Geyser geyser = new Geyser(GEYSER_ID);
		HTTPclientPOST(APP_URI, M2MxmlFactory.registerApplication(GEYSER_ID, aPoC));
		HTTPclientPOST(CONTAINER_URI, M2MxmlFactory.addContainer(CONTAINER_ID));
		
		
		
		while(true){
			
			geyser.simulate_step();
			HTTPclientPOST(CONTENT_URI, geyser.toCSV());
			Thread.sleep(5000);
		}
		
		
		// The use of server.join() the will make the current thread join and
		// wait until the server is done executing.
		// See
		// http://docs.oracle.com/javase/7/docs/api/java/lang/Thread.html#join()
		//server.join();
	}

	@SuppressWarnings("serial")
	public static class HelloServlet extends HttpServlet
	{
		@Override
		protected void doGet( HttpServletRequest request, HttpServletResponse response ) throws ServletException, IOException{
					response.setContentType("text/html");
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().println("<h1>Hello from HelloServlet</h1>");
				}
		
		@Override
		protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().println("<h1>Succesful post</h1>");
			System.out.println("Post REQUEST successful");
		}
	}
	
	
	private static void HTTPclientPOST(String postURI, String postData){
		try{
			URL url = new URL("http://" + postURI);
			HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
			urlcon.setRequestMethod("POST");
			urlcon.setRequestProperty("Content-type", "text/html");
			urlcon.setRequestProperty("Authorization", "Basic YWRtaW46YWRtaW4=");
			urlcon.setDoOutput(true); 
			urlcon.setDoInput(true);
			PrintWriter pout = new PrintWriter(new OutputStreamWriter(urlcon.getOutputStream(), "8859_1"), true);
			pout.print(postData);
			pout.flush();

			InputStream in = urlcon.getInputStream();
			InputStreamReader inr = new InputStreamReader(in);
			BufferedReader bin = new BufferedReader(inr);

			String line;
			while((line = bin.readLine()) != null){
				System.out.println(line);
			}

		}catch (MalformedURLException e){
			System.out.println(e);
		}catch (IOException e2){
			System.out.println(e2);
		}
		
		
		
	}
	
}