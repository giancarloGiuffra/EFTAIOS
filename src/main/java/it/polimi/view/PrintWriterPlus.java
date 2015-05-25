package it.polimi.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

public abstract class PrintWriterPlus extends PrintWriter {

	protected PrintWriter printWriter;
	
	public PrintWriterPlus(Writer out) {
		super(out);
		this.printWriter = new PrintWriter(out);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterPlus(OutputStream out) {
		super(out);
		this.printWriter = new PrintWriter(out);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterPlus(String fileName) throws FileNotFoundException {
		super(fileName);
		this.printWriter = new PrintWriter(fileName);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterPlus(File file) throws FileNotFoundException {
		super(file);
		this.printWriter = new PrintWriter(file);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterPlus(Writer out, boolean autoFlush) {
		super(out, autoFlush);
		this.printWriter = new PrintWriter(out, autoFlush);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterPlus(OutputStream out, boolean autoFlush) {
		super(out, autoFlush);
		this.printWriter = new PrintWriter(out, autoFlush);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterPlus(String fileName, String csn)
			throws FileNotFoundException, UnsupportedEncodingException {
		super(fileName, csn);
		this.printWriter = new PrintWriter(fileName, csn);
		// TODO Auto-generated constructor stub
	}

	public PrintWriterPlus(File file, String csn) throws FileNotFoundException,
			UnsupportedEncodingException {
		super(file, csn);
		this.printWriter = new PrintWriter(file, csn);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public abstract void println(String x);

}
