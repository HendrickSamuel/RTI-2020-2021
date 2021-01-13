//Auteur : HENDRICK Samuel                                                                                              
//Projet : JAVA                               
//Date de la création : 04/12/2020

package Mails;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.Vector;

public class MailPoolingThread extends Thread
{
    /********************************/
    /*           Variables          */
    /********************************/
    private JTable table;
    private JTable table2;
    private MailHelper mailHelper;
    private HashMap<Integer, Message> receivedMessages;


    /********************************/
    /*         Constructeurs        */
    /********************************/
    public MailPoolingThread(JTable table, JTable table2, MailHelper mailHelper, HashMap<Integer, Message> receivedMessages)
    {
        this.table = table;
        this.table2 = table2;
        this.mailHelper = mailHelper;
        this.receivedMessages = receivedMessages;
    }


    /********************************/
    /*           Methodes           */
    /********************************/
    @Override
    public void run()
    {
        while (!this.isInterrupted())
        {
            Message[] messages = mailHelper.ReceiveMail();
            DefaultTableModel dtm = (DefaultTableModel)table.getModel();
            DefaultTableModel dtm2 = (DefaultTableModel)table2.getModel();
            int compteur = 0;
            for (int i = messages.length -1; i >= 0 && compteur < 5; i--)
            {
                try
                {
                    Message message = messages[i];
                    String[] rt = message.getHeader("Return-Path");
                    if (rt != null && (rt[0].contains("samuel.hendrick@student.hepl.be") || rt[0].contains("hydroblade@outlook.com")))
                    {
                        if(!receivedMessages.containsKey(message.getMessageNumber()))
                        {
                            Vector vector = new Vector();
                            vector.add(message.getMessageNumber());
                            vector.add(message.getFrom()[0]);
                            vector.add(message.getSubject());
                            vector.add(message.getSentDate());
                            vector.add(message);
                            receivedMessages.put(message.getMessageNumber(), message);
                            dtm.addRow(vector);
                            dtm2.addRow(vector);
                            compteur++;
                        }
                    }
                }
                catch (MessagingException e)
                {
                    e.printStackTrace();
                }
            }

            try
            {
                Thread.sleep(20000);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}
