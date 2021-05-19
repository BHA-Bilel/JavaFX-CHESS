package bg.chess.game;

import static bg.chess.game.GameApp.TILE_SIZE;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class Tile extends StackPane {

    private int x, y;
    private final Rectangle border;
    private Piece piece;
    private final Handler handler;
    private final Paint defaultFill;

    public boolean hasPiece() {
        return piece != null;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        if (piece != null) {
            getChildren().add(piece);
        } else if (hasPiece()) {
            getChildren().remove(this.piece);
        }
        this.piece = piece;
    }

    public Tile(Handler handler, boolean light, int x, int y) {
        this.x = x;
        this.y = y;
        border = new Rectangle(x * TILE_SIZE, y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        this.handler = handler;
        border.setWidth(TILE_SIZE);
        border.setHeight(TILE_SIZE);
        border.setFill(light ? Color.valueOf("#feb") : Color.valueOf("#582"));
        getChildren().add(border);
        defaultFill = light ? Color.valueOf("#feb") : Color.valueOf("#582");

        setOnMousePressed(e -> {
            if (!handler.getGame().isPlayable() || !handler.getGame().isYourTurn()) {
                return;
            }
            if (handler.getGame().getSelectedTile() == null) {
                if (hasPiece() && handler.getGame().canIselect(getPiece())) {
                    handler.getGame().selectTile(this);
                    setSelected(true);
                }
            } else if (handler.getGame().getSelectedTile() == this) {
                handler.getGame().selectTile(null);
                setSelected(false);
            } else {
                if (hasPiece() && handler.getGame().canIselect(getPiece())) {
                    handler.getGame().getSelectedTile().setSelected(false);
                    handler.getGame().selectTile(null);
                    handler.getGame().selectTile(this);
                    setSelected(true);
                }
                if (!handler.getGame().isLegal(x, y))
                    return;
                play(getX(), getY());
            }
        });
    }

    private void setSelected(boolean isSelected) {
        if (isSelected) {
            border.setFill(Color.GOLD);
        } else {
            border.setFill(defaultFill);
        }
    }

    public void play(int x, int y) {
        Piece selectedPiece = handler.getGame().getSelectedTile().getPiece();
        handler.getGame().getSelectedTile().setSelected(false);
        boolean isAttackMove = handler.getGame().isAttackMove(x, y);
        if (isAttackMove) {
            handler.getGame().getBoard()[x][y].setPiece(null);
        }
        handler.getGame().getCSC().sendCoor(selectedPiece, x, y, isAttackMove);
        handler.getGame().getBoard()[x][y].setPiece(selectedPiece);
        // handler.getGame().getBoard()[selectedPiece.getX()][selectedPiece.getY()].setSelected(false);
        handler.getGame().getBoard()[selectedPiece.getX()][selectedPiece.getY()].setPiece(null);
        selectedPiece.setX(x);
        selectedPiece.setY(y);
        handler.getGame().selectTile(null);
        if ((y == 0 || y == 7) && selectedPiece.getType() == PieceType.Pawn)
            selectedPiece.setType(PieceType.Queen);
        if (handler.getGame().NothingHappened()) {
            handler.getGame().waitForYourTurn();
        }
        handler.getGame().setYourTurn(false);
    }

    public void play(Piece selectedPiece, int x, int y, boolean isAttackMove) {
        if (isAttackMove) {
            handler.getGame().getBoard()[x][y].setPiece(null);
        }
        handler.getGame().getBoard()[x][y].setPiece(selectedPiece);
        handler.getGame().getBoard()[selectedPiece.getX()][selectedPiece.getY()].setPiece(null);
        selectedPiece.setX(x);
        selectedPiece.setY(y);
        if ((y == 0 || y == 7) && selectedPiece.getType() == PieceType.Pawn)
            selectedPiece.setType(PieceType.Queen);
        if (handler.getGame().NothingHappened()) {
            handler.getGame().setYourTurn(true);
        }
    }

    // GETTERS SETTERS

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setHighlighted(MoveType type, boolean highlight) {
        if (highlight) {
            if (type == MoveType.Attack)
                border.setFill(Color.RED);
            else
                border.setFill(Color.SILVER);
        } else {
            border.setFill(defaultFill);
        }
    }

}