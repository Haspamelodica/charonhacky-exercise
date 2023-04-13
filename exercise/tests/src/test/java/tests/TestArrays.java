package tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import arrays.ArrayOperations;
import arrays.StringArray;
import arrays.StringSSI;
import net.haspamelodica.charon.StudentSide;
import net.haspamelodica.charon.junitextension.CharonExtension;

@ExtendWith(CharonExtension.class)
@Disabled
public class TestArrays
{
	private static StringSSI.Prototype			StringP;
	private static StringArray.Prototype		StringArrayP;
	private static ArrayOperations.Prototype	ArrayOperationsP;

	@BeforeAll
	public static void setupPrototypes(StudentSide studentSide)
	{
		StringP = studentSide.createPrototype(StringSSI.Prototype.class);
		StringArrayP = studentSide.createPrototype(StringArray.Prototype.class);
		ArrayOperationsP = studentSide.createPrototype(ArrayOperations.Prototype.class);
	}

	@Test
	public void testConcatViaSSI()
	{
		StringArray test = StringArrayP.createArray(StringP.new_("this"), StringP.new_("is"), StringP.new_("a"), StringP.new_("test"));
		assertEquals("this is a test", ArrayOperationsP.concat(test));
	}

	@Test
	public void testConcat()
	{
		StringArray test = StringArrayP.createArray("this", "is", "a", "test");
		assertEquals("this is a test", ArrayOperationsP.concat(test));
	}

	@Test
	public void testSum()
	{
		int[] test = {5, 6, 7, 8};
		assertEquals(5 + 6 + 7 + 8, ArrayOperationsP.sum(test));
	}
}
