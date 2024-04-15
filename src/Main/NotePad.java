package Main;

import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.text.BadLocationException;
import javax.swing.JMenu;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JTabbedPane;

public class NotePad extends JFrame {
	static ArrayList<NotePad> np = new ArrayList<>();
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextArea textArea;
    static ArrayList<Integer> wordindex;
    private static File selectedFilepath;
    static int findwordindex =0 ;
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	np.add(new NotePad());
            		np.get(np.size()-1).setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public NotePad() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 600, 600);
        JMenuBar menuBar = new JMenuBar();
        
        // Create File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem menuItem = new JMenuItem("New                ctrl+N");
        menuItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(selectedFilepath == null) {
        			int choice = JOptionPane.showConfirmDialog(null, "Do You Want to Save THis FIle", "Error", JOptionPane.YES_NO_CANCEL_OPTION);
        			switch (choice) {
                    case JOptionPane.YES_OPTION:
                    	saveAs(textArea);
                    	selectedFilepath = null;
                    	textArea.setText("");
                        break;
                    case JOptionPane.NO_OPTION:
                    	textArea.setText("");
                        break;
                    case JOptionPane.CANCEL_OPTION:
                        break;
                    default:
                        System.out.println("User closed the dialog");
                        break;
        		}
        	}
        }
      });
        fileMenu.add(menuItem);
        JMenuItem menuItem_3 = new JMenuItem("New Window   ctrl+shift+N");
        menuItem_3.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		np.add(new NotePad());
        		np.get(np.size()-1).setVisible(true);
        	}
        });
        fileMenu.add(menuItem_3);
        JMenuItem menuItem_4 = new JMenuItem("Open               ctrl+O");
        menuItem_4.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		showFileChooser(np.get(np.size()-1),textArea);
        	}
        });
        fileMenu.add(menuItem_4);
        JMenuItem menuItem_6 = new JMenuItem("Save               ctrl+S");
        menuItem_6.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		if(selectedFilepath != null) {
        			save(textArea);
        		}
        		else {
        			saveAs(textArea);
        		}
        		
        	}
        });
        fileMenu.add(menuItem_6);
        JMenuItem menuItem_5 = new JMenuItem("Save As            ctrl+shift+S");
        menuItem_5.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		saveAs(textArea);
        	}
        });
        fileMenu.add(menuItem_5);
        fileMenu.add(new JMenuItem("Page Setup"));
        JMenuItem menuItem_2 = new JMenuItem("Print              ctrl+P");
        menuItem_2.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		PrinterJob printerJob = PrinterJob.getPrinterJob();

                // Show the print dialog
                if (printerJob.printDialog()) {
                    try {
                        // Perform the print operation
                        printerJob.print();
                    } catch (PrinterException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    System.out.println("User canceled the print operation.");
                }
        		
        	}
        });
        fileMenu.add(menuItem_2);
        fileMenu.addSeparator();  // Add a separator
        JMenuItem menuItem_1 = new JMenuItem("Exit");
        menuItem_1.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.exit(0);
        	}
        });
        fileMenu.add(menuItem_1);
        menuBar.add(fileMenu);

        // Create Edit menu
        JMenu editMenu = new JMenu("Edit");
        editMenu.add(new JMenuItem("Undo               ctrl+Z"));
        editMenu.addSeparator();  // Add a separator
        JMenuItem menuItem_7 = new JMenuItem("Cut                ctrl+X");
        menuItem_7.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		
        		String slectedtext = textArea.getSelectedText();
        		textArea.setText(textArea.getText().replaceAll(slectedtext,""));
        		
        	}
        });
        editMenu.add(menuItem_7);
        JMenuItem menuItem_10 = new JMenuItem("Copy               ctrl+C");
        menuItem_10.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
                        StringSelection selection = new StringSelection(textArea.getSelectedText());
                        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                        clipboard.setContents(selection, null);
        	}
        });
        editMenu.add(menuItem_10);
        JMenuItem menuItem_11 = new JMenuItem("Paste              ctrl+V");
        menuItem_11.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                Transferable contents = clipboard.getContents(this);

                if (contents != null && contents.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                    try {
                        String text = (String) contents.getTransferData(DataFlavor.stringFlavor);
                        textArea.replaceSelection(text);
                    } catch (UnsupportedFlavorException | IOException ex) {
                        ex.printStackTrace();
                    }
                }
        	}
        });
        editMenu.add(menuItem_11);
        JMenuItem menuItem_9 = new JMenuItem("Delete             del");
        menuItem_9.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String slectedtext = textArea.getSelectedText();
        		textArea.setText(textArea.getText().replaceAll(slectedtext,""));
        	}
        });
        editMenu.add(menuItem_9);
        editMenu.addSeparator();  // Add a separator
        editMenu.add(new JMenuItem("Search With Bing   ctrl+E"));
        JMenuItem menuItem_13 = new JMenuItem("Find               ctrl+F");
        menuItem_13.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		showCustomDialog(np.get(np.size()-1),textArea);
        	}
        });
        editMenu.add(menuItem_13);
        JMenuItem menuItem_14 = new JMenuItem("Find Next          F3");
        menuItem_14.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		String searchText = textArea.getSelectedText();
                String text = textArea.getText();

                String regex = "\\b" + Pattern.quote(searchText) + "\\b";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(text);
                System.out.println(matcher.toString());

                while (matcher.find()) {
                    int startIndex = matcher.start();
                    int endIndex = matcher.end() ;
                    wordindex.add(startIndex);
                    
                }
                textArea.setSelectionStart(wordindex.get(findwordindex));
                textArea.setSelectionEnd(wordindex.get(findwordindex)+searchText.length());
                textArea.setSelectedTextColor(new Color(37, 161, 146));
                findwordindex++;
        	}
        });
        editMenu.add(menuItem_14);
        editMenu.add(new JMenuItem("Find previous      shift+F3"));
        editMenu.add(new JMenuItem("Replace            ctrl+H"));
        editMenu.addSeparator();  // Add a separator
        editMenu.add(new JMenuItem("Goto               ctrl+J"));
        JMenuItem menuItem_8 = new JMenuItem("Select All         ctrl+A");
        menuItem_8.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		textArea.selectAll();
        	}
        });
        editMenu.add(menuItem_8);
        JMenuItem menuItem_12 = new JMenuItem("Time / Date        F5");
        menuItem_12.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		LocalDateTime currentDateTime = LocalDateTime.now();

                // Format the date and time using a specific pattern
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedDateTime = currentDateTime.format(formatter);
                textArea.append(formattedDateTime);
        	}
        });
        editMenu.add(menuItem_12);
        menuBar.add(editMenu);


        // Create Format menu
        JMenu formatMenu = new JMenu("Format");
        JMenuItem wordWrapItem = new JMenuItem("Word Wrap");
        wordWrapItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		textArea.setLineWrap(!textArea.getLineWrap());
        	}
        });
        JMenuItem fontItem = new JMenuItem("Font");
        // Add more format menu items as needed
        formatMenu.add(wordWrapItem);
        formatMenu.add(fontItem);
        menuBar.add(formatMenu);

        // Create View menu
        JMenu viewMenu = new JMenu("View");
        JMenuItem zoomInItem = new JMenuItem("Zoom In");
        zoomInItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		textArea.setFont(new Font("Arial", Font.PLAIN, textArea.getFont().getSize()+5));
        	}
        });
        JMenuItem zoomOutItem = new JMenuItem("Zoom Out");
        zoomOutItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		textArea.setFont(new Font("Arial", Font.PLAIN, textArea.getFont().getSize()-5));
        	}
        });
        // Add more view menu items as needed
        viewMenu.add(zoomInItem);
        viewMenu.add(zoomOutItem);
        menuBar.add(viewMenu);

        // Create Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About Notepad");
        aboutItem.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		showDialog("This NotePad is Developed By Navin Kumar Verma");
        	}
        });
        // Add more help menu items as needed
        helpMenu.add(aboutItem);
        menuBar.add(helpMenu);

        // Set the menu bar for the frame
        setJMenuBar(menuBar);
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        

        textArea = new JTextArea();
        JScrollPane scrollPane = new JScrollPane(textArea);
        textArea.setBounds(0, 0, contentPane.getWidth(), contentPane.getHeight());
        contentPane.add(scrollPane,BorderLayout.CENTER);
        setContentPane(contentPane);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                textArea.setBounds(0, 0, contentPane.getWidth(), contentPane.getHeight());
            }
        });
    }
    private static void showDialog(String string) {
    	JOptionPane.showInternalMessageDialog(null, string);
    }

    private static void showFileChooser(JFrame frame,JTextArea textArea) {
        JFileChooser fileChooser = new JFileChooser();

        int result = fileChooser.showOpenDialog(frame);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            readAndDisplayFileContents(selectedFile,textArea);
        }
    }

    private static void readAndDisplayFileContents(File file,JTextArea textArea) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            StringBuilder content = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                content.append(line).append("\n");
            }
            textArea.setText(content.toString());

        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error reading file", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private static void showCustomDialog(JFrame parentFrame, JTextArea textArea) {
        JDialog customDialog = new JDialog(parentFrame, "Find", true);
        JLabel label = new JLabel("Find What");
        JTextField input = new JTextField();
        JButton findButton = new JButton("Find Next");
        JButton closeButton = new JButton("Close");
        String preword;
        wordindex = new ArrayList<>();
       
        findButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String searchText = input.getText();
                String text = textArea.getText();

                String regex = "\\b" + Pattern.quote(searchText) + "\\b";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(text);
                System.out.println(matcher.toString());

                while (matcher.find()) {
                    int startIndex = matcher.start();
                    int endIndex = matcher.end() ;
                    wordindex.add(startIndex);
                    
                }
                textArea.setSelectionStart(wordindex.get(findwordindex));
                textArea.setSelectionEnd(wordindex.get(findwordindex)+searchText.length());
                textArea.setSelectedTextColor(new Color(37, 161, 146));
                findwordindex++;
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	findwordindex=0;
            	wordindex.clear();
                customDialog.dispose();
            }
        });

        JPanel panel = new JPanel();
        panel.setLayout(null);
        label.setBounds(10, 10, 80, 25);
        input.setBounds(100, 10, 100, 25);
        findButton.setBounds(210, 10, 120, 25);
        closeButton.setBounds(210, 50, 120, 25);
        
        panel.add(label);
        panel.add(input);
        panel.add(findButton);
        panel.add(closeButton);

        customDialog.getContentPane().add(panel); // Add the panel to the content pane

        customDialog.setSize(350, 150);
        customDialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        customDialog.setLocationRelativeTo(parentFrame);
        customDialog.setVisible(true);
    }



    private static void saveAs(JTextArea textArea) {
    	JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFilepath = fileChooser.getSelectedFile();

            // The text to be written to the file
            String content =textArea.getText();
            np.get(np.size()-1).setTitle(fileChooser.getSelectedFile().toString());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFilepath))) {
                writer.write(content);
                JOptionPane.showMessageDialog(null, "Text has been written to the file successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error writing to the file.");
            }
        }
    }
    private static void save(JTextArea textArea) {
            String content =textArea.getText();

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFilepath))) {
                writer.write(content);
                JOptionPane.showMessageDialog(null, "Text has been written to the file successfully.");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error writing to the file.");
            }
    }
    
}
