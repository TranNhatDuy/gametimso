/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gametimso;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 *
 * @supporter win10
 */
public class frmMain extends javax.swing.JFrame implements ActionListener, KeyListener {

    /*Thuộc tính*/
    private MyButton[][] matrix;
    private int cols, rows, cellCount;
    private int yourMove;
    private boolean stop;
    private int second, minute;
    int score;

    /*Hàm*/
    /**
     * Vẽ bàn chơi là bảng gồm các nút
     *
     * @param rows Số dòng
     * @param cols Sốt cột
     */
    private void drawBoard(int rows, int cols) {
        this.matrix = new MyButton[rows][cols];
        this.jpanelBoard.setLayout(new GridLayout(rows, cols));
//        lblNumber.set
        this.rows = rows;
        this.cols = cols;
        this.cellCount = this.rows * this.cols;

        for (int i = 0; i < rows; ++i) {
            for (int j = 0; j < cols; ++j) {
                this.matrix[i][j] = new MyButton(i, j, "000");
                this.matrix[i][j].setForeground(Color.red);
                this.matrix[i][j].setFont(new Font("Times New Roman", 1, 18));
                this.matrix[i][j].addActionListener(this);//Sự kiện bấm chuôt
                this.matrix[i][j].addKeyListener(this);//Sự kiện bấm phím
                this.jpanelBoard.add(this.matrix[i][j]);
            }
        }

    }

    /**
     * Điền giá trị ngẫu nhiên vào mảng có sẵn
     */
    List<Integer> list = new ArrayList<>();
    List<Integer> list_luckyNumbers = new ArrayList<>();
    Random rand = new Random();
    int firstNum;
    int check_firstNum = 0;

    private void fillRandMatrix() {
        timer.stop();
        this.btnAutoPlay.setEnabled(true);
        this.lblTime.setText("00:00");
        this.minute = this.second = 0;
//        this.yourMove = 0;
        this.score = 0;
        this.stop = false;
        this.btnStart.setEnabled(true);
        this.btnCheat.setEnabled(false);

//        list = new ArrayList<>();
        for (int i = 1; i <= this.cellCount; ++i) {
            list.add(i);
        }
//        int firstNum = rand.nextInt(list.size());

        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.cols; ++j) {

                /*Kĩ thuật random không trùng*/
                int pos = rand.nextInt(list.size());
                this.matrix[i][j].setText(list.get(pos) + "");
                this.matrix[i][j].setEnabled(true);
                this.matrix[i][j].setBackground(Color.white);
                list.remove(pos);
                /**
                 * **************************
                 */
            }
        }
        lblDiem.setText("Score");
        lblNumber.setText("Find Number");
        if (this.thread != null) {
            this.thread.stop();
            this.thread = null;
        }
    }

    /**
     * Hàm hỗ trợ người chơi [Bôi vàng ô cần đánh]
     */
    private void findValue(int value, Color color) {
        for (int i = 0; i < this.rows; ++i) {
            for (int j = 0; j < this.cols; ++j) {
                if (this.matrix[i][j].getText().equals(value + "")) {
                    this.matrix[i][j].setBackground(color);
                    break;
                }
            }
        }
    }

    private void autoPlay() {

        this.stop = true;
        this.timer.start();
        if (yourMove > cellCount) {
            this.timer.stop();
            JOptionPane.showMessageDialog(null, "Chúc mừng Bạn đã hoàn thành màn chơi !!!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);

            this.fillRandMatrix();

            return;
        }

        findValue(this.yourMove++, Color.GREEN);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(frmMain.class.getName()).log(Level.SEVERE, null, ex);
        }

        autoPlay();
    }
    /**
     * Dùng timer để load thời gian
     */
    private Timer timer = new Timer(1000, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String time1 = second + "";
            String time2 = minute + "";
            if (time1.length() == 1) {
                time1 = "0" + time1;
            }
            if (time2.length() == 1) {
                time2 = "0" + time2;
            }
            if (second == 59) {
                lblTime.setText(time2 + ":" + time1);
                minute++;
                second = 0;
            } else {
                lblTime.setText(time2 + ":" + time1);
                second++;
            }
        }
    });

    /**
     * Kiểm tra giá trị truyền vào có bằng yourMove không, nếu bằng thì tô màu
     * cho nút có tọa độ [curRow : curCol]
     *
     * @param value Giá trị [từ 1 ~> 100]
     * @param curRow Vị trí dòng trên ma trận
     * @param curCol Vị trí cột trên ma trận
     */
    private void check(int value, int curRow, int curCol) {
        value = Integer.parseInt(lblNumber.getText());
        this.yourMove = Integer.parseInt(this.matrix[curRow][curCol].getText());
        if (value == this.yourMove) {
            
//            if (value != 1) {
//                this.matrix[this.lastCurRow][this.lastCurCol].setBackground(Color.gray);
//            }
            this.matrix[curRow][curCol].setBackground(Color.blue);
            this.matrix[curRow][curCol].setEnabled(false);

            this.lastCurRow = curRow;
            this.lastCurCol = curCol;
            if(list_luckyNumbers.contains(value)){
                System.out.println(yourMove + "là số may mắn");
            }
            
            int pos;
            if (list.size() == cellCount) {
                list.remove(firstNum);
                if(!list.isEmpty()){
                    pos = rand.nextInt(list.size());
                    System.out.println(list.size());
                    lblNumber.setText(list.get(pos).toString());
                    cellCount--;
                    firstNum = pos;
                }
                
                
            }
            score++;
            lblDiem.setText(String.valueOf(score));
        }

    }
    private int lastCurRow, lastCurCol;

    /**
     * Creates new form frmMain
     */
    public frmMain() {
        initComponents();
        this.drawBoard(3, 3);
        this.fillRandMatrix();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpanelBoard = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        btnReferesh = new javax.swing.JButton();
        lblTime = new javax.swing.JLabel();
        btnStart = new javax.swing.JButton();
        btnAutoPlay = new javax.swing.JButton();
        btnCheat = new javax.swing.JButton();
        lblDiem = new javax.swing.JLabel();
        lblNumber = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Nhanh Tay Nhanh Mắt");
        setResizable(false);

        jpanelBoard.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 51, 51)));

        javax.swing.GroupLayout jpanelBoardLayout = new javax.swing.GroupLayout(jpanelBoard);
        jpanelBoard.setLayout(jpanelBoardLayout);
        jpanelBoardLayout.setHorizontalGroup(
            jpanelBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 633, Short.MAX_VALUE)
        );
        jpanelBoardLayout.setVerticalGroup(
            jpanelBoardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 418, Short.MAX_VALUE)
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Control"));

        btnReferesh.setText("Refresh");
        btnReferesh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRefereshActionPerformed(evt);
            }
        });

        lblTime.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        lblTime.setText("time");

        btnStart.setText("Start");
        btnStart.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStartActionPerformed(evt);
            }
        });

        btnAutoPlay.setText("Auto play");
        btnAutoPlay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAutoPlayActionPerformed(evt);
            }
        });

        btnCheat.setText("Cheat");
        btnCheat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCheatActionPerformed(evt);
            }
        });

        lblDiem.setText("Score");

        lblNumber.setText("Find Number");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(67, 67, 67)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAutoPlay, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnCheat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnReferesh, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnStart, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(84, 84, 84)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDiem)
                            .addComponent(lblTime)
                            .addComponent(lblNumber))))
                .addContainerGap(64, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btnStart)
                .addGap(18, 18, 18)
                .addComponent(btnReferesh)
                .addGap(18, 18, 18)
                .addComponent(btnCheat)
                .addGap(18, 18, 18)
                .addComponent(btnAutoPlay)
                .addGap(30, 30, 30)
                .addComponent(lblNumber)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblTime)
                .addGap(29, 29, 29)
                .addComponent(lblDiem)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(17, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jpanelBoard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jpanelBoard, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnRefereshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRefereshActionPerformed
        // TODO add your handling code here:
        this.fillRandMatrix();
        lblNumber.setText("Find Number");
        lblDiem.setText("Score");
    }//GEN-LAST:event_btnRefereshActionPerformed

    private void btnCheatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCheatActionPerformed
        // TODO add your handling code here:
        int value = Integer.parseInt(lblNumber.getText());
        this.findValue(value, Color.yellow);
    }//GEN-LAST:event_btnCheatActionPerformed
    private Thread thread = null;

    private void btnAutoPlayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutoPlayActionPerformed
        // TODO add your handling code here:
        this.btnCheat.setEnabled(false);
        this.btnAutoPlay.setEnabled(false);
        this.btnStart.setEnabled(false);
        thread = new Thread() {
            @Override
            public void run() {
                autoPlay();
            }
        };
        thread.start();
    }//GEN-LAST:event_btnAutoPlayActionPerformed

    private void btnStartActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStartActionPerformed
        // TODO add your handling code here:
        this.stop = true;
        this.btnStart.setEnabled(false);
        this.btnCheat.setEnabled(true);
        score = 0;
        list_luckyNumbers.clear();
        for (int i = 1; i <= this.cellCount; i++) {
            list.add(i);

        }
        
        firstNum = rand.nextInt(list.size());
        
        lblNumber.setText(list.get(firstNum).toString());
        lblDiem.setText(String.valueOf(score));
        timer.start();
