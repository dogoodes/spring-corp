package spring.corp.framework.email;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import spring.corp.framework.configuracao.GerenciadorConfiguracao;
import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.io.SerializableInputStream;
import spring.corp.framework.json.JSONFileAttachment;

public class EmailAttachment {

	@SuppressWarnings("resource")
	public static void main(String argv[]) throws Exception{
		JSONFileAttachment attachmentXML = new JSONFileAttachment();
		attachmentXML.setContentType("text/plain");
		attachmentXML.setFile("<root><name>Alberto Cerqueira</name></root>");
		attachmentXML.setFileName("xml-file.xml");
		
		String folder = GerenciadorConfiguracao.getConfiguracao("diretorio.arquivos");
		File file = new File(folder + "pdf-file.pdf");
		SerializableInputStream s = new SerializableInputStream(new FileInputStream(file));
		
		JSONFileAttachment attachmentPDF = new JSONFileAttachment();
		attachmentPDF.setFile(s.getByte());
		attachmentPDF.setContentType(JSONFileAttachment.PDF_ATTACHMENT);
		attachmentPDF.setFileName("pdf-file.pdf");
		
		try {
			String subject = "Teste envio de email";
			String message = "Texto do email aqui.";
			String nameSender = GerenciadorConfiguracao.getConfiguracao("name.user");
			String sender = GerenciadorConfiguracao.getConfiguracao("mail.user");
			Map<String, String> recipients = new HashMap<String, String>();
			recipients.put("alberto.cerqueira1990@gmail.com", "Alberto Cerqueira");
			GerenciadorEmail ge = GerenciadorEmail.builderInstance()
				.recipients(recipients)
				.subject(subject)
				.attach(new JSONFileAttachment[]{attachmentXML, attachmentPDF})
				.message(message) /*Se nao tiver mensagem vai ser enviado o HTML*/
				.name(nameSender)
				.from(sender)
				.build();
			Thread t = new Thread(ge);
			t.start();
			
			System.out.println(GerenciadorMensagem.getMessage("view.mensagem.enviada.com.sucesso"));
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL));
		}
	}
}