import javax.swing.*;

public class RankingController {
    private final BinarySearchTree bst;
    private Node highlightedNode = null;

    public RankingController(BinarySearchTree bst) {
        this.bst = bst;
    }

    public void addPlayer(String nickname, int ranking) {
        bst.insert(new Player(nickname, ranking));
    }

    public Player searchPlayer(String nickname) {
        return bst.searchPlayerByName(nickname);
    }

    public Player searchPlayer(int ranking) { return bst.searchPlayerByRanking(ranking); }

    public boolean searchByName(String nickname, Runnable onRepaint) {
        Node found = bst.findNodeByName(nickname);
        handleHighlight(found, onRepaint);
        return bst.search(nickname);
    }

    public boolean searchByRanking(int ranking, Runnable onRepaint) {
        Node found = bst.findNodeByRanking(ranking);
        handleHighlight(found, onRepaint);
        return bst.search(ranking);
    }

    private void handleHighlight(Node found, Runnable onRepaint) {
        if (highlightedNode != null) {
            highlightedNode.setHighlighted(false);
            highlightedNode = null;
        }
        if (found != null) {
            highlightedNode = found;
            highlightedNode.setHighlighted(true);
            onRepaint.run();

            Timer timer = new Timer(8000, e -> {
                if (highlightedNode != null) {
                    highlightedNode.setHighlighted(false);
                    highlightedNode = null;
                }
                onRepaint.run();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            onRepaint.run();
        }
    }

    public Player removePlayer(String nickname) {
        return bst.remove(nickname);
    }

    public Player removePlayer(int ranking) {
        return bst.removeByRanking(ranking);
    }

    public int getHeight() {
        return bst.getHeight();
    }

    public Node getRoot(){
        return bst.getRoot();
    }

    public int countNodes() { return bst.countNodes();   }

}
