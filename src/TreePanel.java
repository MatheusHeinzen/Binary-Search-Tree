import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class TreePanel extends JPanel {
    private final RankingController controller;
    private boolean showRanking;
    private double zoom = 1.0;
    private int nodeGapX = 80;
    private int nodeGapY = 100;
    private int currentX;
    private Map<Node, NodeView> nodeViews = new HashMap<>();

    public TreePanel(RankingController controller) {
        this.controller = controller;
        addMouseWheelListener(e -> {
            if (e.getPreciseWheelRotation() < 0) { zoomIn();
            } else { zoomOut(); }
        });
    }

    public void zoomIn() { zoom = Math.min(zoom * 1.1, 3.0); revalidate(); repaint();}
    public void zoomOut() { zoom = Math.max(zoom / 1.1, 0.3); revalidate(); repaint();}

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
        int leftX = drawTree(node.getLeft(), depth + 1);
        int myX = currentX;
        int myY = 50 + depth * nodeGapY;

        currentX += nodeGapX;
        int rightX = drawTree(node.getRight(), depth + 1);
        if (leftX != -1 && rightX != -1) {
            myX = (leftX + rightX) / 2;
        } else if (leftX != -1) {
            myX = leftX + nodeGapX / 2;
        } else if (rightX != -1) {
            myX = rightX - nodeGapX / 2;
        }

        int finalMyX = myX;
        NodeView view = nodeViews.computeIfAbsent(node, n -> new NodeView(finalMyX, myY));
        view.targetX = myX;
        view.targetY = myY;

        return myX;
    }

    private void drawNode(Graphics2D g2d, Node node, int x, int y) {
        String label = showRanking
                ? String.valueOf(node.getPlayer().getRanking())
                : node.getPlayer().getNickname();

        FontMetrics metrics = g2d.getFontMetrics();
        int textWidth = metrics.stringWidth(label);
        int paddingX = 10;
        int paddingY = 8;
        int nodeWidth = textWidth + paddingX * 2;
        int nodeHeight = metrics.getHeight() + paddingY;
        int arc = 10;
        int drawX = x - nodeWidth / 2;
        int drawY = y - nodeHeight / 2;

        g2d.setColor(Color.WHITE);
        g2d.fillRoundRect(drawX, drawY, nodeWidth, nodeHeight, arc, arc);
        g2d.setColor(Color.BLACK);
        g2d.drawRoundRect(drawX, drawY, nodeWidth, nodeHeight, arc, arc);
        int textX = x - textWidth / 2;
        int textY = y + (metrics.getAscent() - metrics.getDescent()) / 2;
        g2d.drawString(label, textX, textY);
    }

    private void drawLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d.drawLine(x1, y1 + 15, x2, y2 - 15);
    }

    private void animate() {
        Timer timer = new Timer(16, e -> {
            boolean moving = false;

            for (NodeView view : nodeViews.values()) {
                double dx = view.targetX - view.x;
                double dy = view.targetY - view.y;

                if (Math.abs(dx) > 1 || Math.abs(dy) > 1) {
                    view.x += dx * 0.2;
                    view.y += dy * 0.2;
                    moving = true;
                } else {
                    view.x = view.targetX;
                    view.y = view.targetY;
                }
            }

            repaint();
            if (!moving) {
                ((Timer) e.getSource()).stop();
            }
        });
        timer.start();
    }

    private void drawNodeAnimated(Graphics2D g2d, Node node) {
        if (node == null) return;

        NodeView view = nodeViews.get(node);
        if (view == null) return;
        int x = (int) view.x;
        int y = (int) view.y;

        if (node.getLeft() != null) {
            NodeView left = nodeViews.get(node.getLeft());
            if (left != null) {
                drawLine(g2d, x, y, (int) left.x, (int) left.y);
                drawNodeAnimated(g2d, node.getLeft());
            }
        }

        if (node.getRight() != null) {
            NodeView right = nodeViews.get(node.getRight());
            if (right != null) {
                drawLine(g2d, x, y, (int) right.x, (int) right.y);
                drawNodeAnimated(g2d, node.getRight());
            }
        }
        drawNode(g2d, node, x, y);
    }

    public void startAnimation() {
        animate();
    }
}
