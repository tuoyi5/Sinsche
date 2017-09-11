package com.clientcore.client.tool;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Zlib {
	public static byte[] compress(byte[] data) {
		byte[] output = new byte[0];

		Deflater compresser = new Deflater();

		compresser.reset();
		compresser.setInput(data);
		compresser.finish();
		ByteArrayOutputStream bos = new ByteArrayOutputStream(data.length);
		try {
			byte[] buf = new byte[1024];
			while (!compresser.finished()) {
				int i = compresser.deflate(buf);
				bos.write(buf, 0, i);
			}
			output = bos.toByteArray();
		} catch (Exception e) {
			output = data;
			e.printStackTrace();
		} finally {
			try {
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		compresser.end();
		return output;
	}

	public static byte[] decompress(byte[] data, int nMax) {
		Inflater decompresser = null;

		ByteArrayOutputStream o = null;
		try {
			o = new ByteArrayOutputStream(data.length);

			decompresser = new Inflater();
			decompresser.reset();
			decompresser.setInput(data);

			byte[] buf = new byte[nMax];
			while (!decompresser.finished()) {
				int i = decompresser.inflate(buf);
				o.write(buf, 0, i);
			}
			return o.toByteArray();
		} catch (Exception e) {
			return null;
		} finally {
			try {
				o.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (decompresser != null) {
				decompresser.end();
			}
		}
	}
}
