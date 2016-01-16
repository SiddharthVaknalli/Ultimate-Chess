import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

/**
 * Keeps track of an abstract Piece class for a game of Chess, including its
 * original row and column it is added to, its current row and column, its
 * color, image and whether its has been moved or captured. Includes methods to
 * construct a Piece given the row, column and Image, to draw it, to check if is
 * the same color as another Piece, to set the position given the row and column
 * from the board, to capture the Piece and to return as a String. Also includes
 * abstract methods to generate all possible moves given the board and get the
 * value of the piece
 * @author Siddharth Vaknalli and Sean Marchand
 * @version Wednesday, January 21, 2015
 */
public abstract class Piece extends Rectangle
{
	private static final long serialVersionUID = 1L;

	// Keep track of the row, column, color and image of this Piece and whether
	// it is captured and has been moved
	protected int originalRow;
	protected int originalColumn;
	protected int row;
	protected int column;
	protected int color; // 0 for white, 1 for black
	private Image pieceImage;
	private boolean isCaptured;
	protected boolean hasMoved;

	/**
	 * Constructs a new Piece object with the given row, column, color as an
	 * integer (0 for white and 1 for black) and Image
	 * @param row the row of of this Piece on this chess board
	 * @param column the column of this Piece on this chess board
	 * @param color the color integer of this Piece (0 for white, 1 for black)
	 * @param image the Image of this Piece
	 */
	public Piece(int row, int column, int color, Image image)
	{
		// Calls the constructor of the super Rectangle class calculating the x
		// and y positions and the width and height as those of the image of the
		// Piece
		super(Board.BOARD_X + (column * Square.SIDE), Board.BOARD_Y
				+ (row * Square.SIDE), Square.SIDE, Square.SIDE);

		// Set the row, column, color and image of this Piece to those given
		originalRow = row;
		originalColumn = column;
		this.row = row;
		this.column = column;
		this.color = color;
		pieceImage = image;

		// This Piece has not been captured or moved when it is created
		isCaptured = false;
		hasMoved = false;
	}

	/**
	 * Draws this Piece given a Graphics object
	 * @param g the Graphics object to draw this Piece with
	 */
	public void draw(Graphics g)
	{
		// Draw the image of this Piece at its x and y positions and width and
		// height (from the Rectangle class)
		g.drawImage(pieceImage, super.x, super.y, super.width, super.height,
				null);
	}

	/**
	 * Returns whether this Piece is the same color as the other given Piece
	 * @param other the other Piece to compare this Piece's color to
	 * @return true if the color of this Piece is the same as the color of the
	 *         other given Piece, false otherwise
	 */
	public boolean isSameColor(Piece other)
	{
		return this.color == other.color;
	}

	/**
	 * Sets the position of this Piece to the given Point
	 * @param position the Point to which the position of this Piece is set
	 */
	public void setPosition(Point position)
	{

	}

	/**
	 * Sets the row and column of this Piece to those given and updates the x
	 * and y positions accordingly
	 * 
	 * @param row the row of this Piece
	 * @param column the column of this Piece
	 */
	public void setPosition(int row, int column, boolean lookAhead)
	{
		// if (this instanceof King)
		// System.out.println(lookAhead);
		super.x = Board.BOARD_X + (column * Square.SIDE);
		super.y = Board.BOARD_Y + (row * Square.SIDE);
		super.width = Square.SIDE;
		super.height = Square.SIDE;

		// If the Piece being moved is a Pawn, record whether it can be captured
		// in an en passant move by another Pawn according to the rules of
		// Chess. Also it has not been moved if its final position is in its
		// original Square
		if (this instanceof Pawn)
		{
			Pawn pawn = (Pawn) this;
			if (!hasMoved)
			{
				if ((pawn.onOppositeSide && row == 3)
						|| (!pawn.onOppositeSide && row == 4))
					pawn.enPassantPossible = true;
				else
					pawn.enPassantPossible = false;

				if (row != originalRow || this.column != originalColumn)
					hasMoved = true;
			}
			else if ((pawn.onOppositeSide && row == 1)
					|| (!pawn.onOppositeSide && row == 6))
				hasMoved = false;
		}

		// If the position of this Piece has been changed, it has been moved
		if (!(this instanceof Pawn)
				&& (row != this.originalRow || column != this.originalColumn))
		{
			if (!lookAhead)
				hasMoved = true;
		}
		this.row = row;
		this.column = column;
	}

