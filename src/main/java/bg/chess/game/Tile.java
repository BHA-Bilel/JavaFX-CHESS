package bg.chess.game;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

public class Tile extends StackPane {

    private final int x, y;
    private final Region border;
    private Piece piece;
    private final GameApp gameApp;
    private final Background default_bg, gold_bg, silver_bg, red_bg;
    private PieceType default_type;
    private PieceColor default_color;

    public Tile(GameApp gameApp, boolean light, int x, int y) {
        this.x = x;
        this.y = y;
        this.gameApp = gameApp;
        default_bg = new Background(new BackgroundFill(Paint.valueOf(light ? "#feb" : "#582"), null, null));
        gold_bg = new Background(new BackgroundFill(Paint.valueOf("gold"), null, null));
        silver_bg = new Background(new BackgroundFill(Paint.valueOf("silver"), null, null));
        red_bg = new Background(new BackgroundFill(Paint.valueOf("red"), null, null));
        border = new Region();
        border.setBackground(default_bg);
        border.prefWidthProperty().bind(gameApp.heightProperty().divide(8));
        border.prefHeightProperty().bind(gameApp.heightProperty().divide(8));
        getChildren().add(border);
        setOnMousePressed(e -> {
            if (!gameApp.isPlayable() || !gameApp.isYourTurn()) {
                return;
            }
            if (gameApp.getSelectedTile() == null) {
                if (hasPiece() && gameApp.canIselect(getPiece())) {
                    gameApp.selectTile(this);
                    setSelected(true);
                }
            } else if (gameApp.getSelectedTile() == this) {
                gameApp.selectTile(null);
                setSelected(false);
            } else {
                if (hasPiece() && gameApp.canIselect(getPiece())) {
                    gameApp.getSelectedTile().setSelected(false);
                    gameApp.selectTile(null);
                    gameApp.selectTile(this);
                    setSelected(true);
                }
                if (!gameApp.isLegal(x, y))
                    return;
                play(getX(), getY());
            }
        });
    }

    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setup_defaults() {
        default_type = piece.getType();
        default_color = piece.getPieceColor();
    }

    public void setPiece(Piece piece) {
        if (piece != null) {
            getChildren().add(piece);
        } else if (hasPiece()) {
            getChildren().remove(this.piece);
        }
        this.piece = piece;
    }

    public void reset() {
        setPiece(null);
        if (default_type != null) {
            setPiece(new Piece(default_type, default_color, x, y));
        }
    }

    private void setSelected(boolean isSelected) {
        if (isSelected) {
            border.setBackground(gold_bg);
        } else {
            border.setBackground(default_bg);
        }
    }

    public void play(int x, int y) {
        Piece selectedPiece = gameApp.getSelectedTile().getPiece();
        gameApp.getSelectedTile().setSelected(false);
        boolean isAttackMove = gameApp.isAttackMove(x, y);
        if (isAttackMove) {
            gameApp.getBoard()[x][y].setPiece(null);
        }
        gameApp.getCSC().sendCoor(selectedPiece, x, y, isAttackMove);
        gameApp.getBoard()[x][y].setPiece(selectedPiece);
        gameApp.getBoard()[selectedPiece.getX()][selectedPiece.getY()].setPiece(null);
        selectedPiece.setX(x);
        selectedPiece.setY(y);
        gameApp.selectTile(null);
        if ((y == 0 || y == 7) && selectedPiece.getType() == PieceType.Pawn)
            selectedPiece.setType(PieceType.Queen);
        if (gameApp.NothingHappened()) {
            gameApp.waitForYourTurn();
        }
        gameApp.setYourTurn(false);
    }

    public void play(Piece selectedPiece, int x, int y, boolean isAttackMove) {
        if (isAttackMove) {
            gameApp.getBoard()[x][y].setPiece(null);
        }
        gameApp.getBoard()[x][y].setPiece(selectedPiece);
        gameApp.getBoard()[selectedPiece.getX()][selectedPiece.getY()].setPiece(null);
        selectedPiece.setX(x);
        selectedPiece.setY(y);
        if ((y == 0 || y == 7) && selectedPiece.getType() == PieceType.Pawn)
            selectedPiece.setType(PieceType.Queen);
        if (gameApp.NothingHappened()) {
            gameApp.setYourTurn(true);
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setHighlighted(MoveType type, boolean highlight) {
        if (highlight) {
            if (type == MoveType.Attack)
                border.setBackground(red_bg);
            else
                border.setBackground(silver_bg);
        } else {
            border.setBackground(default_bg);
        }
    }

}