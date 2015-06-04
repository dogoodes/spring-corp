package spring.corp.framework.view.servlets;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import spring.corp.framework.configuracao.GerenciadorConfiguracao;
import spring.corp.framework.exceptions.UserException;
import spring.corp.framework.ftp.FileTransferTicketsStatus;
import spring.corp.framework.i18n.GerenciadorMensagem;
import spring.corp.framework.log.ManagerLog;
import spring.corp.framework.utils.StringUtils;

public abstract class FileTransferServlet extends AbstractServlet<Void> {

	private static final long serialVersionUID = 4639426374919546324L;
	private Map<String, FileTransferTicketsStatus> ticketsStatus = new ConcurrentHashMap<String, FileTransferTicketsStatus>();
    public static final int INIT = 0;
	public static final int SEND_FULL_HEADER = 1;
	public static final int SEND_FILE_HEADER = 2;
	public static final int SEND_FILE = 3;
	public static final int CLOSE_CHANNEL = 4;
	public static final int WAIT_TO_SEND_FILE = 5;
	public int maxRequestToImport = Integer.parseInt( GerenciadorConfiguracao.getConfiguracao("MAX_REQUEST_TO_IMPORT") );
	public volatile int requestToImport = 0;
	
    public void service(HttpServletRequest request, HttpServletResponse response) {
        try {
            int len = request.getContentLength();
            byte[] input = new byte[len];
            ServletInputStream sin = request.getInputStream();
            int c, count = 0 ;
            while ((c = sin.read(input, count, input.length-count)) != -1) {
                count +=c;
            }
            sin.close();
            OutputStreamWriter out = new OutputStreamWriter(response.getOutputStream());
            String fromClient = new String(input);
            int i  = fromClient.indexOf("INIT") ;
        	if (i!= -1) {
        		String ticket = fromClient.substring(fromClient.indexOf("_")+1);
        		String webClassId = request.getParameter("webClassId");
				FileTransferTicketsStatus wftpStatus = new FileTransferTicketsStatus();
        		wftpStatus.setTicket(ticket);
        		wftpStatus.setStatus(SEND_FULL_HEADER);
        		wftpStatus.setWebClassId(webClassId);
        		ticketsStatus.put(ticket, wftpStatus);
        		out.write(""+SEND_FULL_HEADER);
        		out.flush();
        	} else {
        		int ticketExpression = fromClient.indexOf("ticket:");
        		if (ticketExpression != -1) {
        			int ini = fromClient.indexOf(":");
        			int fim = fromClient.indexOf(";");
        			if (ini < fim) {
	        			String ticket = fromClient.substring(ini+1, fim);
	        			FileTransferTicketsStatus wftpStatus  = ticketsStatus.get(ticket);
	        			if (wftpStatus != null) {
	        				switch (wftpStatus.getStatus()) {
		        				case SEND_FULL_HEADER:
		        					readFullHeader(wftpStatus, fromClient);
		        					if (wftpStatus.getQtFiles() == 0) {
		        						requestToImport--;
		        						wftpStatus.setStatus(CLOSE_CHANNEL);	
		        						closeChannel(request, response, wftpStatus);
		        					} else {
		        						wftpStatus.setStatus(SEND_FILE_HEADER);
		        					}
		        					ticketsStatus.put(ticket, wftpStatus);
		        					out.write(""+wftpStatus.getStatus());
		        					out.flush();
		        					break;
		        				case SEND_FILE_HEADER:
		        					if (requestToImport >= maxRequestToImport) {
		        						out.write(""+WAIT_TO_SEND_FILE);
		        					} else {
		        						readFileHeader(wftpStatus, fromClient);
		        						wftpStatus.setStatus(SEND_FILE);
		        						ticketsStatus.put(ticket, wftpStatus);
		        						out.write(""+wftpStatus.getStatus());
		        						requestToImport++;
		        					}		
		        					out.flush();
		        					break;
		        				case SEND_FILE:
		        					requestToImport--;
		        					boolean fileProcessed = readFile(request, response, wftpStatus, fromClient);
		        					if (fileProcessedOrMaximumRetriesExceeded(fileProcessed, wftpStatus)) {
		        						wftpStatus.addQtFilesProcessed(); //Se um arquivo falhou 3 vezes vou dar como processado
		        						if (allFilesProcessed(wftpStatus)) {
		        							wftpStatus.setStatus(CLOSE_CHANNEL);
		                					ticketsStatus.remove(ticket);
		                					//Nao chamar closeChannel quando acabar de receber o arquivo
		                					//pois o processamento dos mesmo pode estar ocorrendo e nao
		                					//queremos que o checkStatus pare de mandar atualizacoes para 
		                					//o cliente
		                					//closeChannel(request, response, wftpStatus);
		        						} else {
			        						wftpStatus.setStatus(SEND_FILE_HEADER);
			            					ticketsStatus.put(ticket, wftpStatus);
		        						}
		        						out.write(""+wftpStatus.getStatus());
	                					out.flush();
		        						break;
		        					} else if (tryReceiveFileAgain(fileProcessed, wftpStatus)) {
		        						//tenta enviar de novo
		        						wftpStatus.addRetryFileReceived();
		    							wftpStatus.setStatus(SEND_FILE);
		            					ticketsStatus.put(ticket, wftpStatus);
		            					out.write(""+wftpStatus.getStatus());
	                					out.flush();
		            					break;
		        					}
	        				}
	        			}
        			}
        		}
        	}
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (IOException e) {
            try{
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                response.getWriter().print(e.getMessage());
                response.getWriter().close();
            } catch (IOException ioe) {
            }
        }
    }  
    
    private boolean allFilesProcessed(FileTransferTicketsStatus wftpStatus) {
    	return (wftpStatus.getQtFilesProcessed() >= wftpStatus.getQtFiles());
    }
    
    private boolean fileProcessedOrMaximumRetriesExceeded(boolean fileProcessed, FileTransferTicketsStatus wftpStatus) {
    	return (fileProcessed || wftpStatus.getRetryFileReceived() >= 3);
    }
    
    private boolean tryReceiveFileAgain(boolean fileProcessed, FileTransferTicketsStatus wftpStatus) {
    	return (!fileProcessed && wftpStatus.getRetryFileReceived() < 3);
    }
    
    protected void readFullHeader(FileTransferTicketsStatus wftpStatus, String fromClient) throws IOException {
		System.out.println(fromClient);
		Map<String, String> options = StringUtils.makeOptions(fromClient);
		String sQtFiles = options.get("qtFiles");
		if (!StringUtils.isBlank(sQtFiles)) {
			wftpStatus.setQtFiles(Integer.parseInt(sQtFiles));
		}
	}
    
    protected void readFileHeader(FileTransferTicketsStatus wftpStatus, String fromClient) throws IOException {
    	System.out.println(fromClient);
		Map<String, String> options = StringUtils.makeOptions(fromClient);
		String fileName = options.get("fileName");
		if (!StringUtils.isBlank(fileName)) {
			fileName = URLDecoder.decode(fileName, "UTF-8");
			wftpStatus.setFileName(fileName);
		}
		String fileSize = options.get("fileSize");
		if (!StringUtils.isBlank(fileSize)) {
			wftpStatus.setFileSize(Integer.valueOf(fileSize));
		}
		wftpStatus.resetRetryFileReceived();
	}
    
    protected void closeChannel(HttpServletRequest request, HttpServletResponse response, FileTransferTicketsStatus wftpStatus) throws IOException {
    	request.setAttribute("ticket", wftpStatus.getTicket());
		request.setAttribute("totalFiles", Integer.valueOf(0));
		doExecute(request, response, wftpStatus);
    }
    
    protected boolean readFile(HttpServletRequest request, HttpServletResponse response, FileTransferTicketsStatus wftpStatus, String fromClient) throws IOException {
    	boolean fileProcessed = true;
    	String fileEncoded = fromClient.substring(fromClient.indexOf(";")+1);
    	if (fileEncoded.indexOf("ERROR") == -1) {
			String file = URLDecoder.decode(fileEncoded, "UTF-8");
			int correctLength = (int)file.length();
			if (correctLength != wftpStatus.getFileSize()) {
				fileProcessed = false;
			} else {
				//InputStream is = IOUtils.toInputStream(file, "UTF-8");
				//UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(is);
	            //ubis.skipBOM();
	            //file = IOUtils.toString(ubis);
				if (file.charAt(0) != '<') {
					file = file.substring(file.indexOf("<"));
				}
				//Enviar o arquivo para o fila de processamento
				request.setAttribute("ticket", wftpStatus.getTicket());
				request.setAttribute("fileContent", file);
				request.setAttribute("fileName", wftpStatus.getFileName());
				request.setAttribute("totalFiles", wftpStatus.getQtFiles());
				doExecute(request, response, wftpStatus);
			}
    	} else {
    		Map<String, String> options = StringUtils.makeOptions(fromClient);
    		String fileName = options.get("ERROR");
    		//Logar
    	}
		return fileProcessed;
	}
    
    private void doExecute(HttpServletRequest request, HttpServletResponse response, FileTransferTicketsStatus wftpStatus) {
    	try {
			preExecute(request, response);
			executeWebClassSpring(request, response, wftpStatus.getWebClassId(), "importarArquivo");
		} catch (UserException e) {
			ManagerLog.error(FileTransferServlet.class, e);
		} catch (Exception e) {
			String message = GerenciadorMensagem.getMessage(GerenciadorMensagem.ERRO_GERAL);
			ManagerLog.error(FileTransferServlet.class, e, message);
		} finally {
			posExecute(request, response);
		}
    }
}