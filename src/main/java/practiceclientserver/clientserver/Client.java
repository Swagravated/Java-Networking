package practiceclientserver.clientserver;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class Client {
    
    private Socket client;
    private DataInputStream fromServer;
    private DataOutputStream toServer;
    
    
    private JFrame window;
    private JTextField input;
    private JTextArea chat;
    
    public Client(){
        
        // Initialize Chat Window
        input = new JTextField();
        input.addKeyListener(new KeyListener() {
            public void keyTyped(KeyEvent e) {}
            public void keyPressed(KeyEvent e) {}
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    String message = input.getText();
                    if(message.length() > 0 && message.length() < 250){
                        input.setText("");
                        chat.append("You: " + message + "\n");
                        try{
                        send(message);
                        } catch (IOException ex){
                        ex.printStackTrace(System.err);
                        }    
                    }
                }
            }
        });
                
        chat = new JTextArea(34, 50);
        chat.setEditable(false);
        
        window = new JFrame("Networking");
        window.setVisible(true);
        window.setResizable(false);
        window.setSize(800, 600);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(chat, BorderLayout.PAGE_START);
        window.add(input, BorderLayout.PAGE_END);
        
        // Initialize Networking
        
        
        try{            
            client = new Socket(Server.HOST, Server.PORT);
            
            if(client.isConnected()){
                fromServer = new DataInputStream(client.getInputStream());
                toServer = new DataOutputStream(client.getOutputStream());
                
                while(!client.isClosed()){
                    receive();
                }
                
            }else{
                System.err.println("Could not connect to server.");
            }
        } catch (IOException ex){
            ex.printStackTrace(System.err);
        }
        
    }
    
    // Helper Method Receive
    private void receive() throws IOException {
        String message = fromServer.readUTF();
        chat.append(message + "\n");
    }
    // Helper Method Send
    private void send(String str) throws IOException {
        toServer.writeUTF(str);
    }

    public static void main(String[] args) {
       
        new Client();
        
    }
    
}
