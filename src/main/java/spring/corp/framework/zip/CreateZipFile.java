package spring.corp.framework.zip;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CreateZipFile {

	private ZipOutputStream zos = null;
	
	public CreateZipFile(OutputStream os) {
		this.zos = new ZipOutputStream(os);
	}
	
	public void add(ZipContent zipContent) {
		try {
			ZipEntry entrada = new ZipEntry(zipContent.getName());
			zos.putNextEntry(entrada);
			zos.setMethod(ZipOutputStream.DEFLATED);
			zos.write(zipContent.getContent());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void close() {
		if (zos != null) {
			try {
				zos.close();
			} catch (Exception e) {
			}
		}
	}
	
	public static void main(String[] argv) throws Exception{
		CreateZipFile zipFile = new CreateZipFile(new FileOutputStream(new File("/home/albertocerqueira/xml.zip")));
		zipFile.add(new ZipContent("a.xml", "alberto".getBytes()));
		zipFile.close();
	}
}