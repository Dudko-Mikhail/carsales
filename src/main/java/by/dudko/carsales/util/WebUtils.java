package by.dudko.carsales.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.experimental.UtilityClass;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringJoiner;

@UtilityClass
public class WebUtils {
    public static String readRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        var joiner = new StringJoiner("\n");
        while (reader.ready()) {
            joiner.add(reader.readLine());
        }
        return joiner.toString();
    }

    public static void writeBody(String text, HttpServletResponse response) throws IOException {
        PrintWriter writer = response.getWriter();
        writer.write(text);
        writer.flush();
    }
}
