package org.pikater.shared.utilities.mailing;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailing {
	/** Adresa, ktera bude uvedena ve FROM u vsech mailu */
	private static final String SENDER_EMAIL = "pikater@noreply.cz";

	/** Odesle plaintext e-mail dle parametru pomoci lokalniho SMTP serveru, nebo vyhodi vyjimku. */
	public static void sendEmail(String to, String subject, String body) throws MessagingException {
		Properties properties = new Properties();
		// predpokladam funkcni SMTP server na localhostu
		properties.setProperty("mail.smtp.host", "localhost");

		MimeMessage message = new MimeMessage(Session.getDefaultInstance(properties));
		// od koho
		message.setFrom(new InternetAddress(SENDER_EMAIL));
		// komu
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
		// predmet
		message.setSubject(subject);
		// telo
		message.setText(body);

		Transport.send(message);
	}
}
