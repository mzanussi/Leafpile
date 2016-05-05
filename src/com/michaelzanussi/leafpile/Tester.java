package com.michaelzanussi.leafpile;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.michaelzanussi.leafpile.ui.components.Console;
import com.michaelzanussi.leafpile.ui.components.V3ScreenModel;

/**
 * Test driver for the interpreter.
 * 
 * Much of this code is culled from LeafpileGUI.
 * This interface includes a debug console that
 * is not available in the main GUI.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (3 May 2016) 
 */
public class Tester extends JFrame {
	
	private Console console;
	private JButton fileButton;
	private JButton aboutButton;
	private JButton exitButton;
	private JTextArea bot;
			
	private StringBuilder botBuffer = new StringBuilder();
	
	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = 4268565568788659838L;

	public Tester() {
		
		// Set the window title.
		super("Leafpile Tester");
						
		// The container.
		Container container = getContentPane();
		container.setLayout(new FlowLayout());

		// The console.
		console = new V3ScreenModel(null/*zm*/, 80, 25, new Font("Courier", Font.PLAIN, 18));
		console.init();
		
		// **********************************************************
		// **********************************************************
		fileButton = new JButton("   Open Story File...   ");
		fileButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						printBot("you just pressed the Open Story File button\n");
					}
				}
		);
		
		// **********************************************************
		// **********************************************************
		aboutButton = new JButton("   About   ");
		aboutButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						console.write_lines("Test\nBoo\n ");
					}
				}
		);
		
		// **********************************************************
		// **********************************************************
		exitButton = new JButton("   Exit   ");
		exitButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						console.write_lines("Exit.\nNow?\n ");
					}
				}
		);
		
		container.add(new JLabel("          "));
		container.add(console);
		container.add(new JLabel("          "));
		pack();
		container.add(fileButton);
		container.add(aboutButton);
		container.add(exitButton);
		
		// Bottom text area.
		bot = new JTextArea(21, 73);
		bot.setLineWrap(true);
		bot.setWrapStyleWord(true);
		bot.setEditable(false);
		container.add(new JScrollPane(bot), BorderLayout.SOUTH);
		
		setSize(1000, 900);
	    setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		
	} 
	
	public void error(String text) {
		JOptionPane.showMessageDialog(null, text, "Exception", JOptionPane.ERROR_MESSAGE);
	}
	
	/**
	 * Print some text in the bottom pane.
	 * 
	 * @param text the text to print.
	 */
	private void printBot(String text) {
		botBuffer.append(text);
		bot.setText(botBuffer.toString());		
	}
	
	public static void main(String[] args) {
		Tester tester = new Tester();
		tester.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
