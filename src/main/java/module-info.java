module MainModule {
    requires javafx.controls;
    requires com.jfoenix;
    requires org.controlsfx.controls;
    requires java.desktop;

    exports shared;
    exports bg.chess.server.local;
    exports bg.chess.server.room;
    exports bg.chess.lang;
    exports bg.chess.room;
    exports bg.chess.game;
    exports bg.chess;
}