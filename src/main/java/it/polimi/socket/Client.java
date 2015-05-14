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
    
    /**
     * Costruttore
     * @param inputstream
     * @param outputstream
     */
    public Client(Socket socket){
        this.socket = socket;
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
}
