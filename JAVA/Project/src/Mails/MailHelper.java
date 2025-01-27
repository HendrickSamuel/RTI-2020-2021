//Auteur : HENDRICK Samuel                                                                                              
//Projet : JAVA                               
//Date de la création : 02/12/2020

package Mails;

import genericRequest.MyProperties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.*;
import java.util.*;

public class MailHelper
{
    /********************************/
    /*           Variables          */
    /********************************/
    private String _userAdresse;
    private String _password;
    private Session _sendingSession;
    private Session _receivingSession;
    private Folder folder;


    /********************************/
    /*           Setters            */
    /********************************/
    public void set_userAdresse(String _userAdresse)
    {
        this._userAdresse = _userAdresse;
    }

    public void set_password(String _password)
    {
        this._password = _password;
    }


    /********************************/
    /*           Methodes           */
    /********************************/
    public synchronized void SendMail(String contact, String subject, String content, List<File> files)
    {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.office365.com");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.starttls.enable", "true");

        if(_sendingSession == null)
        {
            _sendingSession = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(_userAdresse, _password);
                }
            });
        }

        try
        {
            MimeMessage msg = new MimeMessage(_sendingSession);
            msg.setFrom(new InternetAddress(_userAdresse));
            msg.setRecipients(Message.RecipientType.TO, contact);
            msg.setSubject(subject);
            msg.setSentDate(new Date());

            Multipart multipart = new MimeMultipart();

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(content);
            multipart.addBodyPart(mimeBodyPart);


            if(files != null)
            {
                for(File attachedFile : files)
                {
                    mimeBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachedFile.getAbsolutePath());
                    mimeBodyPart.setDataHandler(new DataHandler(source));
                    mimeBodyPart.setFileName(attachedFile.getName()); //todo: enlever le chemin et garder que le nom
                    multipart.addBodyPart(mimeBodyPart);
                }
            }
            msg.setContent(multipart);

            Transport.send(msg);
        }
        catch (MessagingException e)
        {
            System.out.println("send failed, exception: " + e);
        }
        System.out.println("Sent Ok") ;
    }

    public synchronized Message[] ReceiveMail()
    {
        try
        {
            Properties props = new Properties();
            props.put("mail.host", "outlook.office365.com");
            props.put("mail.store.protocol", "pop3s");
            props.put("mail.pop3s.auth", "true");
            props.put("mail.pop3s.port", "995");
            props.put("file.encoding", "iso-8859-1");

            if(_receivingSession == null)
            {
                _receivingSession = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(_userAdresse, _password);
                    }
                });
            }

            CloseFolder();
            OpenFolder();

            return folder.getMessages();

        }
        catch (MessagingException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void InitFolder() throws MessagingException
    {
        Store store = _receivingSession.getStore("pop3s");
        store.connect();
        folder = store.getFolder("INBOX");
    }

    public synchronized void OpenFolder() throws MessagingException
    {
        if(folder == null)
            InitFolder();
        folder.open(Folder.READ_ONLY);
    }

    public synchronized void CloseFolder() throws MessagingException
    {
        if(folder != null)
        folder.close(false);
    }


    /********************************/
    /*             Main             */
    /********************************/
    public static void main(String[] args) throws IOException, ClassNotFoundException
    {
        MailHelper mh = new MailHelper();

        MyProperties mp = new MyProperties("./Confs/MailClient.conf");
        mh.set_userAdresse(mp.getContent("USERNAME"));
        mh.set_password(mp.getContent("PASSWORD"));

        mh.SendMail("thomas.beck@student.hepl.be","Subject", "Hello World", null);

        Message[] messages = mh.ReceiveMail();
        System.out.println(messages.length);
    }
}
