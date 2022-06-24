package cz.kodytek.exploding.atoms.logic.ai;

import cz.kodytek.exploding.atoms.logic.ExplodingAtomsGame;
import cz.kodytek.exploding.atoms.logic.IExplodingAtomsGame;
import cz.kodytek.exploding.atoms.logic.models.Board;
import cz.kodytek.exploding.atoms.logic.models.Coordinate;
import cz.kodytek.exploding.atoms.logic.models.PlayerType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MyMiniMaxAI implements IExplodingAtomsAI {
    @Override
    public Coordinate makeMove(IExplodingAtomsGame currentGameState, PlayerType playerType) {
        ArrayList<Coordinate> possibleMoves = getAllPossibleMoves(currentGameState.getBoard(), playerType);
        List<AIMove> aiMoves = possibleMoves.stream().map(move -> new AIMove(minMax(play((ExplodingAtomsGame) currentGameState, move), 3, playerType.togglePlayer(), true), move)).toList();
        return aiMoves.stream().max(Comparator.comparingInt(AIMove::getStatus)).get().getCoords();
    }

    public int minMax(ExplodingAtomsGame game, int depth, PlayerType player, boolean isMax) {
        if(depth == 0) return status(game.getBoard());
        ArrayList<Coordinate> possibleMoves = getAllPossibleMoves(game.getBoard(), player);
        ArrayList<Integer> moveStatuses = new ArrayList<>();
        for (Coordinate possibleMove : possibleMoves) {
            ExplodingAtomsGame playedGame = play(game, possibleMove);
            moveStatuses.add(minMax(playedGame, depth-1, player.togglePlayer(), isMax));
        }
        return isMax ? moveStatuses.stream().max(Comparator.comparingInt(i -> i)).get() : moveStatuses.stream().max(Comparator.comparingInt(i -> i)).get();
    }

    public int status(Board board) {
        int count = 0;
        for (int i = 0; i < board.getRowCount(); i++) {
            for (int j = 0; j < board.getColumnCount(); j++) {
                if(board.getOrNull(i, j).getOwner() == PlayerType.Blue) count += board.getOrNull(i, j).getValue().value ;
                else count -= board.getOrNull(i, j).getValue().value;
            }
        }
        return count;
    }

    public ExplodingAtomsGame play(ExplodingAtomsGame game, Coordinate coords) {
        try {
            ExplodingAtomsGame playedGame = new ExplodingAtomsGame(game);
            playedGame.move(coords.getX(), coords.getY());
            return playedGame;
        } catch (Exception e) {
            System.out.println(game.getPlayerOnMove());
            System.out.println(game.getBoard().getOrNull(coords.getX(), coords.getY()).getOwner());
            System.out.println(coords.getX()+" "+coords.getY());
            e.printStackTrace();
            System.exit(100);
        }
        return game;
    }

    public ArrayList<Coordinate> getAllPossibleMoves(Board game, PlayerType player) {
        ArrayList<Coordinate> list = new ArrayList<>();
        for (int i = 0; i < game.getRowCount(); i++) {
            for (int j = 0; j < game.getColumnCount(); j++) {
                if(game.getOrNull(i, j) != null && !game.getOrNull(i, j).getOwner().equals(player.togglePlayer())) {
                    list.add(new Coordinate(i, j));
                }
            }
        }
        return list.size() > 10 ? new ArrayList<>(list.stream().sorted((i, j) -> (int) (Math.random() - 0.5) * 100).toList().subList(0, 10)) : list;
    }

    @Override
    public String getName() {
        return "MinimaxAI Martin";
    }
}
