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
import java.util.*; 

public class NotePadUtils {

    private static ArrayList<File> recentFiles; // Tracks individual opened files
    private static ArrayList<JMenuItem> recentMenuItems; // Tracks file-associated GUI elements
    private static int recentFileCount = 0; // Tracks how many recent files are saved

    private static final int MAX_RECENT_FILES = 5;

    // SAVE FILE FUNCTIONALITY

    // Writes written text to a new file to save
    public static void saveFile(JTextPane textPane){
        File fileToWrite = null;

        JFileChooser fc = new JFileChooser(); // pick location to save to

        if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION)
            fileToWrite = fc.getSelectedFile();

        try {
            PrintWriter out = new PrintWriter(new FileWriter(fileToWrite));
            out.println(textPane.getText());
            JOptionPane.showMessageDialog(null, "File is saved successfully...");
            out.close();

        } catch (IOException ex) {
        }
    }

    // OPEN FILE FUNCTIONALITY

    // Opens the JFileChooser for user to select file to open
    public static void openFileChooser(JTextPane textPane, JMenu openRecentFileMenu, SimpleNotePad notepad){
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(null); 
        File fileToOpen = fc.getSelectedFile();

        manageRecentFiles(fileToOpen, openRecentFileMenu, notepad); // tracks which file has been opened to be shown as "recent"

        openFile(textPane, fileToOpen);
    }

    // Given a file, moves the text of that file into the notepad window
    public static void openFile(JTextPane textPane, File fileToOpen){
        try {
            FileReader fileReader = new FileReader(fileToOpen);
            BufferedReader buffReader = new BufferedReader(fileReader);

            textPane.read(buffReader, null);
            buffReader.close();
            fileReader.close();
  
        } catch (FileNotFoundException ex) {
        } catch (IOException ix){}
    }

    // Manages opening recent files
    public static void openRecent(int fileToOpenIndex, JTextPane textPane){
        openFile(textPane, recentFiles.get(fileToOpenIndex));
    }

    // Manages recent files: saves information of opened files, creates GUI elements for those files, and removes older files
    private static void manageRecentFiles(File file, JMenu openRecentFileMenu, SimpleNotePad notepad){

        // If there are no recent files, instantiate file-tracking ArrayLists
        if (recentFileCount == 0){
            recentFiles = new ArrayList<File>();
            recentMenuItems = new ArrayList<JMenuItem>();
        }

        String fileName = file.getName();
        int index;

        // Checks and manages repeated files -> a previously opened file is moved to the most recent position, and out of its original order
        if ((index = recentFiles.indexOf(file)) != -1){
            recentFiles.remove(file);
            openRecentFileMenu.remove(recentMenuItems.remove(index));
            recentFileCount--;
        }

        // If there are the maximum recent files, remove the oldest
        if (recentFileCount == MAX_RECENT_FILES){
            recentFiles.remove(MAX_RECENT_FILES - 1); // The file itself
            recentFileCount--;
            openRecentFileMenu.remove(recentMenuItems.remove(4)); // The file GUI element
        }

        // Adds file and file GUI elements to associated ArrayLists
        recentFiles.add(0, file); // Adds to the beginning of the ArrayList -> list is sorted most recent descending
        recentMenuItems.add(0, new JMenuItem(fileName));
        recentFileCount++;
        
        // If there are no other recent files, add the first one
        // Otherwise, "refresh" the GUI such that the top file is the most recent, and the bottom in the order is the oldest
        if (recentFileCount == 1){
            openRecentFileMenu.add(recentMenuItems.get(0));
        } else {
            refreshRecentFiles(openRecentFileMenu, notepad);
        }
    }

    // Removes all GUI elements to add elements back in the correct order
    private static void refreshRecentFiles(JMenu openRecentFileMenu, SimpleNotePad notepad){
        openRecentFileMenu.removeAll();
        
        for (int i = 0; i < recentMenuItems.length(); i++){
            JMenuItem recentFile = recentMenuItems.get(i);

            recentFile.addActionListener(notepad);
            recentFile.setActionCommand(i + "");
            openRecentFileMenu.add(recentFile);
        }
    }

    // PRINT FILE FUNCTIONALITY

    public static void printFile(JTextPane textPane, SimpleNotePad frame){
        try {
            PrinterJob pjob = PrinterJob.getPrinterJob();
            pjob.setJobName("Sample Command Pattern");
            pjob.setCopies(1);
            pjob.setPrintable(new Printable() {
                public int print(Graphics pg, PageFormat pf, int pageNum) {
                    if (pageNum>0){
                        return Printable.NO_SUCH_PAGE;
                    }
                    pg.drawString(textPane.getText(), 500, 500);
                    frame.paint(pg);
                    return Printable.PAGE_EXISTS;
                }
            });

            if (pjob.printDialog() == false)
                return;
            pjob.print();
        } catch (PrinterException pe) { 
            JOptionPane.showMessageDialog(null,
                    "Printer error" + pe, "Printing error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // REPLACE WORD FUNCTIONALITY

    // Given a highlighted word, prompts the user for text input to replace the highlighted word with, then replaces it
    public static void replaceWord(JTextPane textPane, JFrame replaceWordFrame){
     
        String replaceWord = JOptionPane.showInputDialog(replaceWordFrame, "Replace or insert with", "Replace");
        textPane.replaceSelection(replaceWord);
   
    }

}
