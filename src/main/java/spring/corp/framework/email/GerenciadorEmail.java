package spring.corp.framework.email;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import spring.corp.framework.configuracao.GerenciadorConfiguracao;
import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.io.IOUtils;
import spring.corp.framework.json.JSONFileAttachment;
import spring.corp.framework.log.GerenciadorLog;

public class GerenciadorEmail implements Runnable {

	private final Map<String, String> recipients; //key = email | value = name
	private final String subject;
	private final String message;
	private final String name; // name' sender
	private final String from; // sender
	private final JSONFileAttachment[] attach;
	private String[] usernames = null;
	private static String html = null;
	private static transient int posUserName = 0;
	
	static {
		String arquivo = "email-falha-envio.html";
		try {
			URL url = GerenciadorEmail.class.getResource("/" + arquivo);
			if (url == null) {
				url = GerenciadorEmail.class.getResource(arquivo);
			}
			byte[] b = IOUtils.toByteArray(url.openStream(), 2000);
			html = new String(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			String message = GerenciadorMensagem.getMessage("arquivo.nao.encontrado", arquivo);
			GerenciadorLog.critical(GerenciadorEmail.class, message);
		} catch (IOException e) {
			e.printStackTrace();
			String message = GerenciadorMensagem.getMessage("arquivo.invalido", arquivo);
			GerenciadorLog.critical(GerenciadorEmail.class, message);
		}
	}
	
	public static GerenciadorEmail.Builder builderInstance() {
		return new GerenciadorEmail.Builder();
	}
	
	private GerenciadorEmail(Builder builder) {
		this.attach = builder.attach;
		this.name = builder.name;
		this.from = builder.from;
		this.message = builder.message;
		this.recipients = builder.recipients;
		this.subject = builder.subject;
		GerenciadorEmail.html = builder.html;
		this.usernames = GerenciadorConfiguracao.getConfiguracao("mail.login").split("[;]");
	}
	
	public static class Builder {
		private Map<String, String> recipients; //key = email | value = name
		private String subject;
		private String message;
		private String name; // name' sender
		private String from; // sender
		private String html;
		private JSONFileAttachment[] attach;
		
		public Builder recipients(Map<String, String> recipients) {
			this.recipients = recipients;
			return this;
		}
		
		public Builder subject(String subject) {
			this.subject = subject;
			return this;
		}
		
		public Builder html(String html) {
			this.html = html;
			return this;
		}
		
		public Builder message(String message) {
			this.message = message;
			return this;
		}
		
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder from(String from) {
			this.from = from;
			return this;
		}
		
		public Builder attach(JSONFileAttachment...attach) {
			this.attach = attach;
			return this;
		}

		public GerenciadorEmail build() {
			return new GerenciadorEmail(this);
		}
	}
	
	@Override
	public void run() throws UserException {
		try {
			if (this.message != null) {
				enviarEmail();
			} else {
				enviarHTMLEmail();
			}
		} catch (Exception e) {
			 throw new UserException(e);
		}
	}
	
	public void enviarEmail() throws MessagingException {
		boolean debug = false;
		
		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", GerenciadorConfiguracao.getConfiguracao("mail.smtp.host"));
		props.put("mail.smtp.auth", GerenciadorConfiguracao.getConfiguracao("mail.smtp.auth"));
		props.put("mail.smtp.port", GerenciadorConfiguracao.getConfiguracao("mail.smtp.port"));
		
		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(debug);
		
		// set the from and to address
		MimeBodyPart corpoEmail = new MimeBodyPart();
		corpoEmail.setContent(message, "text/plain");
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(corpoEmail);
		
		if (attach != null) {
			for (JSONFileAttachment _attach : attach) {
				mp.addBodyPart(getMimeBodyAttachment(_attach));
			}
		}
		
		InternetAddress[] addressTo = new InternetAddress[recipients.size()];
		int i = 0;
		for (String email: recipients.keySet()) {
			String name = recipients.get(email);
			try {
				addressTo[i] = new InternetAddress(email, name);
			} catch (UnsupportedEncodingException e) {
				GerenciadorLog.debug(GerenciadorEmail.class, e, email);
			}
			i++;
		}
		
		Message msg = new MimeMessage(session);
		try {
			InternetAddress addressFrom = new InternetAddress(from, name);
			msg.setFrom(addressFrom);
		} catch (UnsupportedEncodingException e) {
			GerenciadorLog.debug(GerenciadorEmail.class, e, from);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		msg.setSubject(subject);
		msg.setContent(mp);
		Transport.send(msg);
	}
	
	public void enviarHTMLEmail() throws MessagingException {
		boolean debug = false;
		// Set the host smtp address
		Properties props = new Properties();
		props.put("mail.smtp.host", GerenciadorConfiguracao.getConfiguracao("mail.smtp.host"));
		props.put("mail.smtp.auth", GerenciadorConfiguracao.getConfiguracao("mail.smtp.auth"));
		props.put("mail.smtp.port", GerenciadorConfiguracao.getConfiguracao("mail.smtp.port"));
		props.put("mail.transport.protocol", "smtp");

		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(debug);
		
		// set the from and to address
		MimeBodyPart corpoEmail = new MimeBodyPart();
		corpoEmail.setContent(html, "text/html");
		Multipart mp = new MimeMultipart("related");
		mp.addBodyPart(corpoEmail);
		
		if (attach != null) {
			for (JSONFileAttachment _attach : attach) {
				mp.addBodyPart(getMimeBodyAttachment(_attach));
			}
		}
		
		InternetAddress[] addressTo = new InternetAddress[recipients.size()];
		int i = 0;
		for (String email: recipients.keySet()) {
			String name = recipients.get(email);
			try {
				addressTo[i] = new InternetAddress(email, name);
			} catch (UnsupportedEncodingException e) {
				GerenciadorLog.debug(GerenciadorEmail.class, e, email);
			}
			i++;
		}
		
		Message msg = new MimeMessage(session);
		try {
			InternetAddress addressFrom = new InternetAddress(from, name);
			msg.setFrom(addressFrom);
		} catch (UnsupportedEncodingException e) {
			GerenciadorLog.debug(GerenciadorEmail.class, e, from);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		msg.setSubject(subject);
		msg.setContent(mp);
		Transport.send(msg);
	}
	
	private MimeBodyPart getMimeBodyAttachment(JSONFileAttachment attach) throws MessagingException{
		MimeBodyPart attachment = new MimeBodyPart();
		if ("text/plain".equals(attach.getContentType())) {
			attachment.setDataHandler(new DataHandler(attach.getFile(), attach.getContentType()));
			attachment.setFileName(attach.getFileName());
		} else {
			DataSource dataSource = new ByteArrayDataSource((byte[]) attach.getFile(), attach.getContentType());
			attachment.setDataHandler(new DataHandler(dataSource));
			attachment.setFileName(attach.getFileName());
			if (attach.getContentType() != null && attach.getContentType().equals(JSONFileAttachment.EMAIL_ATTACHMENT)) {
				attachment.setHeader("Content-Transfer-Encoding", "base64");
			}
		}
		return attachment;
	}
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {
	    public PasswordAuthentication getPasswordAuthentication() {
	        String password = GerenciadorConfiguracao.getConfiguracao("mail.pass");
	        PasswordAuthentication  passAuth = new PasswordAuthentication(usernames[posUserName], password);
	        posUserName++;
	        if (posUserName == usernames.length) {
	        	posUserName = 0;
	    	}
	        return passAuth;
	    }
	}
}