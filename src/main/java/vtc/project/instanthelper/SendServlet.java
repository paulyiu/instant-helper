package vtc.project.instanthelper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.NameValuePair;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;

public class SendServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8223962128497885433L;

	private static final Logger logger = Logger.getLogger(SendServlet.class
			.getCanonicalName());
	
	private static String GCM_KEY = "AIzaSyD12dEs3V36RsuCr6Xmf20r-OdEMlw5t1A";
	private String mResults = "";
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		showData(resp);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		String msg = req.getParameter("msg");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();

		Query q = pm.newQuery(Member.class);
		try{
			List<Member> results = (List<Member>) q.execute();
			if(!results.isEmpty()){
				List<String> registration_ids = new ArrayList<String>();
				for(Member m: results){
					logger.log(Level.FINE,m.getEmail() + ":" + m.getRegid());

					registration_ids.add(m.getRegid());
				}
				new ArrayList<NameValuePair>();
				Sender sender = new Sender(GCM_KEY);
				Message message = new Message.Builder()
				.addData("message",msg).build();
				MulticastResult multicastResult;
				multicastResult = sender.sendNoRetry(message, registration_ids);
				mResults = multicastResult.toString();
				logger.log(Level.WARNING,mResults);

			}else{
				logger.log(Level.WARNING,"No devices found in datastore");
			}
		}finally{
			q.closeAll();
		}
		showData(resp);
	}
	
	protected void showData(HttpServletResponse resp) throws IOException{
		resp.setContentType("text/html");
		PrintWriter out = resp.getWriter();
		out.write("<html>");
		out.write("<body>");
		out.write("<form action='/admin/send' method='Post'>");
		out.write("<input type='text' name='msg'><br>");
		out.write("<input type='submit'>");
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Member.class);
		try{
			List<Member> results = (List<Member>) q.execute();
			out.write("<table border=1 cellpadding=10>");
			out.write("<th>ID</th><th>Email</th><th>RegID</th>");
			for(Member m: results){
				out.write("<tr>");
				out.write("<td>" + m.getId() + "</td>");
				out.write("<td>" + m.getEmail() + "</td>");
				out.write("<td>" + m.getRegid() + "</td>");
				out.write("</tr>");
			}
			out.write("</table>");
			out.write(mResults);
		}finally{
			q.closeAll();
		}
		out.write("</body>");
		out.write("</html>");
	}
}
