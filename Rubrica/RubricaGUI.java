package rubrica;

import com.sun.rowset.internal.Row;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RubricaGUI extends JFrame {

    private JTable tabella;

    RubricaGUI() {
        super("Visualizza Rubrica");

        String col[] = { "Nome", "Cognome", "Numero" };

        DefaultTableModel tableModel = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int i, int i1) {
                return false;
            }
        };

        ArrayList<Contatto> dataFromFile = new ArrayList<Contatto>();

        try {
            Scanner fileScanner = new Scanner(new File("./src/rubrica/rubrica.txt"));
            fileScanner.useDelimiter("\r\n");
            while(fileScanner.hasNext()) {
                String[] dati = fileScanner.nextLine().split("--");
                dataFromFile.add(new Contatto(dati[0], dati[1], dati[2]));
            }
            fileScanner.close();
        } catch(IOException ex) {
            ex.printStackTrace();
        }

        for (Contatto c : dataFromFile) {
            String nomeContatto = c.getNome();
            String cognomeContatto = c.getCognome();
            String telefonoContatto = c.getTelefono();

            Object[] data = {nomeContatto, cognomeContatto, telefonoContatto};

            tableModel.addRow(data);
        }

        JTable table = new JTable(tableModel);

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2) {
                    String nomeSelected = (String) table.getValueAt(table.getSelectedRow(), 0);
                    String cognomeSelected = (String) table.getValueAt(table.getSelectedRow(), 1);
                    String numeroSelected = (String) table.getValueAt(table.getSelectedRow(), 2);
                    //System.out.println(nomeSelected + " " + cognomeSelected + " " + numeroSelected);
                    new ContattoGUI(nomeSelected, cognomeSelected, numeroSelected);
                    setVisible(false);
                    dispose();
                }
            }

        });

        TableRowSorter<TableModel> rowSorter = new TableRowSorter<>(table.getModel());

        table.setRowSorter(rowSorter);

        JTextField ricerca = new JTextField();

        setSize(700, 700);
        setVisible(true);
        setResizable(false);
        setLayout(new BorderLayout());

        add(new JScrollPane(table), BorderLayout.CENTER);
        add(ricerca, BorderLayout.SOUTH);

        ricerca.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                String text = ricerca.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                String text = ricerca.getText();

                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null);
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                throw new UnsupportedOperationException("Errore");
            }
        });

    }
}
