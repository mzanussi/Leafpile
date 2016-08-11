package com.michaelzanussi.leafpile.ui.console;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.michaelzanussi.leafpile.zmachine.Zmachine;

/**
 * This class provides a skeletal implementation of the <code>Console</code> 
 * interface, to minimize the effort required to implement this interface.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (12 February 2008)
 */
public abstract class Console extends JPanel implements KeyListener {

	protected static final Color C64_BLUE = new Color(0, 110, 192);
	protected static final Color C64_WHITE = new Color(216, 216, 216);
	protected static final Color C64_BLACK = new Color(0, 0, 0);
	protected static final Color C64_GREEN = new Color(0, 192, 0);
	
	protected Zmachine zmachine;
	
	// Screen dimensions.
	private int scr_height;
	private int scr_width;
	
	// Text defs.
	private Font font;
	private Font default_font;			// the default font
	private FontMetrics metrics;
	private Color bg_color;
	private Color fg_color;
	
	// Window regions
	private Window statusBar;			// the status bar
	private Window upperWindow;			// the upper window
	private Window lowerWindow;			// the lower window
	private Window cwnd;				// the current window
	
	protected boolean noStatusLine;		// has no status line? (8.2)
	protected boolean splitScreen;		// has split screen? (8.6.1.2)
	protected boolean dfltVarPitch;		// variable-pitch font is default?
	protected boolean bold;				// is boldface available? (8.7.1.1)
	protected boolean italic;			// is italic available? (8.7.1.1)
	protected boolean fixedSpaceAvail;	// is a fixed-space font available?
	protected boolean timedInput;		// is a timed keyboard available?
	
	private int text_height;			// text height (character height)
	private int text_adv;				// text advance (character width)
	private int text_ascent;
	private int text_style;
	
    private Graphics2D osg;				// off-screen graphics object
	private BufferedImage img;			// the off-screen image
	
	private boolean eol = false;			// at end of line?
	private boolean ignore_input = true;	// ignore keypresses?
	private StringBuilder buf;				// text buffer
	private int max_count;					// max character count
	private int char_count;					// character count
	private boolean adj_buf_size;			// adjusted buffer size? (see p. 95)
	private boolean hide_chars = false;		// hide chars from screen?
	
	// Current cursor location.
	private int cx;
	private int cy;
	
	/**
	 * Constructor.
	 * 
	 * @param scr_width		the screen width in chars
	 * @param scr_height	the screen height in chars
	 * @param font			the font for the screen
	 */
	public Console(Zmachine zmachine, int scr_width, int scr_height, Font font) {
		this.zmachine = zmachine;
		this.scr_width = scr_width;
		this.scr_height = scr_height;
		this.font = font;
		default_font = font;
		// Set windows.
		statusBar = null;
		upperWindow = null;
		lowerWindow = null;
		cwnd = null;
		// Default to no split screen (V1-3); screen model will set this.
		splitScreen = false;
		// Default to no status line (V1-3); screen model will set this.
		noStatusLine = true;
		// Default to fixed-pitch (V1-3); screen model will set this.
		dfltVarPitch = false;
		// Default to no bold (V4>); screen model will set this.
		bold = false;
		// Default to no italics (V4>); screen model will set this.
		italic = false;
		// Default to no fixed-space available (V4>); screen model will set this.
		fixedSpaceAvail = false;
		// Default to no timed input (V4>); screen model will set this.
		timedInput = false;
		// Background/foreground colors.
		bg_color = C64_BLUE;
		fg_color = C64_WHITE;
		// TODO: Add support for variable width fonts.
		// Font metrics.
        metrics = getFontMetrics(font);
        // Get the height of a line of text in this font and render context.
        text_height = metrics.getHeight();
        // Get the advance of my text in this font and render context.
        // This is the width of char '0'.
        int widths[] = metrics.getWidths();
        text_adv = widths[48];
        // Get the text ascent.
        text_ascent = metrics.getAscent();
        // By default, don't adjust text-buffer size.
        adj_buf_size = false;
        // Init cursor location.
        cx = cy = 0;
	}

	/**
	 * Create the off-screen image set the graphics context.
	 */
	public void init() {
		img = new BufferedImage(scr_width * text_adv, scr_height * text_height, BufferedImage.TYPE_INT_RGB);
		osg = img.createGraphics();
	}
	
