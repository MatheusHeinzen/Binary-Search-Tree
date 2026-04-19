public class RankingController {
    private final BinarySearchTree bst;

    public RankingController(BinarySearchTree bst) {
        this.bst = bst;
    }

    public void addPlayer(String nickname, int ranking) {
        bst.insert(new Player(nickname, ranking));
    }

    public Player searchPlayer(String nickname) {
        return bst.searchPlayerByName(nickname);
    }

    public Player searchPlayer(int ranking) {
        return bst.searchPlayerByRanking(ranking);
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

}
