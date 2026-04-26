import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.JTextField;
import java.awt.Dimension;
import java.awt.FlowLayout;

public class TreeVisualizer {
    private final RankingController controller = new RankingController(new BinarySearchTree());
    private final PlayerCsvLoader csvLoader = new PlayerCsvLoader();
    private JFrame frame;
    private TreePanel treePanel;
    private JTextField nicknameField;
    private JTextField rankingField;
    private JButton toggleLabelButton;
    private boolean showRanking;

    public void start() {
        csvLoader.load("players.csv", controller);
        createWindow();
        renderTree();
    }

    private void createWindow() {
        frame = new JFrame("Visualizador ABB de Jogadores");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        treePanel = new TreePanel(controller);
        frame.setLayout(new BorderLayout());
        frame.add(createTopBar(), BorderLayout.NORTH);
        JScrollPane scrollPane = new JScrollPane(treePanel);
        scrollPane.getVerticalScrollBar().setUnitIncrement(18);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(18);
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        nicknameField = new JTextField(18);
        rankingField = new JTextField(8);
        JButton addButton = new JButton("Adicionar jogador");
        JButton searchButton = new JButton("Buscar");
        JButton removeButton = new JButton("Remover");
        JButton zoomInBtn = new JButton("+");
        JButton zoomOutBtn = new JButton("-");
        toggleLabelButton = new JButton("Mostrar posição");
        addButton.addActionListener(event -> addPlayerFromInputs());
        searchButton.addActionListener(event -> searchPlayerFromInput());
        removeButton.addActionListener(event -> removePlayerFromInput());
        toggleLabelButton.addActionListener(event -> toggleNodeLabels());
        zoomInBtn.addActionListener(e -> treePanel.zoomIn());
        zoomOutBtn.addActionListener(e -> treePanel.zoomOut());
        topBar.add(new JLabel("Nome:"));
        topBar.add(nicknameField);
        topBar.add(new JLabel("Posição:"));
        topBar.add(rankingField);
        topBar.add(addButton);
        topBar.add(searchButton);
        topBar.add(removeButton);
        topBar.add(toggleLabelButton);
        topBar.add(zoomInBtn);
        topBar.add(zoomOutBtn);
        return topBar;
    }

    private void toggleNodeLabels() {
        showRanking = !showRanking;
        toggleLabelButton.setText(showRanking ? "Mostrar nome" : "Mostrar posição");
        treePanel.setShowRanking(showRanking);
        redrawTree();
    }

    private void addPlayerFromInputs() {
        String nickname = nicknameField.getText().trim();
        String rankingText = rankingField.getText().trim();
        if (nickname.isEmpty() || rankingText.isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Informe nome e posição.");
            return;
        }
        try {
            int ranking = Integer.parseInt(rankingText);
            controller.addPlayer(nickname, ranking);
            nicknameField.setText("");
            rankingField.setText("");
            redrawTree();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Posição inválida.");
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(frame, e.getMessage());
        }
    }

    private void searchPlayerFromInput() {
        String nickname = nicknameField.getText().trim();
        String rankingText = rankingField.getText().trim();
        boolean found;
        Player find;
        if (!nickname.isEmpty()) {
            found = controller.searchByName(nickname, treePanel::repaint);
            find = controller.searchPlayer(nickname);
        } else if (!rankingText.isEmpty()) {
            try {
                int ranking = Integer.parseInt(rankingText);
                found = controller.searchByRanking(ranking, treePanel::repaint);
                find = controller.searchPlayer(ranking);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Posição inválida.");
                return;
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Informe nome ou posição para buscar.");
            return;
        }
        if (!found) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Jogador não encontrado");
        }
        JOptionPane.showMessageDialog(
                frame,
                "Jogador encontrado: " + find.getNickname() + " na posição " + find.getRanking()
        );
    }

    private void removePlayerFromInput() {
        String nickname = nicknameField.getText().trim();
        String rankingText = rankingField.getText().trim();
        Player removed;
        if (!nickname.isEmpty()) {
            removed = controller.removePlayer(nickname);
        } else if (!rankingText.isEmpty()) {
            try {
                int ranking = Integer.parseInt(rankingText);
                removed = controller.removePlayer(ranking);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "Posição inválida.");
                return;
            }
        } else {
            JOptionPane.showMessageDialog(frame, "Informe nome ou posição para remover.");
            return;
        }
        if (removed == null) {
            JOptionPane.showMessageDialog(frame, "Jogador não encontrado.");
            return;
        }
        redrawTree();
        JOptionPane.showMessageDialog(frame, "Removido: " + removed.getNickname() + " (" + removed.getRanking() + ")");
    }

    private void renderTree() {
        int panelWidth = controller.countNodes() * 90;
        int panelHeight = controller.getHeight() * 120;
        treePanel.setPreferredSize(new Dimension(panelWidth, panelHeight));
        treePanel.revalidate();
        treePanel.repaint();
        treePanel.startAnimation();
    }

    private void redrawTree() {
        SwingUtilities.invokeLater(this::renderTree);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TreeVisualizer().start());
    }
}
