package spring.corp.framework.email;

import java.util.HashMap;
import java.util.Map;

import spring.corp.framework.configuracao.GerenciadorConfiguracao;
import spring.corp.framework.i18n.GerenciadorMensagem;

public class Email {

	public static void main(String argv[]) throws Exception{
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