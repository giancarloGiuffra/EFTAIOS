package it.polimi.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    
    /**
     * Costruttore
     * @param inputstream
     * @param outputstream
     * @throws IOException 
     */
    public Client(Socket socket) throws IOException{
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        this.out = new PrintWriter(this.socket.getOutputStream(), true);
    }
    
    /**
     * restituisce un InputStream per il client
     * @return
     * @throws IOException 
     */
    public InputStream inputstream() throws IOException{
        return this.socket.getInputStream();
    }
    
    /**
     * restituisce un OutputStream per questo client 
     * @return
     * @throws IOException
     */
    public OutputStream outputstream() throws IOException{
        return this.socket.getOutputStream();
    }
    
    /**
     * scrive messaggio nel outputstream del client
     * @param message
     */
    public void write(String message){
        this.out.println(message);
    }
    
    /**
     * legge dal inputstream del client
     * @return
     * @throws IOException
     */
    public String read() throws IOException{
        return this.in.readLine();
    }
}