	/**
	 * Wipe the specified window with the specified color.
	 * 
	 * @param win the window to clear.
	 * @param col the color to fill window with.
	 */
	protected void wipe(Window win, Color col) {
		if (win != null) {
	    	// Calculate the rectangle bounds for the current window. 
			// Must take into account the width and height of the text.
			int x = win.getWindow().x * text_adv;
			int y = win.getWindow().y * text_height;
			int width = win.getWindow().width * text_adv;
			int height = win.getWindow().height * text_height;
	        Rectangle r = new Rectangle(x, y, width, height);
			// Set the background color.
			osg.setColor(col);
			// Fill it.
			osg.fillRect(r.x, r.y, r.width, r.height);
			// Repaint the window.
			repaint();
		}
	}
	
	/**
	 * Erase a window.
	 * 
	 * @param window the window to erase
	 */
	public abstract void erase_window(int window);
	
	/**
	 * Split the current window.
	 * 
	 * @param lines number of lines in new window
	 */
	public abstract void split_window(int lines);
	
	/**
	 * Set the current window.
	 * 
	 * @param window the window to set as current
	 */
	public abstract void set_window(int window);
	
	/**
	 * Set the cursor.
	 * TODO: erase old cursor prior to setting new cursor,
	 * will require using getCursor for coordinates. see 
	 * show_cursor() below for actual erasing..., or,
	 * just create another method erase_cursor()...
	 * 
	 * @param line the line where cursor resides
	 * @param column the column where cursor resides
	 * @param window the window where cursor resides
	 */
	public abstract void set_cursor(int line, int column, int window);
	
	/* (non-Javadoc)
	 * @see javax.swing.JComponent#paintComponent(java.awt.Graphics)
	 */
	@Override
	public void paintComponent(Graphics g) {
		// Paint using off-screen image.
		super.paintComponent(g);
    	Graphics2D g2 = (Graphics2D)g;
   		g2.drawImage(img, 0, 0, this);
	}
	
    /**
     * Scroll the window up one line.
     * 
     * TODO: The upper window should never be scrolled: it is legal for a
     * character to be printed on the bottom right portion of the upper
     * window (but the position of the cursor after this operation is 
     * undefined: the author suggests it stay put. (8.7.3.1)
     */
    private void scroll() {
    	
    	// Set the background color to wipe bottom-most line.
    	osg.setColor(bg_color);
    	
    	// Locate the origin of this window.
    	int ox = cwnd.getWindow().x;
    	int oy = cwnd.getWindow().y + 1;
    	
    	// Copy the area to be scrolled.
    	int x = ox * text_adv;									// starting column * text width
    	int y = oy * text_height;								// starting row * text height
    	int width = cwnd.getWindow().width * text_adv;			// width of area to copy
    	int height = (cwnd.getWindow().height * text_height);	// height of area to copy
    	int dx = 0;												// don't move left or right
    	int dy = -text_height;									// move up one row
		osg.copyArea(x, y, width, height, dx, dy);
		
		// Wipe out the line.
		x = (cx + cwnd.getWindow().x) * text_adv;		// typically cx will be 0
		y = (cy + cwnd.getWindow().y) * text_height;	// typically cy will be scr_height - 1
		width = cwnd.getWindow().width * text_adv;		// width of entire window will be erased
		height = text_height;							// erase one row only
    	osg.fillRect(x, y, width, height);
    	
    }
    
    /**
     * Show the cursor on the screen.
     */
    public void show_cursor() {
    	// TODO: need a check here to see if cursor
    	// should be displayed at all (as in, check story version)
    	// Also, erase old cursor first!
    	// probably: osg.setColor(background)
    	// and then: fillRect(,,,) [use proper coords]
    	osg.setColor(Color.CYAN);
    	osg.fillRect(cx * text_adv, cy * text_height, text_adv, text_height);
    }
    
