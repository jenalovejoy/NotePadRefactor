import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.Position;
import javax.swing.text.StyledDocument;

import jdk.jfr.Event.*;
import java.io.FileFilter;

public class SimpleNotePad extends JFrame implements ActionListener {

    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenu editMenu = new JMenu("Edit");

    JTextPane textPane = new JTextPane();

    JMenuItem newFileMenuItem = new JMenuItem("New File");
    JMenuItem saveFileMenuItem = new JMenuItem("Save File");
    JMenuItem printFileMenuItem = new JMenuItem("Print File");
    JMenuItem openFileMenuItem = new JMenuItem("Open File");
    JMenuItem openRecentFileMenuItem = new JMenuItem("Recent");

    JMenuItem undoFileMenuItem = new JMenuItem("Undo");
    JMenuItem copyMenuFileItem = new JMenuItem("Copy");
    JMenuItem pasteMenuFileItem = new JMenuItem("Paste");
    JMenuItem replaceMenuFileItem = new JMenuItem("Simple Replace");

    public SimpleNotePad() {

        setTitle("A Simple Notepad Tool");
        
        JMenuItem[] fileMenuItems = {newFileMenuItem, saveFileMenuItem, printFileMenuItem, openFileMenuItem, openRecentFileMenuItem};
        String[] fileMenuTitle = {"new", "save", "print", "open", "open recent"};
        
        JMenuItem[] actionMenuItems = {copyMenuFileItem, pasteMenuFileItem, undoFileMenuItem, replaceMenuFileItem};
        String[] actionMenuTitle = {"copy", "paste", "undo", "replace"};

        int i = 0;

        for (JMenuItem m : fileMenuItems){
            m.addActionListener(this);
            m.setActionCommand(fileMenuTitle[i]);
            fileMenu.add(m);
            if (i < fileMenuItems.length - 1){
                fileMenu.addSeparator();
            }
            i++;
        }

        i = 0;

        for (JMenuItem m : actionMenuItems){
            m.addActionListener(this);
            m.setActionCommand(actionMenuTitle[i]);
            editMenu.add(m);
            i++;
        }

        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);
        add(new JScrollPane(textPane));
        setPreferredSize(new Dimension(600,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }
    public static void main(String[] args) {
        SimpleNotePad app = new SimpleNotePad();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String action = e.getActionCommand();

        System.out.println(action);

        switch (action){
            case "new":
                textPane.setText("");
                break;

            case "save":
                NotePadUtils.saveFile(textPane);
                break;

            case "open":
                NotePadUtils.openFile(textPane, openRecentFileMenuItem);
                break;

            case "open recent":
                NotePadUtils.openRecentFile(textPane, openRecentFileMenuItem);
                break;

            case "print":
                NotePadUtils.printFile(textPane, this);
                // printFile();
                break;
            
            case "copy":
                textPane.copy();
                break;

            case "paste":
                textPane.paste();  
                break; 

            case "replace":
                break;
        }
    }
}