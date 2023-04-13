package flag;

import static net.haspamelodica.charon.annotations.StudentSideInstanceKind.Kind.CLASS;
import static net.haspamelodica.charon.annotations.StudentSidePrototypeMethodKind.Kind.STATIC_METHOD;

import maze.Maze;
import net.haspamelodica.charon.StudentSideInstance;
import net.haspamelodica.charon.StudentSidePrototype;
import net.haspamelodica.charon.annotations.StudentSideInstanceKind;
import net.haspamelodica.charon.annotations.StudentSidePrototypeMethodKind;

@StudentSideInstanceKind(CLASS)
public interface Flag extends StudentSideInstance
{
	public static interface Prototype extends StudentSidePrototype<Flag>
	{
		@StudentSidePrototypeMethodKind(STATIC_METHOD)
		public long extractFlagFromMaze(Maze maze);
	}
}
