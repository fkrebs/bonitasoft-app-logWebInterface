// src : http://www.codejava.net/java-ee/servlet/java-servlet-download-file-example

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadLogFileServlet extends HttpServlet {
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		ServletOutputStream out = null;

		// reads input file
		String dirPath = request.getServletContext().getInitParameter("logsDirectory");
		if (dirPath == null || dirPath.trim().length() == 0) {
			response.setContentType("text/html");
			out = response.getOutputStream();
			out.println("Initialization unsuccessful.");
			return;
		}
		File folder = new File(dirPath);

		String logfileParameter = request.getParameter("logfile");
		if (logfileParameter == null || logfileParameter.trim().length() == 0) {
			response.setContentType("text/html");
			out = response.getOutputStream();
			out.println("The parameter logfile is mandatory.");		
			return;
		}

		File logfile = new File(folder, logfileParameter);
		if (!logfile.exists() || !logfile.isFile() || !logfile.getParentFile().getCanonicalPath().startsWith(folder.getCanonicalPath())) {		
			String strMess = "The requested log file " + logfileParameter + " does not exist.";
			response.setContentType("text/html");
			out = response.getOutputStream();
			out.println(strMess);	
			return;
		} else {		
		
			FileInputStream inStream = new FileInputStream(logfile);		
			

			// set MIME type of the file
			String mimeType = "application/octet-stream";
			
			// modifies response
			response.setContentType(mimeType);
			response.setContentLength((int) logfile.length());
			
			// forces download
			String headerKey = "Content-Disposition";
			String headerValue = String.format("attachment; filename=\"%s\"", logfile.getName());
			response.setHeader(headerKey, headerValue);
			
			// obtains response's output stream
			out = response.getOutputStream();
			
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			
			while ((bytesRead = inStream.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			
			inStream.close();
			out.close();		
		}
		
	
	}
}
