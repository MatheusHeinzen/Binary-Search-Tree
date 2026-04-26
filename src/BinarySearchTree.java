public class BinarySearchTree {
    private Node root;

    public void insert(Player player) {
        root = insertRecursive(root, player);
    }

    private Node insertRecursive(Node current, Player player) {
        if (current == null) {
            return new Node(player);
        }
        if (player.getRanking() < current.getPlayer().getRanking()) {
            current.setLeft(insertRecursive(current.getLeft(), player));
        } else if (player.getRanking() > current.getPlayer().getRanking()) {
            current.setRight(insertRecursive(current.getRight(), player));
        } else {
            throw new IllegalArgumentException("Ranking duplicado");
        }
        return current;
    }

    public Node getRoot() {
        return root;
    }

    public boolean search(String name) {
        return searchNodeByName(root, name) != null;
    }
    public boolean search(int ranking) {
        return searchNodeByRanking(root, ranking) != null;
    }

    public Player searchPlayerByName(String name) {
        Node foundNode = searchNodeByName(root, name);
        return foundNode == null ? null : foundNode.getPlayer();
    }

    public Player searchPlayerByRanking(int ranking) {
        Node foundNode = searchNodeByRanking(root, ranking);
        return foundNode == null ? null : foundNode.getPlayer();
    }

    private Node searchNodeByName(Node current, String name) {
        if (current == null) {
            return null;
        }
        if (current.getPlayer().getNickname().equals(name)) {
            return current;
        }
        Node leftFound =  searchNodeByName(current.getLeft(), name);
        if  (leftFound != null) {
            return leftFound;
        }
        return searchNodeByName(current.getRight(), name);
    }

    private Node searchNodeByRanking(Node current, int ranking) {
        if (current == null) {
            return null;
        }
        if (ranking < current.getPlayer().getRanking()) {
            return  searchNodeByRanking(current.getLeft(), ranking);
        }
        if (ranking > current.getPlayer().getRanking()) {
            return  searchNodeByRanking(current.getRight(), ranking);
        }
        return current;
    }

    public Node findNodeByName(String name) {
        return searchNodeByName(root, name);
    }

    public Node findNodeByRanking(int ranking) {
        return searchNodeByRanking(root, ranking);
    }

    public Player remove(String name) {
        Node nodeToRemove = searchNodeByName(root, name);
        if (nodeToRemove == null) {
            return null;
        }
        Player removed =  nodeToRemove.getPlayer();
        root = remove(root, name);
        return removed;
    }

    public Player removeByRanking(int ranking) {
        Node nodeToRemove = searchNodeByRanking(root, ranking);
        if (nodeToRemove == null) {
            return null;
        }
        Player removed =  nodeToRemove.getPlayer();
        root = removeByRankingRecursive(root, ranking);
        return removed;
    }

    private Node remove(Node current, String name) {
        if (current == null) {
            return null;
        }
        if (current.getPlayer().getNickname().equals(name)) {
            return removeNode(current);
        }
        current.setLeft(remove(current.getLeft(), name));
        current.setRight(remove(current.getRight(), name));
        return current;
    }

    private Node removeNode(Node current) {
        if (current.getLeft() == null || current.getRight() == null) {
            return removeNodeWithZeroOrOneChild(current);
        }
        return removeNodeWithTwoChildren(current);
    }

    private Node removeNodeWithZeroOrOneChild(Node current) {
        if (current.getLeft() == null) {
            return current.getRight();
        }
        return current.getLeft();
    }

    private Node removeNodeWithTwoChildren(Node current) {
        Node sucessor = findMin(current.getRight());
        current.setPlayer(sucessor.getPlayer());
        current.setRight(removeByRankingRecursive(current.getRight(), sucessor.getPlayer().getRanking()));
        return current;
    }

    private Node removeByRankingRecursive(Node current, int ranking) {
        if (current == null) {
            return null;
        }
        if (ranking < current.getPlayer().getRanking()) {
            current.setLeft(removeByRankingRecursive(current.getLeft(), ranking));
            return current;
        }
        if (ranking > current.getPlayer().getRanking()) {
            current.setRight(removeByRankingRecursive(current.getRight(), ranking));
            return current;
        }
        return removeNode(current);
    }

    private Node findMin(Node current) {
        if (current.getLeft() == null) {
            return current;
        }
        return findMin(current.getLeft());
    }

    public void inOrder(){
        inOrder(root);
        System.out.println();
    }

    private void inOrder(Node current){
        if (current == null) {
            return;
        }
        inOrder(current.getLeft());

    }

    public int getHeight(){
        return getHeight(root);
    }

    private int getHeight(Node current){
        if (current == null) {
            return 0;
        }
        return 1 + Math.max(getHeight(current.getLeft()),getHeight(current.getRight()));
    }

    public int countNodes() {
        return countNodesRecursive(root);
    }

    private int countNodesRecursive(Node node) {
        if (node == null) {
            return 0;
        }
        return 1
                + countNodesRecursive(node.getLeft())
                + countNodesRecursive(node.getRight());
    }
}
