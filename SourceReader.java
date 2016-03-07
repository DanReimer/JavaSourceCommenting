package reading;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SourceReader {
	static List<String> output = new ArrayList<String>();
	static int tabValue = 0;
	static int currentLine = 0;
	static int outLine = 0;
	final static Path FILE_PATH = Paths.get("D:\\Dan R\\Google Drive\\Backups Main\\workspace\\File Reader\\src\\reading\\SourceReader.java");
//			"C:\\Users\\Dan R\\Desktop\\PropertyCommissionCommentTest.java"); 

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(FILE_PATH);
		String className = FILE_PATH.getFileName().toString();
		className = className.substring(0, className.length() - 5);
		System.out.println(className);
		List<String> text = new ArrayList<String>();
		
		while (sc.hasNextLine()) {
			text.add(sc.nextLine());
		}
		sc.close();
		output.addAll(text);
		
		generateComments(text, className);
		
		Files.write(FILE_PATH.getParent().resolve("C:\\Users\\Dan R\\Desktop\\SourceReader.java"), output);

		
		for (int i = 0; i < 10; i++) {
			System.out.println(output.get(i));
		}
		
		System.out.println(text.get(text.size() - 1));
	}

	private static void generateComments(List<String> text, String className) throws IOException {
		//imports
		for (int i = currentLine; i < text.size() && !(text.get(i).matches("^\\s*.+" + className + " \\{\\s*$")); i++, currentLine++, outLine++) {
			if (!text.get(i).equals("")) {
				Pattern p = Pattern.compile("(?<=\\.)[A-Za-z]+");
				Matcher m = p.matcher(text.get(i));
				m.find();
				String comment = "\n//imports " + ((text.get(i).charAt(text.get(i).lastIndexOf('.')+1) == '*')
						? m.group() + " package" : (text.get(i).substring(text.get(i).lastIndexOf('.')+1)).replace(";", "") + " class");
				output.add(outLine, comment);
				outLine++;
			}
		}
		
		//Class javadoc
		addLineO("/**");
		addLineO(" * " + className + ": ");
		addLineO(" *");
		addLineO(" * @author: Daniel Reimer");
		addLineO(" * @version: ");
		addLineO(" *");
		addLineO(" * Assignment: ");
		addLineO(" * Course: ADEV-1003");
		addLineO(" * Section: 2");
		addLineO(" * Date Created: " + Files.getAttribute(FILE_PATH, "creationTime").toString().substring(0, 10));
		addLineO(" * Last Updated: " + Files.getAttribute(FILE_PATH, "lastModifiedTime").toString().substring(0, 10));
		addLineO(" */");
//		System.out.println(text.get(currentLine));
//		System.out.println(output.get(outLine));
		
		Pattern methodRegex = Pattern.compile("^\\s*p[a-z]+ (static )?(\\w+) \\w*\\(([^()]*)\\)\\s+(?:throws\\s+.*?\\s+)?\\{\\s*$");
		Pattern varRegex = Pattern.compile("^\\s*(?:((?:p[a-z]+)|static|final)\\s+){0,3}([A-Za-z]+)\\s+\\w+(?:\\s*=\\s*.+)?;\\s*$");
		int numMethods = 0;
		tabValue++;
		
		for (int i = currentLine; i < text.size(); i++) {
			if (methodRegex.matcher(text.get(i)).find()) {
				numMethods++;
			}
		}
		boolean justMain = (numMethods == 1) ? true : false;
		
		for (int i = currentLine; i < text.size(); i++, currentLine++, outLine++) {
			Matcher methMatch = methodRegex.matcher(text.get(i));
			Matcher varMatch = varRegex.matcher(text.get(i));
			if (methMatch.find()) {
				if (!justMain) {
					//Methods jdoc
					System.out.println(currentLine+1);
					addLineO("/**");
					addLineO(" * DESCRIPTION: ");
					addLineO(" *");
					
					String[] params = methMatch.group(3).split("\\s*,\\s*");
					for (int j = 0; j < params.length; j++) {
						addLineO(" * @param " + params[j].substring(params[j].indexOf(' ')) + " DESCRIPTION");
					}
					if (!methMatch.group(2).equals("void")) {
						addLineO(" *");
						addLineO(" * @return " + methMatch.group(2) + " DESCRIPTION");
					}
					addLineO(" */");
				}
				tabValue++;
				//init vars
			}
			else if (text.get(i).matches("\\t{" + (tabValue-1) + "}\\}")) {
				tabValue--;
				System.out.println("unTabbed at " + (currentLine+1));
			}
//			else if (varMatch.find() && tabValue == 2) {
//				String toComment = text.get(i);
//				for (int j = i; j < text.size() && !(text.get(j).equals("")); j++) {
//					
//				}
//				String comment = "//";
//			}
		}
		//Fields		
	}
	
	public static void addLineO(String toAdd) {
		for (int i = 0; i < tabValue; i++) {
			toAdd = "\t" + toAdd;
		}
		output.add(outLine, toAdd);
		outLine++;
	}
}
