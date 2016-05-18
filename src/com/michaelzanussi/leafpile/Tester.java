package com.michaelzanussi.leafpile;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.EventQueue;
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
import javax.swing.text.DefaultCaret;

import com.michaelzanussi.leafpile.ui.IUI;
import com.michaelzanussi.leafpile.ui.components.Console;
import com.michaelzanussi.leafpile.ui.components.V3ScreenModel;
import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * Test driver for the interpreter.
 * 
 * Much of this code is culled from LeafpileGUI. This interface includes 
 * a debug console that is not available in the main GUI.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (3 May 2016) 
 */
public class Tester extends JFrame implements IUI {
	
	private Console console;
	private JTextArea bot;

	private StringBuilder botBuffer = new StringBuilder();

	private Font font;
	private int scr_height;
	private int scr_width;
	
	private StringBuilder buffer;	// console (output) buffer
	private int lines;				// number of lines buffered
	private int cur_line_len;		// the current line length
	private String more_prompt;		// More prompt
	
	private Zmachine zmachine;		// the virtual machine
	
	/**
	 * Because: It is strongly recommended that all serializable
	 * classes explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = 4268565568788659838L;

	public Tester() {
		
		// Set the window title.
		super("Leafpile Tester");
						
		// Load the driver. 
		zmachine = new Zmachine(this);
		
		// setup buffer
		buffer = new StringBuilder();
		lines = 0;
		cur_line_len = 0;
		more_prompt = "<More> Press any key to continue.";
		
		// The container.
		Container container = getContentPane();
		container.setLayout(new FlowLayout());

		// The console.
		font = new Font("Courier", Font.PLAIN, 14);
		scr_height = 25;
		scr_width = 80;
		console = new V3ScreenModel(zmachine, scr_width, scr_height, font);
		console.init();
		
		// **********************************************************
		// **********************************************************
		JButton fileButton = new JButton("   Load & Run Story File   ");
		fileButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						String str = filename(false);
						printBot("Loading " + str);
						start(new File(str));
						fileButton.setEnabled(false);
					}
				}
		);
		
		// **********************************************************
		// **********************************************************
		JButton aboutButton = new JButton("   About   ");
		aboutButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
					}
				}
		);
		
		// **********************************************************
		// **********************************************************
		JButton exitButton = new JButton("   Exit   ");
		exitButton.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						System.exit(0);
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
		DefaultCaret caret = (DefaultCaret)bot.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
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
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#debug(java.lang.String)
	 */
	public void debug(String string) {
		printBot(string);
	}
	
	/**
	 * Start the game.
	 * 
	 * @param fn the story filename.
	 */
	private void start(File fn) {
		zmachine.setStory(fn);
		zmachine.start();
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#filename(boolean)
	 */
	public String filename(boolean isSave) {
		return "/Users/michael/Hot/Dev/Java/Projects/Leafpile/test/tdata.z3";
	}
	
	public void write(String data) {

		// There may be a better way to do this, but since 
		// MOREs need to be handled, this is the easiest. 
		
		StringBuilder word_buf = new StringBuilder();

		// Keep writing data to buffer. Buffer is flushed
		// on demand, when user input is required, or if
		// text needs to be paged (using More).
		
        for (int i= 0; i < data.length(); i++) {
        	char ch = data.charAt(i);
        	switch(ch) {
        	case '\t':
        		ch = ' ';
        	case ' ':
        		word_buf.append(ch);
        		write_to_buffer(word_buf);
        		break;
        	case '\n':
        		write_to_buffer(word_buf);
    			crlf();
        		break;
        	default:
        		word_buf.append(ch);
        	}
        }
		
        write_to_buffer(word_buf);
        
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#read(java.lang.StringBuilder, int)
	 */
	public void read(StringBuilder in, int count) {
		// TODO: flush buffer here instead of sread and the like?
		flush_buf();
		console.read(in, count);
		// TODO: backspace ^H^H^H^H^H^H^H^H over More
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#flush_buf()
	 */
	public void flush_buf() {
		EventQueue.invokeLater(new EventDispatcher(buffer.toString(), false));
		buffer.delete(0, buffer.length());
		// reset lines
		lines = 0;
    	//zm.getLogger().debug("    Lines reset to " + lines);
	}
	
	/**
	 * AWT thread event dispatcher provided for console output.
	 */
	private class EventDispatcher implements Runnable {

		private String data;
		private boolean erase;
		
		public EventDispatcher(String data, boolean erase) {
			this.data = data;
			this.erase = erase;
		}
		
		public void run() {
			if (erase) {
				console.erase_chars(data);
			}
			else {
				console.write_lines(data);
			}
		}

	}
	
    /**
     * Helper function for buffered <code>write</code>s, adds
     * a word to the buffered output. Wraps words as required.
     * 
     * @param word_buf the word to write to buffer.
     */
    private void write_to_buffer(StringBuilder word_buf) {
    	
    	// Check first for valid length word. 
    	// This method is only necessary for
    	// non zero-length strings.
    	
		if (word_buf.length() > 0) {
			
			// Verbose debugging.
	    	//zm.getLogger().debug("    ~" + word + "~");
	    	
			// Wrap word to next line if required, that is,
			// if adding new word to current line would
			// exceed the screen width. If it does, just
			// crlf first.
	    	if (cur_line_len + word_buf.length() > scr_width) {
	    		crlf();
	    	}
	    	
	    	// Append word to buffer then update current line length.
	    	buffer.append(word_buf);
	    	cur_line_len += word_buf.length();
	    	
			// Delete contents of word buffer.
	    	word_buf.delete(0, word_buf.length());
	    	
	    	// Verbose debugging.
			//zm.getLogger().debug("    cur_line_len = " + cur_line_len);
	    	
		}
		
    }

    /**
     * Helper function for buffered <code>write</code>s, inserts
     * a newline at current location in buffer, then checks if
     * text paging (More) is required. If so, flushes buffer than
     * waits for user to press a key to continue displaying text.
     */
    private void crlf() {
    	// Append a newline at end of buffer.
    	buffer.append('\n');
    	// Increment line count and reset the
    	// current line length.
    	lines++;
    	cur_line_len = 0;
    	//zm.getLogger().debug("    Lines out = " + lines + ", cur_line_len is reset to " + cur_line_len);
    	// Check if More is required.
    	if (lines == scr_height - 2) {
    		buffer.append(more_prompt);
    		flush_buf();
    		console.wait_for_key();
    		erase_more_prompt();
    	}
    }
    
	/**
	 * Erases More prompt from screen.
	 */
	private void erase_more_prompt() {
		for (int i = 0; i < more_prompt.length(); i++) {
			buffer.append('\b');
		}
		EventQueue.invokeLater(new EventDispatcher(buffer.toString(), true));
		buffer.delete(0, buffer.length());
	}
	
	public static void main(String[] args) {
		Tester tester = new Tester();
		tester.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
}
