public class Node {
    private Player player;
    private Node left;
    private Node right;
    private boolean highlighted = false;

    double viewX, viewY;
    double targetX, targetY;
    boolean viewInitialized = false;

    public Node(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Node getLeft() {
        return left;
    }

    public void setLeft(Node left) {
        this.left = left;
    }

    public Node getRight() {
        return right;
    }

    public void setRight(Node right) {
        this.right = right;
    }

    public boolean isHighlighted() { return highlighted; }

    public void setHighlighted(boolean highlighted) { this.highlighted = highlighted; }

}
