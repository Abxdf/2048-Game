/**
 * @author Narendra Vardi
 */
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
/*
IMPORTANT NOTICE IF YOU ARE TYRING TO UNDERSTAND THE CODE

- First understand how mergeleft and mergeright are working
- Why did I say that endup() and enddown() are same (try using transpose of a matrix property)
- Few bugs are there while checking individual endup(), endright(), endUp() and endBottom() , but the isend() works fine
- No advanced concepts are used. Basic swings are used. But, should keep track of function calls, in order to understand it properly.
- get to know about the JLabel and layout options in swings.
*/
/*
 * basically endUp() and endDown() are same
 * as long as we can transpose the matrix
 * endRight() and endLeft() are also same
 * create functions like endLeft() 
 * endRight() endUp() and endBottom()
 * 
 * finally its easy to do endGame()
 * up-left after transpose
 * down-right after transpose
 * 
 */
public class game2048 extends JFrame implements KeyListener {
	JLabel[][] label = new JLabel[4][4];//This is used to display the message
	int[][] value = new int[4][4];//the logical implementation is done with the help this 2-D array
	Random r = new Random(); 

	public int return2or4(boolean b) {
		/*we generate the random boolean if it is 1 return 2 to pop up else 4 to pop up new element after 
		a key event */
		if (b)
			return 2;
		else
			return 4;
	}

