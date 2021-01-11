//Auteur : HENDRICK Samuel                                                                                              
//Projet : JAVA                               
//Date de la création : 03/12/2020

package Mails;

import genericRequest.MyProperties;

import javax.mail.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;

public class MailClient extends JFrame{
    
    private JTabbedPane tabbedPane1;
    private JPanel Title;
    private JComboBox ContactList;
    private JTextField MailObject;
    private JTextArea MailMessage;
    private JButton attacherUnePièceButton;
    private JButton envoyerButton;
    private JButton annulerButton;
    private JPanel MainPanel;
    private JTextArea TextMailArea;
    private JList contactList;
    private JTextField newContactField;
    private JButton addContact;
    private JButton removeContact;
    private JTable MailTable;
    private JLabel FromLabel;
    private JLabel TitleLabel;
    private JLabel DateLabel;
    private JList AttachedPiecesList;
    private JButton downloadAllButton;
    private JTree tree1;
    private JTable MailTableAnalyse;
    private JList AttachedPiecesSendList;

    private MailHelper mailHelper;
    private ArrayList<String> contacts;
    private ArrayList<File> attachedFiles;

    private HashMap<Integer, Message> receivedMessages;

    public static void main (String[] args)
    {
        JFrame frame = null;
        frame = new MailClient();
        frame.setVisible(true);
    }

