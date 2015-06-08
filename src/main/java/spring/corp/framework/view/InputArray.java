package spring.corp.framework.view;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.servlet.ServletRequest;

import spring.corp.framework.exceptions.ConverterException;
import spring.corp.framework.exceptions.UserLinkException;
import spring.corp.framework.i18n.ManagerMessage;
import spring.corp.framework.utils.StringUtils;

/**
 * Esta implementacao de IComponentView trabalha em conjunto com o InputHolder que eh uma variavel thread-local responsavel por armazenar as excecoes 
 * ocorridas durante o processo de leitura do componente, esta previsto nesta implementacao excecoes de conversao
 * @link{spring.corp.framework.exceptions.ConverterException} e excecao de restricao quando o required eh informado, porem todas as 
 * excecoes sao lancadas como @link{spring.corp.framework.exceptions.UserLinkException}. Uma forma de nao lancar excecao e informando na 
 * hora de recuperar o valor a parametro silent no qual nao lanca a excecao deixando a cargo do usuario recupera-la somente atraves do InputHolder.
 * @see InputHolder
 * @param <T>
 */
public class InputArray<T> implements IComponentView<T> {

	private final String name;
	private final String label;
	private final T value;
	private Boolean required = null;
	
	/**
	 * @param builder
	 */
	private InputArray(Builder<T> builder) {
		this.name  = builder.name;
		this.label = builder.label;
		this.value = builder.convertedValue;
		this.required = builder.required;
	}
	
	public static <T> InputArray.Builder<T> builderInstance(String value, Class<T> type, int idx) {
		return new InputArray.Builder<T>(value, type, idx);
	}
	
	public static <T> InputArray.Builder<T> builderInstance(ServletRequest value, Class<T> type, int idx) {
		return new InputArray.Builder<T>(value, type, idx);
	}
	
	public static <T> InputArray.Builder<T> builderInstance(ServletRequest value, Class<T> type, int idx, boolean screenSaver) {
		return new InputArray.Builder<T>(value, type, idx, screenSaver);
	}
	
	public static <T> InputArray.Builder<T> builderInstance(String value, Class<T> type, int idx, boolean screenSaver) {
		return new InputArray.Builder<T>(value, type, idx, screenSaver);
	}
	
	public static class Builder<T> {
		private String name = null;
		private String label = null;
		private String value = null;
		private Boolean required = null;
		private String defaultValue = null;
		private ComplexValidation complexValidation = null;
		private IRegexValidation regexValidation = null;
		private ComplexValidation requiredComplexValidation = null;
		private DependenceValidation dependenceValidation = null;
		private IComponentView<?> dependence = null;
		private boolean screenSaver =false;
		private T convertedValue = null;
		private Class<T> type;
		private String focus = null;
		private Integer length = null;
		private Integer min = null;
		private Integer max = null;
		private Integer lessThan = null;
		private Integer biggerThan = null;
		private int idx = 0;
		
		private ServletRequest request = null;
		
		public Builder(String value, Class<T> type, int idx, boolean screenSaver) {
			this.value = value;
			this.type = type;
			this.screenSaver = screenSaver;
			this.idx = idx;
		}
		
		public Builder(String value, Class<T> type, int idx) {
			this.value = value;
			this.type = type;
			this.idx = idx;
		}
		
		public Builder(ServletRequest request, Class<T> type, int idx) {
			this.request = request;
			this.type = type;
			this.idx = idx;
		}
		
		public Builder(ServletRequest request, Class<T> type, int idx, boolean screenSaver) {
			this.request = request;
			this.type = type;
			this.screenSaver = screenSaver;
			this.idx = idx;
		}
		
		public Builder<T> validation(ComplexValidation complexValidation) {
			if (!screenSaver) {
				this.complexValidation = complexValidation;
			}
			return this;
		}
		
		public Builder<T> validation(IRegexValidation regexValidation) {
			if (!screenSaver) {				
				this.regexValidation = regexValidation;
			}
			return this;
		}
		
		public Builder<T> validation(DependenceValidation dependenceValidation, IComponentView<?> dependence) {
			if (!screenSaver) {
				this.dependenceValidation = dependenceValidation;
				this.dependence = dependence;
			}
			return this;
		}
		
		public Builder<T> name(String name) {
			this.name = name;
			return this;
		}
		
		public Builder<T> label(String label) {
			this.label = label;
			return this;
		}
		
		public Builder<T> required() {
			if (!screenSaver) {
				this.required = Boolean.TRUE;
			}
			return this;
		}
		
		public Builder<T> required(ComplexValidation requiredComplexValidation) {
			if (!screenSaver) {
				this.requiredComplexValidation = requiredComplexValidation;
			}
			return this;
		}
		
		public Builder<T> defaultValue(String value) {
			this.defaultValue = value;
			return this;
		}
		
		public Builder<T> focus(String focus) {
			this.focus = focus;
			return this;
		}
		
		public Builder<T> length(Integer length) {
			this.length = length;
			return this;
		}
		
		public Builder<T> min(Integer min) {
			this.min = min;
			return this;
		}
		
		public Builder<T> max(Integer max) {
			this.max = max;
			return this;
		}
		
		public Builder<T> lessThan(Integer lessThan) {
			this.lessThan = lessThan;
			return this;
		}
		
		public Builder<T> biggerThan(Integer biggerThan) {
			this.biggerThan = biggerThan;
			return this;
		}
		
