package org.pikater.shared.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Utility class for sending emails.
 * 
 * @author krajj7
 */
public class Mailing {
	/**
	 * All emails will be sent with this "from" address.
	 */
	private static final String SENDER_EMAIL = "pikater@noreply.cz";

	/**
	 * Sends a plain text email defined by the arguments. Local SMTP
	 * server needs to be running for this method to work.
	 * 
	 * @throws MessagingException if the email could not be sent
	 */
	public static void sendEmail(String to, String subject, String body) throws MessagingException {
		Properties properties = new Properties();
		properties.setProperty("mail.smtp.host", "localhost"); // local SMTP server is required to be running

		MimeMessage message = new MimeMessage(Session.getDefaultInstance(properties));
		message.setFrom(new InternetAddress(SENDER_EMAIL));
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		message.setSubject(subject);
		message.setText(body);

		Transport.send(message);
	}
}
