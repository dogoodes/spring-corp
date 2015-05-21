package spring.corp.framework.view;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.io.ProgressFileGeneration;
import spring.corp.framework.json.Consequence;
import spring.corp.framework.json.JSONReturn;
import spring.corp.framework.log.GerenciadorLog;
import spring.corp.framework.zip.CreateZipFile;
import spring.corp.framework.zip.ZipContent;

public abstract class GerenciadorZipView extends MonitorStatus {
	
	public abstract String getZipFileName(ServletRequest request);
	
	public boolean isXmlValidoParaSerZipado(String xml){
		return !"".equals(xml);
	}
	
	@SuppressWarnings("unchecked")
	protected void putZipContentsInSession(ServletRequest request, String nid, List<ZipContent> zipContents){
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(true);
		Map<String, List<ZipContent>> mapZipContents = (Map<String, List<ZipContent>>) session.getAttribute("mapZipContents");
		if (mapZipContents == null){
			mapZipContents = new HashMap<String, List<ZipContent>>();
		}
		mapZipContents.put(nid, zipContents);
		session.setAttribute("mapZipContents", mapZipContents);
	}
	
	protected  List<ZipContent> getZipContentsInSession(ServletRequest request, String nid){
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(true);
		@SuppressWarnings("unchecked")
		Map<String, List<ZipContent>> mapZipContents = (Map<String, List<ZipContent>>) session.getAttribute("mapZipContents");
		List<ZipContent> zipContents = null;
		if (mapZipContents != null){
			zipContents = mapZipContents.get(nid);
		}else {
			zipContents = Collections.emptyList();
		}
		return zipContents;
	}
	
	
	public void downloadZipFile(ServletRequest request, ServletResponse response){
		String ticket = request.getParameter("ticket");
		List<ZipContent> zipContents = getZipContentsInSession( request,  ticket);
		ProgressFileGeneration progress = new ProgressFileGeneration();
		progress.setTotalFiles(zipContents.size());
		String fileName = getZipFileName(request);
		if (zipContents.isEmpty()){
			JSONReturn jsonReturn = JSONReturn.newInstance(Consequence.ERRO).message("Nenhum arquivo foi encontrado para geração.");
			updateStatusZip(request, ticket, jsonReturn);
			fileName = "semdados";
		}
		HttpServletResponse httpResponse = (HttpServletResponse)response;
		httpResponse.setContentType("application/octet-stream");  
		httpResponse.setHeader("Content-Disposition", "attachment; filename="+fileName+".zip");  
		ServletOutputStream out = null;
		try{
			out = httpResponse.getOutputStream();
			CreateZipFile zipFile = new CreateZipFile(out);
			//int i = zipContents.size();
			int i = 0;
			int x = 0;
			//O valor eh multiplicado por dois pois fazemos a prepacao dos dados num primeiro momento
			//e depois fazemos a zipagem dos dados, como percorremos duas vezes para fazer isso
			//entao adequamos a porcentagem como sendo o dobro do tamanho de notas.
			//TODO: Multiplicar com por dois quando conseguir fazer conjugado com a geracao do conteudo
			int totalPercentual = zipContents.size();
			for(ZipContent zipContent : zipContents){
				i++;
				x++;
				progress.setCurrentFileGeneration(zipContent.getName());
				progress.setCurrentPercent((int)Math.floor(100 * i / totalPercentual));
				updateStatusZip(request, ticket, JSONReturn.newInstance(Consequence.AVISO, progress).message("Zipando [" + x + "/" + zipContents.size() + "]: " + progress.getCurrentFileGeneration() ));
				zipFile.add(zipContent);
			}
			updateStatusZip(request, ticket, JSONReturn.newInstance(Consequence.SUCESSO, progress).message("Arquivo pronto para download"));
			zipFile.close();
		}catch(IOException e){
			updateStatusZip(request, ticket, JSONReturn.newInstance(Consequence.ERRO, progress).message(GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL)));
			GerenciadorLog.critical(GerenciadorZipView.class, e, GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL));
		}finally{
			if (out != null){
				try{
					out.flush();
					HttpServletRequest httpRequest = (HttpServletRequest) request;
					HttpSession session = httpRequest.getSession(true);
					session.removeAttribute("mapZipContents");
				}catch(Exception e){
					
				}
			}
		}
	}
	
	protected void updateStatusZip(ServletRequest request, String nid, JSONReturn jsonReturn){
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpSession session = httpRequest.getSession(true);
		session.setAttribute(nid, jsonReturn);
		//Acho que nao compensa criar uma thread para limpara um dado tao pequeno
		//FutureRemoveSession removeSession = new FutureRemoveSession(session, nid, 20);
	}
}
