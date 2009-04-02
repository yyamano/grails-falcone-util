/*
 * FileUtils.java
 *
 * Created on August 23, 2002, 2:16 PM
 */


package com.infusion.util;

import java.io.*;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileUtils {
// ========================================================================================================================
//    Static Fields
// ========================================================================================================================

    public static int KB = 1024;
    public static int MB = 1048576;

// ========================================================================================================================
//    Static Methods
// ========================================================================================================================

    /**
     * Opens a file (targetFile) and appends a String (toWrite) to it
     */
    public static void appendFile(File targetFile, String toWrite) throws Exception {
        if (!targetFile.exists()) {
            if (!targetFile.createNewFile()) throw new Exception("File was not created!");
        }
        if (!targetFile.canWrite())
            throw new Exception("No permission to write file!");
        else {
            try {
                // WE CAN WRITE TO THE FILE
                FileWriter fw = new FileWriter(targetFile, true);
                fw.write(toWrite);
                fw.close();
            } catch (Exception e) {
                throw e;
            }
        }
    }

    /**
     * Appends a String to a given file
     *
     * @param fileName The absolute path to the file to be written
     * @param toWrite  The String to write to the file
     */
    public static final void appendFile(String fileName, String toWrite) throws Exception {
        appendFile(new File(fileName), toWrite);
    }

    /**
     * Copies an inputstream to an output stream
     *
     * @param in
     * @param out
     * @param bufferSize
     * @throws IOException
     */
    public static void copyBuffer(BufferedInputStream in, BufferedOutputStream out, int bufferSize) throws IOException {
        while (in.available() > 0) {
            out.write(in.read());
        }
        in.close();
        out.flush();
        out.close();
    }

    /**
     * Copies an inputstream to an output stream
     *
     * @param in
     * @param out
     * @param bufferSize
     * @throws IOException
     */
    public static void copyBuffer(InputStream in, OutputStream out, int bufferSize) throws IOException {

        byte[] buffer = new byte[bufferSize];

        try {

            while (true) {
                int amountRead = in.read(buffer);
                if (amountRead == -1) {
                    break;
                }
                out.write(buffer, 0, amountRead);
            }
        } finally {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * Copies a file from one location to another
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void copyFile(String source, String dest) throws IOException {
        File s = new File(source);
        File d = new File(dest);
        copyFile(s, d);
    }

    /**
     * Copies from one file to another
     *
     * @param s source file
     * @param d destination file
     * @throws IOException
     */
    public static void copyFile(File s, File d) throws IOException {
        if (!s.canRead()) throw new IOException("Unable to read source file.");
        if (d.exists() && !d.canWrite()) throw new IOException("Unable to write dest file");
        if (!d.exists() && !d.createNewFile()) throw new IOException("Unable to create new file");

        BufferedInputStream is = new BufferedInputStream(new FileInputStream(s));
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(d));
        byte[] b = new byte[1024];
        int amountRead = is.read(b);

        while (amountRead > 0) {
            os.write(b, 0, amountRead);
            amountRead = is.read(b);
        }
        os.close();
        is.close();
    }

    /**
     * Returns the number of days since a file has been modified
     *
     * @param file The file to get the age of
     */
    public static int daysOld(File file) {
        if (file.lastModified() < 1) return 0;
        return milliToDay(Calendar.getInstance().getTimeInMillis() - file.lastModified());
    }

    /**
     * Reads all bytes from an InputStream
     *
     * @param attachment
     * @throws IOException
     */
    public static byte[] getBytes(InputStream attachment) throws IOException {
        ByteArrayOutputStream read = new ByteArrayOutputStream();
        copyBuffer(attachment, read, 2056);
        return read.toByteArray();
    }

    /**
     * Returns a readable filesize in the form of a String
     *
     * @param file The file to get the size of
     * @return String filesize
     */
    public static final String getFileSize(File file) {
        return readableSize(file.length());
    }

    /**
     * Determines if a file matches a certain extension/s
     *
     * @param file
     * @param exts Extension list to compare against
     */
    public static boolean isType(File file, String... exts) {
        if (file == null) return false;
        for (String extension : exts) {
            String lowerFile = file.getName().toLowerCase();
            if (lowerFile.endsWith(extension.toLowerCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Converts milliseconds to days
     */
    public static int milliToDay(long milli) {
        return (int) ((double) milli / (1000 * 24 * 60 * 60));
    }

    /**
     * Moves a file from one location to another (deletes source file on completion)
     *
     * @param source
     * @param dest
     * @throws IOException
     */
    public static void moveFile(String source, String dest) throws IOException {
        copyFile(source, dest);
        File s = new File(source);
        s.delete();
    }

    /**
     * Converts a String[] containing file paths to File[]
     */
    public static File[] pathToFileAry(String[] s) {
        File[] me = new File[s.length];
        for (int i = 0; i < s.length; i++) {
            me[i] = new File(s[i]);
        }
        return me;
    }

    /**
     * Reads data from BufferedReader into a String
     *
     * @param reader
     * @throws IOException
     */
    public static String read(BufferedReader reader) throws IOException {
        StringBuffer tmp = new StringBuffer();
        String tmpS;
        while ((tmpS = reader.readLine()) != null) {
            tmp.append(tmpS);
        }
        return tmp.toString();
    }

    /**
     * Reads contents of file (f) into a String (up to five megs)
     */
    public static String readFile(File f) throws IOException {
        return readFile(f, "\n");
    }

    public static String readFile(File f, int startLine, int numRowsToRead) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(f));
        for (int i = 1; i < startLine; i++) {
            reader.readLine();
        }
        StringBuilder rtn = new StringBuilder();

        counter: for (int i = 0; i < numRowsToRead; i++) {
            String line = reader.readLine();
            if (line != null) {
                rtn.append(line);
            } else {
                 break counter;
            }
        }

        reader.close();
        return rtn.toString();

    }

    /**
     * Reads contents of file (f) into a String (up to five megs) using newLineChar as a line seperator
     */
    public static String readFile(File f, String newLineChar) throws IOException {
        if (f.length() > 5000000) return "File too long";
        BufferedReader buf = new BufferedReader(new FileReader(f));
        StringBuffer results = new StringBuffer();
        while (buf.ready()) {
            results.append(buf.readLine()).append(newLineChar);
        }
        return results.toString();
    }

    /**
     * Reads data from a Stream into a String
     *
     * @param asciiStream
     * @throws IOException
     */
    public static String readStream(InputStream asciiStream) throws IOException {
        if (asciiStream == null) {
            return null;
        } else {
            BufferedReader in = new BufferedReader(new InputStreamReader(asciiStream));
            StringBuffer buf = new StringBuffer();
            while (in.ready()) {
                buf.append(in.readLine() + "\n");
            }
            return StringUtils.chop(buf.toString());
        }
    }

    /**
     * Reads from a File (f) into a Writer (out)
     */
    public static void readWrite(File f, OutputStream out) throws IOException {
        if (!f.exists()) return;
        if (!f.canRead() || !f.canWrite()) throw new IOException("Can't read or write template file.");

        FileInputStream input = new FileInputStream(f);
        BufferedInputStream bis = new BufferedInputStream(input);

        BufferedOutputStream bos = new BufferedOutputStream(out);


        int aByte;
        while ((aByte = bis.read()) != -1) {
            bos.write(aByte);
        }
        bos.flush();
        bos.close();
        bis.close();
    }

    /**
     * Reads from a File (f) into a Writer (out)
     */
    public static void readWrite(File f, Writer out) throws FileNotFoundException, IOException {
        if (!f.exists()) return;
        if (!f.canRead() || !f.canWrite()) throw new FileNotFoundException("Can't read or write template file.");
        BufferedReader fr = new BufferedReader(new FileReader(f));
        int aByte;
        while ((aByte = fr.read()) != -1) {
            out.write(aByte);
        }
        fr.close();
        out.flush();
    }

    /**
     * Given a raw size of a file, returns a readable size for it, ie:
     * <p/>
     * 1MB <BR>
     * 12KB <BR>
     * 30 bytes <BR>
     * 30GB<BR>
     *
     * @param i File size
     */
    public static String readableSize(long i) {
        if (i < 0) return "N/A";
        if (i > 999999999) {
            return NumberUtils.doubleFormat((double) i / 1000000000) + " GB";
        }

        if (i > 999999) {
            return NumberUtils.doubleFormat((double) i / 1000000) + " MB";
        } else if (i > 999) {
            return (int) (i / 1000) + " KB";
        } else {
            return i + " Bytes";
        }
    }

    /**
     * Recursively deletes a file (folder) and all children
     *
     * @param file
     * @see #recursiveDelete(java.io.File,boolean)
     */
    public static void recursiveDelete(File file) {
        recursiveDelete(file, true);
    }

    /**
     * Recursively deletes a file (folder) and all children
     *
     * @param file
     * @param deleteParentFile Whether or not to delete the top level
     */
    public static void recursiveDelete(File file, boolean deleteParentFile) {
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            File file1 = files[i];
            if (file1.isDirectory()) {
                recursiveDelete(file1);
            } else {
                file1.delete();
            }
        }
        if (deleteParentFile) {
            file.delete();
        }
    }

    /**
     * @see #verifyFileType(java.io.File,String)
     */
    public static boolean verifyFileType(File file, String type) {
        if (type == null) {
            return false;
        }
        return isType(file, type);
    }

    /**
     * Writes a string(str) to a File(f) ** THis method overwrites any data in the file
     */
    public static void writeFile(File f, String str) throws Exception {
        if (!f.exists()) {
            if (!f.createNewFile()) {
                throw new Exception("File was not created!");
            }
        }
        if (!f.canWrite())
            throw new Exception("No permission to write file!");
        else {
            try {
                // WE CAN WRITE TO THE FILE
                FileWriter fw = new FileWriter(f, false);
                fw.write(str);
                fw.close();
            } catch (Exception e) {
                throw e;
            }
        }
    }

    /**
     * Simple method to zip up a file
     *
     * @param source The file to be zipped
     * @param target The target zip file
     */
    public static final void zipFile(File source, File target) throws IOException {

        FileOutputStream fo = new FileOutputStream(target);
        ZipOutputStream zo = new ZipOutputStream(fo);
        zo.setMethod(ZipOutputStream.DEFLATED);

        FileInputStream fi = new FileInputStream(source);
        BufferedInputStream bi = new BufferedInputStream(fi);
        ZipEntry ze = new ZipEntry(source.getName());
        zo.putNextEntry(ze);

        byte[] dt = new byte[1024];
        int bCnt;
        while ((bCnt = bi.read(dt, 0, 1024)) != -1) {
            zo.write(dt, 0, bCnt);
        }
        zo.flush();
        zo.closeEntry();
        bi.close();

        zo.close();
        source.delete();
    }

    /**
     * Creates a path given a list of path items, uses File.separator
     *
     * @param prefix Whether to prefix with separator
     * @param suffix Whether to suffix with separator
     * @param parts  The parts
     * @return A String
     */
    public static String createPath(boolean prefix, boolean suffix, String... parts) {
        StringBuilder path = new StringBuilder();

        for (int i = 0; i < parts.length; i++) {
            String part = parts[i];
            if (!part.startsWith(File.separator)) {
                if (i == 0 && prefix) {
                    path.append(File.separator);
                }
            }

            path.append(part);

            if (!part.endsWith(File.separator)) {
                if (i < (parts.length - 1) || suffix) {
                    path.append(File.separator);
                }
            }
        }
        return path.toString();
    }

    /**
     * @see #createPath(boolean, boolean, String[])
     */
    public static String createPath(String... parts) {
        return createPath(true, true, parts);
    }

    public static String createFilePath(String... parts) {
        return createPath(true, false, parts);
    }
}

