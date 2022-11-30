//Following program is a Java implementation
//of Rabin Karp Algorithm 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;

public class StringMatch {
	// d is the number of characters in the input alphabet
	public final static int d = 256;

	/*
	 * pattern -> pattern text -> text q -> A prime number
	 */
	static void search(String pattern, String text, int q) {
		int M = pattern.length();
		int N = text.length();
		int i, j;
		int p = 0; // hash value for pattern
		int t = 0; // hash value for text
		int h = 1;

		// The value of h would be "pow(d, M-1)%q"
		for (i = 0; i < M - 1; i++)
			h = (h * d) % q;

		// Calculate the hash value of pattern and first
		// window of text
		for (i = 0; i < M; i++) {
			p = (d * p + pattern.charAt(i)) % q;
			t = (d * t + text.charAt(i)) % q;
		}

		// Slide the pattern over text one by one
		for (i = 0; i <= N - M; i++) {

			// Check the hash values of current window of text
			// and pattern. If the hash values match then only
			// check for characters on by one
			if (p == t) {
				/* Check for characters one by one */
				for (j = 0; j < M; j++) {
					if (text.charAt(i + j) != pattern.charAt(j))
						break;
				}

				// if p == t and pat[0...M-1] = txt[i, i+1, ...i+M-1]
				if (j == M)
					System.out.println("Pattern found at index " + i);
			}

			// Calculate hash value for next window of text: Remove
			// leading digit, add trailing digit
			if (i < N - M) {
				t = (d * (t - text.charAt(i) * h) + text.charAt(i + M)) % q;

				// We might get negative value of t, converting it
				// to positive
				if (t < 0)
					t = (t + q);
			}
		}
	}

	static void KMPSearch(String pattern, String text) {
		int M = pattern.length();
		int N = text.length();
        
		// create lps[] that will hold the longest
		// prefix suffix values for pattern
		int lps[] = new int[M];
		int j = 0; // index for pat[]

		// Preprocess the pattern (calculate lps[]
		// array)
		computeLPSArray(pattern, M, lps);

		int i = 0; // index for txt[]
		while (i < N) {
			if (pattern.charAt(j) == text.charAt(i)) {
				j++;
				i++;
			}
			if (j == M) {
				System.out.println("Found pattern " + "at index " + (i - j));
				j = lps[j - 1];
			}

			// mismatch after j matches
			else if (i < N && pattern.charAt(j) != text.charAt(i)) {
				// Do not match lps[0..lps[j-1]] characters,
				// they will match anyway
				if (j != 0)
					j = lps[j - 1];
				else
					i = i + 1;
			}
		}
	}

	static void computeLPSArray(String pat, int M, int lps[]) {
		// length of the previous longest prefix suffix
		int len = 0;
		int i = 1;
		lps[0] = 0;

		// the loop calculates lps[i] for i = 1 to M-1
		while (i < M) {
			if (pat.charAt(i) == pat.charAt(len)) {
				len++;
				lps[i] = len;
				i++;
			} else {
				// This is tricky. Consider the example.
				// AAACAAAA and i = 7. The idea is similar
				// to search step.
				if (len != 0) {
					len = lps[len - 1];

					// Also, note that we do not increment
					// i here
				} else {
					lps[i] = len;
					i++;
				}
			}
		}
	}

	/* Driver program to test above function */
	public static void main(String[] args) {

		try {
			Scanner s = new Scanner(System.in);
			Runtime rt = Runtime.getRuntime();
			// File handling
			Path myFilePath = Paths.get("/Users/sivareddy/Desktop/runtime.txt");
			String line = "";

			// input
			System.out.println("Enter Text");
			String text = s.nextLine();
			System.out.println("Enter pattern to be searched");
			String pattern = s.nextLine();

			// System.out.println("length of pattern: " + pattern.length());
			// System.out.println("length of text: " + text.length());
			
			line += text.length();
			
			//Robin Karp
			int q = 101; // A prime number
			long startTime = System.nanoTime();
			search(pattern, text, q);
			long endTime = System.nanoTime();
			
			//Knuth Moris
			long kstartTime = System.nanoTime();
			KMPSearch(pattern,text);
			long kendTime = System.nanoTime();
			
			
			long totalTime = (endTime - startTime)/1000;
			long kTotalTime = (kendTime - kstartTime)/1000;
			line += " " + totalTime + " " + kTotalTime + "\n";
			
			Files.write(myFilePath,line.getBytes(),StandardOpenOption.APPEND);
			
			System.out.println("Running Time RK: " + totalTime);
			System.out.println("Running Time KM: " + kTotalTime);
			if(Files.lines(myFilePath).count()>5){
				System.out.println("Press 2 to see graph?");
				if(s.nextInt()==2){
					Process pr=rt.exec("python3 plotter.py");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
