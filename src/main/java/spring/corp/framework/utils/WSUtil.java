package spring.corp.framework.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.apache.soap.util.xml.DOM2Writer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import spring.corp.framework.exceptions.CriticalUserException;
import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.i18n.ManagerMessage;
import spring.corp.framework.log.ManagerLog;

public class WSUtil {

	public static <T> OutputStream marshall(T object, String schemaPath) throws IOException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
			javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
			Schema schema = createSchema(schemaPath);
			if (schema != null) {
				jaxbMarshaller.setSchema(schema);
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			jaxbMarshaller.marshal(object, baos);
			if (ManagerLog.isDebug(WSUtil.class)) {
				ManagerLog.debug(WSUtil.class, baos.toString());
			}
			return baos;
		} catch (Exception e) {
			String message = ManagerMessage.getMessage("webservice.geracaoxml.parser.error");
			try {
				JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
				javax.xml.bind.Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				jaxbMarshaller.marshal(object, baos);
				String xmlComErrodeParser = baos.toString();
				ManagerLog.critical(WSUtil.class, e, message + " : " + xmlComErrodeParser);
			} catch (Exception ex) {
				ManagerLog.critical(WSUtil.class, e, message);
			}
			throw new UserException(message);
		}
	}

	public static Schema createSchema(String schemaPath) throws UserException {
		Schema schema = null;
		if (schemaPath != null) {
			SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
			if (schemaPath != null) {
				URL urlSchema = WSUtil.class.getResource(schemaPath);
				try {
					schema = sf.newSchema(urlSchema);
				} catch (SAXException e) {
					String message = ManagerMessage.getMessage("webservice.geracaoxml.parser.error");
					throw new UserException(message);
				}
			}
		}
		return schema;
	}

	@SuppressWarnings("unchecked")
	public static <T> T unMarshall(T object, String dado, String schemaPath) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
			javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			if (schemaPath != null) {
				SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				URL urlSchema = WSUtil.class.getResource(schemaPath);
				Schema schema = sf.newSchema(urlSchema);
				jaxbUnmarshaller.setSchema(schema);
			}
			T retObject = (T) jaxbUnmarshaller.unmarshal(new StringReader(dado));
			return retObject;
		} catch (Exception e) {
			String message = ManagerMessage.getMessage("webservice.unmarshall.exception", object, dado, (schemaPath == null?"":schemaPath));
			throw new CriticalUserException(WSUtil.class, message, e);
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T silentUnMarshall(T object, String dado, String schemaPath) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(object.getClass());
			javax.xml.bind.Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			if (schemaPath != null) {
				SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
				URL urlSchema = WSUtil.class.getResource(schemaPath);
				Schema schema = sf.newSchema(urlSchema);
				jaxbUnmarshaller.setSchema(schema);
			}
			T retObject = (T) jaxbUnmarshaller.unmarshal(new StringReader(dado));
			return retObject;
		} catch (Exception e) {
			String message = ManagerMessage.getMessage("webservice.unmarshall.exception", object, "silent", (schemaPath == null?"":schemaPath));
			throw new UserException(WSUtil.class, message, e);
		}
	}

	/**
	 * Metodo responsavel por transforma um Node na classe informada pelo
	 * parametro
	 * 
	 * @param <T> O tipo da classe
	 * @param node O Node que possui a estrutura tree que sera transformada para objeto do tipo T
	 * @param classResult A classe que representa o que esta na estrutura tree do node
	 * @return O Objeto transformado de Node para Objeto Java
	 * @throws UserException Caso algum erro ocorra na transformacao.
	 */
	public static <T> T unMarshall(Node node, Class<T> classResult) throws UserException {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(classResult);
			javax.xml.bind.Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
			Object result = jaxbMarshaller.unmarshal(node);
			return classResult.cast(result);
		} catch (Exception e) {
			String message = "";
			throw new CriticalUserException(WSUtil.class, message, e);
		}
	}

	public static SOAPBody getSOAPBody(SOAPMessage soapMessage) {
		try {
			SOAPBody body = soapMessage.getSOAPBody();
			return body;
		} catch (SOAPException e) {
			String message = ManagerMessage.getMessage(ManagerMessage.ERRO_GERAL);
			throw new CriticalUserException(WSUtil.class, message, e);
		}
	}
	
	public static String getSOAPBodyContent(Node contentBody){
		String resultContent = DOM2Writer.nodeToString(contentBody);
		return resultContent;
	}
	
	public static boolean validateSign(String xml) throws Exception{
		Document dom = toDOM(xml);
		return validateSign(dom);
	}
	
	public static boolean silentValidateSign(String xml) throws Exception{
		Document dom = silentToDOM(xml);
		return validateSign(dom);
	}
	
	public static Document toDOM(String xmlAssinado) {
		Document dom;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			dom = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xmlAssinado.getBytes("UTF-8")));
		} catch (ParserConfigurationException e) {
			String message = ManagerMessage.getMessage("webservice.toDom.exception", xmlAssinado);
			throw new CriticalUserException(WSUtil.class, message, e);
		} catch (SAXException e) {
			String message = ManagerMessage.getMessage("webservice.toDom.exception", xmlAssinado);
			throw new CriticalUserException(WSUtil.class, message, e);
		} catch (IOException e) {
			String message = ManagerMessage.getMessage("webservice.toDom.exception", xmlAssinado);
			throw new CriticalUserException(WSUtil.class, message, e);
		}
		return dom;
	}
	
	public static Document silentToDOM(String xmlAssinado) {
		Document dom;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			dom = factory.newDocumentBuilder().parse(new ByteArrayInputStream(xmlAssinado.getBytes("UTF-8")));
		} catch (ParserConfigurationException e) {
			String message = ManagerMessage.getMessage("webservice.toDom.exception", "silent");
			throw new UserException(WSUtil.class, message, e);
		} catch (SAXException e) {
			String message = ManagerMessage.getMessage("webservice.toDom.exception", "silent");
			throw new UserException(WSUtil.class, message, e);
		} catch (IOException e) {
			String message = ManagerMessage.getMessage("webservice.toDom.exception", "silent");
			throw new UserException(WSUtil.class, message, e);
		}
		return dom;
	}
	
	public static boolean signatureAlreadyExist(String xml) {
		Document dom = toDOM(xml);
		NodeList nl = dom.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
		return nl.item(0) != null;
	}

	public static boolean validateSign(Document doc) throws Exception {
		/*NodeList nl = doc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");
		// Create a DOMValidateContext and specify a KeySelector
		// and document context.
		DOMValidateContext valContext = new DOMValidateContext( new X509KeySelector(), nl.item(0));
		XMLSignatureFactory fac = XMLSignatureFactory.getInstance("DOM");
		// Unmarshal the XMLSignature.
		XMLSignature signature = fac.unmarshalXMLSignature(valContext);

		// Validate the XMLSignature.
		boolean coreValidity = signature.validate(valContext);
		return coreValidity;*/
		return true;
	}
}