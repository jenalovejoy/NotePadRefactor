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

    private static ArrayList<File> recentFiles;
    private static ArrayList<JMenuItem> recentMenuItems;
    private static int recentFileCount = 0;
    private static RecentFilter filter;

    private static void manageRecentFiles(File file, JMenuItem openRecentFileMenuItem){
        if (recentFileCount == 0){
            recentFiles = new ArrayList<File>();
            recentMenuItems = new ArrayList<JMenuItem>();
        }

        if (recentFileCount == 5){
            recentFiles.remove(4);
            recentFileCount--;
            openRecentFileMenuItem.remove(recentMenuItems.remove(4));
        }

        String fileName = file.getName();
        recentFiles.add(0, file);
        recentMenuItems.add(new JMenuItem(fileName));
        recentFileCount++;
        
        if (recentFileCount == 1){
            openRecentFileMenuItem.add(recentMenuItems.get(0));

        } else {
            refreshRecentFiles(openRecentFileMenuItem);
        }
    }

    private static void refreshRecentFiles(JMenuItem openRecentFileMenuItem){
        for (JMenuItem recentFile : recentMenuItems){
            openRecentFileMenuItem.remove(recentFile);
        }

        for (JMenuItem recentFile : recentMenuItems){
            openRecentFileMenuItem.add(recentFile);
        }
    }

    public static void saveFile(JTextPane textPane){
        File fileToWrite = null;
        JFileChooser fc = new JFileChooser();
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

    public static void openFile(JTextPane textPane, JMenuItem openRecentFileMenuItem){
        JFileChooser fc = new JFileChooser();

        fc.showOpenDialog(null); 
        File fileToOpen = fc.getSelectedFile();
        manageQueue(fileToOpen, openRecentFileMenuItem);

        try {
            FileReader fileReader = new FileReader(fileToOpen);
            BufferedReader buffReader = new BufferedReader(fileReader);
            StyledDocument doc = (StyledDocument) textPane.getStyledDocument();
            char c;
            textPane.read(buffReader, null);
            buffReader.close();
            fileReader.close();
  
        } catch (FileNotFoundException ex) {
        } catch (IOException ix){}
    }

    public static void openRecent(){}

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

}