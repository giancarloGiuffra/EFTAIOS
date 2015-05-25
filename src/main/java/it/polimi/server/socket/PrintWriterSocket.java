package it.polimi.server.socket;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import it.polimi.view.PrintWriterPlus;

public class PrintWriterSocket extends PrintWriterPlus {

	public PrintWriterSocket(Writer out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterSocket(OutputStream out) {
		super(out);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterSocket(String fileName) throws FileNotFoundException {
		super(fileName);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterSocket(File file) throws FileNotFoundException {
		super(file);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterSocket(Writer out, boolean autoFlush) {
		super(out, autoFlush);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterSocket(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterSocket(String fileName, String csn)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterSocket(File file, String csn)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(file, csn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void println(String x) {
		this.printWriter.println(x);
	}

}
