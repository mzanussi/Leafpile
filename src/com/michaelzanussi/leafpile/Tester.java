package com.michaelzanussi.leafpile;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import com.michaelzanussi.leafpile.dictionary.Dictionary;
import com.michaelzanussi.leafpile.dictionary.V1Dictionary;
import com.michaelzanussi.leafpile.objecttable.ObjectTable;
import com.michaelzanussi.leafpile.objecttable.ObjectTableObject;
import com.michaelzanussi.leafpile.ui.components.Console;
import com.michaelzanussi.leafpile.ui.components.V3ScreenModel;
import com.michaelzanussi.leafpile.zmachine.Memory;
import com.michaelzanussi.leafpile.zscii.V3ZSCII;
import com.michaelzanussi.leafpile.zscii.ZSCII;

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
		console = new V3ScreenModel(null/*zm*/, 80, 25, new Font("Courier", Font.PLAIN, 14));
		console.init();
		
		// **********************************************************
		// **********************************************************
		JButton fileButton = new JButton("   Open Story File...   ");
		fileButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						printBot("you just pressed the Open Story File button\n");
					}
				}
		);
		
		// **********************************************************
		// **********************************************************
		JButton aboutButton = new JButton("   About   ");
		aboutButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						console.write_lines("Test\nBoo\n ");
					}
				}
		);
		
		// **********************************************************
		// **********************************************************
		JButton exitButton = new JButton("   Exit   ");
		exitButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						console.write_lines("Exit.\nNow?\n ");
					}
				}
		);
		
		container.add(new JLabel("                                   "));
		container.add(console);
		container.add(new JLabel("                                   "));
		pack();
		container.add(fileButton);
		container.add(aboutButton);
		container.add(exitButton);
		
		// Bottom text area.
		bot = new JTextArea(20, 73);
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
	
	public void cli() {
		// CLI tests
		File file = new File("test/tdata.z3");
		Memory memory = new Memory(file);
		System.out.println("Version: " + memory.getVersion());
		System.out.println("High memory base: " + memory.getHighMemoryBase());
		System.out.println("Initial PC: " + memory.getInitialPC());
		System.out.println("Location of dictionary: " + memory.getDictionaryBase());
		System.out.println("Location of object table base: " + memory.getObjectTableBase());
		System.out.println("Location of global variables table base: " + memory.getGlobalVariablesTableBase());
		System.out.println("Static memory base: " + memory.getStaticMemoryBase());
		System.out.println("Abbreviations table base: " + memory.getAbbreviationTableBase());
		System.out.println("File length: " + memory.getFileLength());
		System.out.println("File checksum: " + memory.getChecksum());
		System.out.println("Interpreter number: " + memory.getInterpreterNumber());
		System.out.println("Interpreter version: " + memory.getInterpreterVersion());
		System.out.println("Screen height (lines): " + memory.getScreenHeight());
		System.out.println("Screen width (chars): " + memory.getScreenWidth());
		System.out.println("Screen width (units): " + memory.getScreenWidthInUnits());
		System.out.println("Screen height (units): " + memory.getScreenHeightInUnits());
		System.out.println("Font width (units): " + memory.getFontWidth());
		System.out.println("Font height (units): " + memory.getFontHeight());
		System.out.println("Routines offset: " + memory.getRoutinesOffset());
		System.out.println("Static strings offset: " + memory.getStaticStringsOffset());
		
		//int[] data = {5002, 25376, 1348, 13978, 57669};	
		//ZSCII frotz = new V3ZSCII(memory);
		//String string = frotz.decode(data);
		//System.out.println("~" + string + "~");
		
		ZSCII zscii = new V3ZSCII(memory);
		byte[] boo = zscii.encode("i");
		int[] data = {14501, 38053};
		String zstr = zscii.decode(data);
		System.out.println("~" + zstr + "~");
		
		ObjectTable ot = new ObjectTable(memory);
		ObjectTableObject oto = ot.getObject(180);
		System.out.println(oto);
		
		//OTO toto = ot.getOTO(156);
		//System.out.println(toto);
		
		Dictionary dict = new V1Dictionary(memory);
	}

	public static void main(String[] args) {
		Tester tester = new Tester();
		tester.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//tester.cli();
	}
	
}