    public MailClient(){
        super("Mails !");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //this.setExtendedState(JFrame.MAXIMIZED_VERT);
        this.setContentPane(this.MainPanel);
        this.setMinimumSize(new Dimension(800,700));
        this.setSize(850,600);
        this.pack();
        this.setVisible(true);

        envoyerButton.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sendMail(evt);
            }
        });

        addContact.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addContact(evt);
            }
        });

        removeContact.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeContact(evt);
            }
        });

        attacherUnePièceButton.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                AttachFile(e);
            }
        });

        downloadAllButton.addActionListener(new java.awt.event.ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                DownloadFiles(e);
            }
        });

        mailHelper = new MailHelper();

        //todo: ouvrir fenetre de login ?
        MyProperties mp = new MyProperties("./Confs/MailClient.conf");
        mailHelper.set_userAdresse(mp.getContent("USERNAME"));
        mailHelper.set_password(mp.getContent("PASSWORD"));

        contacts = new ArrayList<>();
        contacts.add("samuel.hendrick@student.hepl.be");
        contacts.add("thomas.beck@student.hepl.be");
        LoadContacts(contacts);

        receivedMessages = new HashMap<>();
        MailTable.setModel(new DefaultTableModel(
                null,
                new String [] {"Id","From","Subject","Date","Message"}
        ));
        MailTable.setFillsViewportHeight(true);
        MailTable.getSelectionModel().addListSelectionListener(this::OnSelect);

        MailTableAnalyse.setModel(new DefaultTableModel(
                null,
                new String [] {"Id","From","Subject","Date","Message"}
        ));
        MailTableAnalyse.setFillsViewportHeight(true);
        MailTableAnalyse.getSelectionModel().addListSelectionListener(this::OnDetails);

        tree1.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Empty")));

        MailPoolingThread mpooling = new MailPoolingThread(MailTable, MailTableAnalyse, mailHelper, receivedMessages);
        mpooling.start();
        AttachedPiecesSendList.setModel(new DefaultListModel());

    }

    private void OnDetails(ListSelectionEvent lm)
    {
        if(!lm.getValueIsAdjusting())
        {
            Message message = (Message)MailTableAnalyse.getValueAt(MailTableAnalyse.getSelectedRow(), 4);
            try
            {
                DefaultMutableTreeNode top =
                        new DefaultMutableTreeNode("mail -- " + message.getFrom()[0] + " " + message.getSubject() + " " + message.getSentDate());

                DefaultMutableTreeNode fmta = null;
                DefaultMutableTreeNode rmta = null;

                top.add(new DefaultMutableTreeNode("message-id: " + message.getMessageNumber()));
                top.add(new DefaultMutableTreeNode("message-type: " + message.getContentType().split(";")[0]));

                int i = 0;
                Enumeration e = message.getAllHeaders();
                Header h = (Header)e.nextElement();
                while (e.hasMoreElements())
                {
                    if(h.getName().equals("Received"))
                    {
                        if(i == 0)
                        {
                            rmta = new DefaultMutableTreeNode("RMTA");
                            DefaultMutableTreeNode from = new DefaultMutableTreeNode(h.getValue().substring(h.getValue().indexOf("from"), h.getValue().indexOf("by")));
                            rmta.add(from);
                            DefaultMutableTreeNode by = new DefaultMutableTreeNode(h.getValue().substring(h.getValue().indexOf("by"), h.getValue().indexOf("with")));
                            rmta.add(by);
                            DefaultMutableTreeNode with = new DefaultMutableTreeNode(h.getValue().substring(h.getValue().indexOf("with"), h.getValue().indexOf(";")));
                            rmta.add(with);

                            top.add(rmta);
                        }
                        else
                        {
                            fmta = new DefaultMutableTreeNode("FMTA");
                            DefaultMutableTreeNode from = new DefaultMutableTreeNode(h.getValue().substring(h.getValue().indexOf("from"), h.getValue().indexOf("by")));
                            fmta.add(from);
                            DefaultMutableTreeNode by = new DefaultMutableTreeNode(h.getValue().substring(h.getValue().indexOf("by"), h.getValue().indexOf("with")));
                            fmta.add(by);
                            DefaultMutableTreeNode with = new DefaultMutableTreeNode(h.getValue().substring(h.getValue().indexOf("with"), h.getValue().indexOf("id")));
                            fmta.add(with);
                            DefaultMutableTreeNode id = new DefaultMutableTreeNode(h.getValue().substring(h.getValue().indexOf("id"), h.getValue().indexOf(";")));
                            fmta.add(with);

                            top.add(fmta);
                        }
                        i++;
                    }
                    h = (Header)e.nextElement();
                }

                tree1.setModel(new DefaultTreeModel(top));
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }
    }

    private void OnSelect(ListSelectionEvent lm)
    {
        if(!lm.getValueIsAdjusting())
        {
            try {
                TextMailArea.setText("");
                AttachedPiecesList.clearSelection();
                Message message = (Message)MailTable.getValueAt(MailTable.getSelectedRow(), 4);
                DateLabel.setText(String.valueOf(message.getSentDate()));
                TitleLabel.setText(message.getSubject());
                FromLabel.setText(String.valueOf(message.getFrom()[0]));

                if (message.isMimeType("text/plain"))
                {
                    TextMailArea.setText(message.getContent().toString());
                }
                else
                if (message.isMimeType("multipart/*")) {
                    DefaultListModel dlm = new DefaultListModel();
                    Multipart msgMP = (Multipart) message.getContent();
                    for (int i = 0; i < msgMP.getCount(); i++) {
                        Part part = msgMP.getBodyPart(i);
                        String d = part.getDisposition();
                        if (part.isMimeType("text/plain"))
                            TextMailArea.setText((String) part.getContent());
                        if (d!=null && d.equalsIgnoreCase(Part.ATTACHMENT))
                        {
                            System.out.println("Pièce jointe reçue");
                            dlm.addElement(part.getFileName());
                        }

                    }
                    AttachedPiecesList.setModel(dlm);
                }

            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void LoadContacts(ArrayList<String> contacts)
    {
        DefaultListModel dm = new DefaultListModel();
        for(String contact : contacts)
        {
            ContactList.addItem(contact);
            dm.addElement(contact);
        }

        contactList.setModel(dm);
    }

    public void DownloadFiles(java.awt.event.ActionEvent evt){
        Message message = (Message)MailTable.getValueAt(MailTable.getSelectedRow(), 4);
        try {
            if (message.isMimeType("multipart/*")) {
                Multipart msgMP = (Multipart) message.getContent();
                for (int i = 0; i < msgMP.getCount(); i++) {
                    Part part = msgMP.getBodyPart(i);
                    String d = part.getDisposition();
                    if (part.isMimeType("text/plain"))
                        TextMailArea.setText((String) part.getContent());
                    if (d!=null && d.equalsIgnoreCase(Part.ATTACHMENT))
                    {
                        InputStream is = part.getInputStream();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        int c;
                        while ((c = is.read()) != -1) baos.write(c);
                        baos.flush();
                        String nf = part.getFileName();
                        FileOutputStream fos = new FileOutputStream("D:\\Documents\\Mailsµ\\"+nf);
                        baos.writeTo(fos);
                        fos.close();
                        System.out.println("Pièce attachée " + nf + " récupérée");
                    }
                }
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void AttachFile(java.awt.event.ActionEvent evt)
    {
        JFileChooser  jf = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
        int r = jf.showSaveDialog(null);
        if (r == JFileChooser.APPROVE_OPTION)
        {
            if(attachedFiles == null)
                attachedFiles = new ArrayList<>();

            attachedFiles.add(jf.getSelectedFile());
            DefaultListModel model = (DefaultListModel)AttachedPiecesSendList.getModel();
            model.addElement(jf.getSelectedFile().getName());
        }
        // if the user cancelled the operation
        else
            System.out.println("the user cancelled the operation");
    }


    public void sendMail(java.awt.event.ActionEvent evt)
    {
        String contact = ContactList.getSelectedItem().toString();
        String subject =  MailObject.getText();
        String content = MailMessage.getText();

        mailHelper.SendMail(contact,subject, content, attachedFiles);

        ContactList.setSelectedIndex(0);
        MailObject.setText("");
        MailMessage.setText("");
        attachedFiles = null;
        AttachedPiecesSendList.setModel(new DefaultListModel());
    }

    public void addContact(java.awt.event.ActionEvent evt)
    {
        String email = newContactField.getText();
        if(!email.isEmpty())
        {
            newContactField.setText("");

            ContactList.addItem(email);
            DefaultListModel model = (DefaultListModel)contactList.getModel();
            model.addElement(email);
        }
        else
        {
            System.out.println("Error");
        }
    }

    public void removeContact(java.awt.event.ActionEvent evt)
    {
        if(contactList.getSelectedIndex() != -1)
        {
            ContactList.removeItem((String)contactList.getSelectedValue());
            DefaultListModel dm = (DefaultListModel)contactList.getModel();
            dm.removeElement((String)contactList.getSelectedValue());
        }
    }
}
