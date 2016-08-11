package com.michaelzanussi.leafpile.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import com.michaelzanussi.leafpile.ui.console.Console;
import com.michaelzanussi.leafpile.ui.menu.Menu;
import com.michaelzanussi.leafpile.ui.property.GuiPropertyManager;
import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * The Leafpile GUI.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (15 February 2008)
 */
public class LeafpileGUI extends JFrame implements IUI {

	private Console console;		// the text console window

	private Menu menu;				// menu bar

	private Zmachine zmachine;		// the virtual machine

	private Font font;
	private int scr_height;
	private int scr_width;

	private GuiPropertyManager pm;

	private boolean debug;
	
	private int version;

	private StringBuilder buffer;	// console (output) buffer
	private int cur_line_len;		// the current line length
	private int lines;				// number of lines buffered
	private boolean buffer_mode;	// word wrap active? (7.2)
	private String more_prompt;		// More prompt
	
	private int stream_table;		// address of stream table
	private int cur_addr;			// the current address within stream table
	private boolean stream1;		// screen
	private boolean stream2;		// file
	private boolean stream3;		// memory
	private boolean stream4;		// script
	
	/**
	 * Because: It is strongly recommended that all serializable classes
	 * explicitly declare serialVersionUID values.
	 */
	private static final long serialVersionUID = -383756033446196313L;

	/**
	 * No-arg constructor. Setup the application window.
	 */
	public LeafpileGUI() {

		super();

		// Create the property manager object.
		pm = new GuiPropertyManager();

		// Load the driver.
		zmachine = new Zmachine(this);
		
		debug = pm.isDebug();
		// zm.setDebug(debug);

		// setup buffer
		buffer = new StringBuilder();
		lines = 0;
		buffer_mode = true;
		cur_line_len = 0;
		more_prompt = "<More> Press any key to continue.";
		
		// initial streams setup
		stream1 = true;
		stream2 = false;
		stream3 = false;
		stream4 = false;
		stream_table = 0;
		cur_addr = 0;

		//
		// THE APPLICATION WINDOW
		//

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BorderLayout());
		ClassLoader cldr = this.getClass().getClassLoader();
		setIconImage(Toolkit.getDefaultToolkit().getImage(cldr.getResource("resources/images/application_osx.png")));
		setTitle("Leafpile");

		//
		// MENUBAR
		//

		menu = new Menu(this);
		setJMenuBar(menu);

		// duplicated code from Console.java
		font = new Font("Courier New", Font.PLAIN, 18);
		FontMetrics metrics = getFontMetrics(font);
		// Get the height of a line of text in this font and render context.
		int text_height = metrics.getHeight();
		// Get the advance of my text in this font and render context.
		// This is the width of char '0'.
		int widths[] = metrics.getWidths();
		int text_adv = widths[48];
		// Get the text ascent.
		scr_height = 25;
		scr_width = 80;
		// need these bounds set based on current font!!!!!!!!!!
		// failure to do this results in a null pointer exception
		setBounds(0, 0, scr_width * text_adv, (scr_height * text_height) + 55);

