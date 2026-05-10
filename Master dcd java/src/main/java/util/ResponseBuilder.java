package util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.stream.Collectors;

public class ResponseBuilder {

    // Creates a Gson instance that formats the JSON nicely with indents
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    public static String toJson(Object obj) {
        return GSON.toJson(obj);
    }

    public static String toXml(Object obj) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = ctx.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        StringWriter writer = new StringWriter();
        marshaller.marshal(obj, writer);
        return writer.toString();
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        return GSON.fromJson(json, clazz);
    }

    public static <T> T fromXml(String xml, Class<T> clazz) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(clazz);
        Unmarshaller unmarshaller = ctx.createUnmarshaller();
        Object result = unmarshaller.unmarshal(new StringReader(xml));
        return clazz.cast(result);
    }

    public static String readBody(HttpServletRequest req) throws Exception {
        try (BufferedReader reader = req.getReader()) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }

    public static boolean clientWantsXml(HttpServletRequest req) {
        String accept = req.getHeader("Accept");
        return accept != null && accept.contains("application/xml");
    }

    public static boolean clientSentXml(HttpServletRequest req) {
        String contentType = req.getContentType();
        return contentType != null && contentType.contains("application/xml");
    }

    public static void write(HttpServletRequest req, HttpServletResponse resp,
            Object obj, int status) throws Exception {

        resp.setStatus(status);

        // XML LOGIC REMOVED - ALWAYS FORCE JSON
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write(toJson(obj));
    }

    public static <T> T parseBody(HttpServletRequest req, Class<T> clazz) throws Exception {
        String body = readBody(req);
        if (clientSentXml(req)) {
            return fromXml(body, clazz);
        } else {
            return fromJson(body, clazz);
        }
    }
}