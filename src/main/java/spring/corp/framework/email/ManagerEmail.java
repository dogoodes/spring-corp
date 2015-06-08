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

import spring.corp.framework.configuracao.ManagerSetting;
import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.io.SerializableInputStream;
import spring.corp.framework.json.JSONFileAttachment;
import spring.corp.framework.log.ManagerLog;
import spring.corp.framework.utils.IOUtils;

public class ManagerEmail implements Runnable {

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
			URL url = ManagerEmail.class.getResource("/" + arquivo);
			if (url == null) {
				url = ManagerEmail.class.getResource(arquivo);
			}
			byte[] b = IOUtils.toByteArray(url.openStream(), 2000);
			html = new String(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			String message = GerenciadorMensagem.getMessage("arquivo.nao.encontrado", arquivo);
			ManagerLog.critical(ManagerEmail.class, message);
		} catch (IOException e) {
			e.printStackTrace();
			String message = GerenciadorMensagem.getMessage("arquivo.invalido", arquivo);
			ManagerLog.critical(ManagerEmail.class, message);
		}
	}
	
	public static ManagerEmail.Builder builderInstance() {
		return new ManagerEmail.Builder();
	}
	
	private ManagerEmail(Builder builder) {
		this.attach = builder.attach;
		this.name = builder.name;
		this.from = builder.from;
		this.message = builder.message;
		this.recipients = builder.recipients;
		this.subject = builder.subject;
		ManagerEmail.html = builder.html;
		this.usernames = ManagerSetting.getSetting("mail.login").split("[;]");
	}
	
	public static class Builder {
		private Map<String, String> recipients; //key = email | value = name
		private String subject;
		private String message;
		private String name; // name' sender
		private String from; // sender
		private String html;
		private JSONFileAttachment[] attach;
		
		/**
		 * Destinatários do email
		 * @param recipients (Map<String, String>) map com nome e email dos destinatários
		 * @return (Builder) class interna responsável pela construção da estrutura do email
		 */
		public Builder recipients(Map<String, String> recipients) {
			this.recipients = recipients;
			return this;
		}
		
		/**
		 * Assunto do email
		 * @param subject (String) assunto
		 * @return (Builder) class interna responsável pela construção da estrutura do email
		 */
		public Builder subject(String subject) {
			this.subject = subject;
			return this;
		}
		
		/**
		 * HTML do email<br />
		 * Para envio de emails marketing ou relativos
		 * @param html (String) código html
		 * @return (Builder) class interna responsável pela construção da estrutura do email
		 */
		public Builder html(String html) {
			this.html = html;
			return this;
		}
		
		/**
		 * Mensagem do email
		 * @param message (String) mensagem
		 * @return (Builder) class interna responsável pela construção da estrutura do email
		 */
		public Builder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		 * Nome do responsável pelo email
		 * @param name (String) nome do responsável
		 * @return (Builder) class interna responsável pela construção da estrutura do email
		 */
		public Builder name(String name) {
			this.name = name;
			return this;
		}
		
		/**
		 * Email do responsável pelo email
		 * @param form (String) email do responsável
		 * @return (Builder) class interna responsável pela construção da estrutura do email
		 */
		public Builder from(String from) {
			this.from = from;
			return this;
		}
		
		/**
		 * Arquivos para envio em anexo do email
		 * @param attach (JSONFileAttachment...) arquivos java.io.Serializable para anexo 
		 * @return (Builder) class interna responsável pela construção da estrutura do email
		 * @See java.io.Serializable
		 */
		public Builder attach(JSONFileAttachment...attach) {
			this.attach = attach;
			return this;
		}

		/**
		 * Construir estrutura do email
		 * @return (ManagerEmail) Gerenciado de email com estrutura pronta
		 */
		public ManagerEmail build() {
			return new ManagerEmail(this);
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
		props.put("mail.smtp.host", ManagerSetting.getSetting("mail.smtp.host"));
		props.put("mail.smtp.auth", ManagerSetting.getSetting("mail.smtp.auth"));
		props.put("mail.smtp.port", ManagerSetting.getSetting("mail.smtp.port"));
		
		Authenticator auth = new SMTPAuthenticator();
		Session session = Session.getDefaultInstance(props, auth);
		session.setDebug(debug);
		
		// set the from and to address
		MimeBodyPart bodyEmail = new MimeBodyPart();
		bodyEmail.setContent(message, "text/plain");
		Multipart mp = new MimeMultipart();
		mp.addBodyPart(bodyEmail);
		
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
				ManagerLog.debug(ManagerEmail.class, e, email);
			}
			i++;
		}
		
		Message msg = new MimeMessage(session);
		try {
			InternetAddress addressFrom = new InternetAddress(from, name);
			msg.setFrom(addressFrom);
		} catch (UnsupportedEncodingException e) {
			ManagerLog.debug(ManagerEmail.class, e, from);
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
		props.put("mail.smtp.host", ManagerSetting.getSetting("mail.smtp.host"));
		props.put("mail.smtp.auth", ManagerSetting.getSetting("mail.smtp.auth"));
		props.put("mail.smtp.port", ManagerSetting.getSetting("mail.smtp.port"));
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
				ManagerLog.debug(ManagerEmail.class, e, email);
			}
			i++;
		}
		
		Message msg = new MimeMessage(session);
		try {
			InternetAddress addressFrom = new InternetAddress(from, name);
			msg.setFrom(addressFrom);
		} catch (UnsupportedEncodingException e) {
			ManagerLog.debug(ManagerEmail.class, e, from);
		}
		msg.setRecipients(Message.RecipientType.TO, addressTo);
		msg.setSubject(subject);
		msg.setContent(mp);
		Transport.send(msg);
	}
	
	private MimeBodyPart getMimeBodyAttachment(JSONFileAttachment attach) throws MessagingException {
		MimeBodyPart attachment = new MimeBodyPart();
		
		DataHandler dataHandler = null;
		try {
			SerializableInputStream s = (SerializableInputStream) attach.getFile();
			DataSource dataSource = new ByteArrayDataSource(s.getByte(), attach.getContentType());
			dataHandler = new DataHandler(dataSource);
		} catch (Exception e1) {
			try {
				DataSource dataSource = new ByteArrayDataSource(((byte[]) attach.getFile()), attach.getContentType());
				dataHandler = new DataHandler(dataSource);
			} catch (Exception e2) {
				dataHandler = new DataHandler(attach.getFile(), attach.getContentType());
			}
		}
		
		attachment.setDataHandler(dataHandler);
		attachment.setFileName(attach.getFileName());
		if (attach.getContentType() != null && attach.getContentType().equals(JSONFileAttachment.EMAIL_ATTACHMENT)) {
			attachment.setHeader("Content-Transfer-Encoding", "base64");
		}
		
		return attachment;
	}
	
	private class SMTPAuthenticator extends javax.mail.Authenticator {
	    public PasswordAuthentication getPasswordAuthentication() {
	        String password = ManagerSetting.getSetting("mail.pass");
	        PasswordAuthentication  passAuth = new PasswordAuthentication(usernames[posUserName], password);
	        posUserName++;
	        if (posUserName == usernames.length) {
	        	posUserName = 0;
	    	}
	        return passAuth;
	    }
	}
}