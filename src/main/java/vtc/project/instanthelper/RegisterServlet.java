package vtc.project.instanthelper;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import vtc.project.instanthelper.PMF;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5156705583512612464L;
	private static final Logger logger = Logger.getLogger(RegisterServlet.class.getCanonicalName());
	
	public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException{
		resp.setContentType("text/plain");
		resp.getWriter().println("REgister servlet");
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
		String email = req.getParameter("email");
		String regid = req.getParameter("regid");

		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			Member m = Member.find(email, pm);
			if(m == null){
				m = new Member(email, regid);
			}else{
				m.setRegid(regid);
			}
			pm.makePersistent(m);
			logger.log(Level.WARNING,"Registered: " + m.getId());
		}finally{
			pm.close();
		}
	}
	
}
