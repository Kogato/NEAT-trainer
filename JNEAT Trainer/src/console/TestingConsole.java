 package console;

import testing.XORTester;

public class TestingConsole {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		XORTester tester = new XORTester("C:\\Users\\Simon\\Documents\\MarioFun\\jneat\\data\\starterGenome.read");
		String testGenomeFileName ="C:\\Users\\Simon\\Documents\\MarioFun\\jneat\\data\\Winners\\xor_win 0";
		tester.test(testGenomeFileName);

	}

}
