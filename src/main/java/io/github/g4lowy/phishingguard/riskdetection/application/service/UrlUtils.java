package io.github.g4lowy.phishingguard.riskdetection.application.service;


import com.google.common.net.InternetDomainName;
import com.norconex.commons.lang.url.URLNormalizer;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public final class UrlUtils {

    private UrlUtils() {}

    private static final Pattern URL_PATTERN = Pattern.compile(
            "(?:(?:https?)://)?(?:www\\.)?" +
                    "[\\w-]+(?:\\.[\\w-]+)+" +
                    "(?::\\d{2,5})?" +
                    "(?:/[\\w./?%&=+#-]*)?",
            Pattern.CASE_INSENSITIVE
    );

    public static List<String> extractUrls(String text) {
        List<String> urls = new ArrayList<>();
        Matcher matcher = URL_PATTERN.matcher(text);
        while (matcher.find()) {
            urls.add(matcher.group());
        }
        return urls;
    }

    public static String normalize(String raw) {
        String input = raw.trim();

        if (!input.toLowerCase().startsWith("http")) {
            input = "http://" + input;
        }

        String normalized = new URLNormalizer(input)
                .lowerCaseSchemeHost()
                .removeDefaultPort()
                .removeFragment()
                .toString();

        try {
            URI uri = new URI(normalized);
            if (isEmpty(uri.getPath()) && !normalized.endsWith("/")) {
                normalized = normalized + "/";
            }
        } catch (URISyntaxException ignored) {}

        return normalized;
    }

    public static String hostOf(String url) {
        try {
            return new URI(url).getHost();
        } catch (URISyntaxException uriSyntaxException) {
            return url;
        }
    }

    public static String eTldPlusOne(String host) {
        try {

            return InternetDomainName.from(host)
                    .topPrivateDomain()
                    .toString();

        } catch (IllegalStateException e) {

            throw new IllegalArgumentException("Invalid domain: " + host, e);
        }
    }
}