		public InputArray<T> build() {
			try {
				if (recoverValueByRequest()) {
					this.value = request.getParameter(name);
				}
				if (isNullValue() && defaultValue != null) {
					this.value = defaultValue;
				}
				if (isNullValue()) { //Se for nulo entao vamos ver se ele eh requerido
					this.convertedValue = null;
					if (Boolean.TRUE.equals(required)) {
						String messageKey = "framework.utils.required.array";
						String message = ManagerMessage.getMessage(messageKey, label, idx+1);
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
					if (dependenceValidation != null) {
						try {
							dependenceValidation.validate(new InputArray<T>(this), dependence);
						} catch (UserLinkException e) {
							InputHolder.get().add(e);
						}
					}
				} else { //Se o campo nao for nulo entao faz sentido validar
					if (regexValidation != null) {
						if (regexValidation.evaluate(value)) {
							String messageKey = "framework.utils.regexValidation." + regexValidation.name();
							String message = ManagerMessage.getMessage(messageKey);
							UserLinkException userLinkException = new UserLinkException(name, message);
							InputHolder.get().add(userLinkException);
						}
					}
					if (complexValidation != null) {
						try {
							complexValidation.validate(name, value);
						} catch (UserLinkException e) {
							InputHolder.get().add(e);
						}
					}
					this.convertedValue = ConverterView.convert(type,value);
					if (dependenceValidation != null) {
						try {
							dependenceValidation.validate(new InputArray<T>(this), dependence);
						} catch (UserLinkException e) {
							InputHolder.get().add(e);
						}
					}
					if (this.value != null) {
						int theLength = this.value.length();
						// work with length
						if (length != null) {
							if (theLength != length) {
								String message = ManagerMessage.getMessage("framework.utils.length", label, length);
								String link = (focus == null ? name : focus);
								UserLinkException userLinkException = new UserLinkException(link, message);
								InputHolder.get().add(userLinkException);
							}
						} else if (min != null && max != null) {
							if (theLength < min || theLength > max) {
								String message = ManagerMessage.getMessage("framework.utils.min.and.max", label, min, max);
								String link = (focus == null ? name : focus);
								UserLinkException userLinkException = new UserLinkException(link, message);
								InputHolder.get().add(userLinkException);
							}
						} else if (min != null) {
							if (theLength < min) {
								String message = ManagerMessage.getMessage("framework.utils.min", label, min);
								String link = (focus == null ? name : focus);
								UserLinkException userLinkException = new UserLinkException(link, message);
								InputHolder.get().add(userLinkException);
							}
						} else if (max != null) {
							if (theLength > max) {
								String message = ManagerMessage.getMessage("framework.utils.max", label, max);
								String link = (focus == null ? name : focus);
								UserLinkException userLinkException = new UserLinkException(link, message);
								InputHolder.get().add(userLinkException);
							}
						}
						
						Integer theValue = null;
						if (convertedValue instanceof Integer) {
							theValue = (Integer) convertedValue;
						} else if (convertedValue instanceof String) {
							if (!RegexValidation.OnlyNumbers.evaluate(value)) {
								theValue = Integer.parseInt(this.value);
							}
						} else if (convertedValue instanceof Long) {
							theValue = Integer.parseInt((convertedValue).toString());
						} else if (convertedValue instanceof BigDecimal) {
							theValue = Integer.valueOf((((BigDecimal) convertedValue)).intValue());
						} else if (convertedValue instanceof BigInteger) {
							theValue = (Integer) convertedValue;
						}
						
						if (theValue != null) {
							// work with value
							if (lessThan != null && biggerThan != null) {
								if (theValue >= lessThan || theValue <= biggerThan) {
									String message = ManagerMessage.getMessage("framework.utils.lessThan.and.biggerThan", label, biggerThan, lessThan);
									String link = (focus == null ? name : focus);
									UserLinkException userLinkException = new UserLinkException(link, message);
									InputHolder.get().add(userLinkException);
								}
							} else if (lessThan != null) {
								if (theValue >= lessThan) {
									String message = ManagerMessage.getMessage("framework.utils.lessThan", label, lessThan);
									String link = (focus == null ? name : focus);
									UserLinkException userLinkException = new UserLinkException(link, message);
									InputHolder.get().add(userLinkException);
								}
							} else if (biggerThan != null) {
								if (theValue <= biggerThan) {
									String message = ManagerMessage.getMessage("framework.utils.biggerThan", label, biggerThan);
									String link = (focus == null ? name : focus);
									UserLinkException userLinkException = new UserLinkException(link, message);
									InputHolder.get().add(userLinkException);
								}
							}
						}
					}
				}
			} catch (ConverterException e) {
				String message = null;
				if (e.getClass() == ConverterException.class) {
					String nameType = type.getSimpleName();
					String messageKey = "framework.utils." + nameType.toLowerCase() + ".com.campo.invalido.array";
					message = ManagerMessage.getMessage(messageKey, label, idx+1);		
				} else {
					message = e.getMessage();
				}
				UserLinkException userLinkException = new UserLinkException(name, message);
				InputHolder.get().add(userLinkException);
			}
			return new InputArray<T>(this);
		}
	
		private boolean recoverValueByRequest() {
			return (value == null && name != null);
		}
		
		private boolean isNullValue( ){
			return StringUtils.isBlank(value);
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
			return getValue();
		}
	}
	
	public Boolean isRequired() {
		return required;
	}
}