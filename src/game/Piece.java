package game;

import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Piece extends StackPane {

    private int x, y;
    private PieceType type;
    private final PieceColor color;
    private final Text text;

    public Piece(PieceType type, PieceColor color, int x, int y) {
        this.type = type;
        this.color = color;
        this.x = x;
        this.y = y;
        text = new Text();
        text.setFont(Font.font(50));

        text.setText(type.toString());
        if (type == PieceType.Pawn) {
            if (color == PieceColor.WHITE)
                text.setText("\u2659");
            else
                text.setText("\u265F");
        } else if (type == PieceType.Rook) {
            if (color == PieceColor.WHITE)
                text.setText("\u2656");
            else
                text.setText("\u265C");
        } else if (type == PieceType.Bishop) {
            if (color == PieceColor.WHITE)
                text.setText("\u2657");
            else
                text.setText("\u265D");
        } else if (type == PieceType.Knight) {
            if (color == PieceColor.WHITE)
                text.setText("\u2658");
            else
                text.setText("\u265E");

        } else if (type == PieceType.Queen) {
            if (color == PieceColor.WHITE)
                text.setText("\u2655");
            else
                text.setText("\u265B");
        } else if (type == PieceType.King) {
            if (color == PieceColor.WHITE)
                text.setText("\u2654");
            else
                text.setText("\u265A");
        }

        getChildren().addAll(text);

    }

    // GETTERS SETTERS

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public PieceType getType() {
        return type;
    }

    public void setType(PieceType type) {
        this.type = type;
        if (type == PieceType.Queen) {
            if (color == PieceColor.WHITE)
                text.setText("\u2655");
            else
                text.setText("\u265B");
        }
    }

    public PieceColor getPieceColor() {
        return color;
    }

}
