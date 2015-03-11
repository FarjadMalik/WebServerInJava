import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
 
 
/**
 * Simple Web Server for learning purposes. Handles one client connection 
 * at a time and sends back a static HTML page as response.
 */
public class SimpleWebServer {
	 ServerSocket s;
 
	 /**
	  * Creates and returns server socket.
	  * @param port Server port.
	  * @return created server socket
	  * @throws Exception Exception thrown, if socket cannot be created.
	  */
    protected ServerSocket getServerSocket(int port) throws Exception {
        return new ServerSocket(port);
    }
 
    /**
     * Starts web server and handles web browser requests.
     * @param port Server port(ex. 80, 8080)
     * @throws Exception Exception thrown, if server fails to start.
     */
    public void runServer(int port) throws Exception {
        s = getServerSocket(port);
        System.out.println("Webserver starting up on port: " + port);
        System.out.println("(press ctrl-c to exit)");
 
        while (true) {
            try {
                Socket serverSocket = s.accept();
                handleRequest(serverSocket);
            } catch(IOException e) {
            	 System.out.println("Failed to start server: " + e.getMessage());
                System.exit(0);
                return;
            }
        }
    }
 
    /**
     * Handles web browser requests and returns a static web page to browser.
     * @param s socket connection between server and web browser.
     * @throws IOException 
     */
    public void handleRequest(Socket s) throws IOException {
        BufferedReader is;     // inputStream from web browser
        PrintWriter os = null;	// outputStream to web browser
        String filename="";
        BufferedReader reader = null;
        boolean getFound = true;
        boolean httpVersion = true;
        boolean headFound = false;
 
        try {
            String webServerAddress = s.getInetAddress().toString(  );
            System.out.println("Accepted connection from " + webServerAddress);
            is = new BufferedReader(new InputStreamReader(s.getInputStream()));

    		os = new PrintWriter(s.getOutputStream(), true);
            
            String str = ".";
            while (!str.equals("")){
              str = is.readLine();
              if(str.contains("GET")){
            	  	getFound=false;
            	  	System.out.println(str);
              		String[] tokens=str.split(" ");
              		if(tokens[2].equalsIgnoreCase("HTTP/1.1")){
              			httpVersion=false;
              		}
              		filename=tokens[1].replace("/", "");
              		System.out.println(filename);
              		if(filename.equalsIgnoreCase(""))
              			filename="Server.txt";
              		
              }
              else if(str.contains("HEAD")){
            	  headFound = true;
              }
            }
            
            
            if(getFound){
            	os.println("HTTP/1.1 400 Bad Syntax");
        		os.println("Content-type: text/html");
        		os.println("Server-name: myserver");
        		os.println("");
        		filename = "BadSyntax.txt" ;
        		reader= new BufferedReader(new FileReader(filename));
            }
            else if(httpVersion){
            	os.println("HTTP/1.1 505 HTTP Version not supported");
        		os.println("Content-type: text/html");
        		os.println("Server-name: myserver");
        		os.println("");
        		filename = "VersionNot.txt" ;
        		reader= new BufferedReader(new FileReader(filename));
            }
            else if(filename.length()>512){
            	os.println("HTTP/1.1 414 URI TOO LARGE");
        		os.println("Content-type: text/html");
        		os.println("Server-name: myserver");
        		os.println("");
        		filename = "TooLarge.txt" ;
        		reader= new BufferedReader(new FileReader(filename));
            }
            else{
            
            	try{
            		reader= new BufferedReader(new FileReader(filename));
            		os.println("HTTP/1.1 200");
            		os.println("Content-type: text/html");
            		os.println("Server-name: myserver");
            		os.println("");
            	}catch(FileNotFoundException e){
            		os.println("HTTP/1.1 404 Url not found");
            		os.println("Content-type: text/html");
            		os.println("Server-name: myserver");
            		os.println("");
            		reader = new BufferedReader(new FileReader("NoFileFound.txt"));
            		
            	}
            }
            if(headFound){
            	//Head already sent we dont have to send the data
            }else{
            	readFileAndHandleRequest(os, reader);
            }
            s.close();
            
        } catch (IOException e) {
            System.out.println("Failed to send response to client: " + e.getMessage());
            os.println("HTTP/1.1 500 Internal Server Error");
    		os.println("Content-type: text/html");
    		os.println("Server-name: myserver");
    		os.println("");
    		filename = "ServerError.txt" ;
    		reader= new BufferedReader(new FileReader(filename));
    		if(headFound){
            	//Head already sent we dont have to send the data
            }else{
            	readFileAndHandleRequest(os, reader);
            }
        }catch(NullPointerException e){
        	System.out.println("Null Pointer Exception caught : "+e.getMessage());
        }
        finally {
        	if(os!=null){
        		os.close();
        	}
        	if(s != null) {
        		try {
					s.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
        
        
        return;
    }

	private void readFileAndHandleRequest(PrintWriter os, BufferedReader reader)
			throws IOException {
		
				String line;
		while((line=reader.readLine())!=null){
			os.println(line+"");
		}
		
		os.flush();
		reader.close();
		
	}
}