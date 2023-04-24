package tests;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import flag.Flag;
import maze.Maze;
import net.haspamelodica.charon.StudentSide;
import net.haspamelodica.charon.junitextension.CharonExtension;

@ExtendWith(CharonExtension.class)
public class TestFlag
{
	private static Flag.Prototype FlagP;

	@BeforeAll
	public static void setupPrototypes(StudentSide studentSide)
	{
		studentSide.createPrototype(Maze.Prototype.class);
		FlagP = studentSide.createPrototype(Flag.Prototype.class);
	}

	@Test
	public void testKnowsFlag()
	{
		int flagBits = Long.BYTES * 8;
		// Changes every 24 hours
		long flag = FlagConstants.FLAG;

		/*
		 * The maze contains the flag encoded as a pattern of walls.
		 * This pattern, however, is unreachable from the start position,
		 * because it is surrounded by walls on all sides.
		 * 
		 * Maze layout (space: no wall, *: wall, ?: flag):
		 * 
		 *  +----------------------------------------------------------------------+
		 *  |                                                                      |
		 *  |                                                                      |
		 *  |  ******************************************************************  |
		 *  |  *????????????????????????????????????????????????????????????????*  |
		 *  |  ******************************************************************  |
		 *  |                                                                      |
		 *  |                                                                      |
		 *  +----------------------------------------------------------------------+
		 */
		MazeSolutionBuilder mazeBuilder = new MazeSolutionBuilder(2 + 1 + flagBits + 1 + 2, 2 + 1 + 1 + 1 + 2);
		// start and target don't really matter, just use top left corner
		mazeBuilder.setStart(0, 0).setTarget(1, 0);
		// leftmost wall
		mazeBuilder.setWall(2, 2).setWall(2, 3).setWall(2, 4);
		// rightmost wall
		mazeBuilder.setWall(flagBits + 3, 2).setWall(flagBits + 3, 3).setWall(flagBits + 3, 4);
		// each row: wall above and below, plus wall if flag has a 1 at that bit
		for(int bit = 0; bit < flagBits; bit ++)
		{
			mazeBuilder.setWall(bit + 3, 2).setWall(bit + 3, 4);
			if((flag & (1L << bit)) != 0)
				mazeBuilder.setWall(bit + 3, 3);
		}

		MazeSolution maze = mazeBuilder.build();

		if(!FlagConstants.IS_PRODUCTION)
			for(int y = 0; y < maze.getHeight(); y ++)
			{
				for(int x = 0; x < maze.getWidth(); x ++)
					System.out.print(maze.isWall(x, y) ? '*' : ' ');
				System.out.println();
			}

		// Don't use assertEquals; error message would leak flag
		if(flag != FlagP.extractFlagFromMaze(maze))
			fail("Wrong flag");
	}
}
