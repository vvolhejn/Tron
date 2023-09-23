/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GamePanel.java
 *
 * Created on 18.3.2011, 18:32:35
 */
package GUI;

import Logic.Player;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.RepaintManager;

/**
 *
 * @author Vašek
 */
public class GamePanel extends javax.swing.JPanel {

    int winConditionPlayers = 1;
    static int maxPlayers = 4;
    int playerCount = 1;
    boolean repaintMid = false;
    String endgameText = "";
    boolean gameRunning = true;
    boolean gamePaused = true;
    boolean gameInit = true;
    int[][] map = new int[300][200];
    Player[] players = new Player[maxPlayers];
    RepaintManager manager = new RepaintManager();
    boolean paintBlack = false;

    public GamePanel() {
        initComponents();
        initMap();
        players[0] = new Player(2, Color.red, 3, 250, 100, //3, 250, 100
                KeyEvent.VK_UP, KeyEvent.VK_RIGHT, KeyEvent.VK_DOWN, KeyEvent.VK_LEFT);
        players[1] = new Player(3, Color.blue, 1, 50, 100,
                KeyEvent.VK_W, KeyEvent.VK_D, KeyEvent.VK_S, KeyEvent.VK_A);
        players[2] = new Player(4, Color.green, 0, 150, 175,
                KeyEvent.VK_T, KeyEvent.VK_H, KeyEvent.VK_G, KeyEvent.VK_F);
        players[3] = new Player(5, Color.yellow, 2, 150, 25,
                KeyEvent.VK_I, KeyEvent.VK_L, KeyEvent.VK_K, KeyEvent.VK_J);

        Timer grafikaTimer = new Timer();
        grafikaTimer.scheduleAtFixedRate(new Drawing(), 100, 20);
    }

