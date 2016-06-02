package com.michaelzanussi.leafpile.ui.components;

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

/**
 * This class provides a skeletal implementation of the <code>Console</code> 
 * interface, to minimize the effort required to implement this interface.
 * 
 * @author <a href="mailto:iosdevx@gmail.com">Michael Zanussi</a>
 * @version 1.0 (12 February 2008)
 */
public abstract class Console extends JPanel implements KeyListener {

	private static final Color C64_BLUE = new Color(0, 110, 192);
	private static final Color C64_WHITE = new Color(216, 216, 216);
	//private static final Color C64_BLACK = new Color(0, 0, 0);
	//private static final Color C64_GREEN = new Color(0, 192, 0);
	
	// Screen dimensions.
	private int scr_height;
	private int scr_width;
	
	// Text defs.
	private Font font;
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
	
    private Graphics2D osg;				// off-screen graphics object
	private BufferedImage img;			// the off-screen image
	
	private boolean eol = false;
	private boolean ignore_input = true;
	private StringBuilder buf;
	private int max_count;
	private int char_count;
	private boolean adj_buf_size;
	
	// Current cursor location.
	private int cx;
	private int cy;
	
	public Console(int scr_width, int scr_height, Font font) {
		this.scr_width = scr_width;
		this.scr_height = scr_height;
		this.font = font;
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
		}
	}
	
	public abstract void erase_window(int window);
	
	public abstract void split_window(int lines);
	
	public abstract void set_window(int window);
	
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
     */
    private void scroll() {
    	
    	// Set the background color to wipe bottom-most line.
    	osg.setColor(bg_color);
    	
    	// Locate the origin of this window.
    	int ox = cwnd.getOrigin().x;
    	int oy = cwnd.getOrigin().y + 1; 	// begin with line below top line
    	
    	// Copy the area to be scrolled.
    	int x = ox * text_adv;							// starting column * text width
    	int y = oy * text_height;						// starting row * text height
    	int width = scr_width * text_adv;				// width of area to copy
    	int height = (scr_height * text_height) - y;	// height of area to copy
    	int dx = 0;										// don't move left or right
    	int dy = -text_height;							// move up one row
		osg.copyArea(x, y, width, height, dx, dy);
		
		// Wipe out the line.
		x = cx * text_adv;				// typically cx will be 0
		y = cy * text_height;			// typically cy will be scr_height - 1
		width = scr_width * text_adv;	// width of entire window will be erased
		height = text_height;			// erase one row only
    	osg.fillRect(x, y, width, height);
    	
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

    	String [] lines = null;
    	lines = out.split("\n");

        int j = lines.length;
    	for (int i = 0; i < lines.length - 1; i++) {
    		console_write(lines[i]);
    		crlf();
    	}
    	console_write(lines[j-1]);
    	cx += lines[j-1].length();
		cwnd.setCursor(cx, cy);
		
    }

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
		if (cy == scr_height) {
			cy--;
			scroll();
		}
		
		//zm.getLogger().debug("(cx,cy) = (" + cx + "," + cy + ")");
		//System.out.println("(cx,cy) = (" + cx + "," + cy + ")");
		
    	// Calculate the absolute cursor position. 
    	int ax = cx * text_adv;
    	int ay = cy * text_height;

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
    	osg.setFont(font);
System.out.println("FONT STYLE: " + font.getStyle());
    	osg.setColor(fg_color);

    	// Set to the background color to clear the window.
    	// TODO: if reverse mode, use reverse bg color
    	//osg.setColor(bg_color);
    	// TODO: if reverse mode Clear the text area prior to printing text.
    	//osg.fillRect(ax, ay, metrics.stringWidth(str), text_height);
    	
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
     * Returns the background color.
     * 
     * @return the background color.
     */
    protected Color getBgColor() {
    	return bg_color;
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
    
    public boolean hasSplitScreen() {
    	return splitScreen;
    }
    
    public boolean hasNoStatusLine() {
    	return noStatusLine;
    }
    
    public boolean isVariablePitchDefault() {
    	return dfltVarPitch;
    }
    
    public boolean isBoldAvailable() {
    	return bold;
    }
    
    public boolean isItalicAvailable() {
    	return italic;
    }
    
    public boolean isFixedSpaceFontAvailable() {
    	return fixedSpaceAvail;
    }
    
    public boolean hasTimedInput() {
    	return timedInput;
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
    protected Window getCurrentWindow() {
    	return cwnd;
    }
    
    public synchronized void read(StringBuilder in, int count) {
    	/*System.out.println("count: " + count);
    	if (count <= 0) {
    		// ODO: set to some other val, like 255?
    	}*/
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
     * @param keycode
     * @param keychar
     */
    private synchronized void processKey(int keycode, char keychar) {
    	
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
