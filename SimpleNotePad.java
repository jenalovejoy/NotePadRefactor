import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import java.io.FileWriter;
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
public class SimpleNotePad extends JFrame implements ActionListener{
    JMenuBar mb = new JMenuBar();
    JMenu fm = new JMenu("File");
    JMenu em = new JMenu("Edit");
    JTextPane d = new JTextPane();
    JMenuItem nf = new JMenuItem("New File");
    JMenuItem sf = new JMenuItem("Save File");
    JMenuItem pf = new JMenuItem("Print File");
    JMenuItem u = new JMenuItem("Undo");
    JMenuItem c = new JMenuItem("Copy");
    JMenuItem p = new JMenuItem("Paste");
    public SimpleNotePad() {
        setTitle("A Simple Notepad Tool");
        fm.add(nf);
        fm.addSeparator();
        fm.add(sf);
        fm.addSeparator();
        fm.add(pf);
        em.add(u);
        em.add(c);
        em.add(p);
        nf.addActionListener(this);
        nf.setActionCommand("new");
        sf.addActionListener(this);
        sf.setActionCommand("save");
        pf.addActionListener(this);
        pf.setActionCommand("print");
        c.addActionListener(this);

        c.setActionCommand("copy");
        p.addActionListener(this);
        p.setActionCommand("paste");
        u.addActionListener(this);
        u.setActionCommand("undo");
        mb.add(fm);
        mb.add(em);
        setJMenuBar(mb);
        add(new JScrollPane(d));
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
        if(e.getActionCommand().equals("new")) {
            d.setText("");
        }else if(e.getActionCommand().equals("save")) {
            File fileToWrite = null;
            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showSaveDialog(null);
            if (returnVal == JFileChooser.APPROVE_OPTION)
                fileToWrite = fc.getSelectedFile();
            try {
                PrintWriter out = new PrintWriter(new FileWriter(fileToWrite));
                out.println(d.getText());
                JOptionPane.showMessageDialog(null, "File is saved successfully...");
                out.close();
            } catch (IOException ex) {
            }
        }else if(e.getActionCommand().equals("print")) {
            try{
                PrinterJob pjob = PrinterJob.getPrinterJob();
                pjob.setJobName("Sample Command Pattern");
                pjob.setCopies(1);
                pjob.setPrintable(new Printable() {
                    public int print(Graphics pg, PageFormat pf, int pageNum) {
                        if (pageNum>0)
                            return Printable.NO_SUCH_PAGE;
                        pg.drawString(d.getText(), 500, 500);
                        paint(pg);
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
        }else if(e.getActionCommand().equals("copy")) {
            d.copy();
        }else if(e.getActionCommand().equals("paste")) {
            StyledDocument doc = d.getStyledDocument();
            Position position = doc.getEndPosition();
            System.out.println("offset"+position.getOffset());
            d.paste();
        }else if(e.getActionCommand().equals("undo")) {
//TODO: implement undo operation
        }
    }
}