    protected void handleKeyPress(KeyEvent evt) {
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyCode() == KeyEvent.VK_ESCAPE) {
            repaintMid = true;
            if (gamePaused && !(gameInit)) {
                gamePaused = false;
            } else {
                gamePaused = true;

            }
        }
        if (evt.getKeyCode() == KeyEvent.VK_C) {
            paintBlack = true;
        }
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!(gameRunning)) {
                for (int i = 0; i < players.length; i++) {
                    players[i].reset();
                }
                initPlayers();
                initMap();
                repaintMid = true;
                gameRunning = true;
                paintBlack = true;
            }

        }
        if (evt.getKeyCode() == KeyEvent.VK_R) {
            int n = JOptionPane.showConfirmDialog(this.getParent(),
                    "Reset score?", "Confirm", JOptionPane.YES_NO_OPTION);


        }
        for (int i = 0; i < players.length; i++) {
            if (evt.getKeyCode() == players[i].getUpCode()) {
                players[i].rotate(0);
            } else if (evt.getKeyCode() == players[i].getRightCode()) {
                players[i].rotate(1);
            } else if (evt.getKeyCode() == players[i].getDownCode()) {
                players[i].rotate(2);
            } else if (evt.getKeyCode() == players[i].getLeftCode()) {
                players[i].rotate(3);
            }
        }
    }

    protected void initGame(int playerCount, boolean isNewGame) {
        for (int i = maxPlayers - 1; i >= playerCount; i--) {
            players[i].kill();
        }
        if (playerCount == 1 && isNewGame) {
            winConditionPlayers = 0;
        }
        this.playerCount = playerCount;
        gameInit = false;
        gamePaused = false;
        repaintMid = true;
    }

    protected void initPlayers() {
        for (int i = maxPlayers - 1; i >= playerCount; i--) {
            players[i].kill();
        }
    }

    protected void move(long time) {
        if (gameRunning && !(gamePaused)) {
            for (int i = 0; i < players.length; i++) {
                checkLoss(i);
                map[players[i].getX()][players[i].getY()] = players[i].getId();
                if (players[i].isAlive()) {
                    players[i].move(time);
                }
            }
        }
        if (gameRunning) {
            checkEnd();
        }
    }

    private void checkLoss(int playerNo) {
        if (map[players[playerNo].getX()][players[playerNo].getY()] != 0
                && players[playerNo].isAlive()) {
            System.out.println("Player lost!");
            players[playerNo].kill();
        }
    }

    private void checkEnd() {
        boolean[] winners = new boolean[playerCount];
        int winnerCount = 0;
        boolean end = true;
        for (int i = 0; i < playerCount; i++) {
            if (players[i].isAlive()) {
                winners[i] = true;
                winnerCount++;
            } else {
                winners[i] = false;
            }
        }
        if (winnerCount <= winConditionPlayers) {
            initGame(playerCount, false);
            repaint();
            gameRunning = false;
            repaintMid = true;
            if (winnerCount == 1) {
                System.out.println("A player won.");
            } else if (winnerCount > 0) {
                endgameText = "Some players won!";
            } else {
                endgameText = "It's a draw!";
            }
        }
    }

    protected JComponent getThis() {
        return this;
    }

    private void initMap() {
        for (int i = 0; i < map.length; i++) {
            for (int i2 = 0; i2 < map[0].length; i2++) {
                map[i][i2] = 0;
            }
        }
        //Okraje
        for (int i = 0; i < map.length; i++) {
            map[i][0] = 1;
            map[i][map[0].length - 1] = 1;
        }
        for (int i = 0; i < map[0].length; i++) {
            map[0][i] = 1;
            map[map.length - 1][i] = 1;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (paintBlack) {
            g.setColor(Color.BLACK);
            g.fillRect(0, 0, 600, 400);
            paintBlack = false;
        }
        for (int i = 0; i < map.length; i++) {
            for (int i2 = 0; i2 < map[0].length; i2++) {
                switch (map[i][i2]) {
                    case 0:
                        g.setColor(Color.white);
                        break;
                    case 1:
                        g.setColor(Color.black);
                        break;
                    case 2:
                        g.setColor(Color.red);
                        break;
                    case 3:
                        g.setColor(Color.blue);
                        break;
                    case 4:
                        g.setColor(Color.green);
                        break;
                    case 5:
                        g.setColor(Color.yellow);
                        break;
                    case 6:
                        g.setColor(Color.black);
                        break;
                }
                g.fillRect(i * 2, i2 * 2, 2, 2);
            }
            String scoreText = "Paused: Enter or escape to resume";
            g.drawChars(scoreText.toCharArray(), 0, scoreText.length(), 5, 5);
        }
        /*for(int i = 0;i<playerCount;i++){
        players[playerCount].draw(g);
        }*/

        if (gamePaused && gameRunning) {
            String textToDraw;
            if (!(gameInit)) {
                textToDraw = "Paused: Enter or escape to resume";
            } else {
                textToDraw = "Select the number of players (2-4)";
            }
            g.drawChars(textToDraw.toCharArray(), 0, textToDraw.length(), 250, 200);
        }
        if (!gameRunning) {
            g.drawChars(endgameText.toCharArray(), 0, endgameText.length(), 250, 200);
            String playAgainText = "Press space to play again!";
            g.drawChars(playAgainText.toCharArray(), 0, playAgainText.length(), 225, 225);
        }
    }

    private class Drawing extends TimerTask {

        int currentPlayer = 0;
        long time;
        long newTime;
        public void run() {
            newTime = System.currentTimeMillis();
           // System.out.println(newTime - time);
            time = newTime;
            move(time);
            if (repaintMid == true) {
                repaint(250, 200, 350, 50);
                repaintMid = false;
            }
            currentPlayer++;
            if (currentPlayer == playerCount) {
                currentPlayer = 0;
            }
            //for (int i = 0; i < playerCount; i++) {
            repaint(players[currentPlayer].getPrevX() * 2 - 2,
                    players[currentPlayer].getPrevY() * 2 - 2, 6, 6);
            repaint(players[currentPlayer].getX() * 2 - 2,
                    players[currentPlayer].getY() * 2 - 2, 6, 6);
            //}
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(600, 400));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 600, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
