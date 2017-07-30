package InvoiceUI;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class BowlingScoreCard implements Runnable
{
  public static void main(String[] args)
  {
    SwingUtilities.invokeLater(new BowlingScoreCard());
  }

  public void run()
  {
    JFrame frame = new JFrame("Demo");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.getContentPane().add(createScorecard(4), BorderLayout.CENTER);
    frame.pack();
    frame.setVisible(true);
  }

  private JPanel createScorecard(int numPlayers)
  {
    JPanel p = new JPanel(new GridBagLayout());

    p.add(new JLabel("Player"), gbc(0, 0, 1, 1));
    for (int x = 1; x <= 10; x++)
    {
      p.add(new JLabel(Integer.toString(x)), gbc(x, 0, 1, 1));
    }

    for (int y = 1; y <= numPlayers; y++)
    {
      JTextArea textArea = new JTextArea(2, 10);
      p.add(textArea, gbc(0, y, 1, 1));

      for (int i = 1; i <= 9; i++)
      {
        p.add(createFrame(2), gbc(i, y, 1, 1));
      }

      p.add(createFrame(3), gbc(10, y, 1, 1));
    }

    return p;
  }

  private JPanel createFrame(int entries)
  {
    JLabel label = new JLabel(" ");
    label.setBackground(Color.GRAY);

    JPanel p = new JPanel(new GridBagLayout());
    p.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
    for (int i = 0; i < entries; i++)
    {
      p.add(new JTextField(3), gbc(i, 0, 1, 1));
    }
    p.add(label, gbc(0, 1, 2, 1));
    return p;
  }

  private GridBagConstraints gbc(int x, int y, int colspan, int rowspan)
  {
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.gridx = x;
    gbc.gridy = y;
    gbc.gridwidth = colspan;
    gbc.gridheight = rowspan;
    gbc.weightx = 0;
    gbc.weighty = 0;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.fill = GridBagConstraints.NONE;
    return gbc;
  }
}