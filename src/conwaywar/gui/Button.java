package conwaywar.gui;

import conwaywar.ButtonContainer;
import conwaywar.Game;
import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * Button class which can be rendered with a graphics object
 */
public class Button implements MouseListener {

    private String text;
    Font font;
    int x, y, width, height;
    Color backgroundColor, outlineColor, highlightColor, textColor;
    FontMetrics fontMetrics;
    int fontWidth, fontHeight;
    Game frame;
    private ArrayList<ButtonListener> listeners = new ArrayList<>();

    public Button(Game frame, ButtonContainer bc, String text, Font font, int x, int y, int width, int height, Color backgroundColor, Color outlineColor, Color highlightColor, Color textColor) {
        addListener(bc);
        frame.addMouseListener(this);
        this.frame = frame;
        this.text = text;
        this.font = font;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.backgroundColor = backgroundColor;
        this.outlineColor = outlineColor;
        this.highlightColor = highlightColor;
        this.textColor = textColor;

        Canvas c = new Canvas();
        fontMetrics = c.getFontMetrics(font);
        fontWidth = fontMetrics.stringWidth(text);
        fontHeight = fontMetrics.getAscent() - fontMetrics.getDescent();
    }

    public void render(Graphics2D g) {
        g.setFont(font);
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        mouseLocation = new Point(mouseLocation.x - frame.getLocationOnScreen().x, mouseLocation.y - frame.getLocationOnScreen().y);

        if (backgroundColor != null) {
            g.setColor(backgroundColor);
            g.fillRect(x, y, width, height);
        }
        if (outlineColor != null) {
            g.setColor(outlineColor);
            g.setStroke(new BasicStroke(2));
            g.drawRect(x + 1, y + 1, width - 1, height - 1);
        }
        if (highlightColor != null && mouseOver(mouseLocation)) {
            g.setColor(highlightColor);
            g.setStroke(new BasicStroke(2));
            g.drawRect(x + 1, y + 1, width - 2, height - 2);
        }
        if (textColor != null) {
            g.setColor(textColor);
            g.drawString(getText(), x + ((width - fontWidth) / 2), y + fontHeight + (height - fontHeight) / 2);
        }
    }

    /**
     * Returns true if mouse is over button
     *
     * @param m Location of the mouse pointer
     * @return True if mouse is over button
     */
    public boolean mouseOver(Point m) {
        return new Rectangle(x, y, width, height).intersects(new Rectangle(m.x, m.y, 1, 1));
    }

    public void addListener(ButtonListener bl) {
        listeners.add(bl);
    }

    @Override
    public void mouseClicked(MouseEvent me) {
    }

    @Override
    public void mousePressed(MouseEvent me) {
        if (mouseOver(me.getPoint())) {
            for(ButtonListener bl : listeners){
                bl.buttonPressed(this);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
    }

    @Override
    public void mouseEntered(MouseEvent me) {
    }

    @Override
    public void mouseExited(MouseEvent me) {
    }
    
    public String getText() {
        return text;
    }
}