		setLocationRelativeTo(null);	// center window
		setResizable(false);			// don't allow resizing
		setVisible(true);				// make window visible

	}

	/**
	 * Start the game.
	 * 
	 * @param fn the story filename.
	 */
	public void start(File fn) {
		// Set the story and initialize memory.
		zmachine.setStory(fn);
		
		// get the story version
		version = zmachine.memory().getVersion();
		
		// Get the screen model for this story and init
		// the fame console.
		console = zmachine.getFactory().createConsole(scr_width, scr_height, font);
		console.init();
		
		// Update a few settings in memory specific to GUI.
		zmachine.memory().setScreenHeight(scr_height);	// (8.4)
		zmachine.memory().setScreenWidth(scr_width);	// (8.4)
		
		if (version >= 5) {
			assert(false) : "set screen width and height in units (8.4.3)";
		}
		
		// add to container, add listener
		add(console);
		pack();
		addKeyListener(console);
		
		// Start the game.
		zmachine.start();
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#filename(boolean)
	 */
	@Override
	public String filename(boolean isSave) {
		JFileChooser file = new JFileChooser();
		File dir = pm.getDir();
		file.setCurrentDirectory(dir);
		file.setFileSelectionMode(JFileChooser.FILES_ONLY);
		file.setFileFilter(new ZavFileFilter());
		int choice = file.showDialog(this, (isSave ? "Save" : "Restore"));
		if (choice != JFileChooser.CANCEL_OPTION) {
			String filename = file.getSelectedFile().toString();
			if (!file.getSelectedFile().getAbsolutePath().endsWith(".zav")) {
				filename += ".zav";
			}
			return filename;
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#read(java.lang.StringBuilder, int)
	 */
	@Override
	public void read(StringBuilder in, int count) {
		// TODO: flush buffer here instead of sread and the like?
		flush_buf();
		console.read(in, count);
		// TODO: backspace ^H^H^H^H^H^H^H^H over More
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#read_char(java.lang.StringBuilder)
	 */
	@Override
	public void read_char(StringBuilder in) {
		flush_buf();
		System.out.println("buffer flushed!");
		console.read_char(in);
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#write(java.lang.String)
	 */
	@Override
	public void write(String data) {
		
		// Output stream 3 writes to a table in dynamic memory.
		// When the stream is selected, the table may have any
		// contents (even the initial 'size' word will be 
		// ignored by the interpreter). While the stream is
		// selected, the table's contents are unspecified (and
		// a game cannot safely read or write to it). When the
		// stream is deselected, the initial word of the table
		// holds the number of characters printed and subsequent
		// bytes hold those characters. (NOTE: # of chars printed
		// and the chars are actually updated here, not when the
		// stream is deselected.) Similarly, in Version 6, the
		// total width of printing (in units) will then be
		// stored in the word at $30 in the header. (7.1.2.1)
		// Output stream 3 is unusual in that, while it is 
		// selected, no text is sent to any other output streams
		// which are selected. (However, they remain selected.)
		// (7.1.2.2) Newlines are written to output stream 3 as
		// ZSCII 13. (A game should never print_char the value
		// 10, or any other value which is undefined as a ZSCII
		// output code.) (7.1.2.2.1)
		if (stream3) {
			// write out data to memory
			for (int i = 0; i < data.length(); i++) {
				char ch = data.charAt(i);
				zmachine.memory().setByte(cur_addr, ch);
				cur_addr++;
			}
			// Get current character account and add to it length
			// of current string, then update the character count.
			int length = zmachine.memory().getWord(stream_table);
			length += data.length();
			zmachine.memory().setWord(stream_table, length);
			// All done, return.
			return;
		}

		// There may be a better way to do this, but since
		// MOREs need to be handled, this is the easiest.

		StringBuilder word_buf = new StringBuilder();

		// Keep writing data to buffer. Buffer is flushed
		// on demand, when user input is required, or if
		// text needs to be paged (using More).

		for (int i = 0; i < data.length(); i++) {
			char ch = data.charAt(i);
			switch (ch) {
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
	 * @see com.michaelzanussi.leafpile.ui.IUI#flush_buf()
	 */
	@Override
	public void flush_buf() {
		System.out.println(buffer.length() + " chars ~~~" + buffer.toString() + "~~~");
		if (buffer.length() == 0) {
			System.out.println("(nothing to flush; empty buffer)\n");
			lines = 0;
			return;
		}
		console.write_lines(buffer.toString());
		buffer.delete(0, buffer.length());
		// reset lines
		lines = 0;
		// zm.getLogger().debug(" Lines reset to " + lines);
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#debug(java.lang.String)
	 */
	@Override
	public void debug(String string) {

	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#getStreamTableAddress()
	 */
	@Override
	public int getStreamTableAddress() {
		return stream_table;
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#setStreamTableAddress(int)
	 */
	@Override
	public void setStreamTableAddress(int addr) {
		stream_table = addr;
		cur_addr = stream_table + 2;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#isStreamSelected(int)
	 */
	@Override
	public boolean isStreamSelected(int stream) {
		switch (stream) {
		case 1:
			// In Versions 1 and 2, output stream 1 
			// is always selected. (7.3)
			if (version == 1 || version == 2) {
				return true;
			} else {
				return stream1;
			}
		case 2:
			return stream2;
		case 3:
			return stream3;
		case 4:
			return stream4;
		}
		return false;
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#setStream(int, boolean)
	 */
	public void setStream(int stream, boolean selected) {
		// In Versions 1 and 2, stream 2 can be selected
		// or deselected by the game, by setting or clearing
		// bit 0 of 'Flags 2'. (7.3) In Versions 3 and later,
		// all four output streams can be selected or deselected
		// using the output_stream opcode. In addition,
		// stream 2 can be selected or deselected by setting
		// or clearing bit 0 of 'Flags 2'. (7.4)
		switch (stream) {
		case 1:
			stream1 = selected;
		case 2:
			stream2 = selected;
		case 3:
			stream3 = selected;
		case 4:
			stream4 = selected;
		}
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#setBufferMode(boolean)
	 */
	@Override
	public void setBufferMode(boolean buffer_mode) {
		// Buffering is always on in Versions 1 to 3.
		if (version < 4) {
			this.buffer_mode = true;
		} else {
			this.buffer_mode = buffer_mode;
		}
	}

	/**
	 * Helper function for buffered <code>write</code>s, adds a word to the
	 * buffered output. Wraps words as required.
	 * 
	 * @param word_buf the word to write to buffer.
	 */
	private void write_to_buffer(StringBuilder word_buf) {

		// Check first for valid length word.
		// This method is only necessary for
		// non zero-length strings.

		if (word_buf.length() > 0) {

			// Verbose debugging.
			// zm.getLogger().debug(" ~" + word + "~");

			// Wrap word to next line if required, that is,
			// if adding new word to current line would
			// exceed the screen width. If it does, just
			// crlf first. Occurs only if buffer_mode is
			// active (via BUFFER_MODE opcode).
			if (cur_line_len + word_buf.length() > console.getCurrentWindow().getWindow().width) {
				if (buffer_mode) {
System.out.println("<-- word wrap occurred");
					crlf();
				}
			}

			// Append word to buffer then update current line length.
			buffer.append(word_buf);
			cur_line_len += word_buf.length();

			// Delete contents of word buffer.
			word_buf.delete(0, word_buf.length());

			// Verbose debugging.
			// zm.getLogger().debug(" cur_line_len = " + cur_line_len);

		}

	}

	/**
	 * Helper function for buffered <code>write</code>s, inserts a newline at
	 * current location in buffer, then checks if text paging (More) is
	 * required. If so, flushes buffer than waits for user to press a key to
	 * continue displaying text.
	 */
	private void crlf() {
		// Append a newline at end of buffer.
		buffer.append('\n');
		// Increment line count and reset the
		// current line length.
		lines++;
		cur_line_len = 0;
		// zm.getLogger().debug(" Lines out = " + lines + ", cur_line_len is
		// reset to " + cur_line_len);
		// Check if More is required.
		if (lines == console.getCurrentWindow().getWindow().height - 1) {
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
		console.erase_chars(buffer.toString());
		buffer.delete(0, buffer.length());
		// zm.getLogger().debug(" More prompt erased.");
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#hasSplitScreen()
	 */
	@Override
	public boolean hasSplitScreen() {
		return console.hasSplitScreen();
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#hasNoStatusLine()
	 */
	@Override
	public boolean hasNoStatusLine() {
		return console.hasNoStatusLine();
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#isVariablePitchDefault()
	 */
	@Override
	public boolean isVariablePitchDefault() {
		return console.isVariablePitchDefault();
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#isBoldAvailable()
	 */
	@Override
	public boolean isBoldAvailable() {
		return console.isBoldAvailable();
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#isItalicAvailable()
	 */
	@Override
	public boolean isItalicAvailable() {
		return console.isItalicAvailable();
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#isFixedSpaceFontAvailable()
	 */
	@Override
	public boolean isFixedSpaceFontAvailable() {
		return console.isFixedSpaceFontAvailable();
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#hasTimedInput()
	 */
	@Override
	public boolean hasTimedInput() {
		return console.hasTimedInput();
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#erase_window(int)
	 */
	@Override
	public void erase_window(int window) {
		console.erase_window(window);
	}
	
	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#split_window(int)
	 */
	@Override
	public void split_window(int lines) {
		console.split_window(lines);
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#set_window(int)
	 */
	@Override
	public void set_window(int window) {
		console.set_window(window);
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#set_cursor(int, int, int)
	 */
	@Override
	public void set_cursor(int line, int column, int window) {
		console.set_cursor(line, column, window);
	}

	/* (non-Javadoc)
	 * @see com.michaelzanussi.leafpile.ui.IUI#setTextStyle(int)
	 */
	@Override
	public void set_text_style(int style) {
		console.set_text_style(style);
	}

	/**
	 * @return
	 */
	public GuiPropertyManager getPropertyManager() {
		return pm;
	}

	/**
	 * @param debug
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
		// zm.setDebug(debug);
	}

	/**
	 * @return
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Zav file filter class.
	 */
	private class ZavFileFilter extends FileFilter {
		@Override
		public boolean accept(File f) {
			return f.isDirectory() | (f.isFile()
					&& f.getName().toLowerCase().endsWith(".zav"));
		}

		@Override
		public String getDescription() {
			return ".zav files";
		}
	}

}