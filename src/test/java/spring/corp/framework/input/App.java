package spring.corp.framework.input;

import java.math.BigDecimal;
import java.util.ArrayList;

import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.exceptions.UserLinkException;
import spring.corp.framework.json.Consequence;
import spring.corp.framework.json.JSONReturn;
import spring.corp.framework.view.Input;
import spring.corp.framework.view.InputHolder;

public class App {

	// TODO: Organizar um teste melhor...
	
	public static void main(String argv[]) throws Exception {
		InputHolder.set(new ArrayList<UserException>());
		Input<BigDecimal> x = Input.builderInstance("10,00", BigDecimal.class).name("txtValor").label("Valor de Compra").build();
		Input<String> s = Input.builderInstance("ABCD", String.class).name("txtString").label("Valor String").length(3).build();
		try {
			System.out.println(x.getValue());
			System.out.println(s.getValue());
		} catch (UserLinkException e) {
			JSONReturn json = JSONReturn.newInstance(Consequence.MUITOS_ERROS, InputHolder.get());
			System.out.println(json.serialize());
		}
	}
}