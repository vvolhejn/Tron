/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Logic;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Vašek
 */
public class Player {

    static long speed = 20;
    int prevX;
    int prevY;
    int origX;
    int origY;
    int origRotation;
    int wins = 0;
    boolean alive = true;
    int id;
    int rotation; //0 je nahoru a  jde to po směru h. ručiček
    Color color;
    double x = 0;
    double y = 0;
    int upCode, rightCode, downCode, leftCode;
    boolean hasJustRotated = false;

    public Player(int id, Color color, int rotation, int x, int y,
            int upCode, int rightCode, int downCode, int leftCode) {

        this.rotation = rotation;
        this.x = x;
        this.y = y;
        origRotation = rotation;
        origX = x;
        origY = y;

        this.id = id;


        this.upCode = upCode;
        this.rightCode = rightCode;
        this.downCode = downCode;
        this.leftCode = leftCode;
    }

    public void move(long time) {
        prevX = (int) x;
        prevY = (int) y;

        //double moveCoef = time / speed;
        //System.out.println("Coef: "+moveCoef);
        hasJustRotated = false;
        
        switch (rotation) {
            case 0:
                y--;
                break;
            case 1:
                x++;
                break;
            case 2:
                y++;
                break;
            case 3:
                x--;
                break;
        }
        hasJustRotated = false;
    }

    public void rotate(int newRotation) {
        boolean stillRotate = true;
        if (newRotation == 0 && rotation == 2) {
            stillRotate = false;
        }
        if (newRotation == 1 && rotation == 3) {
            stillRotate = false;
        }
        if (newRotation == 2 && rotation == 0) {
            stillRotate = false;
        }
        if (newRotation == 3 && rotation == 1) {
            stillRotate = false;
        }
        if (stillRotate && !(hasJustRotated)) {
            rotation = newRotation;
        }
        hasJustRotated = true;
    }

    /*public void draw(Graphics g) {
    g.setColor(color);
    g.drawLine(x, y, prevX, prevY);
    }*/
    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int)y;
    }

    public int getPrevX() {
        return prevX;
    }

    public int getPrevY() {
        return prevY;
    }

    public void setX(int newX) {
        x = newX;
    }

    public void setY(int newY) {
        y = newY;
    }

    public int getId() {
        return id;
    }

    public Color getColor() {
        return color;
    }

    public boolean isAlive() {
        return alive;
    }

    public void kill() {
        alive = false;
    }

    public int getUpCode() {
        return upCode;
    }

    public int getRightCode() {
        return rightCode;
    }

    public int getDownCode() {
        return downCode;
    }

    public int getLeftCode() {
        return leftCode;
    }

    public void reset() {
        alive = true;
        x = origX;
        y = origY;
        rotation = origRotation;
    }

    public void addWin() {
        wins++;
    }

    public int getWins() {
        return wins;
    }
}
