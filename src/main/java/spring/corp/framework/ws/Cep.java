package spring.corp.framework.ws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Cep {

	/**
	 * Consulta dados de endereço de acordo com o cep
	 * @param cep (String) contendo o cep para consulta (formato 99999-999 ou 99999999)
	 * @return (Webservicecep) o retorno do webservice é no formato xml, com isso temos a classe Webservicecep com XmlRootElement   
	 * @throws JAXBException lançanda caso exista algum problema no unmarshal() do xml com a classe spring.corp.framework.ws.Webservicecep
	 * @throws MalformedURLException URL de conexão inválida
	 * 
	 * @see spring.corp.framework.ws.Webservicecep
	 * @link http://cep.republicavirtual.com.br
	 */
	public static Webservicecep getAddress(String cep) throws JAXBException, MalformedURLException {
		JAXBContext jc = JAXBContext.newInstance(Webservicecep.class);
		Unmarshaller u = jc.createUnmarshaller();
		URL url = new URL("http://cep.republicavirtual.com.br/web_cep.php?cep=" + cep + "&formato=xml");
		// TODO : Setar o timeout por aqui
		Webservicecep wCep = (Webservicecep) u.unmarshal(url);
		return wCep;
	}
}