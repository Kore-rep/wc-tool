/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package ccwc;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.cli.*;

public class Client {
    public static int getNumBytes(byte[] data) {
        return data.length;
        // int byteCount = 0;
        // for (String line : data) {
        // byteCount += line.getBytes().length;
        // }
        // return byteCount;
    }

    public static int getNumLines(List<String> data) {
        return data.size();
    }

    public static int getNumChars(List<String> data) {
        int charCount = 0;
        for (String line : data) {
            charCount += line.length();
        }
        return charCount;
    }

    public static int getNumWords(List<String> data) {
        int wordCount = 0;
        StringTokenizer st;
        for (String line : data) {
            st = new StringTokenizer(line);
            wordCount += st.countTokens();
        }
        return wordCount;
    }

    public static String getRequiredResponse(boolean requireBytes, boolean requireChars, boolean requireWords,
            boolean requireLines,
            boolean noOptions,
            String targetFilePath) {

        List<String> targetData = new ArrayList<>();
        byte[] targetBytes;

        try {
            targetData = Files.readAllLines(Paths.get(targetFilePath));
            targetBytes = Files.readAllBytes(Paths.get(targetFilePath));
        } catch (IOException e) {
            System.out.println("Unable to find file " + targetFilePath);
            System.exit(1);
            return null;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("  ");
        if (noOptions) {
            sb.append(getNumLines(targetData) + " ");
            sb.append(getNumWords(targetData) + " ");
            sb.append(getNumBytes(targetBytes) + " ");
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
                sb.append(getNumBytes(targetBytes) + " ");
            }
        }
        sb.append(targetFilePath);
        return sb.toString();
    }

    public static CommandLine parseCommandLine(String[] args, Options options, HelpFormatter formatter) {
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("CCWC", options);
            System.exit(1);
            return null;
        }
    }

    public static Options generateOptions() {
        Options options = new Options();
        options.addOption("c", "bytes", false, "print the byte count.");
        options.addOption("m", "chars", false, "print the character count");
        options.addOption("w", "words", false, "print the word count.");
        options.addOption("l", "lines", false, "print the newline count.");

        return options;
    }

    public static void main(String[] args) {
        Options options = generateOptions();
        HelpFormatter formatter = new HelpFormatter();

        CommandLine cmd = parseCommandLine(args, options, formatter);

        boolean requireBytes = cmd.hasOption("bytes");
        boolean requireChars = cmd.hasOption("chars");
        boolean requireWords = cmd.hasOption("words");
        boolean requireLines = cmd.hasOption("lines");
        boolean noOptions = cmd.getOptions().length == 0 ? true : false;

        String targetFile = cmd.getArgs()[0];
        String output = getRequiredResponse(requireBytes, requireChars, requireWords, requireLines, noOptions,
                targetFile);
        System.out.println(output);
    }
}