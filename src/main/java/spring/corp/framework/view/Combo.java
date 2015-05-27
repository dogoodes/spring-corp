package spring.corp.framework.view;

import javax.servlet.ServletRequest;

import spring.corp.framework.exceptions.ConverterException;
import spring.corp.framework.exceptions.UserLinkException;
import spring.corp.framework.i18n.GerenciadorMensagem;

/**
 * Esta implementacao de IComponentView trabalha em conjunto com o InputHolder que eh uma variavel thread-local responsavel por armazenar as excecoes
 * ocorridas durante o processo de leitura do componente, esta previsto nesta implementacao excecoes de conversao  
 * @link{spring.corp.framework.exceptions.ConverterException} e excecao de restricao quando o required eh informado, porem todas as 
 * excecoes sao lancadas como @link{spring.corp.framework.exceptions.UserLinkException}. Uma forma de nao lancar excecao e informando na 
 * hora de recuperar o valor a parametro silent no qual nao lanca a excecao deixando a cargo do usuario recupera-la somente atraves do InputHolder.
 * @see InputHolder
 * @param <T>
 */
public class Combo<T> implements IComponentView<T> {

	private final String name;
	private final String label;
	private final T value;
	private final Boolean required;
	public static final Long SEM_SELECAO = Long.valueOf(-1);
	
	/**
	 * @param builder
	 */
	private Combo(Builder<T> builder){
		this.name  = builder.name;
		this.label = builder.label;
		this.value = builder.convertedValue;
		this.required = builder.required;
	}
	
	public static <T> Combo.Builder<T> builderInstance(String value, Class<T> type) {
		return new Combo.Builder<T>(value, type);
	}
	
	public static <T> Combo.Builder<T> builderInstance(ServletRequest value, Class<T> type) {
		return new Combo.Builder<T>(value, type);
	}
	
	public static <T> Combo.Builder<T> builderInstance(String value, Class<T> type, boolean screenSaver) {
		return new Combo.Builder<T>(value, type, screenSaver);
	}
	
	public static <T> Combo.Builder<T> builderInstance(ServletRequest value, Class<T> type, boolean screenSaver) {
		return new Combo.Builder<T>(value, type, screenSaver);
	}
	
	public static class Builder<T> {
		private String name = null;
		private String label = null;
		private String value = null;
		private Boolean required = null;
		private String focus = null;
		private boolean screenSaver = false;
		private T convertedValue = null;
		private Class<T> type;
		private ComplexValidation complexValidation = null;
		private ComplexValidation requiredComplexValidation = null;
		private ServletRequest request = null;
		
		public Builder(String value, Class<T> type) {
			this.value = value;
			this.type = type;
		}
		
		public Builder(ServletRequest request, Class<T> type) {
			this.request = request;
			this.type = type;
		}
		
		public Builder(String value, Class<T> type, boolean screenSaver) {
			this.value = value;
			this.type = type;;
			this.screenSaver = screenSaver;
		}
		
		public Builder(ServletRequest request, Class<T> type, boolean screenSaver) {
			this.request = request;
			this.type = type;
			this.screenSaver = screenSaver;
		}
		
		public Builder<T> required() {
			if (!screenSaver) {				
				this.required = Boolean.TRUE;
			}
			return this;
		}
		
		public Builder<T> required(boolean required) {
			if (!screenSaver) {				
				this.required = required;
			}
			return this;
		}
		
		public Builder<T> required(ComplexValidation requiredComplexValidation) {
			if (!screenSaver) {
				this.requiredComplexValidation = requiredComplexValidation;
			}
			return this;
		}
		
		public Builder<T> name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder<T> focus(String focus) {
			this.focus = focus;
			return this;
		}
		
		public Builder<T> label(String label) {
			this.label = label;
			return this;
		}
		
		public Builder<T> validation(ComplexValidation complexValidation) {
			if (!screenSaver) {				
				this.complexValidation = complexValidation;
			}
			return this;
		}
		
		public Combo<T> build() {
			try {
				if (recoverValueByRequest()) {
					this.value = request.getParameter(name);
				}
				if (isNullValue()) {
					this.convertedValue = null;
					if (Boolean.TRUE.equals(required)) {
						String messageKey = "framework.utils.required";
						String message = GerenciadorMensagem.getMessage(messageKey, label);
						String link = (focus == null ? name : focus);
						UserLinkException userLinkException = new UserLinkException(link, message);
						InputHolder.get().add(userLinkException);
					}
					if (requiredComplexValidation != null) {
						try {
							requiredComplexValidation.validate(name, value);
						} catch (UserLinkException e) {
							InputHolder.get().add(e);
						}
					}
				} else {
					if (complexValidation != null) {
						try {
							complexValidation.validate(name, value);
						} catch (UserLinkException e) {
							InputHolder.get().add(e);
						}
					}
					this.convertedValue = ConverterView.convert(type,value);
				}
			} catch (ConverterException e) {
				String nameType = type.getSimpleName();
				String messageKey = "framework.utils." + nameType.toLowerCase() + ".com.campo.invalido";
				String message = GerenciadorMensagem.getMessage(messageKey, label);
				String link = (focus == null ? name : focus);
				UserLinkException userLinkException = new UserLinkException(link, message);
				InputHolder.get().add(userLinkException);
			}
			return new Combo<T>(this);
		}
		
		private boolean recoverValueByRequest() {
			return (value == null && name != null);
		}
		
		private boolean isNullValue() {
			return (value != null && (value.equals(SEM_SELECAO.toString()) || value.equals("")));
		}
	}
	
	public String getName() {
		return name;
	}

	public String getLabel() {
		return label;
	}

	public T getValue() {
		if (!InputHolder.get().isEmpty()) {
			throw InputHolder.get().get(0);
		}
		return value;
	}
	
	public T getValue(boolean silent) {
		if (silent) {
			return value;
		} else {
			getValue();
		}
		return value;
	}
	
	public Boolean isRequired() {
		return required;
	}
}