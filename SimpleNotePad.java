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

    // Creates all menus
    JMenuBar menuBar = new JMenuBar();
    JMenu fileMenu = new JMenu("File");
    JMenu editMenu = new JMenu("Edit");

    // Creates main text window
    JTextPane textPane = new JTextPane();

    // Creates interactable menu elements
    JMenuItem newFileMenuItem = new JMenuItem("New File");
    JMenuItem saveFileMenuItem = new JMenuItem("Save File");
    JMenuItem printFileMenuItem = new JMenuItem("Print File");
    JMenuItem openFileMenuItem = new JMenuItem("Open File");

    JMenu openRecentFileMenu = new JMenu("Recent");

    JMenuItem undoFileMenuItem = new JMenuItem("Undo");
    JMenuItem copyMenuFileItem = new JMenuItem("Copy");
    JMenuItem pasteMenuFileItem = new JMenuItem("Paste");
    JMenuItem replaceMenuFileItem = new JMenuItem("Replace");

    // Creates pop-up menu frame for replacing text prompt
    JFrame replaceWordFrame = new JFrame();

    public SimpleNotePad() {

        setTitle("A Simple Notepad Tool");
        
        JMenuItem[] fileMenuItems = {newFileMenuItem, saveFileMenuItem, printFileMenuItem, openFileMenuItem};
        String[] fileMenuTitle = {"new", "save", "print", "open"};
        
        JMenuItem[] actionMenuItems = {copyMenuFileItem, pasteMenuFileItem, undoFileMenuItem, replaceMenuFileItem};
        String[] actionMenuTitle = {"copy", "paste", "undo", "replace"};

        int i = 0;

        // Add each file menu item to the file menu, and attach a listener for user interaction
        for (JMenuItem m : fileMenuItems){
            m.addActionListener(this);
            m.setActionCommand(fileMenuTitle[i]);
            fileMenu.add(m);
            if (i < fileMenuItems.length - 1){ // if it's not the last element, add a separator
                fileMenu.addSeparator();
            }
            i++;
        }

        fileMenu.add(openRecentFileMenu);

        i = 0;

        // Add each edit menu item to the edit menu, and attach a listener for user interaction
        for (JMenuItem m : actionMenuItems){
            m.addActionListener(this);
            m.setActionCommand(actionMenuTitle[i]);
            editMenu.add(m);
            i++;
        }

        // Add file and edit menus to menu bar
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        setJMenuBar(menuBar);
        add(new JScrollPane(textPane));
        setPreferredSize(new Dimension(600,600));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        pack();
    }

    // Creates and runs SimpleNotePad instance
    public static void main(String[] args) {
        SimpleNotePad app = new SimpleNotePad();
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        String action = e.getActionCommand();

        switch (action){
            case "new": // Creating a new file
                textPane.setText("");
                break;

            case "save": // Saving current file
                NotePadUtils.saveFile(textPane);
                break;

            case "open": // Opening a file
                NotePadUtils.openFileChooser(textPane, openRecentFileMenu, this);
                break;

            case "print": // Print current file
                NotePadUtils.printFile(textPane, this);
                break;
            
            case "copy": // Copy highlighted text
                textPane.copy();
                break;

            case "paste": // Paste previously copied text
                textPane.paste();  
                break; 

            case "replace": // Replace highlighted text with user-prompted input
                NotePadUtils.replaceWord(textPane, replaceWordFrame);
                break;

            default: // catches all "open recent file" options
                NotePadUtils.openRecent(action, textPane);
        }
    }
}