package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class view extends JFrame {
	private static int second;
	private static JLabel jltime;
	private static Timer timer;
	private static JButton jbstart;

	public view() {
		this.init();
	}

	private void init() {
		timer = new Timer();
		Board board = new Board();

		board.setEndgamelistener(new EndGameListener() {
			@Override
			public void end(String player, int st) {
				if (st == board.st_win) {
					JOptionPane.showMessageDialog(null, "người chơi " + board.currenplayer + " thắng");
					stopGame();
				} else if (st == board.st_draw) {
					JOptionPane.showMessageDialog(null, "2 người chơi hòa nhau");
					stopGame();
				}

			}
		});

		board.setPreferredSize(new Dimension(300, 300));

		JPanel jpbotom = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
		jpbotom.setBackground(Color.YELLOW);

		JPanel jpmain = new JPanel();
		BoxLayout boxLayout = new BoxLayout(jpmain, BoxLayout.Y_AXIS);
		jpmain.setLayout(boxLayout);
		jpmain.add(board);
		jpmain.add(jpbotom);

		jbstart = new JButton("Start");
		jpbotom.add(jbstart);
		jbstart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// Nếu mới mở chương trình
				jltime.setText("0:00");
				board.resetMatrix();
				repaint();
				if (jbstart.getText().equals("Start")) {
					startgame();
				} else {
					stopGame();
				}

			}
		});

		jltime = new JLabel("0:00");
		jpbotom.add(jltime);

		JFrame jframe = new JFrame("TicTacToe");
		jframe.add(jpmain);
		jframe.setSize(600, 600);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setResizable(true);
		jframe.setLocationRelativeTo(null);

		// hàm pack giúp tự co dãn
		jframe.pack();
		jframe.setVisible(true);
	}

	private void startgame() {

		int choice = JOptionPane.showConfirmDialog(null, "O đi trước?", "Ai đi trước", JOptionPane.YES_NO_OPTION);

		if (choice == 0) {
			Board.currenplayer = cell.O_value;
		} else {
			Board.currenplayer = cell.X_value;
		}

		second = 0;
		timer.cancel();
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				second++;
				// convert sang 00:00
				String value = (second / 60 + " : " + second % 60);
				jltime.setText(value);
			}
			// time delay lúc ban đầu, time delay giữa khoảng giây
		}, 1000, 1000);

		// khi nhấn nút start lần tiếp theo
		jbstart.setText("Stop");
	}

	private static void stopGame() {
		jbstart.setText("Start");

		second = 0;
		jltime.setText("0:0");
		timer.cancel();
		timer = new Timer();

		Board board = new Board();
		board.resetMatrix();
	}

}
