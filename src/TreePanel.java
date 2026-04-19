import javax.swing.JPanel;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

public class TreePanel extends JPanel {
    private final RankingController controller;
    private boolean showRanking;

    public TreePanel(RankingController controller) {
        this.controller = controller;
    }

    public void setShowRanking(boolean showRanking) {
        this.showRanking = showRanking;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawTree(g2d, getWidth(), getHeight());
        g2d.dispose();
    }

    private void drawTree(Graphics2D g2d, int width, int height) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);
        drawNode(g2d, controller.getRoot(), width / 2.0, 45, width / 4.0);
    }

    private void drawNode(Graphics2D g2d, Node node, double x, double y, double xOffset) {
        if (node == null) {
            return;
        }
        String label = showRanking ? String.valueOf(node.getPlayer().getRanking()) : node.getPlayer().getNickname();
        FontMetrics metrics = g2d.getFontMetrics();
        int textWidth = metrics.stringWidth(label);
        int nodeWidth = Math.max(58, textWidth + 24);
        int nodeHeight = 34;
        int halfW = nodeWidth / 2;
        int halfH = nodeHeight / 2;
        if (node.getLeft() != null) {
            double childX = x - xOffset;
            double childY = y + 110;
            g2d.drawLine((int) x, (int) (y + halfH), (int) childX, (int) (childY - (nodeHeight / 2)));
            drawNode(g2d, node.getLeft(), childX, childY, xOffset / 2);
        }
        if (node.getRight() != null) {
            double childX = x + xOffset;
            double childY = y + 110;
            g2d.drawLine((int) x, (int) (y + halfH), (int) childX, (int) (childY - (nodeHeight / 2)));
            drawNode(g2d, node.getRight(), childX, childY, xOffset / 2);
        }
        g2d.drawOval((int) (x - halfW), (int) (y - halfH), nodeWidth, nodeHeight);
        int textX = (int) (x - (textWidth / 2.0));
        int textY = (int) (y + (metrics.getAscent() - metrics.getDescent()) / 2.0);
        g2d.drawString(label, textX, textY);
    }
}
