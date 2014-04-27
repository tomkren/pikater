package vietpot.server;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public final class MailerClass
{
	// ---------------------------------------------------------------------------
	// app interface for both client and server

	public static void sendThrowableEmail(String stackTrace, String problemDescription) throws AddressException,
			MessagingException
	{
		// send email
		Session session = getSession();
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(vietpotMail));
		addAdminRecipients(msg);
		msg.setSubject(errorSubjectStub + problemDescription);
		msg.setText(stackTrace);
		Transport.send(msg);
	}

	public static void sendProblemAnnounceEmail(String fileAndMethod, String problemDescription) throws AddressException, MessagingException
	{
		Session session = getSession();
		Message msg = new MimeMessage(session);
		msg.setFrom(new InternetAddress(vietpotMail));
		addAdminRecipients(msg);
		msg.setSubject(problemSubjectStub.replace("{0}", fileAndMethod));
		msg.setText(problemBodyStub + problemDescription);
		Transport.send(msg);
	}

	// ---------------------------------------------------------------------------
	// inner interface not to be used by other classes

	private static String vietpotMail = "no.reply@viet-pot.appspotmail.com";
	private static String errorSubjectStub = "EXCEPTION CAUGHT: ";
	private static String problemSubjectStub = "SERVER ERROR: attention needed in '{0}'";
	private static String problemBodyStub = "A general problem regarding the server implementation has "
			+ "been encountered and needs to be addressed:\n\n";
	private static String[] adminEmails = {"kukurka@gmail.com", "honnza@gmail.com"};

	private static void addAdminRecipients(Message msg) throws AddressException, MessagingException
	{
		for(String adminEmail : adminEmails)
		{
			msg.addRecipient(Message.RecipientType.TO, new InternetAddress(adminEmail));
		}
	}

	private static Session getSession()
	{
		//Session session = Session.getDefaultInstance(new Properties(), null);
		return Session.getDefaultInstance(null, null);
	}
}