	public game2048() {
		//In this function the swing properties has been defined.
		//you are most welcome to play with these
		setTitle("Play 2048 Game");
		setSize(400, 400);
		setLayout(new GridLayout(4, 4));
		setVisible(true);
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				label[i][j] = new JLabel("", JLabel.CENTER);
				label[i][j].setFont(new Font("Avenir", Font.BOLD, 24));
				value[i][j] = 0;
				add(label[i][j]);
				label[i][j].setBorder(BorderFactory.createLineBorder(
						Color.GRAY, 1));//adding border to the JLabels

			}
		}
		addKeyListener(this);
		value[Math.abs(r.nextInt() % 4)][Math.abs(r.nextInt() % 4)] = return2or4(r
				.nextBoolean());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		display();

	}

	public String str(int a) {
		//return string value of an integer to display on the JLabel
		if (a != 0)
			return Integer.toString(a);
		else
			return "";
		
	}

	public void display() {
		//basic display function
		int i, j;
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 4; j++) {
				label[i][j].setText(str(value[i][j]));
			}
		}
	}

	public void keyPressed(KeyEvent ke) {
		//handling key events
		int key = ke.getKeyCode();
		switch (key) {
		case KeyEvent.VK_UP:
			checkSuccess();//checks if game is own 
			if (!isEnd())
				moveUp();
			else
				gameOver();
			break;
		case KeyEvent.VK_DOWN:
			checkSuccess();
			if (!isEnd())
				moveDown();
			else
				gameOver();
			break;
		case KeyEvent.VK_RIGHT:
			checkSuccess();
			if (!isEnd())
				moveRight();
			else
				gameOver();
			break;
		case KeyEvent.VK_LEFT:
			checkSuccess();
			if (!isEnd())
				moveLeft();
			else
				gameOver();
			break;

		}

	}

	public void gameOver() {
		//game is over when we are unable to merge any two elements and when there is no empty block 
		setTitle(":(");
		String str = "    GAMEOVER    ";
		for (int i = 0; i < str.length(); i++) {
			label[i / 4][i % 4].setText(str.charAt(i) + "");
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				System.out.print(value[i][j] + " ");

			}
			System.out.println();
		}

	}

	public void moveLeft() {
		// moving to left using queue
		
		
		if (endLeft())
			return;

		int i, j;
		for (i = 0; i < 4; i++) {
			int[] queue = new int[4];
			int front = 0;
			int end = -1;
			for (j = 0; j < 4; j++) {
				if (value[i][j] != 0) {
					queue[++end] = value[i][j];
					value[i][j] = 0;
				}
			}
			// System.out.print("Quw ");
			for (int k = 0; k <= end; k++) {
				// System.out.print(queue[k] + " ");
			}
			for (j = 0; j < 4; j++) {
				if (front <= end && value[i][j] == 0)
					value[i][j] = queue[front++];
			}
		}
		// end of moving towards left using queue
		// merging equal terms
		mergeLeft();
		// end of merging equal terms
		// new element generation
		generateNewElement();
		display();
		// end of new element generation
	}

	public void moveUp() {
		if (endUp())
			return;
		transpose();

		int i, j;

		for (i = 0; i < 4; i++) {
			int[] queue = new int[4];
			int front = 0;
			int end = -1;
			for (j = 0; j < 4; j++) {

				if (value[i][j] != 0) {

					queue[++end] = value[i][j];
					value[i][j] = 0;

				}

			}
			// System.out.print("Quw ");
			for (int k = 0; k <= end; k++) {
				// System.out.print(queue[k] + " ");

			}

			for (j = 0; j < 4; j++) {
				if (front <= end && value[i][j] == 0)
					value[i][j] = queue[front++];

			}
		}
		// end of moving towards left using queue
		// merging equal terms
		mergeLeft();

		// end of merging equal terms
		// new element generation
		generateNewElement();
		transpose();
		display();

	}

	public void moveRight() {
		if (endRight())
			return;
		int i, j;
		// moving to right
		for (i = 0; i < 4; i++) {
			int[] stack = new int[4];
			int top = -1;

			for (j = 0; j < 4; j++) {

				if (value[i][j] != 0) {

					stack[++top] = value[i][j];
					value[i][j] = 0;

				}

			}
			// System.out.print("\n");
			for (int k = top; k >= 0; k--) {
				// System.out.print(stack[k] + " ");

			}

			for (j = 3; j >= 0; j--) {
				if (top >= 0 && value[i][j] == 0)
					value[i][j] = stack[top--];

			}
		}
		// end of moving to right
		// merge code
		mergeRight();
		// end of merge
		// new element generation
		generateNewElement();

		// end of new element generation
		display();
	}

	public void moveDown() {
		if (endDown())
			return;
		transpose();// transposing the matrix so i can use the moveright
					// function details
		int i, j;
		// moving to right
		for (i = 0; i < 4; i++) {
			int[] stack = new int[4];
			int top = -1;

			for (j = 0; j < 4; j++) {

				if (value[i][j] != 0) {

					stack[++top] = value[i][j];
					value[i][j] = 0;
				}
			}
			// System.out.print("\n");
			for (int k = top; k >= 0; k--) {
				// System.out.print(stack[k] + " ");

			}

			for (j = 3; j >= 0; j--) {
				if (top >= 0 && value[i][j] == 0)
					value[i][j] = stack[top--];

			}
		}
		// end of moving to right
		// merge code
		mergeRight();
		// end of merge
		// new element generation
		generateNewElement();

		transpose();
		display();
	}

	public void mergeLeft() {
		//starts from left and merges when two beside elements are equal and shifts all elements to left when merging happens
		int i, j;
		for (i = 0; i < 4; i++) {

			for (j = 0; j < 3; j++) {

				if (value[i][j] == value[i][j + 1]) {
					value[i][j] += value[i][j + 1];
					int k = j + 1;
					while (k < 3) {
						value[i][k] = value[i][k + 1];
						k++;
					}
					value[i][3] = 0;
				}
			}
		}
	}

	public boolean endLeft() {
		//checking if we are unable to merge anything and check if no block is available to generate a new 2 or 4
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 0)

					return false;
			}
		}
		int i, j;
		for (i = 0; i < 4; i++) {

			for (j = 0; j < 3; j++) {

				if (value[i][j] == value[i][j + 1]) {
					return false;
				}

			}
		}
		return true;
		
	}

	public boolean endRight() {

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 0)

					return false;
			}
		}
		int i, j;
		for (i = 0; i < 4; i++) {

			for (j = 3; j > 0; j--) {

				if (value[i][j] == value[i][j - 1]) {
					return false;
				}

			}
		}
		return true;
	}

	public boolean endUp() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 0)

					return false;
			}
		}
		int i, j;
		for (i = 0; i < 3; i++) {
			for (j = 0; j < 4; j++) {
				if (value[i][j] == value[i + 1][j])
					return false;
			}

		}

		return true;

	}

	public boolean endDown() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 0)

					return false;
			}
		}
		for(int i=3;i>0;i--)
		{
			for(int j=0;j<4;j++)
			{
				if(value[i][j]==value[i-1][j])
					return false;
				
			}
		}
			
	return true;	
	}

	public void mergeRight() {
		int i, j;
		for (i = 0; i < 4; i++) {

			for (j = 3; j > 0; j--) {

				if (value[i][j] == value[i][j - 1]) {
					value[i][j] += value[i][j - 1];
					int k = j - 1;
					while (k > 0) {
						value[i][k] = value[i][k - 1];
						k--;
					}
					value[i][0] = 0;
				}
			}
		}
	}

	public void generateNewElement() {
		boolean iszeros = false;
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 0)
					iszeros = true;
				break;

			}

		}
		if (!iszeros)
			return;

		int indexi, indexj;
		indexi = Math.abs(r.nextInt() % 4);
		indexj = Math.abs(r.nextInt() % 4);
		while (value[indexi][indexj] != 0) {
			indexi = Math.abs(r.nextInt() % 4);
			indexj = Math.abs(r.nextInt() % 4);
		}
		value[indexi][indexj] = return2or4(r.nextBoolean());
	}

	public void transpose() {
		int[][] temp = new int[4][4];
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				temp[i][j] = value[i][j];
			}
		}
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				value[i][j] = temp[j][i];
			}
		}
	}

	public boolean isEnd() {
		return (endRight() && endDown() && endUp() && endLeft());
	}

	public void checkSuccess() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (value[i][j] == 2048) {
					setTitle("YOU won the game :)");
					break;
				}
			}
		}
	}

	public void keyReleased(KeyEvent ke) {

	}

	public void keyTyped(KeyEvent ke) {

	}

	public static void main(String args[]) {
		game2048 game = new game2048();

	}

}
