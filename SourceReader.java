import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SourceReader {

    static ArrayList<String> sourceCode = new ArrayList<>();

    static File file;

    public static void main(String[] args) throws IOException {

        file = new File(getFileName());

        BufferedReader reader = new BufferedReader(new FileReader(file));

        String line = reader.readLine();

        while (line != null) {
            sourceCode.add(line);
            line = reader.readLine();
        }

        generateImportComments();
        generateClassComments();

        for (String s : sourceCode) {
            System.out.println(s);
        }
    }

    static void generateImportComments() {
        int imports = 0;
        int firstImportLine = 0;

        for (int i = 0; i < sourceCode.size(); i++) {
            String line = sourceCode.get(i);

            if(imports == 0)
                firstImportLine = i;

            if(line.toLowerCase().contains("import")) {
                imports ++;
            }

            if(line.contains("class"))
                break;
        }

        sourceCode.add(firstImportLine == 0 ? 0 : firstImportLine - 1, "// import " + imports + " statements");
    }

    static void generateClassComments() throws ArrayIndexOutOfBoundsException {
        int classLine = 0;
        Class a = new Class();

        for (int i1 = 0; i1 < sourceCode.size(); i1++) {
            String s = sourceCode.get(i1);
            if (s.contains("class")) {
                a.isPublic = s.contains("public");
                a.isStatic = s.contains("static");
                a.isAbstract = s.contains("abstract");

                String[] classData = s.split(" ");

                for (int i = 0; i < classData.length; i++) {
                    if (classData[i].equals("class"))
                        a.name = classData[i + 1];
                }

                classLine = i1;

                break;
            }
        }

        addBlock(String.format(
                        "\n/**\n" +
                        " * %s:\n" +
                        " * \n" +
                        " * <pre>\n" +
                        " * Assignment: \n" +
                        " * Course: ADEV-1003\n" +
                        " * Section: 4\n" +
                        " * Date Created: \n" +
                        " * Last Updated: \n" +
                        " * </pre>\n" +
                        " *\n" +
                        " * @author \n" +
                        " * @version 01\n" +
                        " */", a.name), classLine);
    }

    /*private static void generateComments(List<String> text, String className) throws IOException {
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
                    addLineO(" *");
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
    }*/

    static void addBlock(String block, int index) {
        String[] blockData = block.split("\n");

        for (int i = blockData.length - 1; i > 0; i--) {
            sourceCode.add(index, blockData[i]);
        }
    }

    public static String getFileName() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("FILEPATH: ");

        return scanner.nextLine();
    }
}
