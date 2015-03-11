
 
public class TestClass {
	/**
	 * Main Method
	 */
	public static void main(String[] args) {
	        SimpleWebServer webServer = new SimpleWebServer();
	        try {
			webServer.runServer(8080);
		} catch (Exception e) {
			e.printStackTrace();
		}   
	}
}