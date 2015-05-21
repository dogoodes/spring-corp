package spring.corp.framework.ws;

public class FindCep {

	public static void main(String argv[]) throws Exception {
		Webservicecep cep = Cep.getEndereco("01452-001");
		System.out.println(cep.getLogradouro());
	}
}