	/**
	 * Gets the position of this Piece
	 * @return the Point position at which this Piece exists
	 */
	public Point getPosition()
	{
		return new Point(super.x, super.y);
	}

	/**
	 * Moves this Piece by the required amount between the initial and final
	 * positions for dragging it
	 * @param initialPos the initial Point to start dragging this Piece
	 * @param finalPos the final Point to stop dragging this Piece
	 */
	public void move(Point initialPos, Point finalPos)
	{
		super.x += finalPos.x - initialPos.x;
		super.y += finalPos.y - initialPos.y;
	}

	/**
	 * Captures the given Piece and updates it x and y positions appropriately
	 * based on the given Pieces that have already been captured
	 * @param capturedPieces the Pieces that have already been captured
	 */
	public void capture(ArrayList<Piece> capturedPieces)
	{
		// This Piece is captured
		isCaptured = true;

		// Keep track of the number of white and black Pieces that have already
		// been captured
		int capturedWhites = 0;
		int capturedBlacks = 0;
		for (Piece next : capturedPieces)
		{
			if (next.color == 0)
				capturedWhites++;
			else
				capturedBlacks++;
		}

		super.width = 50;
		super.height = 50;

		// If the color of this Piece is white, do the following
		if (color == 0)
		{
			// Set the x position of the Piece according to the layout of the
			// Squares and the Board (on the left side of the board)
			if (capturedWhites <= 8)
			{
				super.x = 35 + (capturedWhites * 50);
				super.y = 210;
			}
			else
			{
				super.x = 35 + ((capturedWhites - 9) * 50);
				super.y = 270;
			}

			// Set the y position of the Piece according to the number of rows
			// of white Pieces already captured (draws 2 per row)
		}

		// Else if the color of this Piece is black, do the following
		else
		{
			// Set the x position of the Piece according to the layout of the
			// Squares and the Board (on the right side of the board)
			if (capturedBlacks <= 8)
			{
				super.x = 35 + (capturedBlacks * 50);
				super.y = 455;
			}
			else
			{
				super.x = 35 + ((capturedBlacks - 9) * 50);
				super.y = 515;
			}
		}
	}

	/**
	 * Checks whether the row and column of the given Object, if a piece, equals
	 * the row and column of this Piece
	 * @param other the other Object to check for row and column
	 * @return true if the row and column of this Piece equal those of the other
	 *         Object if it is a Piece, false otherwise
	 */
	public boolean equals(Object other)
	{
		if (!(other instanceof Piece))
			return false;
		else
		{
			Piece otherPiece = (Piece) other;
			return (this.row == otherPiece.row
					&& this.column == otherPiece.column && this.isCaptured == otherPiece.isCaptured);
		}
	}

	/**
	 * Returns the color and name of this Piece as a String
	 * @return a String representation of the color and name of this Piece
	 */
	public String toString()
	{
		// Keep track of the name of this Piece using its subclass
		String name = this.getClass().toString().substring(6);

		// Return the appropriate String representation of the name and color of
		// this Piece
		if (color == 0)
			return String.format("white %s at %d, %d %b", name, row, column,
					hasMoved);
		else
			return String.format("black %s at %d, %d %b", name, row, column,
					hasMoved);
	}

	/**
	 * Generates a List of all possible Squares to which this Piece can move in
	 * a game of Chess given the board as an Array of Squares
	 * @param board an Array of Squares representing the Chess board
	 * @return an ArrayList of Squares from this Board to which this Piece can
	 *         legally move according to the rules of the game of Chess
	 */
	public abstract ArrayList<Square> generateMoves(Square[][] board);

	/**
	 * Gets the value of this Piece at the current position on a Chess board,
	 * based on its color and how favorable its position is from a game winning
	 * perspective
	 * @return the integer value of this Piece based on its color and current
	 *         position on a Chess board
	 */
	public abstract int getValue();

}