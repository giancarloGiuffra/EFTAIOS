package it.polimi.view;

import java.io.OutputStream;
import java.io.PrintWriter;

public class PrintWriterPlus {

	protected PrintWriter printWriter;
	
	/**
	 * Costruttore vuoto
	 */
	public PrintWriterPlus(){
		
	}
	
	/**
	 * Costruttore
	 * @param out
	 */
	public PrintWriterPlus(OutputStream out) {
		this.printWriter = new PrintWriter(out);
	}

	/**
	 * Costruttore
	 * @param out
	 * @param autoFlush
	 */
	public PrintWriterPlus(OutputStream out, boolean autoFlush) {
		this.printWriter = new PrintWriter(out, autoFlush);
	}
	
	/**
	 * println 
	 * @param x
	 */
	public void println(String x){
		this.printWriter.println(x);
	}
	
	/**
	 * Chiama close del printwriter
	 */
	public void close(){
		this.printWriter.close();
	}

}