    /**
     * Write lines to the screen.
     * 
     * @param out the output.
     */
    public void write_lines(String out) {
    	
    	// Get the cursor location.
    	cx = cwnd.getCursor().x;
    	cy = cwnd.getCursor().y;

    	StringBuilder cur_buf = new StringBuilder();
    	
    	// Cycle through the out string and output its contents.
    	for (int i = 0; i < out.length(); i++) {
    		// Get the next char.
    		char ch = out.charAt(i);
    		// Check for newline.
    		if (ch == '\n') {
    			// If the current buffer is not empty, write
    			// buffer to the console, then delete the
    			// contents of the buffer.
    			if (cur_buf.length() > 0) {
    				console_write(cur_buf.toString());
    				cur_buf.delete(0, cur_buf.length());
    			}
    			// Reset the cursor.
    			crlf();
    		} else {
    			// Add to the current buffer.
    			cur_buf.append(ch);
    		}
    	}
    	
    	// Anything left in the buffer? If so, write the buffer
    	// contents to the console and advance the cursor.
    	if (cur_buf.length() > 0) {
    		console_write(cur_buf.toString());
    		cx += cur_buf.length();
    	}
    	
		cwnd.setCursor(cx, cy);
		
    }

    /**
     * Erase characters using stream of backspaces.
     * 
     * @param str string containing backspaces
     */
    public void erase_chars(String str) {
    	
    	// Get the cursor location.
    	cx = cwnd.getCursor().x;
    	cy = cwnd.getCursor().y;

    	for (int i = 0; i < str.length(); i++) {
    		char ch = str.charAt(i);
    		if (ch != '\b') {
    			break;
    		}
    		write_char(ch);
    	}
    	
    }
    
    /**
     * Write a character to the screen.
     * 
     * @param ch the character to write.
     */
    public void write_char(char ch) {
    	
    	// Get the cursor location.
    	cx = cwnd.getCursor().x;
    	cy = cwnd.getCursor().y;

    	switch(ch) {
    	case '\n':
			crlf();
    		break;
    	case '\b':
    		if (cx == 0) {
    			cx = scr_width - 1;
    			cy--;
    		}
    		else {
    			cx--;
    		}
    		console_write("\b");
    		cwnd.setCursor(cx, cy);
        	break;
    	default:
	    	if (cx + 1 > scr_width) {
	    		crlf();
	    	}
	    	// Output buffer and update cursor location.
	    	console_write(Character.toString(ch));
        	cx++;
			cwnd.setCursor(cx, cy);
			break;
    	}
    	
    }

    /**
     * Move cursor to beginning of next line.
     */
    private void crlf() {
		cx = 0;
		cy++;
		// TODO: if cursor off screen, scroll up a line.
		if (cy == cwnd.getWindow().height) {
			cy--;
			scroll();
		}
		cwnd.setCursor(cx, cy);
    }
    
