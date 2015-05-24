package spring.corp.framework.i18n;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import spring.corp.framework.configuracao.GerenciadorConfiguracao;

public class GerenciadorTradutor {
	
	private static Map<String, ResourceBundle> addonMessages = new ConcurrentHashMap<String, ResourceBundle>();
	public static final String LABEL_TRANSLATE = "label_translate";
	private final String language;
	private List<ResourceBundle> bundles = new ArrayList<ResourceBundle>();
	
	static {
	   String[] languagesSupported = GerenciadorConfiguracao.getConfiguracao("languages.supported").split(",");
	   for (String languageSupported : languagesSupported) {		   
		   addonMessages.put(languageSupported.trim(), ResourceBundle.getBundle("Labels", new Locale(languageSupported.trim())));
	   }
	}
	
	public GerenciadorTradutor(String language) {
		this.language = language;
		if (addonMessages.containsKey(language)) {
			bundles.add(addonMessages.get(language));
		}
	}
	
	public void addMessageBundle(ResourceBundle bundle) {
		if (!addonMessages.containsKey(language)) {
			addonMessages.put(language, bundle);
			bundles.clear();
			bundles.add(bundle);
		}
	}
	
	public String getMessage(String key) {
		String message = null;
		try {
			for (Iterator<ResourceBundle> it = bundles.iterator(); it.hasNext() && message == null;) {
				try {
					message = it.next().getString(key);
				} catch (MissingResourceException e) {
					//Ok pode nao estar no bundle da iteracao
				}
			}
		} finally {
			
		}
		message = message == null ? "" : message;
		return message;
	}
	
	public Map<String, String> externalize() {
		Map<String, String> externalized = null;
		if (!this.bundles.isEmpty()) {
			Set<String> keys = this.bundles.iterator().next().keySet();
			externalized = new HashMap<String, String>();
			for (String key : keys) {
				externalized.put(key, getMessage(key));
			}
		}
		return externalized;
	}
}