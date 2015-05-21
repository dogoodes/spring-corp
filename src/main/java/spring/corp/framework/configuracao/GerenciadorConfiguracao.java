package spring.corp.framework.configuracao;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import spring.corp.framework.log.GerenciadorLog;
import spring.corp.framework.utils.StringUtils;

public class GerenciadorConfiguracao {
	
	private static Properties p = new Properties();
	
	static {
		try {
			load(GerenciadorConfiguracao.class.getResourceAsStream("/configuracao.properties"));
			String file = System.getProperty("configuracao");
			if (file != null) {
				load(new FileInputStream(file));
			}
		} catch (IOException e) {
			throw new RuntimeException("ARQUIVO DE CONFIGURA\u00c7\u00c3O N\u00c3O ENCONTRADO.");
		} finally {
			
		}
	}

	public static void load(InputStream fis) {
		synchronized (p) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(fis));
				String line = null;
				while ((line = br.readLine()) != null) {
					if (!StringUtils.isBlank(line) && !line.startsWith("#")) {
						String[] values = line.split("[=]");
						if (GerenciadorLog.isDebug(GerenciadorConfiguracao.class)) {
							try {
								GerenciadorLog.debug(GerenciadorConfiguracao.class, "Carregando a seguinte chave: [" + values[0] +"] e Valor: [" + values[1] + "]");
							} catch (IndexOutOfBoundsException e) {
								GerenciadorLog.debug(GerenciadorConfiguracao.class, "Erro ao ler o array. Tamanha:[" + values.length + "] Esperado: 2");
								if (values.length == 1) {
									GerenciadorLog.error(GerenciadorConfiguracao.class, e, "Carregando a seguinte chave: [" + values[0] +"]");
								}
							}
						}
						//nao vai chamar o metodo put da classe por que senao vai dar deadlock.
						p.put(values[0].trim(), values[1].trim());
					}
				}
			} catch (IOException e) {
				throw new RuntimeException("ERRO NA LEITURA DO ARQUIVO DE CONFIGURA\u00c7\u00c3O.");
			} finally {
				
			}
		}
	}
	
	public static void put(String key, String value){
		synchronized (p) {
			p.put(key.trim(), value.trim());
		}
	}
	
	public static String getConfiguracao(String key){
		synchronized (p) {
			return (String) p.get(key);
		}
	}
}