package server.ui;

import java.util.concurrent.ScheduledFuture;

import javafx.fxml.FXML;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.event.ActionEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import tools.GuiUtils;
import server.Start;
import server.ShutdownServer;
import server.Timer;
import handling.world.World;
import handling.login.LoginServer;
import tools.MaplePacketCreator;

public class MainController {

    private Thread server = null;

    protected static Thread t = null;
    private static ScheduledFuture<?> ts = null;
    private int minutesLeft = 2;

    @FXML
    private Button btnStartServer;
    @FXML
    private Button btnStopServer;
    @FXML
    private Button btnRestartServer;

    @FXML
    private Label lblStatus;

    private final BooleanProperty isServerRunning = new SimpleBooleanProperty(false);

    @FXML
    public void initialize() {
        btnStartServer.disableProperty().bind(isServerRunning);
        btnStopServer.disableProperty().bind(isServerRunning.not());
        btnRestartServer.disableProperty().bind(isServerRunning.not());
    }

    @FXML
    private void handleStartServer(ActionEvent event) {
        startServer();
    }

    @FXML
    private void handleStopServer(ActionEvent event) {
        shutdownServer();
    }

    @FXML
    private void handleRestartServer(ActionEvent event) {
        reStartServer();
    }

    private void startServer() {
        if (LoginServer.isShutdown() && server == null) {
            server = new Thread() {
                @Override
                public void run() {
                    Start.main(null);
                    Platform.runLater(() -> {
                        GuiUtils.showAlert(AlertType.INFORMATION, "伺服器啟動", "", "伺服端啟動完成。", false, null);
                        isServerRunning.set(true);
                    });
                }
            };
            server.start();
        } else {
            GuiUtils.showAlert(AlertType.INFORMATION, "伺服器啟動", "", "伺服器已經在運行中，請勿重複啟動。", false, null);
        }
    }

    private void shutdownServer() {
        if (ts == null && (t == null || !t.isAlive())) {
            t = new Thread(ShutdownServer.getInstance());
            ts = Timer.EventTimer.getInstance().register(new Runnable() {
                @Override
                public void run() {
                    if (minutesLeft == 0) {
                        //ShutdownServer.getInstance().run();
                        t.start();
                        ts.cancel(false);
                        server = null;
                        ts = null;
                        isServerRunning.set(false);
                        minutesLeft = 2;
                        return;
                    }
                    World.Broadcast.broadcastMessage(MaplePacketCreator.serverNotice("伺服器將在" + minutesLeft + " 分鐘後進行停機維護, 請及時安全的下線, 以免造成不必要的損失。").getBytes());
                    minutesLeft--;
                }
            }, 5000);
            GuiUtils.showAlert(AlertType.INFORMATION, "伺服器關閉", "", "伺服器將在" + minutesLeft + "分鐘後關閉", false, null);
        } else {
            GuiUtils.showAlert(AlertType.INFORMATION, "伺服器關閉", "", "關閉進程正在進行或者關閉已完成，請稍候。", false, null);
        }
    }

    private void reStartServer() {
        ButtonType rtn = GuiUtils.showAlert(AlertType.CONFIRMATION, "伺服器重新啟動", "", "確定要重新啟動伺服器嗎?", true, null);
        if (rtn == ButtonType.OK) {
            ShutdownServer.getInstance().run();
            server = null;
            startServer();
        }
    }
}