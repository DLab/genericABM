import java.util.concurrent.ThreadLocalRandom;

public class TestRandom
{
	public static double[] generateRandomArray(int N) {
        return ThreadLocalRandom.current().doubles(N).toArray();
    }
	public static void main(String[] args)
	{
		double[] d = generateRandomArray(10);
		for (double e : d)
		{
			System.out.println(e);
		}
		Math.signum(12);
		System.out.println(15 % 2);
		
	}
}
