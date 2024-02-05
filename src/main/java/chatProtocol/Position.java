package chatProtocol;

import java.io.Serializable;

public class Position implements Serializable {

    private int column;
    private int row;
    private String state;
    private int numW;

    public Position(int column, String state, int idW) {
        this.column = column;
        this.state = state;
        this.numW = idW;
    }
    public Position(int row, int column, String state) {
        this.column = column;
        this.row = row;
        this.state = state;
        this.numW = 0;
    }
    public Position() {
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public String getState() {
        return state;
    }

    public int getNumW() {
        return numW;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "Position{" +
                "column=" + column +
                ", row=" + row +
                ", state=" + state +
                '}';
    }
}
