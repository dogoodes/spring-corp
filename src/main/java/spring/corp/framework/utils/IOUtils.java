package spring.corp.framework.utils;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import spring.corp.framework.io.UnicodeBOMInputStream;

public class IOUtils {

	public static InputStream getInputStreamWithoutBOM(InputStream fileContent) throws Exception {
		 UnicodeBOMInputStream ubis = new UnicodeBOMInputStream(fileContent);
		 ubis.skipBOM();
		 return ubis;
	}

	public static byte[] toByteArray(InputStream input, int bufferSize) throws IOException {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		byte[] buffer = new byte[bufferSize];
		long count = 0;
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
			count += n;
		}
		return output.toByteArray();
	}

	static String byteArrayToHexString(byte in[]) {
		byte ch = 0x00;
		int i = 0;
		if (in == null || in.length <= 0)
			return null;

		String pseudo[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F" };
		StringBuffer out = new StringBuffer(in.length * 2);
		while (i < in.length) {
			ch = (byte) (in[i] & 0xF0); // Strip off high nibble
			ch = (byte) (ch >>> 4);
			// shift the bits down
			ch = (byte) (ch & 0x0F);
			// must do this is high order bit is on!
			out.append(pseudo[(int) ch]); // convert the nibble to a String
											// Character
			ch = (byte) (in[i] & 0x0F); // Strip off low nibble
			out.append(pseudo[(int) ch]); // convert the nibble to a String
											// Character
			i++;
		}

		String rslt = new String(out);
		return rslt;
	}

	public static void serialize(Object dado, String toFileName) throws IOException {
		FileOutputStream o = new FileOutputStream(toFileName);
		ObjectOutput out = null;
		try {
			out = new ObjectOutputStream(o);
			out.writeObject(dado);
		} finally {
			out.close();
		}
	}
	
	public static Object deserialize(String fromFileName) throws IOException, ClassNotFoundException {
		FileInputStream fileIn = new FileInputStream(fromFileName);
		ObjectInputStream ou = new ObjectInputStream(fileIn);
		return ou.readObject();
	}
}