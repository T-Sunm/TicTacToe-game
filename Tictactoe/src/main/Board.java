package main;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JPanel;

public class Board extends JPanel {
	private static final int N = 3;
	private static final int M = 3;
	
	public static final int st_draw = 0;
	public static final int st_win = 1;
	public static final int st_normal = 2;
	private EndGameListener endgamelistener;
	
	private Image imgX;
	private Image imgO;

	private cell[][] matrix = new cell[N][M];
	// mặc định là O đánh trước

	public static String currenplayer = cell.Empty_value;

	public Board() {
		this.initMatrix();
		
		addMouseListener(new MouseAdapter() {
			@Override

			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mousePressed(e);
				// lấy tọa độ chuột lúc click
				int clickx = e.getX();
				int clicky = e.getY();
				
				//khi mà chưa nhấn start thì kh được đánh x,o
				if(currenplayer.equals(cell.Empty_value))return;

				// phát ra âm thanh luc click
				soundClick();
				// Tính toán x,y rơi vào ô nào trong board , sau đó vẽ hình x,o tùy ý
				// chạy for để lấy tọa độ tất cả của 9 ô
				for (int i = 0; i < N; i++) {
					for (int j = 0; j < M; j++) {
						cell c = matrix[i][j];

						int cXStart = c.getX();
						int cYStart = c.getY();
						int cXEnd = cXStart + c.getW();
						int cYEnd = cYStart + c.getH();
						// xem lúc click thì tọa độ x,y ở vị trí trong ô nào
						if (clickx >= cXStart && clickx <= cXEnd) {
							if (clicky >= cYStart && clicky <= cYEnd) {
								System.out.println("Click vào: i =" + i + "và j =" + j);
								
								if (c.getValue().equals(c.Empty_value)) {
									c.setValue(currenplayer);
									// khi vẽ xong 1 hình thì yêu cầu hàm paint vẽ lại
									repaint();
									int result = checkWin(currenplayer);
									if(endgamelistener != null) {
										endgamelistener.end(currenplayer, result);
									}
									
									if(result == st_normal) {
										// xét nếu người trước đánh O thì tiếp theo đánh X và ngược lại
									currenplayer = currenplayer.equals(c.O_value) ? cell.X_value : cell.O_value;
									}
									
									

								}
							}
						}
					}
				}
			}
		});
		try {
			imgX = ImageIO.read(getClass().getResource("X.png"));
			imgO = ImageIO.read(getClass().getResource("O.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	public synchronized void soundClick() {
		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Clip clip = AudioSystem.getClip();
					try {
						AudioInputStream audioinputstream = AudioSystem
								.getAudioInputStream(getClass().getResource("click.wav"));
						clip.open(audioinputstream);
						clip.start();
					} catch (UnsupportedAudioFileException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				} catch (LineUnavailableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
		thread.start();

	}

	// hàm khởi tạo ma trận
	private void initMatrix() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				cell cell = new cell();
				matrix[i][j] = cell;
				System.out.println("i: " + i + " j: " + j);
			}
			System.out.println("");
		}
	}
	
	public void resetMatrix(){
		this.initMatrix();
		Board.currenplayer = cell.Empty_value;
		repaint();
	}
	// 0.không ai thắng,1.có người thắng
	//chưa có ai thắng => còn nước thì đánh tiếp
	public int checkWin(String player) {
		//đường chéo thứ nhất
		if(this.matrix[0][0].getValue().equals(player) && this.matrix[1][1].getValue().equals(player) && this.matrix[2][2].getValue().equals(player)) {
			return st_win;
		}
		//đường chéo thứ 2
		else if(this.matrix[0][2].getValue().equals(player)&&this.matrix[1][1].getValue().equals(player)&&this.matrix[2][0].getValue().equals(player)) {
			return st_win;
		}
		//dòng thứ 1
		else if(this.matrix[0][0].getValue().equals(player)&&this.matrix[0][1].getValue().equals(player)&&this.matrix[0][2].getValue().equals(player)) {
			return st_win;
		}
		//dòng thứ 2
		else if(this.matrix[1][0].getValue().equals(player)&&this.matrix[1][1].getValue().equals(player)&&this.matrix[1][2].getValue().equals(player)) {
			return st_win;
		}
		//dòng thứ 3
		else if(this.matrix[2][0].getValue().equals(player)&&this.matrix[2][1].getValue().equals(player)&&this.matrix[2][2].getValue().equals(player)) {
			return st_win;
		}
		//cột thứ 1
		else if(this.matrix[0][0].getValue().equals(player)&&this.matrix[1][0].getValue().equals(player)&&this.matrix[2][0].getValue().equals(player)) {
			return st_win;
		}
		//cột thứ 2
		else if(this.matrix[0][1].getValue().equals(player)&&this.matrix[1][1].getValue().equals(player)&&this.matrix[2][1].getValue().equals(player)) {
			return st_win;
		}
		//cột thứ 3
		else if(this.matrix[0][2].getValue().equals(player)&&this.matrix[1][2].getValue().equals(player)&&this.matrix[2][2].getValue().equals(player)) {
			return st_win;
		}
		// hòa
		if(this.isFull()) {
			return st_draw;
		}
		// chưa đánh hết
		return st_normal;
	}
	
	//kiểm tra đánh full 9 nước chưa
	private boolean isFull() {
		int Number = N	* M;
		int k = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				cell c = matrix[i][j];
				if(c.equals(cell.Empty_value)) {
					k++;
				}
			}
		}
		// nếu đánh full 9 ô
		return k == Number;
	}

	@Override
	public void paint(Graphics g) {
		// vì cần 3 hàng và 3 cột ô nên mỗi ô bằng chiều rộng /3 và chiều cao chia 3
		int w = getWidth() / 3;
		int height = getHeight() / 3;
		Graphics2D graphic2d = (Graphics2D) g;

		// vẽ board
		int k = 0;
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				// x là trục ngang nên phải nhân vs chiều dài
				int x = j * w;
				// y là trục dọc nên phải nhân vs chiều rộng
				int y = i * height;

				// Lợi dụng vị trí của ma trận để lấy x,y,w,h
				// chạy for hết ma trận để lấy x,y,w,h từng ô , cứ mỗi ô là 1 cell
				cell cell = matrix[i][j];
				cell.setY(y);
				cell.setX(x);
				cell.setW(w);
				cell.setH(height);

				// nếu k chẵn thì in ra blue không thì in ra red
				Color color = k % 2 == 0 ? Color.CYAN : Color.PINK;
				graphic2d.setColor(color);
				graphic2d.fillRect(x, y, w, height);

				if (cell.getValue().equals(cell.X_value)) {
					Image img = imgX;
					graphic2d.drawImage(img, x, y, w, height, this);
				} else if (cell.getValue().equals(cell.O_value)) {
					Image img = imgO;
					graphic2d.drawImage(img, x, y, w, height, this);
				}

				k++;
			}
		}
	}

	public EndGameListener getEndgamelistener() {
		return endgamelistener;
	}

	public void setEndgamelistener(EndGameListener endgamelistener) {
		this.endgamelistener = endgamelistener;
	}
	
}
