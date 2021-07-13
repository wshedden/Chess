package chess;

import com.github.bhlangonijr.chesslib.move.*;

import com.github.bhlangonijr.chesslib.*;
import java.util.*;

public class Engine {
    PieceMap pieceMap = new PieceMap();

    public Engine() {

    }

    public Move getMove(String fen, boolean isWhite) {
        Node node = new Node();
        node.loadFromFen(fen);
        Node evalBoard = minimax(node.clone(), 3, isWhite);
        return evalBoard.move;
    }

    private Node minimax(Node node, int depth, boolean isMaximisingPlayer) {
        if (depth == 0 || node.isDraw() || node.isMated()) {
            node.score = getStaticScore(node); // TODO: Make this ref
            return node;
        }

        List<Move> legalMoves = node.legalMoves();

        if (isMaximisingPlayer) {
            node.score = -999f;
            for (int i = 0; i < legalMoves.size(); i++) {
                Node newNode = node.clone();
                newNode.doMove(legalMoves.get(i));
                float eval = minimax(newNode, depth - 1, false).score;
                if (eval > node.score) {
                    node.score = eval;
                    node.move = legalMoves.get(i);
                }
            }
            return node;

        } else {
            node.score = 999f;
            for (int i = 0; i < legalMoves.size(); i++) {
                Node newNode = node.clone();
                newNode.doMove(legalMoves.get(i));
                float eval = minimax(newNode, depth - 1, true).score;
                if (eval < node.score) {
                    node.score = eval;
                    node.move = legalMoves.get(i);
                }
            }
            return node;
        }
    }

    private float getStaticScore(Node board) {
        Side side = board.getSideToMove();
        if (board.isMated()) {
            if (side == Side.WHITE) {
                return -999f;
            } else {
                return 999f;
            }
        } else if (board.isDraw()) {
            return 0f;
        } else {
            return getPieceCount(board);
        }

    }

    private float getPieceCount(Node board) {
        String pieces = board.getFen().split(" ")[0].replace("/", "").replaceAll("\\d", "");
        float score = 0f;
        for (int i = 0; i < pieces.length(); i++) {
            score += pieceMap.getPiece(pieces.charAt(i));
        }
        return score;
    }

    private class PieceMap {
        HashMap<Character, Float> map;

        public PieceMap() {
            map = new HashMap<Character, Float>();
            map.put('p', -1f);
            map.put('n', -3.05f);
            map.put('b', -3.33f);
            map.put('r', -5.63f);
            map.put('q', -9.5f);
            map.put('k', 0f);
            map.put('P', 1f);
            map.put('N', 3.05f);
            map.put('B', 3.33f);
            map.put('R', 5.63f);
            map.put('Q', 9.5f);
            map.put('K', 0f);
        }

        public float getPiece(char character) {
            return map.get(character);
        }
    }

    private class Node extends Board {
        public float score;
        public Move move;

        public Node clone() {
            Node temp = new Node();
            temp.loadFromFen(getFen());
            return temp;
        }
    }
}
