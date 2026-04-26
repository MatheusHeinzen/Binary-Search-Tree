import javax.swing.*;
import java.awt.*;

public class TreePanel extends JPanel {
    private final RankingController controller;
    private boolean showRanking;
    private double zoom = 1.0;
    private int nodeGapX = 45;
    private int nodeGapY = 100;
    private int currentX;

    public TreePanel(RankingController controller) {
        this.controller = controller;
        addMouseWheelListener(e -> {
            if (e.getPreciseWheelRotation() < 0) zoomIn();
            else zoomOut();
        });
    }

    public void zoomIn() { zoom = Math.min(zoom * 1.1, 2.0); revalidate(); repaint();}
    public void zoomOut() { zoom = Math.max(zoom / 1.1, 0.8); revalidate(); repaint();}

    @Override
    public Dimension getPreferredSize() {
        int width = (int) (super.getPreferredSize().width * zoom);
        int height = (int) (super.getPreferredSize().height * zoom);
        return new Dimension(width, height);
    }

    public void setShowRanking(boolean showRanking) {
        this.showRanking = showRanking;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.scale(zoom, zoom);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        currentX = 80;
        drawTree(controller.getRoot(), 0);
        drawNodeAnimated(g2d, controller.getRoot());
        g2d.dispose();
    }

    private int drawTree(Node node, int depth) {
        if (node == null) return -1;

        int leftX  = drawTree(node.getLeft(),  depth + 1);
        int myX    = currentX;
        int myY    = 30 + depth * nodeGapY;
        currentX  += nodeGapX;
        int rightX = drawTree(node.getRight(), depth + 1);

        if      (leftX != -1 && rightX != -1) myX = (leftX + rightX) / 2;
        else if (leftX != -1)                 myX = leftX  + nodeGapX / 2;
        else if (rightX != -1)                myX = rightX - nodeGapX / 2;

        node.targetX = myX;
        node.targetY = myY;

        if (!node.viewInitialized) {
            node.viewX = myX;
            node.viewY = myY;
            node.viewInitialized = true;
        }

        return myX;
    }

    private void drawNode(Graphics2D g2d, Node node, int x, int y) {
        String label = showRanking
                ? String.valueOf(node.getPlayer().getRanking())
                : node.getPlayer().getNickname();

        FontMetrics metrics = g2d.getFontMetrics();
        int textWidth  = metrics.stringWidth(label);
        int paddingX   = 10;
        int paddingY   = 8;
        int nodeWidth  = textWidth + paddingX * 2;
        int nodeHeight = metrics.getHeight() + paddingY;
        int arc        = 10;
        int drawX      = x - nodeWidth / 2;
        int drawY      = y - nodeHeight / 2;

        g2d.setColor(node.isHighlighted() ? Color.YELLOW : Color.WHITE);
        g2d.fillRoundRect(drawX, drawY, nodeWidth, nodeHeight, arc, arc);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(drawX, drawY, nodeWidth, nodeHeight, arc, arc);
        g2d.drawString(label, x - textWidth / 2, y + (metrics.getAscent() - metrics.getDescent()) / 2);
    }

    private void drawLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d.drawLine(x1, y1 + 15, x2, y2 - 15);
    }

    private void drawNodeAnimated(Graphics2D g2d, Node node) {
        if (node == null) return;

        int x = (int) node.viewX;
        int y = (int) node.viewY;

        if (node.getLeft() != null) {
            drawLine(g2d, x, y, (int) node.getLeft().viewX, (int) node.getLeft().viewY);
            drawNodeAnimated(g2d, node.getLeft());
        }
        if (node.getRight() != null) {
            drawLine(g2d, x, y, (int) node.getRight().viewX, (int) node.getRight().viewY);
            drawNodeAnimated(g2d, node.getRight());
        }

        drawNode(g2d, node, x, y);
    }

    private void animateNodes(Node node, boolean[] moving) {
        if (node == null) return;

        double dx = node.targetX - node.viewX;
        double dy = node.targetY - node.viewY;

        if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
            node.viewX += dx * 0.2;
            node.viewY += dy * 0.2;
            moving[0] = true;
        } else {
            node.viewX = node.targetX;
            node.viewY = node.targetY;
        }

        animateNodes(node.getLeft(),  moving);
        animateNodes(node.getRight(), moving);
    }

    private void animate() {
        Timer timer = new Timer(16, e -> {
            boolean[] moving = { false };
            animateNodes(controller.getRoot(), moving);
            repaint();
            if (!moving[0]) ((Timer) e.getSource()).stop();
        });
        timer.start();
    }

    public void startAnimation() {
        animate();
    }
}