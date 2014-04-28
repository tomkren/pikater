package org.pikater.web.servlets;

import jade.content.lang.Codec.CodecException;
import jade.content.onto.OntologyException;
import jade.lang.acl.ACLMessage;
import jade.wrapper.ControllerException;
import jade.wrapper.gateway.JadeGateway;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.pikater.web.pikater.PikaterGateway;
import org.pikater.web.pikater.PikaterActionInitiator;

import org.pikater.core.agents.system.Agent_Mailing;
import org.pikater.core.ontology.messages.SendEmail;

/**
 * Servlet testuje propojení: Tomcat <=> JADE Gateway <=> lokální JADE.
 * Očekává, že je na lokálním JADE spuštěný mailAgent a skrz něj pošle testovací mail na zadanou adresu.
 */

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/JadeGW", displayName="JADE GW example")
public class JadeGatewayExample extends HttpServlet {
    @Override
    public void init() throws ServletException {
        JadeGateway.init("org.newpikater.pikater.PikaterGateway", null);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>MailAgent tester</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<form method=post>Adresa kam se posle mail: <input type=text name=addr><input type=submit value=ok></form>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ACLMessage response;
        try {
            ACLMessage msg = PikaterGateway.makeActionRequest("mailAgent",
                    new SendEmail(Agent_Mailing.EmailType.TEST, req.getParameter("addr")));
            PikaterActionInitiator initiator = new PikaterActionInitiator(msg);
            JadeGateway.execute(initiator, 10000);
            response = initiator.getOkResponse();
        } catch (CodecException | OntologyException e1) {
            throw new ServletException("Failed to make request");
        } catch (ControllerException | InterruptedException e) {
            ServletException x = new ServletException("JadeGateway.execute() failed");
            x.setStackTrace(e.getStackTrace());
            throw x;
        }
        
        resp.setContentType("text/html");
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("<head>");
        out.println("<title>MailAgent tester</title>");
        out.println("</head>");
        out.println("<body>");
        out.println("<p>Response: " + ACLMessage.getPerformative(response.getPerformative())+" "+response.getContent() + "</p>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
