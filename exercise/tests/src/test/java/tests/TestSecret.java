package tests;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import maze.Maze;
import net.haspamelodica.charon.StudentSide;
import net.haspamelodica.charon.junitextension.CharonExtension;
import secret.Secret;

@ExtendWith(CharonExtension.class)
public class TestSecret
{
	private static Secret.Prototype SecretP;

	@BeforeAll
	public static void setupPrototypes(StudentSide studentSide)
	{
		studentSide.createPrototype(Maze.Prototype.class);
		SecretP = studentSide.createPrototype(Secret.Prototype.class);
	}

	@Test
	public void testKnowsSecret()
	{
		int secretBits = Long.BYTES * 8;
		// Changes every 24 hours
		long secret = SecretConstants.SECRET;

		/*
		 * The maze contains the secret encoded as a pattern of walls.
		 * This pattern, however, is unreachable from the start position,
		 * because it is surrounded by walls on all sides.
		 * 
		 * Maze layout (space: no wall, *: wall, ?: secret):
		 *          \\
		 *  +-------\\----+
		 *  |       \\    |
		 *  | ******\\*** |
		 *  | *?????\\??* |
		 *  | ******\\*** |
		 *  |       \\    |
		 *  +-------\\----+
		 *          \\
		 */
		MazeSolutionBuilder mazeBuilder = new MazeSolutionBuilder(2 + secretBits + 2, 2 + 1 + 2);
		// start and target don't really matter, just use top left corner
		mazeBuilder.setStart(0, 0).setTarget(1, 0);
		// leftmost wall
		mazeBuilder.setWall(1, 1).setWall(1, 2).setWall(1, 3);
		// rightmost wall
		mazeBuilder.setWall(secretBits + 2, 1).setWall(secretBits + 2, 2).setWall(secretBits + 2, 3);
		// each row: wall above and below, plus wall if secret has a 1 at that bit
		for(int bit = 0; bit < secretBits; bit ++)
		{
			mazeBuilder.setWall(bit + 2, 1).setWall(bit + 2, 3);
			if((secret & (1L << bit)) != 0)
				mazeBuilder.setWall(bit + 2, 2);
		}

		MazeSolution maze = mazeBuilder.build();

		if(!SecretConstants.IS_PRODUCTION)
			for(int y = 0; y < maze.getHeight(); y ++)
			{
				for(int x = 0; x < maze.getWidth(); x ++)
					System.out.print(maze.isWall(x, y) ? '*' : ' ');
				System.out.println();
			}

		// Don't use assertEquals; error message would leak secret
		if(secret != SecretP.extractSecretFromMaze(maze))
			fail("Wrong secret");
	}
}
