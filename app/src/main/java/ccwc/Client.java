/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ccwc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;

import org.apache.commons.cli.*;

public class Client {
    final static String cmdLineUsage = "ccwc [OPTIONS]... [FILE]...";
    final static String helpHeader = "Get the newline, word and bytecounts for each FILE\r\n\n";
    final static String helpFooter = "\nWhere no OPTIONS are provided, newline, word and byte-count will be returned by default.\nWhere no FILEs are provided, input will be read from StdIn\n\nAuthor: Angus Longmore.\nhttps://github.com/kore-rep/wc-tool";
    static HelpFormatter helpFormatter = new HelpFormatter();
    static Options options;

    public static int getNumBytes(String data) {
        return data.getBytes().length;
    }

    public static int getNumLines(String data) {
        return data.split("\n").length;
    }

    public static int getNumChars(String data) {
        return data.length();
    }

    public static int getNumWords(String data) {
        StringTokenizer st = new StringTokenizer(data);
        int count = st.countTokens();
        return count;
    }

    public static void printGenericHelp() {
        helpFormatter.printHelp(cmdLineUsage, helpHeader, options, helpFooter);
    }

    public static String getRequiredResponse(boolean requireBytes, boolean requireChars, boolean requireWords,
            boolean requireLines,
            boolean noOptions,
            String targetData, String filename) {

        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        if (noOptions) {
            sb.append(getNumLines(targetData) + " ");
            sb.append(getNumWords(targetData) + " ");
            sb.append(getNumBytes(targetData) + " ");
        } else {
            if (requireLines) {
                sb.append(getNumLines(targetData) + " ");
            }
            if (requireWords) {
                sb.append(getNumWords(targetData) + " ");
            }
            if (requireChars) {
                sb.append(getNumChars(targetData) + " ");
            }
            if (requireBytes) {
                sb.append(getNumBytes(targetData) + " ");
            }
        }
        sb.append(filename);
        return sb.toString();
    }

    public static String getFileData(String filepath) {
        try {
            return Files.readString(Paths.get(filepath));
        } catch (IOException e) {
            System.out.println("Unable to open file " + filepath);
            printGenericHelp();
            System.exit(1);
            return null;
        }
    }

    public static String getStdInData() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public static CommandLine parseCommandLine(String[] args) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printGenericHelp();
            System.exit(1);
            return null;
        }
    }

    public static void generateOptions() {
        options = new Options();
        options.addOption("c", "bytes", false, "print the byte count");
        options.addOption("m", "chars", false, "print the character count");
        options.addOption("w", "words", false, "print the word count");
        options.addOption("l", "lines", false, "print the newline count");
        options.addOption("h", "help", false, "print help");
    }

    public static void main(String[] args) {
        generateOptions();

        CommandLine cmd = parseCommandLine(args);
        if (cmd.hasOption("help")) {
            printGenericHelp();
            System.exit(0);
        }
        boolean requireBytes = cmd.hasOption("bytes");
        boolean requireChars = cmd.hasOption("chars");
        boolean requireWords = cmd.hasOption("words");
        boolean requireLines = cmd.hasOption("lines");
        boolean noOptions = cmd.getOptions().length == 0 ? true : false;
        List<String> plainArgs = cmd.getArgList();

        if (plainArgs.isEmpty()) {
            String output = getRequiredResponse(requireBytes, requireChars, requireWords, requireLines, noOptions,
                    getStdInData(), "StdIn");
            System.out.println(output);
        }

        for (String file : plainArgs) {
            String output = getRequiredResponse(requireBytes, requireChars, requireWords, requireLines, noOptions,
                    getFileData(file), file);
            System.out.println(output);
        }

    }
}