    /**
     * Write the passed string starting at the cursor location
     * specified by <code>cx</code> and <code>cy</code>.
     * 
     * @param str the string to write.
     */
    private void console_write(String str) {
    	
    	// Is the cursor currently beyond the screen bounds?
    	// If so, scroll up one line before writing the text 
    	// by setting cursor to bottom line of window and 
    	// then scrolling up a single line.
		if (cy == cwnd.getWindow().height) {
			cy--;
			scroll();
		}
		
		// Calculate cursor position with respect to console.
		int ox = cx + cwnd.getWindow().x;
		int oy = cy + cwnd.getWindow().y;
		
		//zm.getLogger().debug("(cx,cy) = (" + cx + "," + cy + ")");
		System.out.println("(cx,cy)=(" + cx + "," + cy + ")  (ox,oy)=(" + ox + "," + oy + ")  string=\"" + str + "\"");
		
    	// Calculate the absolute cursor position. 
    	int ax = ox * text_adv;
    	int ay = oy * text_height;

    	// If user is trying to backspace, save the current
    	// background color then set it to new background.
    	// Clear the character (via call to clearRect) then
    	// repaint and reset to previous background color.
    	// Finished, no need to continue executing the method. 
    	if (str.length() > 0 && str.charAt(0) == '\b') {
    		Color obg = osg.getBackground();
    		osg.setBackground(bg_color);
    		osg.clearRect(ax, ay, text_adv, text_height);
    		repaint();
    		osg.setBackground(obg);
    		return;
    	}
    	
    	// Set the font and color.
    	switch (text_style) {
    	case 0:	// default
    		font = default_font;
    		osg.setFont(font);
    		osg.setColor(fg_color);
    		osg.setBackground(bg_color);
    		break;
    	case 1: // reverse
        	// Set the rectangle color to the fg color.
    		osg.setColor(fg_color);
    		// Fill the rectangle.
    		osg.fillRect(ax, ay, metrics.stringWidth(str), text_height);
    		// Reset font.
    		font = default_font;
    		osg.setFont(font);
    		// Set the text color to the bg color.
    		osg.setColor(bg_color);
    		break;
    	case 2:	// bold
    		font = new Font("Courier New Bold", Font.BOLD, 18);
    		osg.setFont(font);
    		osg.setColor(fg_color);
    		osg.setBackground(bg_color);
    		break;
    	case 4:	// italic
    		font = new Font("Courier New Bold", Font.ITALIC, 18);
    		osg.setFont(font);
    		osg.setColor(fg_color);
    		osg.setBackground(bg_color);
    		break;
    	case 8:	// fixed pitch
    		font = default_font;
    		osg.setFont(font);
    		osg.setColor(fg_color);
    		osg.setBackground(bg_color);
    		break;
    	}
    	
//System.out.println("FONT STYLE: " + font.getStyle());

    	// Adjust using the text ascent so the text 
    	// won't get cut-off on the first line.
    	ay += text_ascent;

        // Draw the string.
    	osg.drawString(str, ax, ay);
    	
    	// Repaint.
        repaint();
        
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#getPreferredSize()
     */
	@Override
    public Dimension getPreferredSize() {
    	return new Dimension(scr_width * text_adv, scr_height * text_height);
    }
    
    /**
     * Returns the foreground color.
     * 
     * @return the foreground color.
     */
    protected Color getFgColor() {
    	return fg_color;
    }
    
    /**
     * Returns the lower window background color.
     * 
     * @return the lower window background color.
     */
    protected Color getLowerBgColor() {
    	return C64_BLUE;
    }
    
    /**
     * Returns the upper window background color.
     * 
     * @return the upper window background color.
     */
    protected Color getUpperBgColor() {
    	return C64_BLUE;
    }
    
    /**
     * Returns the screen width.
     * 
     * @return the screen width.
     */
    protected int getScreenWidth() {
    	return scr_width;
    }
    
    /**
     * Returns the screen height.
     * 
     * @return the screen height.
     */
    protected int getScreenHeight() {
    	return scr_height;
    }
    
    /**
     * Sets the status bar.
     * 
     * @param statusBar the status bar.
     */
    protected void setStatusBar(Window statusBar) {
    	this.statusBar = statusBar;
    }
    
    /**
     * Returns the status bar.
     * 
     * @return the status bar.
     */
    protected Window getStatusBar() {
    	return statusBar;
    }
    
    /**
     * Returns split screen status.
     * 
     * @return split screen status
     */
    public boolean hasSplitScreen() {
    	return splitScreen;
    }
    
    /**
     * Returns status line status.
     * 
     * @return status line status
     */
    public boolean hasNoStatusLine() {
    	return noStatusLine;
    }
    
    /**
     * Returns if variable pitch default.
     * 
     * @return if variable pitch default
     */
    public boolean isVariablePitchDefault() {
    	return dfltVarPitch;
    }
    
    /**
     * Returns if bold is available.
     * 
     * @return if bold is available.
     */
    public boolean isBoldAvailable() {
    	return bold;
    }
    
    /**
     * Returns if italic is available.
     * 
     * @return if italic is available
     */
    public boolean isItalicAvailable() {
    	return italic;
    }
    
    /**
     * Returns if fixed space font is available.
     * 
     * @return if fixed space font is available
     */
    public boolean isFixedSpaceFontAvailable() {
    	return fixedSpaceAvail;
    }
    
    /**
     * Returns if timed input.
     * 
     * @return if timed input.
     */
    public boolean hasTimedInput() {
    	return timedInput;
    }
    
    /**
     * The text style.
     * 
     * @param text_style the text style
     */
    public void set_text_style(int text_style) {
    	this.text_style = text_style;
    }
    
    /**
     * Sets the upper window.
     * 
     * @param upperWindow the upper window.
     */
    protected void setUpperWindow(Window upperWindow) {
    	this.upperWindow = upperWindow;
    }
    
    /**
     * Returns the upper window.
     * 
     * @return the upper window.
     */
    protected Window getUpperWindow() {
    	return upperWindow;
    }
    
    /**
     * Sets the lower window.
     * 
     * @param lowerWindow the lower window.
     */
    protected void setLowerWindow(Window lowerWindow) {
    	this.lowerWindow = lowerWindow;
    }
    
    /**
     * Returns the lower window.
     * 
     * @return the lower window.
     */
    protected Window getLowerWindow() {
    	return lowerWindow;
    }

    /**
     * Sets the current window.
     * 
     * @param cwnd the current window to set.
     */
    protected void setCurrentWindow(Window cwnd) {
    	this.cwnd = cwnd;
    }
    
    /**
     * Returns the current window.
     * 
     * @return the current window.
     */
    public Window getCurrentWindow() {
    	return cwnd;
    }
    
    /*
     * KeyListener methods, and other keyboard support.
     */
    
    /**
     * Read user input from keyboard, up to count characters.
     * 
     * @param in the buffer to hold keypresses
     * @param count the max number of characters to read
     */
    public synchronized void read(StringBuilder in, int count) {
    	// Set the internal buffer.
    	buf = in;
    	// Max # of chars to read. For adj_buf_size, see p. 95.
    	if (adj_buf_size) {
    		max_count = count + 1;
    	}
    	else {
        	max_count = count;
    	}
    	char_count = 0;
    	// Reset end-of-line.
    	eol = false;
    	// Don't ignore any user input!
    	ignore_input = false;
    	// Block on user input.
    	while (!eol) {
        	try {
    			wait();
    		} catch (InterruptedException e) {
    			e.printStackTrace();
    		}
    	}
    	// Ignore subsequent keypresses until next read().
    	ignore_input = true;
    }
    
    /**
     * Read a character from the keyboard.
     * 
     * @param in the buffer to hold the keypress
     */
    public synchronized void read_char(StringBuilder in) {
    	// Set the internal buffer.
    	buf = in;
    	// Reset character count.
    	char_count = 0;
    	// Max # of chars to read.
    	max_count = 1;
    	// Don't ignore any user input...
    	ignore_input = false;
    	// ...but we don't want to see what user pressed.
    	hide_chars = true;
    	// Wait for a key to be pressed.
    	wait_for_key();
    	// Ignore subsequent keypresses until next read_char().
    	ignore_input = true;
    	// Make characters visible again.
    	hide_chars = false;
    }
    
    /**
     * Wait for user keypress.
     */
    public synchronized void wait_for_key() {
    	try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Process the key just pressed.
     * 
     * @param keycode
     * @param keychar
     */
    private synchronized void processKey(int keycode, char keychar) {
    	
    	if (hide_chars) {
    		buf.append(keychar);
    		notifyAll();
    		return;
    	}
    	
    	if (keycode == KeyEvent.VK_ENTER) {	
    		eol = true;
    		write_char('\n');
    	}
    	else if (keycode == KeyEvent.VK_BACK_SPACE) {
    		if (buf.length() != 0) {
    			buf.deleteCharAt(buf.length() - 1);
    			char_count--;
    			write_char('\b');
    		}
    	}
    	else if ((byte)keychar >= 32 && (byte)keychar <= 126) {
    		// Read up to max_count characters.
        	if (char_count < max_count) {
        		buf.append(Character.toLowerCase(keychar));
        		char_count++;
        		write_char(keychar);
        	}
    	}
    	
       	notifyAll();
       	
    }

    /* (non-Javadoc)
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
	@Override
    public synchronized void keyPressed(KeyEvent evt) {
    	if (!ignore_input) {
            if (evt.getKeyCode() != KeyEvent.VK_SHIFT) {
            	processKey(evt.getKeyCode(), evt.getKeyChar());
            }
    	}
    	else {
        	notifyAll();
    	}

    }
    
    /* (non-Javadoc)
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
	@Override
    public synchronized void keyReleased(KeyEvent evt) {
    }
    
    /* (non-Javadoc)
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
	@Override
    public synchronized void keyTyped(KeyEvent evt) {
    }

    public void setAdjustBufferSize(boolean adj_buf_size) {
    	this.adj_buf_size = adj_buf_size;
    }
    
}
