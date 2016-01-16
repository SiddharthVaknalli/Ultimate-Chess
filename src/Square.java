import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;

import javax.swing.ImageIcon;

/**
 * The Square object for a chess game, can hold pieces and helps handle checks
 * 
 * @author Sean and Siddharth
 * @version 21/01/2015
 * 
 */
public class Square extends Rectangle
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// Keep track of the constant side dimension of all Square objects and the
	// highlighted/selected images
	public final static int SIDE = 65;
	private final static Image HIGHLIGHTED_IMAGE = new ImageIcon(
			"Temp\\Yellow.png").getImage();
	private final static Image HIGHLIGHTED_PIECE = new ImageIcon(
			"Temp\\Red.png").getImage();
	private final static Image SELECTED_IMAGE = new ImageIcon(
			"Temp\\Selected.png").getImage();

	// Keep track of the row, column, color, piece, image and other boolean
	// variables for this Square object
	protected int row;
	protected int column;
	private int color; // 0 for white, 1 for black
	protected Piece piece;
	private Image image;
	private boolean isHighlighted;
	private boolean isSelected;

	/**
	 * Constructs a Square object with the given x and y positions, color as an
	 * integer (0 for white, 1 for black) and the contained Piece
	 * 
	 * @param x the x position of this Square
	 * @param y the y position of this Square
	 * @param color the color of this Square as an integer (0 for white, 1 for
	 *            black)
	 * @param piece the Piece object contained in this square, if any
	 */
	public Square(int row, int column, int color, Piece piece)
	{
		// Call the constructor from the super Rectangle class using the given x
		// and y positions and the constant dimension of this Square
		super(Board.BOARD_X + (column * Square.SIDE), Board.BOARD_Y
				+ (row * Square.SIDE), SIDE, SIDE);

		// Set the row, column, color and piece of this Square to those given
		this.row = row;
		this.column = column;
		this.color = color;
		this.piece = piece;

		// Set the image of this Square (black or white) according to the color
		// given
		if (color == 0)
			image = new ImageIcon("Temp\\White.png").getImage();
		else
			image = new ImageIcon("Temp\\Brown.png").getImage();

		// This Square is not highlighted or selected initially
		isHighlighted = false;
		isSelected = false;
	}

	/**
	 * Draws this Square object in a Graphics context
	 * 
	 * @param g the Graphics object to draw the card in
	 */
	public void draw(Graphics g)
	{
		// Draw the appropriate image of this Square object and then that of the
		// piece over it if this Square contains a Piece object
		g.drawImage(image, super.x, super.y, SIDE, SIDE, null);

		// If the Square is highlighted, draw the appropriate image
		if (isHighlighted)
		{
			if (piece == null)
				g.drawImage(HIGHLIGHTED_IMAGE, super.x, super.y, SIDE, SIDE,
						null);
			else
				g.drawImage(HIGHLIGHTED_PIECE, super.x, super.y, SIDE, SIDE,
						null);
		}

		// If this Square is selected, draw the appropriate background image
		if (isSelected)
		{
			g.drawImage(SELECTED_IMAGE, super.x, super.y, SIDE, SIDE, null);
			System.out.println("hi");
		}

		// Draw the Piece contained if any
		if (piece != null)
			piece.draw(g);
	}

	/**
	 * Adds the given Piece to this Square and removes any existing piece if
	 * possible (according to the rules of Chess)
	 * 
	 * @param addPiece the Piece to add to this Square
	 * @return the Piece which has been removed from this Square, if any
	 */
	public Piece addPiece(Piece addPiece, boolean lookAhead)
	{

		// If this Square does not contain a Piece, the given Piece can be added
		// to it. Also update its position
		if (piece == null)
		{
			piece = addPiece;
			piece.setPosition(this.row, this.column, lookAhead);
			return null;
		}

		// If the Square contains a Piece which is not the same color as the
		// Piece to be added, return the captured Piece and add the given Piece
		// to this Square (also update its position). Otherwise, this Piece
		// cannot be added to it
		else if (addPiece != null)
		{
			if (!piece.isSameColor(addPiece))
			{
				Piece removedPiece = piece;
				piece = addPiece;
				piece.setPosition(this.row, this.column, lookAhead);
				return removedPiece;
			}
			else
				return null;
		}

		else
			return null;
	}

	/**
	 * Removes the Piece from this Square if it contains any
	 * 
	 * @return the Piece removed from this Square, if any
	 */
	public Piece removePiece()
	{
		Piece pieceRemoved = piece;
		piece = null;
		return pieceRemoved;
	}

	/**
	 * Returns whether this Square contains a Piece
	 * 
	 * @return true if this Square contains a Piece, false otherwise
	 */
	public boolean containsPiece()
	{
		return piece != null;
	}

	/**
	 * Checks if this Square contains a King piece which is not the same color
	 * (opponent King) as the given Piece
	 * 
	 * @param check the Piece to check with
	 * @return true if this Square contains an opponent King, false otherwise
	 */
	public boolean containsOpponentKing(Piece check)
	{
		// If the Square does not contain a Piece, return false
		if (piece == null)
			return false;

		// If the Square contains a King Piece which is not the same color as
		// the given Piece to check, it contains an opponent King
		if (piece instanceof King && !piece.isSameColor(check))
			return true;

		// Return false if the Piece in this Square is not a King or it is the
		// same color as the given Piece
		return false;
	}

	/**
	 * Checks if this Square contains the given Piece's King (of the same color
	 * as the Piece)
	 * 
	 * @param check the Piece to check with
	 * @return true if this Square contains the Piece's King, false otherwise
	 */
	public boolean containsOwnKing(Piece check)
	{
		// If the Square does not contain a Piece, return false
		if (piece == null)
			return false;

		// If the Square contains a King Piece which is the same color as the
		// given Piece to check, it contains the given Piece's King
		if (piece instanceof King && piece.isSameColor(check))
			return true;

		// Return false if the Piece in this Square is not a King or it is not
		// the same color as the given Piece
		return false;
	}

	/**
	 * Checks if a Square can be attacked
	 * 
	 * @param team the team that the piece is on
	 * @param board the ArrayList of Squares used to play the game
	 * @return true if the Square is under attack, false if not
	 */
	public boolean threatenSquare(int team, Square[][] board)
	{
		// Going through the board
		for (int row = 0; row < board.length; row++)
		{
			for (int col = 0; col < board[row].length; col++)
			{
				// If the sqaure we are checking contains a piece and it is not
				// on your team
				if (board[row][col].containsPiece())
				{
					if (board[row][col].piece.color != team)
					{
						// If the piece is not a King or Rook
						if ((!(board[row][col].piece instanceof King) && !(board[row][col].piece instanceof Rook))
								|| board[row][col].piece.color == team)
						{
							// Generating moves with that piece to see if they
							// attack this Square
							ArrayList<Square> moves = board[row][col].piece
									.generateMoves(board);
							if (moves.contains(this))
							{
								for (int rows = 0; rows < board.length; rows++)
								{
									for (int column = 0; column < board[rows].length; column++)
									{
										board[rows][column].unhighlight();
									}
								}
								return true;
							}
						}
					}
				}
			}
		}
		// Unhighlighting the board
		for (int rows = 0; rows < board.length; rows++)
		{
			for (int column = 0; column < board[rows].length; column++)
			{
				board[rows][column].unhighlight();
			}
		}
		return false;
	}

	/**
	 * Highlights this Square object and sets it to the appropriate color
	 */
	public void highlight()
	{
		isHighlighted = true;
	}

	/**
	 * Unhighlights this Square and resets its image
	 */
	public void unhighlight()
	{
		isHighlighted = false;
	}

	/**
	 * Selects the Piece on this Square
	 */
	public void select()
	{
		if (piece != null)
			isSelected = true;
	}

	// Unselects the Piece on this Square
	public void deselect()
	{
		isSelected = false;
	}

	/**
	 * Checks if the given Square is equal to this Square object based on their
	 * rows and columns
	 * 
	 * @param other the other Object to check for equality
	 * @return true if both the row and column of this Square are equal to those
	 *         of the given Object if it is a Square, false otherwise
	 */
	public boolean equals(Object other)
	{
		// If the other Object given is not a Square, return false
		if (!(other instanceof Square))
			return false;

		// If the given Object is a Square, cast it and return true if its row
		// and column are equal to those of this Square
		Square otherSquare = (Square) other;
		return (this.row == otherSquare.row && this.column == otherSquare.column);
	}

	/**
	 * Returns this Square as a String representation including its row and
	 * column in Chess notation, color and the name of the Piece it contains
	 * 
	 * @return a String containing the row and column of this Square in Chess
	 *         notation, its color and the name of the piece it contains
	 */
	public String toString()
	{
		// Find the column of this Square in Chess notation
		char colChar = "ABCDEFGH".charAt(column);

		// Find the actual color of this Square
		String colorStr;
		if (color == 0)
			colorStr = "White";
		else
			colorStr = "Black";

		// Return this Square as a String representation
		if (piece == null)
			return String.format("Empty %s square %c%d", colorStr, colChar,
					row + 1);
		else
			return String.format("%s square %c%d with a %s", colorStr, colChar,
					row + 1, piece.toString());
	}
}
