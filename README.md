# Ranking de Jogadores - Binary-Search-Tree

Projeto desenvolvido para a disciplina de Resolução de Problemas Estruturados. Implementa uma **Árvore Binária de Busca (ABB)** para gerenciar um ranking de jogadores, com visualização gráfica interativa em Java Swing.

## 🖥️ Funcionalidades
 
- **Adicionar** jogador por nome e posição (ranking)
- **Buscar** jogador por nome ou por posição
- **Remover** jogador por nome ou por posição
- **Highlight** do nó encontrado na busca (destaque em amarelo por 8 segundos)
- **Zoom** com scroll do mouse ou botões `+` / `-`
- **Alternar** exibição entre nome e posição nos nós
---
 
## 🗂️ Estrutura do projeto
 
```
📦 projeto
 ┣ BinarySearchTree.java   # Implementação da ABB
 ┣ Node.java               # Nó da árvore (dados + estado visual)
 ┣ Player.java             # Modelo do jogador
 ┣ RankingController.java  # Camada de controle entre UI e ABB
 ┣ TreePanel.java          # Painel Swing de renderização da árvore
 ┣ TreeVisualizer.java     # Janela principal e barra de controles
 ┣ PlayerCsvLoader.java    # Carregamento do CSV
 ┗ players.csv             # Base de dados inicial
```
 
---
 
## 🧩 Estruturas de dados utilizadas
 
### Árvore Binária de Busca (ABB)
A estrutura principal do projeto. Cada jogador é um nó da árvore, ordenado pelo **ranking** (chave única). Isso garante:
- **Inserção:** O(log n) em média
- **Busca por ranking:** O(log n) — aproveita a ordenação da árvore
- **Busca por nome:** O(n) — percorre todos os nós, pois o nome não é o critério de ordenação
- **Remoção:** O(log n) em média
Rankigs duplicados não são permitidos — a posição funciona como identificador único de cada jogador, e tentativas de inserção com ranking já existente lançam uma exceção.
 
