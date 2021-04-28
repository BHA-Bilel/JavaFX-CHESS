package game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

public class GameApp extends GridPane {

    private int playerID;
    private final GameClient gameClient;

    public static final double TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    List<Tile> NormalMoves = new ArrayList<>();
    List<Tile> AttackMoves = new ArrayList<>();
    private final Tile[][] board = new Tile[WIDTH][HEIGHT];
    private final Handler handler;
    private boolean yourTurn, playable = false;
    private Tile selectedTile;
    private final String yourName, opName;
    public int parties_won, parties_lost;

    public GameApp(Socket gameSocket, String name, String opName) {
        this.yourName = name;
        this.opName = opName;
        gameClient = new GameClient(gameSocket);
        gameClient.handShake();
        handler = new Handler(this);
//        setPrefSize(WIDTH * TILE_SIZE, HEIGHT * TILE_SIZE);
        setAlignment(Pos.CENTER);
        getChildren().clear();
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile(handler, (x + y) % 2 == 0, x, y);
                GridPane.setHalignment(tile, HPos.CENTER);
                add(tile, x, y);
                board[x][y] = null;
                board[x][y] = tile;

                Piece piece = null;

                if (y == 0) {
                    if (x == 0 || x == 7) {
                        piece = new Piece(PieceType.Rook, PieceColor.BLACK, x, y);
                    } else if (x == 1 || x == 6) {
                        piece = new Piece(PieceType.Knight, PieceColor.BLACK, x, y);
                    } else if (x == 2 || x == 5) {
                        piece = new Piece(PieceType.Bishop, PieceColor.BLACK, x, y);
                    } else if (x == 3) {
                        piece = new Piece(PieceType.Queen, PieceColor.BLACK, x, y);
                    } else {
                        piece = new Piece(PieceType.King, PieceColor.BLACK, x, y);
                    }
                } else if (y == 7) {
                    if (x == 0 || x == 7) {
                        piece = new Piece(PieceType.Rook, PieceColor.WHITE, x, y);
                    } else if (x == 1 || x == 6) {
                        piece = new Piece(PieceType.Knight, PieceColor.WHITE, x, y);
                    } else if (x == 2 || x == 5) {
                        piece = new Piece(PieceType.Bishop, PieceColor.WHITE, x, y);
                    } else if (x == 3) {
                        piece = new Piece(PieceType.Queen, PieceColor.WHITE, x, y);
                    } else {
                        piece = new Piece(PieceType.King, PieceColor.WHITE, x, y);
                    }
                } else if (y == 1) {
                    piece = new Piece(PieceType.Pawn, PieceColor.BLACK, x, y);
                } else if (y == 6) {
                    piece = new Piece(PieceType.Pawn, PieceColor.WHITE, x, y);
                }

                if (piece != null) {
                    tile.setPiece(piece);
                }
            }
        }
    }

    public boolean isLegal(int x, int y) {
        return NormalMoves.contains(board[x][y]) || AttackMoves.contains(board[x][y]);
    }

    public boolean NothingHappened() {
        boolean blackKing = false, whiteKing = false;
        int x = 0;
        int y = 0;
        boolean complete = false;
        while (!complete && (!blackKing || !whiteKing)) {
            if (board[x][y].hasPiece()) {
                if (board[x][y].getPiece().getType() == PieceType.King) {
                    if (board[x][y].getPiece().getPieceColor() == PieceColor.BLACK) {
                        blackKing = true;
                    } else {
                        whiteKing = true;
                    }
                }
            }
            x++;
            if (x == WIDTH) {
                x = 0;
                y++;
                if (y == HEIGHT) {
                    complete = true;
                }
            }

        }
        boolean youWon = false;

        if (playerID == 1 && !whiteKing || playerID == 2 && !blackKing) {
            parties_won++;
            youWon = true;
            playable = false;
        } else if (playerID == 2 && !whiteKing || playerID == 1 && !blackKing) {
            parties_lost++;
            playable = false;
            youWon = false;
        }
        if (!playable) {
            startNewGame(youWon);
            showResults();
        }
        return playable;
    }

    private void startNewGame(boolean youWon) {
        Platform.runLater(() -> {
            getChildren().clear();
            for (int y = 0; y < HEIGHT; y++) {
                for (int x = 0; x < WIDTH; x++) {
                    Tile tile = new Tile(handler, (x + y) % 2 == 0, x, y);
                    GridPane.setHalignment(tile, HPos.CENTER);
                    add(tile, x, y);
                    board[x][y] = null;
                    board[x][y] = tile;

                    Piece piece = null;

                    if (y == 0) {
                        if (x == 0 || x == 7) {
                            piece = new Piece(PieceType.Rook, PieceColor.BLACK, x, y);
                        } else if (x == 1 || x == 6) {
                            piece = new Piece(PieceType.Knight, PieceColor.BLACK, x, y);
                        } else if (x == 2 || x == 5) {
                            piece = new Piece(PieceType.Bishop, PieceColor.BLACK, x, y);
                        } else if (x == 3) {
                            piece = new Piece(PieceType.Queen, PieceColor.BLACK, x, y);
                        } else {
                            piece = new Piece(PieceType.King, PieceColor.BLACK, x, y);
                        }
                    } else if (y == 7) {
                        if (x == 0 || x == 7) {
                            piece = new Piece(PieceType.Rook, PieceColor.WHITE, x, y);
                        } else if (x == 1 || x == 6) {
                            piece = new Piece(PieceType.Knight, PieceColor.WHITE, x, y);
                        } else if (x == 2 || x == 5) {
                            piece = new Piece(PieceType.Bishop, PieceColor.WHITE, x, y);
                        } else if (x == 3) {
                            piece = new Piece(PieceType.Queen, PieceColor.WHITE, x, y);
                        } else {
                            piece = new Piece(PieceType.King, PieceColor.WHITE, x, y);
                        }
                    } else if (y == 1) {
                        piece = new Piece(PieceType.Pawn, PieceColor.BLACK, x, y);
                    } else if (y == 6) {
                        piece = new Piece(PieceType.Pawn, PieceColor.WHITE, x, y);
                    }

                    if (piece != null) {
                        tile.setPiece(piece);
                    }
                }
            }
            if (!youWon) {
                waitForYourTurn();
            }
            setYourTurn(youWon);
            setPlayable(true);
        });
    }

    public void waitForYourTurn() {
        Thread t = new Thread(() -> {
            Object[] coor = gameClient.receive();
            int pieceX = (int) coor[0];
            int pieceY = (int) coor[1];
            Piece selectedPiece = board[pieceX][pieceY].getPiece();
            int toX = (int) coor[2];
            int toY = (int) coor[3];
            boolean isAttackMove = (boolean) coor[4];
            Platform.runLater(() -> board[pieceX][pieceY].play(selectedPiece, toX, toY, isAttackMove));

        });
        t.start();
    }

    public synchronized void closeGameApp() {
        gameClient.closeConn();
        Platform.runLater(() -> getChildren().clear());
    }

    public void showResults() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Game");
            alert.setHeaderText("Results");
            String text = yourName + " : " + parties_won + "\n";
            text += opName + " : " + parties_lost + "\n";
            alert.setContentText(text);
            alert.show();
        });
    }

    class GameClient {
        private Socket gameSocket;
        private DataInputStream dataIn;
        private DataOutputStream dataOut;

        public GameClient(Socket gameSocket) {
            try {
                this.gameSocket = gameSocket;
                dataIn = new DataInputStream(gameSocket.getInputStream());
                dataOut = new DataOutputStream(gameSocket.getOutputStream());
            } catch (IOException ignore) {
            }
        }

        public void closeConn() {
            try {
                dataOut.close();
                dataIn.close();
                gameSocket.close();
            } catch (IOException ignore) {
            }
        }

        public void handShake() {
            try {
                playerID = dataIn.readInt();
                if (playerID == 1) {
                    yourTurn = true;
                    Thread t = new Thread(() -> {
                        try {
                            playable = dataIn.readBoolean();
                        } catch (IOException ignore) {
                        }
                    });
                    t.start();
                } else {
                    yourTurn = false;
                    playable = true;
                    waitForYourTurn();
                }
            } catch (IOException ignore) {
            }
        }

        public void sendCoor(Piece selectedPiece, int x, int y, boolean isAttackMove) {
            try {
                dataOut.writeInt(selectedPiece.getX());
                dataOut.writeInt(selectedPiece.getY());
                dataOut.writeInt(x);
                dataOut.writeInt(y);
                dataOut.writeBoolean(isAttackMove);
                dataOut.flush();
                selectTile(null);
            } catch (IOException ignore) {
            }
        }

        public Object[] receive() {
            Object[] coor = new Object[5];
            try {
                coor[0] = dataIn.readInt();
                coor[1] = dataIn.readInt();
                coor[2] = dataIn.readInt();
                coor[3] = dataIn.readInt();
                coor[4] = dataIn.readBoolean();
            } catch (IOException ignore) {
            }
            return coor;
        }
    }

    public void selectTile(Tile selectedTile) {
        this.selectedTile = selectedTile;
        highlightPossibleMoves(selectedTile != null);
    }

    private void highlightPossibleMoves(boolean highlight) {
        if (highlight) {

            if (selectedTile.getPiece().getType() == PieceType.Pawn) {
                // FORWARD
                if (selectedTile.getPiece().getPieceColor() == PieceColor.WHITE) {
                    if (!board[selectedTile.getPiece().getX()][selectedTile.getPiece().getY() - 1].hasPiece()) {
                        NormalMoves.add(board[selectedTile.getPiece().getX()][selectedTile.getPiece().getY() - 1]);
                        if (selectedTile.getPiece().getY() == 6) {
                            // CAN MOVE TWO STEPS THE FIRST TIME
                            if (!board[selectedTile.getPiece().getX()][selectedTile.getPiece().getY() - 2].hasPiece()) {
                                NormalMoves
                                        .add(board[selectedTile.getPiece().getX()][selectedTile.getPiece().getY() - 2]);
                            }
                        }
                    }
                    int x = selectedTile.getPiece().getX() - 1;
                    int y = selectedTile.getPiece().getY() - 1;
                    if (x > -1 && y > -1 && board[x][y].hasPiece()
                            && selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                    x = selectedTile.getPiece().getX() + 1;
                    y = selectedTile.getPiece().getY() - 1;
                    if (x < 8 && y > -1 && board[x][y].hasPiece()
                            && selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                } else {
                    if (!board[selectedTile.getPiece().getX()][selectedTile.getPiece().getY() + 1].hasPiece()) {
                        NormalMoves.add(board[selectedTile.getPiece().getX()][selectedTile.getPiece().getY() + 1]);
                        if (selectedTile.getPiece().getY() == 1) {
                            if (!board[selectedTile.getPiece().getX()][selectedTile.getPiece().getY() + 2].hasPiece()) {
                                NormalMoves
                                        .add(board[selectedTile.getPiece().getX()][selectedTile.getPiece().getY() + 2]);
                            }
                        }
                    }
                    int x = selectedTile.getPiece().getX() + 1;
                    int y = selectedTile.getPiece().getY() + 1;
                    if (x < 8 && y < 8 && board[x][y].hasPiece()
                            && selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                    x = selectedTile.getPiece().getX() - 1;
                    y = selectedTile.getPiece().getY() + 1;
                    if (x > -1 && y < 8 && board[x][y].hasPiece()
                            && selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
            } else if (selectedTile.getPiece().getType() == PieceType.Rook) {
                // LEFT
                int x = selectedTile.getX() - 1;
                while (x > -1 && !board[x][selectedTile.getY()].hasPiece()) {
                    NormalMoves.add(board[x][selectedTile.getY()]);
                    x--;
                }
                if (x > -1 && board[x][selectedTile.getY()].hasPiece() && selectedTile.getPiece()
                        .getPieceColor() != board[x][selectedTile.getY()].getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][selectedTile.getY()]);
                }
                // RIGHT
                x = selectedTile.getX() + 1;
                while (x < 8 && !board[x][selectedTile.getY()].hasPiece()) {
                    NormalMoves.add(board[x][selectedTile.getY()]);
                    x++;
                }
                if (x < 8 && board[x][selectedTile.getY()].hasPiece() && selectedTile.getPiece()
                        .getPieceColor() != board[x][selectedTile.getY()].getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][selectedTile.getY()]);
                }
                // TOP
                int y = selectedTile.getY() - 1;
                while (y > -1 && !board[selectedTile.getX()][y].hasPiece()) {
                    NormalMoves.add(board[selectedTile.getX()][y]);
                    y--;
                }
                if (y > -1 && board[selectedTile.getX()][y].hasPiece() && selectedTile.getPiece()
                        .getPieceColor() != board[selectedTile.getX()][y].getPiece().getPieceColor()) {
                    AttackMoves.add(board[selectedTile.getX()][y]);
                }
                // BOTTOM
                y = selectedTile.getY() + 1;
                while (y < 8 && !board[selectedTile.getX()][y].hasPiece()) {
                    NormalMoves.add(board[selectedTile.getX()][y]);
                    y++;
                }
                if (y < 8 && board[selectedTile.getX()][y].hasPiece() && selectedTile.getPiece()
                        .getPieceColor() != board[selectedTile.getX()][y].getPiece().getPieceColor()) {
                    AttackMoves.add(board[selectedTile.getX()][y]);
                }

            } else if (selectedTile.getPiece().getType() == PieceType.Bishop) {
                // BOTTOM RIGHT
                int x = selectedTile.getX() + 1;
                int y = selectedTile.getY() + 1;
                while (x < 8 && y < 8 && !board[x][y].hasPiece()) {
                    NormalMoves.add(board[x][y]);
                    x++;
                    y++;
                }
                if (x < 8 && y < 8 && board[x][y].hasPiece()
                        && board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][y]);
                }
                // BOTTOM LEFT
                x = selectedTile.getX() - 1;
                y = selectedTile.getY() + 1;
                while (x > -1 && y < 8 && !board[x][y].hasPiece()) {
                    NormalMoves.add(board[x][y]);
                    x--;
                    y++;
                }
                if (x > -1 && y < 8 && board[x][y].hasPiece()
                        && board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][y]);
                }
                // TOP LEFT
                x = selectedTile.getX() - 1;
                y = selectedTile.getY() - 1;
                while (x > -1 && y > -1 && !board[x][y].hasPiece()) {
                    NormalMoves.add(board[x][y]);
                    x--;
                    y--;
                }
                if (x > -1 && y > -1 && board[x][y].hasPiece()
                        && board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][y]);
                }
                // TOP RIGHT
                x = selectedTile.getX() + 1;
                y = selectedTile.getY() - 1;
                while (y > -1 && x < 8 && !board[x][y].hasPiece()) {
                    NormalMoves.add(board[x][y]);
                    y--;
                    x++;
                }
                if (y > -1 && x < 8 && board[x][y].hasPiece()
                        && board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][y]);
                }
            } else if (selectedTile.getPiece().getType() == PieceType.Knight) {
                // TOP RIGHT
                int x = selectedTile.getX() + 1;
                int y = selectedTile.getY() - 2;
                if (x < 8 && y > -1) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
                x = selectedTile.getX() + 2;
                y = selectedTile.getY() - 1;
                if (x < 8 && y > -1) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
                // TOP LEFT
                x = selectedTile.getX() - 1;
                y = selectedTile.getY() - 2;
                if (x > -1 && y > -1) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
                x = selectedTile.getX() - 2;
                y = selectedTile.getY() - 1;
                if (x > -1 && y > -1) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
                // BOTTOM RIGHT
                x = selectedTile.getX() + 1;
                y = selectedTile.getY() + 2;
                if (x < 8 && y < 8) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
                x = selectedTile.getX() + 2;
                y = selectedTile.getY() + 1;
                if (x < 8 && y < 8) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
                // BOTTOM LEFT
                x = selectedTile.getX() - 1;
                y = selectedTile.getY() + 2;
                if (x > -1 && y < 8) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
                x = selectedTile.getX() - 2;
                y = selectedTile.getY() + 1;
                if (x > -1 && y < 8) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[x][y].getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }

            } else if (selectedTile.getPiece().getType() == PieceType.King) {
                // LEFT
                int x = selectedTile.getX() - 1;
                if (x > -1) {
                    if (!board[x][selectedTile.getY()].hasPiece()) {
                        NormalMoves.add(board[x][selectedTile.getY()]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[x][selectedTile.getY()].getPiece()
                            .getPieceColor()) {
                        AttackMoves.add(board[x][selectedTile.getY()]);
                    }
                }
                // RIGHT
                x = selectedTile.getX() + 1;
                if (x < 8) {
                    if (!board[x][selectedTile.getY()].hasPiece()) {
                        NormalMoves.add(board[x][selectedTile.getY()]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[x][selectedTile.getY()].getPiece()
                            .getPieceColor()) {
                        AttackMoves.add(board[x][selectedTile.getY()]);
                    }
                }
                // TOP
                int y = selectedTile.getY() - 1;
                if (y > -1) {
                    if (!board[selectedTile.getX()][y].hasPiece()) {
                        NormalMoves.add(board[selectedTile.getX()][y]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[selectedTile.getX()][y].getPiece()
                            .getPieceColor()) {
                        AttackMoves.add(board[selectedTile.getX()][y]);
                    }
                }
                // BOTTOM
                y = selectedTile.getY() + 1;
                if (y < 8) {
                    if (!board[selectedTile.getX()][y].hasPiece()) {
                        NormalMoves.add(board[selectedTile.getX()][y]);
                    } else if (selectedTile.getPiece().getPieceColor() != board[selectedTile.getX()][y].getPiece()
                            .getPieceColor()) {
                        AttackMoves.add(board[selectedTile.getX()][y]);
                    }
                }
                // BOTTOM RIGHT
                x = selectedTile.getX() + 1;
                y = selectedTile.getY() + 1;
                if (x < 8 && y < 8) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
                // BOTTOM LEFT
                x = selectedTile.getX() - 1;
                y = selectedTile.getY() + 1;
                if (x > -1 && y < 8) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
                // TOP LEFT
                x = selectedTile.getX() - 1;
                y = selectedTile.getY() - 1;
                if (x > -1 && y > -1) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
                // TOP RIGHT
                x = selectedTile.getX() + 1;
                y = selectedTile.getY() - 1;
                if (y > -1 && x < 8) {
                    if (!board[x][y].hasPiece()) {
                        NormalMoves.add(board[x][y]);
                    } else if (board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                        AttackMoves.add(board[x][y]);
                    }
                }
            } else if (selectedTile.getPiece().getType() == PieceType.Queen) {
                // LEFT
                int x = selectedTile.getX() - 1;
                while (x > -1 && !board[x][selectedTile.getY()].hasPiece()) {
                    NormalMoves.add(board[x][selectedTile.getY()]);
                    x--;
                }
                if (x > -1 && board[x][selectedTile.getY()].hasPiece() && board[x][selectedTile.getY()].getPiece()
                        .getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][selectedTile.getY()]);
                }
                // RIGHT
                x = selectedTile.getX() + 1;
                while (x < 8 && !board[x][selectedTile.getY()].hasPiece()) {
                    NormalMoves.add(board[x][selectedTile.getY()]);
                    x++;
                }
                if (x < 8 && board[x][selectedTile.getY()].hasPiece() && board[x][selectedTile.getY()].getPiece()
                        .getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][selectedTile.getY()]);
                }
                // TOP
                int y = selectedTile.getY() - 1;
                while (y > -1 && !board[selectedTile.getX()][y].hasPiece()) {
                    NormalMoves.add(board[selectedTile.getX()][y]);
                    y--;
                }
                if (y > -1 && board[selectedTile.getX()][y].hasPiece() && board[selectedTile.getX()][y].getPiece()
                        .getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[selectedTile.getX()][y]);
                }
                // BOTTOM
                y = selectedTile.getY() + 1;
                while (y < 8 && !board[selectedTile.getX()][y].hasPiece()) {
                    NormalMoves.add(board[selectedTile.getX()][y]);
                    y++;
                }
                if (y < 8 && board[selectedTile.getX()][y].hasPiece() && board[selectedTile.getX()][y].getPiece()
                        .getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[selectedTile.getX()][y]);
                }
                // BOTTOM RIGHT
                x = selectedTile.getX() + 1;
                y = selectedTile.getY() + 1;
                while (x < 8 && y < 8 && !board[x][y].hasPiece()) {
                    NormalMoves.add(board[x][y]);
                    x++;
                    y++;
                }
                if (x < 8 && y < 8 && board[x][y].hasPiece()
                        && board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][y]);
                }
                // BOTTOM LEFT
                x = selectedTile.getX() - 1;
                y = selectedTile.getY() + 1;
                while (x > -1 && y < 8 && !board[x][y].hasPiece()) {
                    NormalMoves.add(board[x][y]);
                    x--;
                    y++;
                }
                if (x > -1 && y < 8 && board[x][y].hasPiece()
                        && board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][y]);
                }
                // TOP LEFT
                x = selectedTile.getX() - 1;
                y = selectedTile.getY() - 1;
                while (x > -1 && y > -1 && !board[x][y].hasPiece()) {
                    NormalMoves.add(board[x][y]);
                    x--;
                    y--;
                }
                if (x > -1 && y > -1 && board[x][y].hasPiece()
                        && board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][y]);
                }
                // TOP RIGHT
                x = selectedTile.getX() + 1;
                y = selectedTile.getY() - 1;
                while (y > -1 && x < 8 && !board[x][y].hasPiece()) {
                    NormalMoves.add(board[x][y]);
                    y--;
                    x++;
                }
                if (y > -1 && x < 8 && board[x][y].hasPiece()
                        && board[x][y].getPiece().getPieceColor() != selectedTile.getPiece().getPieceColor()) {
                    AttackMoves.add(board[x][y]);
                }
            }

            for (Tile tile : NormalMoves) {
                tile.setHighlighted(MoveType.Normal, true);
            }
            for (Tile tile : AttackMoves) {
                tile.setHighlighted(MoveType.Attack, true);
            }
        } else {
            for (Tile tile : NormalMoves) {
                tile.setHighlighted(MoveType.Normal, false);
            }
            for (Tile tile : AttackMoves) {
                tile.setHighlighted(MoveType.Attack, false);
            }
            NormalMoves.clear();
            AttackMoves.clear();
        }
    }

    public boolean isAttackMove(int x, int y) {
        return AttackMoves.contains(board[x][y]);
    }

    // GETTERS SETTERS

    public Tile[][] getBoard() {
        return board;
    }

    public boolean isPlayable() {
        return playable;
    }

    public void setPlayable(boolean isPlayable) {
        this.playable = isPlayable;
    }

    public Tile getSelectedTile() {
        return selectedTile;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public GameClient getCSC() {
        return gameClient;
    }

    public boolean canIselect(Piece piece) {
        return (playerID == 1 && piece.getPieceColor() == PieceColor.BLACK
                || playerID == 2 && piece.getPieceColor() == PieceColor.WHITE);
    }
}
