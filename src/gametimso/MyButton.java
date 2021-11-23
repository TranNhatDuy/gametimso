/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametimso;

import javax.swing.JButton;

/**
 *
 * @supporter anhdiepmmk
 */
public class MyButton extends JButton {

    /*Thuộc tính*/
    private int curRow, curCol;

    /**
     * Lấy giá trị dòng hiện tại mà Button đang ở
     * @return Trả về vị trí dòng hiện tại mà button đang ở
     */
    public int getCurRow() {
        return this.curRow;
    }
    
    public void setCurRow(int value){
        this.curRow = value;
    }

    /**
     * Lấy giá trị cột hiện tại mà Button đang ở
     * @return Trả về vị trí cột hiện tại mà button đang ở
     */
    public int getCurCol() {
        return this.curCol;
    }

    /**
     * Tạo mới nút với tọa độ [dòng , cột], nội dung
     * @param curRow Đang ở dòng trên ma trận
     * @param curCol Đang ở cột trên ma trận
     * @param text Nội dung hiển thị
     */
    public MyButton(int curRow, int curCol, String text) {
        super(text);
        this.curCol = curCol;
        this.curRow = curRow;
    }
}
