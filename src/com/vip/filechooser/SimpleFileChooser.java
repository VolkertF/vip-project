package com.vip.filechooser;
// SimpleFileChooser.java
// A simple file chooser to see what it takes to make one of these work.
//
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;


@SuppressWarnings("serial")
public class SimpleFileChooser extends JFrame {

   public SimpleFileChooser() {
    super("File Chooser Test Frame");
    setSize(750, 550);
    setDefaultCloseOperation(EXIT_ON_CLOSE);

    Container c = getContentPane();
    c.setLayout(new FlowLayout());
    
    final JLabel statusbar = 
                 new JLabel("Output of your selection will go here");

    

    JPanel filePanel = new JPanel();
    JPanel labelPanel = new JPanel();
  
    JButton selectButton = new JButton("Select");
    JButton unselectButton = new JButton("Unselect");

    final JFileChooser chooser = new JFileChooser();
    chooser.setControlButtonsAreShown(false);
    
    FileNameExtensionFilter filter = new FileNameExtensionFilter("Code","java", "c", "cpp");
    chooser.setFileFilter(filter);
                

     selectButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
	    statusbar.setText("You saved " + ((chooser.getSelectedFile()!=null)?
                            chooser.getSelectedFile().getName():"nothing"));
      }
    });

      unselectButton.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent ae) {
	    statusbar.setText("You have unselected all options");
      }
    });


    c.add(labelPanel);
    labelPanel.add(statusbar);
    labelPanel.add(selectButton);
    labelPanel.add(unselectButton);
    c.add(filePanel);
    filePanel.add(chooser);

  }


  public static void main(String args[]) {
    SimpleFileChooser sfc = new SimpleFileChooser();
    sfc.setVisible(true);
  }
}
