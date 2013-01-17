package org.hunmr.acejump.marker;

import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.colors.EditorFontType;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

public class MarkersPanel extends JComponent {
    public static final Color PANEL_BACKGROUND_COLOR = new Color(128, 138, 142);
    public static final Color MARKUP_CHAR_COLOR = new Color(255, 0, 0);
    private Editor _editor;
    private MarkerCollection _markerCollection;
    private JComponent _parent;
    private Font _fontInEditor;

    public MarkersPanel(Editor editor, MarkerCollection markerCollection) {
        _editor = editor;
        _markerCollection = markerCollection;
        _parent = _editor.getContentComponent();
        _fontInEditor = _editor.getColorsScheme().getFont(EditorFontType.BOLD);
        setupLocationAndBoundsOfPanel(editor);
    }

    @Override
    public void paint(Graphics g) {
        drawPanelBackground(g);

        g.setFont(_fontInEditor);

        for (Marker marker : _markerCollection.values()) {
            for (int offset : marker.getOffsets()) {

                double x = getVisiblePosition(offset).getX() + _parent.getLocation().getX();
                double y = getVisiblePosition(offset).getY() + _parent.getLocation().getY();

                drawBackgroundOfMarupChar(g, x, y);
                drawMarkupChar(g, marker, x, y);
            }
        }

        super.paint(g);
    }

    private void drawMarkupChar(Graphics g, Marker markup, double x, double y) {
        float buttomYOfMarkupChar = (float) (y + _fontInEditor.getSize());

        g.setColor(MARKUP_CHAR_COLOR);
        ((Graphics2D)g).drawString(String.valueOf(markup.getMarkupChar()), (float)x, buttomYOfMarkupChar);
    }

    private void drawBackgroundOfMarupChar(Graphics g, double x, double y) {
        Rectangle2D fontRect = _parent.getFontMetrics(_fontInEditor).getStringBounds("a", g);

        g.setColor(_editor.getColorsScheme().getDefaultBackground());
        g.fillRect((int)x, (int)y, (int) fontRect.getWidth(), (int) fontRect.getHeight());
    }

    private Point getVisiblePosition(int offset) {
        return _editor.visualPositionToXY(_editor.offsetToVisualPosition(offset));
    }

    private void drawPanelBackground(Graphics g) {
        ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        g.setColor(PANEL_BACKGROUND_COLOR);
        g.fillRect(0, 0, (int) this.getBounds().getWidth(), (int) this.getBounds().getHeight());
        ((Graphics2D)g).setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
    }

    private void setupLocationAndBoundsOfPanel(Editor editor) {
        this.setLocation(0, 0);
        Rectangle visibleArea = editor.getScrollingModel().getVisibleArea();
        int x = (int) (_parent.getLocation().getX() + visibleArea.getX());
        this.setBounds(x, (int) (visibleArea.getY()), (int) visibleArea.getWidth(), (int) visibleArea.getHeight());
    }
}
