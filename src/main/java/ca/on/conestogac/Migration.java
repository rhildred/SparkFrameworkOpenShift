package ca.on.conestogac;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import com.googlecode.flyway.core.Flyway;

public class Migration extends HttpServlet {
	/**
	 * The idea here is that our servlet is restarted when we push, so the migrations will be run
	 */
	private static final long serialVersionUID = 1L;
	public void init() throws ServletException {
		String sUrl = OpenShiftDataSource.getConnectionString(
				getServletContext().getInitParameter("the.db"));
		System.out.println("Data Source: " + sUrl);
		Flyway flyway = new Flyway();
		flyway.setDataSource(sUrl, null, null);
		flyway.migrate();
	}

}
