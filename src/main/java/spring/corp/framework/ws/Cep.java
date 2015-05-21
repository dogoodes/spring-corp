package spring.corp.framework.ws;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

public class Cep {

	public static Webservicecep getEndereco(String cep) throws JAXBException, MalformedURLException {
		JAXBContext jc = JAXBContext.newInstance(Webservicecep.class);
		Unmarshaller u = jc.createUnmarshaller();
		URL url = new URL("http://cep.republicavirtual.com.br/web_cep.php?cep=" + cep + "&formato=xml");
		// TODO : Setar o timeout por aqui
		Webservicecep wCep = (Webservicecep) u.unmarshal(url);
		return wCep;
	}
}