//        System.out.println("first_pos of list: " + list.get(firstNum).toString());
//
//        System.out.println("list size: " + String.valueOf(list.size()));
//        System.out.println("cell count: " + String.valueOf(cellCount));
////        list.remove(firstNum);
        
        //Tạo số may mắn
        int temp = cellCount/4;
        int i = rand.nextInt(temp);
        if(i == 0 || i == 1){
            i = 2;
        }
        System.out.println("gametimso.frmMain.btnStartActionPerformed()" + i);
        for(int j = 0; j < i; j++){
            list_luckyNumbers.add(rand.nextInt(list.size()));
        }
        
    }//GEN-LAST:event_btnStartActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmMain.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new frmMain().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAutoPlay;
    private javax.swing.JButton btnCheat;
    private javax.swing.JButton btnReferesh;
    private javax.swing.JButton btnStart;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jpanelBoard;
    private javax.swing.JLabel lblDiem;
    private javax.swing.JLabel lblNumber;
    private javax.swing.JLabel lblTime;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
        if (stop == false) {
            JOptionPane.showMessageDialog(null, "Bạn vui lòng bấm bắt đầu.", "Thông Báo", JOptionPane.OK_OPTION);
            return;
        }
        MyButton btn = (MyButton) e.getSource();
        int value = Integer.parseInt(btn.getText());
        int curRow = btn.getCurRow();
        int curCol = btn.getCurCol();

        this.check(value, curRow, curCol);

        if (list.isEmpty()) {
            timer.stop();
            JOptionPane.showMessageDialog(null, "Chúc mừng Bạn đã hoàn thành màn chơi !!!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            cellCount = this.rows * this.cols;
            this.fillRandMatrix();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void keyPressed(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");

        System.out.println("" + e.getKeyCode());

        int code = e.getKeyCode();

        switch (code) {
            case 72:/*Bấm vào chữ h ~> help*/
                if (stop == false) {
                    return;
                }
                this.findValue(this.yourMove, Color.yellow);
                break;
            case 82:/*Bấm vào chữ r ~> reset*/
                this.fillRandMatrix();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        //throw new UnsupportedOperationException("Not supported yet.");
    }
}
