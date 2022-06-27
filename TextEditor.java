import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.beans.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;
import java.awt.Frame.*;

public class TextEditor extends JFrame implements ActionListener{

    JTextArea textArea; //text
    JScrollPane scrollPane; //bezec
    JSpinner fontSizSpinner; //zvolenie velkosti textu
    JLabel fontLabel;   //nazov
    JButton fontColorButton; //zvolenie farby
    JComboBox fontBox;  //zvolenie fontov

    //menu
    JMenuBar menuBar;
    JMenu fileMenu;
    JMenuItem openItem;
    JMenuItem saveItem;
    JMenuItem closeItem;

    TextEditor()
    {
        //nastavenie okna
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Text Editor");
        this.setSize(500,500);
        this.setLayout(new FlowLayout());
        this.setLocationRelativeTo(null);

        //textove pole
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setFont(new Font("Times New Roman", Font.PLAIN, 20));

        //skrolovaci bezec
        scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(450, 450));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        //
        fontLabel = new JLabel("Font: ");
        
        //moznost zmeny fontu a velkosti fontu textu
        fontSizSpinner = new JSpinner();
        fontSizSpinner.setPreferredSize(new Dimension(50, 25));
        fontSizSpinner.setValue(20);
        fontSizSpinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                textArea.setFont(new Font(textArea.getFont().getFamily(),Font.PLAIN,(int) fontSizSpinner.getValue()));
                
            }
            
        });

        //zmena farby textu
        fontColorButton = new JButton("Farba");
        fontColorButton.addActionListener(this);

        //moznost vyberu textu
        String[] fonty = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(); //vyber vsetkych fontov
        fontBox = new JComboBox(fonty);
        fontBox.addActionListener(this);
        fontBox.setSelectedItem("Times New Romans");

        //--------- lista menu----------------------//
        //menu
        menuBar = new JMenuBar();
        fileMenu = new JMenu("Subor");
        openItem = new JMenuItem("otvorit");
        closeItem = new JMenuItem("zavriet");
        saveItem = new JMenuItem("ulozit");

        //moznost vyuzitia
        openItem.addActionListener(this);
        closeItem.addActionListener(this);
        saveItem.addActionListener(this);

        //vlozenie do menu
        fileMenu.add(openItem);
        fileMenu.add(closeItem);
        fileMenu.add(saveItem);

        menuBar.add(fileMenu);

        //vykreslenie
        this.setJMenuBar(menuBar);
        this.add(fontLabel);
        this.add(fontSizSpinner);
        this.add(fontColorButton);
        this.add(fontBox);
        this.add(scrollPane);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //zmena farby
        if(e.getSource() == fontColorButton)
        {
            JColorChooser colorChooser = new JColorChooser();
            Color color = colorChooser.showDialog(null, "Zvol Farbu", Color.BLACK);

            textArea.setForeground(color);
        }

        //zmena fontu
        if(e.getSource() == fontBox)
        {
            textArea.setFont(new Font((String) fontBox.getSelectedItem(), Font.PLAIN, textArea.getFont().getSize()));
        }

        if(e.getSource() == closeItem)
        {
            System.exit(0);
        }

        if(e.getSource() == openItem)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File", "txt");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);

            if(response == JFileChooser.APPROVE_OPTION)
            {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                Scanner fileIn = null;

                try {
                    fileIn = new Scanner(file);
                    if(file.isFile())
                    {
                        while(fileIn.hasNextLine())
                        {
                            String line = fileIn.nextLine()+"\n";
                            textArea.append(line);
                        }
                    }
                } catch(FileNotFoundException e1)
                {
                    e1.printStackTrace();
                }
                finally{
                    fileIn.close();
                }

            }

        }

        if(e.getSource() == saveItem)
        {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("."));

            int response = fileChooser.showSaveDialog(null);

            if(response == JFileChooser.APPROVE_OPTION)
            {
                File file;
                PrintWriter fileOut = null;

                file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                try {
                    fileOut = new PrintWriter(file);
                    fileOut.println(textArea.getText());
                } catch (FileNotFoundException e1){
                    e1.printStackTrace();
                } finally{
                    fileOut.close();
                }

            }
        }
        
    }
    
}
