package spring.corp.framework.json;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import spring.corp.framework.i18n.ManagerMessage;

import flexjson.JSONSerializer;
import flexjson.PathExpression;

public class JSONReturn {
	
	private final Consequence consequence;
	private String message;
	private String localizedMessage;
	private String campo;
	private String page;
	private final Object dado;
	private JSONSerializer jsonSerializer;
	private boolean deep = false;
	private List<String> includes;
	
	private JSONReturn(Consequence consequence, Object dado) {
		this.consequence = consequence;
		this.dado = dado;
		this.jsonSerializer = new JSONSerializer();
		//this.jsonSerializer.transform(new WaveObjectTransformer(), Object.class);
		this.jsonSerializer.exclude("dado.class");
	}
	
	private JSONReturn(Consequence consequence) {
		this.consequence = consequence;
		this.dado = null;
		this.jsonSerializer = new JSONSerializer();
	}
	
	public static JSONReturn newInstance(Consequence consequence) {
		JSONReturn jsonReturn = new JSONReturn(consequence);
		return jsonReturn;
	}
	
	public static JSONReturn newInstance(Consequence consequence, Object dado) {
		JSONReturn jsonReturn;
		if (dado == null) {
			jsonReturn = new JSONReturn(consequence);
		} else {
			jsonReturn = new JSONReturn(consequence, dado);
		}
		return jsonReturn;
	}
	
	protected void checkExclude(Object dado) {
		if (dado.getClass().isAnnotationPresent(Entity.class)) {
			checkExcludeFields(dado, dado.getClass().getSimpleName());
		}
	}
	
	protected void checkExcludeFields(Object dado, String nomeClasse) {
		if (dado != null) {
			Class c = dado.getClass();
			do {
				Field[] fields = c.getDeclaredFields();
				for (Field field : fields) {
					field.setAccessible(true);
					if (field.isAnnotationPresent(OneToMany.class)) {
						String nomeAtributo = null;
						int	fim = field.getName().lastIndexOf("$");
						if (fim == -1) {
							nomeAtributo= field.getName().substring(9, fim);
							nomeAtributo = (""+nomeAtributo.charAt(0)).toLowerCase() + nomeAtributo.substring(1);
							if (nomeClasse != null) {
								nomeAtributo = nomeClasse + "." + nomeAtributo;
							}
						} else {
							nomeAtributo = (nomeClasse == null) ? field.getName() : nomeClasse + "." + field.getName();
						}
						if (nomeAtributo != null) {
							if (!includes.contains(nomeAtributo)) {
								jsonSerializer.exclude("dado." + nomeAtributo);
							}
						}
					} else if (field.isAnnotationPresent(JoinColumn.class)) {
						//TODO: Olha com atencao, pois essa nao implementacao gerar muito problemas em diversas partes do sistema...
						//if (!includes.contains(nomeAtributo)){
						//	jsonSerializer.exclude("dado."+nomeAtributo);
						//}
					}
				}
				c = c.getSuperclass();
			} while (c != Object.class);
		}
	}
	
	public Consequence getConsequence() {
		return consequence;
	}

	public String getMessage() {
		return message;
	}
	
	public String getLocalizedMessage() {
		return message;
	}

	public JSONReturn message(String message) {
		this.localizedMessage = message;
		this.message = message;
		return this;
	}
	
	public JSONReturn deep() {
		this.deep = true;
		return this;
	}
	
	public String getCampo() {
		return campo;
	}

	public JSONReturn campo(String campo) {
		this.campo = campo;
		return this;
	}
	
	public JSONReturn messageKey(String messageKey){
		this.message = ManagerMessage.getMessage(messageKey);
		return this;
	}
	
	public String deepSerialize(Object target) {
		return jsonSerializer.deepSerialize(target);
	}
	
	//public String deepSerialize(String rootName, Object target) {
	//	return jsonSerializer.deepSerialize(rootName, target);
	//}
	
	public JSONReturn exclude(String... arg0) {
		String[] camposExclude = new String[arg0.length];
		for (int i = 0; i < arg0.length; i++) {
			camposExclude[i] = "dado." + arg0[i];
		}
		this.jsonSerializer = jsonSerializer.exclude(camposExclude);
		return this;
	}
	
	public List<PathExpression> getExcludes() {
		return jsonSerializer.getExcludes();
	}
	
	public List<PathExpression> getIncludes() {
		return jsonSerializer.getIncludes();
	}
	
	public JSONReturn include(String... arg0) {
		if (arg0 != null && arg0.length > 0) {
			this.includes = Arrays.asList(arg0);
		}
		String[] campos = new String[arg0.length];
		String[] camposExclude = new String[arg0.length];
		for (int i = 0; i < arg0.length; i++) {
			campos[i] = "dado." + arg0[i];
			camposExclude[i] = "dado." + arg0[i] + ".class";
		}
		this.jsonSerializer = jsonSerializer.exclude(camposExclude);
		this.jsonSerializer = jsonSerializer.include(campos);
		return this;
	}
	 
	//public String prettyPrint(Object target) {
	//	return jsonSerializer.prettyPrint(target);
	//}
	
	//public String prettyPrint(String rootName, Object target) {
	//	return jsonSerializer.prettyPrint(rootName, target);
	//}

	public String serialize() {
		if (deep) {
			return jsonSerializer.deepSerialize(this);
		} else {
			return jsonSerializer.serialize(this);
		}
	}
	
	//public String serialize(String rootName, Object target) {
	//	this.dado = target;
	//	return jsonSerializer.serialize(rootName, this);
	//}

	public void setExcludes(List arg0) {
		jsonSerializer.setExcludes(arg0);
	}
	
	public void setIncludes(List arg0) {
		jsonSerializer.setIncludes(arg0);
	}
	 
	//public JSONSerializer transform(Transformer arg0, String... arg1) {
	//	return jsonSerializer.transform(arg0, arg1);
	//}

	public Object getDado() {
		return dado;
	}

	public String getPage() {
		return page;
	}

	public JSONReturn page(String page) {
		this.page = page;
		return this;
	}
}