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

public class SimpleNotePad extends JFrame {

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

    JMenuItem undoMenuItem = new JMenuItem("Undo");
    JMenuItem copyMenuItem = new JMenuItem("Copy");
    JMenuItem pasteMenuItem = new JMenuItem("Paste");
    JMenuItem replaceMenuItem = new JMenuItem("Replace");

    // Creates pop-up menu frame for replacing text prompt
    JFrame replaceWordFrame = new JFrame();

    public SimpleNotePad() {

        setTitle("A Simple Notepad Tool");
        
        JMenuItem[] fileMenuItems = {newFileMenuItem, saveFileMenuItem, printFileMenuItem, openFileMenuItem};
        JMenuItem[] actionMenuItems = {copyMenuItem, pasteMenuItem, replaceMenuItem};

        for (int i = 0; i < fileMenuItems.length; i++){
            JMenuItem m = fileMenuItems[i];
            fileMenu.add(m);
            if (i < fileMenuItems.length - 1){
                fileMenu.addSeparator();
            }
        }

        for (JMenuItem m : actionMenuItems){
            editMenu.add(m);
        }

        fileMenu.add(openRecentFileMenu);
        menuBar.add(fileMenu);
        menuBar.add(editMenu);

        newFileMenuItem.addActionListener(e -> textPane.setText("")); 
        saveFileMenuItem.addActionListener(e -> NotePadUtils.saveFile(textPane));
        printFileMenuItem.addActionListener(e -> NotePadUtils.printFile(textPane, this));
        openFileMenuItem.addActionListener(e -> NotePadUtils.openFileChooser(textPane, openRecentFileMenu, this));
        
        copyMenuItem.addActionListener(e -> textPane.copy());
        pasteMenuItem.addActionListener(e -> textPane.paste());
        replaceMenuItem.addActionListener(e -> NotePadUtils.replaceWord(textPane, replaceWordFrame));

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

}